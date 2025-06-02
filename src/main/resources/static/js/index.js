let endpointsAPI = {};

window.onload = function() {
    carregarLinks();
};

function carregarLinks() {
    fetch('/api')
        .then(response => response.json())
        .then(data => {
            if (data._links) {
                endpointsAPI = data._links;
                console.log('Links carregados:', endpointsAPI);
            }
        })
        .catch(error => {
            console.error('Erro ao carregar links:', error);
            alert('Falha ao conectar com a API. Verifique o servidor.');
        });
}

function selecionarAba(aba) {
    document.querySelectorAll('.tab').forEach(item => item.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(item => item.classList.remove('active'));
    event.target.classList.add('active');
    document.getElementById(aba).classList.add('active');
}

function consultarEmprestimos() {
    const matricula = document.getElementById('matricula').value.trim();

    if (!matricula) {
        exibirErro('resultadosBusca', 'Informe a matrícula do usuário.');
        return;
    }

    if (!endpointsAPI['listar-emprestimos-by-matricula']) {
        exibirErro('resultadosBusca', 'Link não disponível.');
        return;
    }

    exibirCarregando('resultadosBusca');

    const url = endpointsAPI['listar-emprestimos-by-matricula'].href.replace('{matricula}', matricula);

    fetch(url)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Nenhum empréstimo para essa matrícula.');
                } else if (response.status === 400) {
                    throw new Error('Matrícula inválida.');
                } else {
                    throw new Error('Falha ao buscar empréstimos.');
                }
            }
            return response.json();
        })
        .then(data => {
            mostrarEmprestimos(data._embedded ? data._embedded.dTOListarEmprestimoList : []);
        })
        .catch(error => {
            exibirErro('resultadosBusca', error.message);
        });
}

function mostrarEmprestimos(listaEmprestimos) {
    const container = document.getElementById('resultadosBusca');

    if (!listaEmprestimos || listaEmprestimos.length === 0) {
        container.innerHTML = '<p class="loading">Nenhum empréstimo encontrado.</p>';
        return;
    }

    let html = '<h4>Empréstimos Encontrados:</h4><div class="results-grid">';

    listaEmprestimos.forEach(emprestimo => {
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
                       <span class="info-label">Data Empréstimo:</span> ${formatarDataBR(emprestimo.dataEmprestimo)}
                   </div>
                   <div class="info-item">
                       <span class="info-label">Data Prevista:</span> ${formatarDataBR(emprestimo.dataPrevista)}
                   </div>
               </div>
               <button onclick="detalhesEmprestimo('${emprestimo._links['emprestimo-especifico'].href}')">Ver Detalhes</button>
           </div>
       `;
    });

    html += '</div>';
    container.innerHTML = html;
}

function detalhesEmprestimo(url) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Falha ao buscar detalhes.');
            }
            return response.json();
        })
        .then(data => {
            alert(`Detalhes:

Usuário: ${data.nomeUsuario}
Matrícula: ${data.matricula}
Livro: ${data.titulo}
Autor: ${data.autor}
ISBN: ${data.isbn}
Ano: ${data.ano}
Editora: ${data.editora}
Código Exemplar: ${data.codigoExemplar}
Data Empréstimo: ${formatarDataBR(data.dataEmprestimo)}
Data Prevista: ${formatarDataBR(data.dataPrevista)}
Data Devolução: ${data.dataDevolucao ? formatarDataBR(data.dataDevolucao) : 'Não devolvido'}
Status: ${data.statusEmprestimo}`);
        })
        .catch(error => {
            alert('Falha ao carregar detalhes: ' + error.message);
        });
}

function criarEmprestimo() {
    const matricula = document.getElementById('matriculaEmprestimo').value.trim();
    const codigo = document.getElementById('codigoExemplar').value.trim();
    const dataPrevista = document.getElementById('dataPrevista').value;
    const tipoUsuario = document.getElementById('tipoUsuario').value;

    if (!matricula || !codigo || !dataPrevista || !tipoUsuario) {
        exibirErro('resultadoEmprestimo', 'Preencha todos os campos.');
        return;
    }

    if (!endpointsAPI['realizar-emprestimo']) {
        exibirErro('resultadoEmprestimo', 'Link não disponível.');
        return;
    }

    const novoEmprestimo = {
        codigoExemplar: codigo,
        matricula: matricula,
        dataPrevista: dataPrevista,
        tipoUsuario: tipoUsuario
    };

    fetch(endpointsAPI['realizar-emprestimo'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(novoEmprestimo)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos.');
                } else {
                    throw new Error('Falha ao realizar empréstimo.');
                }
            }
            exibirSucesso('resultadoEmprestimo', 'Empréstimo efetuado com sucesso!');
            resetFormEmprestimo();
        })
        .catch(error => {
            exibirErro('resultadoEmprestimo', error.message);
        });
}

function processarDevolucao() {
    const matricula = document.getElementById('matriculaDevolucao').value.trim();
    const codigo = document.getElementById('codigoExemplarDevolucao').value.trim();
    const tipoUsuario = document.getElementById('tipoUsuarioDevolucao').value;

    if (!matricula || !codigo || !tipoUsuario) {
        exibirErro('resultadoDevolucao', 'Preencha todos os campos.');
        return;
    }

    if (!endpointsAPI['realizar-devolucao']) {
        exibirErro('resultadoDevolucao', 'Link não disponível.');
        return;
    }

    const dadosRetorno = {
        codigoExemplar: codigo,
        matricula: matricula,
        tipoUsuario: tipoUsuario
    };

    fetch(endpointsAPI['realizar-devolucao'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosRetorno)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Dados inválidos.');
                } else {
                    throw new Error('Falha ao processar devolução.');
                }
            }
            exibirSucesso('resultadoDevolucao', 'Devolução realizada com sucesso!');
            resetFormDevolucao();
        })
        .catch(error => {
            exibirErro('resultadoDevolucao', error.message);
        });
}

function exibirErro(containerId, mensagem) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="error">${mensagem}</div>`;
}

function exibirSucesso(containerId, mensagem) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="success">${mensagem}</div>`;
}

function exibirCarregando(containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = '<div class="loading">Carregando...</div>';
}

function formatarDataBR(dataString) {
    if (!dataString) return 'N/A';
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR');
}

function resetFormEmprestimo() {
    document.getElementById('matriculaEmprestimo').value = '';
    document.getElementById('codigoExemplar').value = '';
    document.getElementById('dataPrevista').value = '';
    document.getElementById('tipoUsuario').value = '';
}

function resetFormDevolucao() {
    document.getElementById('matriculaDevolucao').value = '';
    document.getElementById('codigoExemplarDevolucao').value = '';
    document.getElementById('tipoUsuarioDevolucao').value = '';
}