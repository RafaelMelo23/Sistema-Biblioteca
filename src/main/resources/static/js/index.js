// Variável global para armazenar os endpoints da API
let apiEndpoints = {};

// Inicializar sistema carregando endpoints
window.onload = function() {
    carregarEndpoints();
};

// Função para carregar endpoints do root entry point
function carregarEndpoints() {
    fetch('/api')
        .then(response => response.json())
        .then(data => {
            if (data._links) {
                apiEndpoints = data._links;
                console.log('Endpoints carregados:', apiEndpoints);
            }
        })
        .catch(error => {
            console.error('Erro ao carregar endpoints:', error);
            alert('Erro ao conectar com a API. Verifique se o servidor está rodando.');
        });
}

// Função para trocar abas
function trocarAba(aba) {
    // Remover classe active de todas as abas
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

    // Adicionar classe active na aba selecionada
    event.target.classList.add('active');
    document.getElementById(aba).classList.add('active');
}

// ===== FUNÇÕES DE EMPRÉSTIMOS =====

function buscarEmprestimos() {
    const matricula = document.getElementById('matricula').value.trim();

    if (!matricula) {
        mostrarErro('resultadosBusca', 'Por favor, informe a matrícula do usuário.');
        return;
    }

    if (!apiEndpoints['listar-emprestimos-by-matricula']) {
        mostrarErro('resultadosBusca', 'Endpoint não disponível.');
        return;
    }

    mostrarCarregando('resultadosBusca');

    const url = apiEndpoints['listar-emprestimos-by-matricula'].href.replace('{matricula}', matricula);

    fetch(url)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Nenhum empréstimo encontrado para esta matrícula.');
                } else if (response.status === 400) {
                    throw new Error('Matrícula inválida.');
                } else {
                    throw new Error('Erro ao buscar empréstimos.');
                }
            }
            return response.json();
        })
        .then(data => {
            exibirEmprestimos(data._embedded ? data._embedded.dTOListarEmprestimoList : []);
        })
        .catch(error => {
            mostrarErro('resultadosBusca', error.message);
        });
}

function exibirEmprestimos(emprestimos) {
    const container = document.getElementById('resultadosBusca');

    if (!emprestimos || emprestimos.length === 0) {
        container.innerHTML = '<p class="loading">Nenhum empréstimo encontrado.</p>';
        return;
    }

    let html = '<h4>Empréstimos Encontrados:</h4><div class="results-grid">';

    emprestimos.forEach(item => {
        const emprestimo = item.content || item;
        const statusClass = emprestimo.statusEmprestimo === 'ATIVO' ? 'status-ativo' : 'status-devolvido';

        html += `
            <div class="item-card emprestimo-card ${statusClass}">
                <div class="item-info">
                    <div class="info-item">
                        <span class="info-label">Usuário:</span> ${emprestimo.nomeUsuario}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Status:</span> ${emprestimo.statusEmprestimo}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Livro:</span> ${emprestimo.tituloLivro}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Código:</span> ${emprestimo.codigoExemplar}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Data Empréstimo:</span> ${formatarData(emprestimo.dataEmprestimo)}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Data Prevista:</span> ${formatarData(emprestimo.dataPrevista)}
                    </div>
                </div>
                <button onclick="verDetalhesEmprestimo(${emprestimo.emprestimoid})">Ver Detalhes Completos</button>
            </div>
        `;
    });

    html += '</div>';
    container.innerHTML = html;
}

function verDetalhesEmprestimo(id) {
    if (!apiEndpoints['listar-emprestimos-by-emprestimo-id']) {
        alert('Endpoint não disponível.');
        return;
    }

    const url = apiEndpoints['listar-emprestimos-by-emprestimo-id'].href.replace('null', id);

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar detalhes do empréstimo.');
            }
            return response.json();
        })
        .then(data => {
            const detalhes = data.content || data;
            alert(`Detalhes Completos:

Usuário: ${detalhes.nomeUsuario}
Matrícula: ${detalhes.matricula}
Livro: ${detalhes.titulo}
Autor: ${detalhes.autor}
ISBN: ${detalhes.isbn}
Ano: ${detalhes.ano}
Editora: ${detalhes.editora}
Código Exemplar: ${detalhes.codigoExemplar}
Data Empréstimo: ${formatarData(detalhes.dataEmprestimo)}
Data Prevista: ${formatarData(detalhes.dataPrevista)}
Data Devolução: ${detalhes.dataDevolucao ? formatarData(detalhes.dataDevolucao) : 'Não devolvido'}
Status: ${detalhes.statusEmprestimo}`);
        })
        .catch(error => {
            alert('Erro ao carregar detalhes: ' + error.message);
        });
}

function realizarEmprestimo() {
    const matricula = document.getElementById('matriculaEmprestimo').value.trim();
    const codigoExemplar = document.getElementById('codigoExemplar').value.trim();
    const dataPrevista = document.getElementById('dataPrevista').value;
    const tipoUsuario = document.getElementById('tipoUsuario').value;

    if (!matricula || !codigoExemplar || !dataPrevista || !tipoUsuario) {
        mostrarErro('resultadoEmprestimo', 'Por favor, preencha todos os campos.');
        return;
    }

    if (!apiEndpoints['realizar-emprestimo']) {
        mostrarErro('resultadoEmprestimo', 'Endpoint não disponível.');
        return;
    }

    const dadosEmprestimo = {
        matricula: matricula,
        codigoExemplar: codigoExemplar,
        dataPrevista: dataPrevista,
        tipoUsuario: tipoUsuario
    };

    fetch(apiEndpoints['realizar-emprestimo'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosEmprestimo)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos. Verifique os campos preenchidos.');
                } else {
                    throw new Error('Erro ao realizar empréstimo.');
                }
            }
            mostrarSucesso('resultadoEmprestimo', 'Empréstimo realizado com sucesso!');
            limparFormularioEmprestimo();
        })
        .catch(error => {
            mostrarErro('resultadoEmprestimo', error.message);
        });
}

function realizarDevolucao() {
    const matricula = document.getElementById('matriculaDevolucao').value.trim();
    const codigoExemplar = document.getElementById('codigoExemplarDevolucao').value.trim();

    if (!matricula || !codigoExemplar) {
        mostrarErro('resultadoDevolucao', 'Por favor, preencha todos os campos.');
        return;
    }

    if (!apiEndpoints['realizar-devolucao']) {
        mostrarErro('resultadoDevolucao', 'Endpoint não disponível.');
        return;
    }

    const dadosDevolucao = {
        matricula: matricula,
        codigoExemplar: codigoExemplar
    };

    fetch(apiEndpoints['realizar-devolucao'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosDevolucao)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos. Verifique a matrícula e código do exemplar.');
                } else {
                    throw new Error('Erro ao realizar devolução.');
                }
            }
            mostrarSucesso('resultadoDevolucao', 'Devolução realizada com sucesso!');
            limparFormularioDevolucao();
        })
        .catch(error => {
            mostrarErro('resultadoDevolucao', error.message);
        });
}

// ===== FUNÇÕES DE LIVROS =====

function buscarLivroTitulo() {
    const titulo = document.getElementById('tituloLivro').value.trim();

    if (!titulo) {
        mostrarErro('resultadosLivros', 'Por favor, informe o título do livro.');
        return;
    }

    if (!apiEndpoints['buscar-por-titulo']) {
        mostrarErro('resultadosLivros', 'Endpoint não disponível.');
        return;
    }

    mostrarCarregando('resultadosLivros');

    const url = apiEndpoints['buscar-por-titulo'].href.replace('{titulo}', encodeURIComponent(titulo));

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar livros por título.');
            }
            return response.json();
        })
        .then(data => {
            exibirLivros(data._embedded ? data._embedded.livroList || data._embedded.dTOLivroList : [data]);
        })
        .catch(error => {
            mostrarErro('resultadosLivros', error.message);
        });
}

function buscarLivroId() {
    const id = document.getElementById('idLivro').value.trim();

    if (!id) {
        mostrarErro('resultadosLivros', 'Por favor, informe o ID do livro.');
        return;
    }

    if (!apiEndpoints['buscar-livro-id']) {
        mostrarErro('resultadosLivros', 'Endpoint não disponível.');
        return;
    }

    mostrarCarregando('resultadosLivros');

    const url = apiEndpoints['buscar-livro-id'].href + '/' + id;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Livro não encontrado.');
                } else {
                    throw new Error('Erro ao buscar livro.');
                }
            }
            return response.json();
        })
        .then(data => {
            exibirLivros([data]);
        })
        .catch(error => {
            mostrarErro('resultadosLivros', error.message);
        });
}

function exibirLivros(livros) {
    const container = document.getElementById('resultadosLivros');

    if (!livros || livros.length === 0) {
        container.innerHTML = '<p class="loading">Nenhum livro encontrado.</p>';
        return;
    }

    let html = '<h4>Livros Encontrados:</h4><div class="results-grid">';

    livros.forEach(livro => {
        html += `
            <div class="item-card livro-card">
                <div class="item-info">
                    <div class="info-item">
                        <span class="info-label">ID:</span> ${livro.livroid || livro.id || 'N/A'}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Título:</span> ${livro.titulo}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Autor:</span> ${livro.autor}
                    </div>
                    <div class="info-item">
                        <span class="info-label">ISBN:</span> ${livro.isbn}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Ano:</span> ${livro.ano}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Editora:</span> ${livro.editora}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Exemplares:</span> ${livro.quantidadeExemplares || 'N/A'}
                    </div>
                </div>
            </div>
        `;
    });

    html += '</div>';
    container.innerHTML = html;
}

function cadastrarLivro() {
    const titulo = document.getElementById('novoTitulo').value.trim();
    const autor = document.getElementById('novoAutor').value.trim();
    const isbn = document.getElementById('novoIsbn').value.trim();
    const ano = document.getElementById('novoAno').value.trim();
    const editora = document.getElementById('novaEditora').value.trim();
    const quantidade = document.getElementById('novaQuantidade').value;

    if (!titulo || !autor || !isbn || !ano || !editora || !quantidade) {
        mostrarErro('resultadoLivro', 'Por favor, preencha todos os campos.');
        return;
    }

    if (!apiEndpoints['cadastrar-livro']) {
        mostrarErro('resultadoLivro', 'Endpoint não disponível.');
        return;
    }

    const dadosLivro = {
        titulo: titulo,
        autor: autor,
        isbn: isbn,
        ano: parseInt(ano),
        editora: editora,
        quantidadeExemplares: parseInt(quantidade)
    };

    fetch(apiEndpoints['cadastrar-livro'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosLivro)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos. Verifique os campos preenchidos.');
                } else {
                    throw new Error('Erro ao cadastrar livro.');
                }
            }
            mostrarSucesso('resultadoLivro', 'Livro cadastrado com sucesso!');
            limparFormularioLivro();
        })
        .catch(error => {
            mostrarErro('resultadoLivro', error.message);
        });
}

// ===== FUNÇÕES DE USUÁRIOS =====

function cadastrarAluno() {
    const nome = document.getElementById('nomeAluno').value.trim();
    const cpf = document.getElementById('cpfAluno').value.trim();
    const email = document.getElementById('emailAluno').value.trim();
    const matricula = document.getElementById('matriculaAluno').value.trim();

    if (!nome || !cpf || !email || !matricula) {
        mostrarErro('resultadoAluno', 'Por favor, preencha todos os campos.');
        return;
    }

    if (!apiEndpoints['cadastrar-aluno']) {
        mostrarErro('resultadoAluno', 'Endpoint não disponível.');
        return;
    }

    const dadosAluno = {
        nome: nome,
        cpf: cpf,
        email: email,
        matricula: matricula
    };

    fetch(apiEndpoints['cadastrar-aluno'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosAluno)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos. Verifique os campos preenchidos.');
                } else {
                    throw new Error('Erro ao cadastrar aluno.');
                }
            }
            mostrarSucesso('resultadoAluno', 'Aluno cadastrado com sucesso!');
            limparFormularioAluno();
        })
        .catch(error => {
            mostrarErro('resultadoAluno', error.message);
        });
}

function cadastrarProfessor() {
    const nome = document.getElementById('nomeProfessor').value.trim();
    const cpf = document.getElementById('cpfProfessor').value.trim();
    const email = document.getElementById('emailProfessor').value.trim();
    const matricula = document.getElementById('matriculaProfessor').value.trim();

    if (!nome || !cpf || !email || !matricula) {
        mostrarErro('resultadoProfessor', 'Por favor, preencha todos os campos.');
        return;
    }

    if (!apiEndpoints['cadastrar-professor']) {
        mostrarErro('resultadoProfessor', 'Endpoint não disponível.');
        return;
    }

    const dadosProfessor = {
        nome: nome,
        cpf: cpf,
        email: email,
        matricula: matricula
    };

    fetch(apiEndpoints['cadastrar-professor'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosProfessor)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos. Verifique os campos preenchidos.');
                } else {
                    throw new Error('Erro ao cadastrar professor.');
                }
            }
            mostrarSucesso('resultadoProfessor', 'Professor cadastrado com sucesso!');
            limparFormularioProfessor();
        })
        .catch(error => {
            mostrarErro('resultadoProfessor', error.message);
        });
}

// ===== FUNÇÕES UTILITÁRIAS =====

function mostrarErro(containerId, mensagem) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="error">${mensagem}</div>`;
}

function mostrarSucesso(containerId, mensagem) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="success">${mensagem}</div>`;
}

function mostrarCarregando(containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = '<div class="loading">Carregando...</div>';
}

function formatarData(dataString) {
    if (!dataString) return 'N/A';

    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR');
}

// Funções para limpar formulários
function limparFormularioEmprestimo() {
    document.getElementById('matriculaEmprestimo').value = '';
    document.getElementById('codigoExemplar').value = '';
    document.getElementById('dataPrevista').value = '';
    document.getElementById('tipoUsuario').value = '';
}

function limparFormularioDevolucao() {
    document.getElementById('matriculaDevolucao').value = '';
    document.getElementById('codigoExemplarDevolucao').value = '';
}

function limparFormularioLivro() {
    document.getElementById('novoTitulo').value = '';
    document.getElementById('novoAutor').value = '';
    document.getElementById('novoIsbn').value = '';
    document.getElementById('novoAno').value = '';
    document.getElementById('novaEditora').value = '';
    document.getElementById('novaQuantidade').value = '';
}

function limparFormularioAluno() {
    document.getElementById('nomeAluno').value = '';
    document.getElementById('cpfAluno').value = '';
    document.getElementById('emailAluno').value = '';
    document.getElementById('matriculaAluno').value = '';
}

function limparFormularioProfessor() {
    document.getElementById('nomeProfessor').value = '';
    document.getElementById('cpfProfessor').value = '';
    document.getElementById('emailProfessor').value = '';
    document.getElementById('matriculaProfessor').value = '';
}