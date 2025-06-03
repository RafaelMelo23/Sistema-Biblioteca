let apiLinks = {};

async function carregarApiLinks() {
    try {
        const response = await fetch('/api');
        if (!response.ok) throw new Error('Erro ao carregar API links');
        const data = await response.json();
        apiLinks = data._links;
    } catch (error) {
        console.error('Erro ao carregar API links:', error);
    }
}

async function cadastrarLivro(dadosLivro) {
    try {
        if (!apiLinks['criar-livro']) {
            throw new Error('Link da API não encontrado');
        }

        const response = await fetch(apiLinks['criar-livro'].href, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dadosLivro)
        });

        if (response.ok) {
            mostrarMensagem('mensagem-cadastro', 'Livro cadastrado com sucesso!', 'success');
            document.getElementById('formulario-cadastro-livro').reset();
        } else {
            throw new Error('Erro ao cadastrar livro');
        }
    } catch (error) {
        mostrarMensagem('mensagem-cadastro', 'Erro ao cadastrar livro. Verifique os dados e tente novamente.', 'error');
    }
}

async function buscarLivrosPorTitulo(titulo) {
    try {
        if (!apiLinks['buscar-por-titulo']) {
            throw new Error('Link da API não encontrado');
        }

        mostrarCarregamento('mensagem-busca');

        const url = apiLinks['buscar-por-titulo'].href.replace('{titulo}', encodeURIComponent(titulo));
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error('Erro na busca');
        }

        const data = await response.json();
        limparMensagem('mensagem-busca');

        if (data._embedded && data._embedded.livroList && data._embedded.livroList.length > 0) {
            exibirResultadosBusca(data._embedded.livroList);
        } else {
            mostrarMensagem('mensagem-busca', 'Nenhum livro encontrado com esse título.', 'error');
            document.getElementById('resultados-busca').style.display = 'none';
        }
    } catch (error) {
        mostrarMensagem('mensagem-busca', 'Erro ao buscar livros. Tente novamente.', 'error');
        document.getElementById('resultados-busca').style.display = 'none';
    }
}

async function buscarExemplaresLivro(livroId, status = 'DISPONIVEL') {
    try {
        if (!apiLinks['buscar-livro-id']) {
            throw new Error('Link da API não encontrado');
        }

        const url = apiLinks['buscar-livro-id'].href
            .replace('{livroId}', livroId)
            .replace('{statusExemplar}', status);

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error('Erro ao buscar exemplares');
        }

        const data = await response.json();
        return Array.isArray(data) ? data : [];
    } catch (error) {
        console.error('Erro ao buscar exemplares:', error);
        return [];
    }
}

function exibirResultadosBusca(livros) {
    const listaLivros = document.getElementById('lista-livros');
    listaLivros.innerHTML = '';

    livros.forEach(livro => {
        const card = document.createElement('div');
        card.className = 'item-card';

        card.innerHTML = `
                    <div class="item-info">
                        <div class="info-item">
                            <span class="info-label">Título</span>
                            <span>${livro.titulo.value}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Autor</span>
                            <span>${livro.autor.value}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">ISBN</span>
                            <span>${livro.isbn.value}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Ano</span>
                            <span>${livro.ano.value}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Editora</span>
                            <span>${livro.editora.value}</span>
                        </div>
                    </div>
                    <button class="btn-info" onclick="verExemplares(${livro.id})">Ver Exemplares</button>
                    <div id="exemplares-${livro.id}" class="exemplares-section" style="display: none;">
                        <h5>Exemplares:</h5>
                        <div id="lista-exemplares-${livro.id}"></div>
                    </div>
                `;

        listaLivros.appendChild(card);
    });

    document.getElementById('resultados-busca').style.display = 'block';
}

async function verExemplares(livroId) {
    const exemplaresDiv = document.getElementById(`exemplares-${livroId}`);
    const listaExemplares = document.getElementById(`lista-exemplares-${livroId}`);

    if (exemplaresDiv.style.display === 'none') {
        exemplaresDiv.style.display = 'block';
        listaExemplares.innerHTML = '<div class="loading">Carregando exemplares...</div>';

        try {
            const statusTypes = ['DISPONIVEL', 'EMPRESTADO', 'PERDIDO'];
            let todosExemplares = [];

            for (const status of statusTypes) {
                const exemplares = await buscarExemplaresLivro(livroId, status);
                if (Array.isArray(exemplares) && exemplares.length > 0) {
                    const exemplaresComStatus = exemplares.map(ex => ({
                        ...ex,
                        statusExemplar: ex.statusExemplar || status
                    }));
                    todosExemplares = todosExemplares.concat(exemplaresComStatus);
                }
            }

            if (todosExemplares.length === 0) {
                listaExemplares.innerHTML = '<p>Nenhum exemplar encontrado.</p>';
            } else {
                listaExemplares.innerHTML = '';
                todosExemplares.forEach(exemplar => {
                    const exemplarDiv = document.createElement('div');
                    exemplarDiv.className = 'exemplar-item';
                    exemplarDiv.innerHTML = `
                                <span><strong>Código:</strong> ${exemplar.codigoExemplar || 'N/A'}</span>
                                <span class="status-badge status-${(exemplar.statusExemplar || 'disponivel').toLowerCase()}">${exemplar.statusExemplar || 'DISPONÍVEL'}</span>
                            `;
                    listaExemplares.appendChild(exemplarDiv);
                });
            }
        } catch (error) {
            console.error('Erro ao carregar exemplares:', error);
            listaExemplares.innerHTML = '<p>Erro ao carregar exemplares.</p>';
        }
    } else {
        exemplaresDiv.style.display = 'none';
    }
}

function mostrarMensagem(elementId, mensagem, tipo) {
    const elemento = document.getElementById(elementId);
    elemento.innerHTML = `<div class="${tipo}">${mensagem}</div>`;
}

function mostrarCarregamento(elementId) {
    const elemento = document.getElementById(elementId);
    elemento.innerHTML = '<div class="loading">Carregando...</div>';
}

function limparMensagem(elementId) {
    const elemento = document.getElementById(elementId);
    elemento.innerHTML = '';
}

document.getElementById('formulario-cadastro-livro').addEventListener('submit', function (e) {
    e.preventDefault();

    const dadosLivro = {
        titulo: document.getElementById('titulo').value,
        autor: document.getElementById('autor').value,
        isbn: document.getElementById('isbn').value,
        ano: document.getElementById('ano').value,
        editora: document.getElementById('editora').value,
        quantidade: parseInt(document.getElementById('quantidade').value)
    };

    cadastrarLivro(dadosLivro);
});

document.getElementById('btn-buscar').addEventListener('click', function () {
    const titulo = document.getElementById('busca-titulo').value.trim();

    if (titulo) {
        buscarLivrosPorTitulo(titulo);
    } else {
        mostrarMensagem('mensagem-busca', 'Digite um título para buscar.', 'error');
    }
});

document.getElementById('busca-titulo').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        document.getElementById('btn-buscar').click();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    carregarApiLinks();
});