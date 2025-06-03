let apiLinks = {};
let isSearching = false;
let loadingExemplares = new Set();
let initialized = false;

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

async function carregarApiLinks() {
    if (Object.keys(apiLinks).length > 0) {
        return;
    }
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
        if (!apiLinks['criar-livro']) throw new Error('Link da API não encontrado');
        const response = await fetch(apiLinks['criar-livro'].href, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(dadosLivro)
        });
        if (response.ok) {
            mostrarMensagem('mensagem-cadastro', 'Livro cadastrado com sucesso!', 'success');
            document.getElementById('formulario-cadastro-livro').reset();
        } else {
            throw new Error('Erro ao cadastrar livro');
        }
    } catch {
        mostrarMensagem('mensagem-cadastro', 'Erro ao cadastrar livro. Verifique os dados e tente novamente.', 'error');
    }
}

async function buscarLivrosPorTitulo(titulo) {
    if (isSearching) return;
    if (!titulo || titulo.trim() === '') {
        mostrarMensagem('mensagem-busca', 'Digite um título para buscar.', 'error');
        return;
    }
    try {
        if (!apiLinks['buscar-por-titulo']) throw new Error('Link da API não encontrado');
        isSearching = true;
        const btnBuscar = document.getElementById('btn-buscar');
        if (btnBuscar) {
            btnBuscar.disabled = true;
            btnBuscar.textContent = 'Buscando...';
        }
        mostrarCarregamento('mensagem-busca');
        const url = apiLinks['buscar-por-titulo'].href.replace('{titulo}', encodeURIComponent(titulo.trim()));
        const response = await fetch(url);
        if (!response.ok) throw new Error('Erro na busca');
        const data = await response.json();
        limparMensagem('mensagem-busca');
        if (data._embedded && data._embedded.livroList && data._embedded.livroList.length) {
            exibirResultadosBusca(data._embedded.livroList);
        } else {
            mostrarMensagem('mensagem-busca', 'Nenhum livro encontrado com esse título.', 'error');
            document.getElementById('resultados-busca').style.display = 'none';
        }
    } catch (error) {
        console.error('Erro na busca:', error);
        mostrarMensagem('mensagem-busca', 'Erro ao buscar livros. Tente novamente.', 'error');
        document.getElementById('resultados-busca').style.display = 'none';
    } finally {
        isSearching = false;
        const btnBuscar = document.getElementById('btn-buscar');
        if (btnBuscar) {
            btnBuscar.disabled = false;
            btnBuscar.textContent = 'Buscar';
        }
    }
}

async function buscarExemplaresLivro(endpoint) {
    try {
        const response = await fetch(endpoint);
        if (!response.ok) throw new Error('Erro ao buscar exemplares');
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
        const linksJSON = JSON.stringify(livro._links);
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
            <button class="btn-info" data-links='${linksJSON}' id="btn-exemplares-${livro.id}">
                Ver Exemplares
            </button>
            <div id="exemplares-${livro.id}" class="exemplares-section" style="display: none;">
                <h5>Exemplares:</h5>
                <div id="lista-exemplares-${livro.id}"></div>
            </div>
        `;
        listaLivros.appendChild(card);
        const btnExemplares = document.getElementById(`btn-exemplares-${livro.id}`);
        if (btnExemplares) {
            btnExemplares.addEventListener('click', debounce((e) => {
                e.preventDefault();
                e.stopPropagation();
                verExemplares(livro.id);
            }, 300));
        }
    });
    document.getElementById('resultados-busca').style.display = 'block';
}

async function verExemplares(livroId) {
    if (loadingExemplares.has(livroId)) return;
    const exemplaresDiv = document.getElementById(`exemplares-${livroId}`);
    const listaExemplares = document.getElementById(`lista-exemplares-${livroId}`);
    const btnExemplares = document.getElementById(`btn-exemplares-${livroId}`);
    if (!exemplaresDiv || !listaExemplares || !btnExemplares) return;
    if (exemplaresDiv.style.display === 'none') {
        try {
            loadingExemplares.add(livroId);
            btnExemplares.disabled = true;
            btnExemplares.textContent = 'Carregando...';
            exemplaresDiv.style.display = 'block';
            listaExemplares.innerHTML = '<div class="loading">Carregando exemplares...</div>';
            const links = JSON.parse(btnExemplares.getAttribute('data-links'));
            const statusSelecionado = document.getElementById('status-exemplar').value;
            let endpoint;
            if (statusSelecionado === 'DISPONIVEL') {
                endpoint = links['exemplares-disponiveis'].href;
            } else if (statusSelecionado === 'EMPRESTADO') {
                endpoint = links['exemplares-emprestados'].href;
            } else if (statusSelecionado === 'PERDIDO') {
                endpoint = links['exemplares-perdidos'].href;
            } else {
                endpoint = links['exemplares-disponiveis'].href;
            }
            const exemplares = await buscarExemplaresLivro(endpoint);
            if (!Array.isArray(exemplares) || exemplares.length === 0) {
                listaExemplares.innerHTML = '<p>Nenhum exemplar encontrado.</p>';
            } else {
                listaExemplares.innerHTML = '';
                exemplares.forEach(exemplar => {
                    const exemplarDiv = document.createElement('div');
                    exemplarDiv.className = 'exemplar-item';
                    exemplarDiv.innerHTML = `
                        <span><strong>Código:</strong> ${exemplar.codigoExemplar || 'N/A'}</span>
                        <span class="status-badge status-${(exemplar.statusExemplar || statusSelecionado).toLowerCase()}">
                            ${exemplar.statusExemplar || statusSelecionado}
                        </span>
                    `;
                    listaExemplares.appendChild(exemplarDiv);
                });
            }
            btnExemplares.textContent = 'Ocultar Exemplares';
        } catch (error) {
            console.error('Erro ao carregar exemplares:', error);
            listaExemplares.innerHTML = '<p>Erro ao carregar exemplares.</p>';
            btnExemplares.textContent = 'Ver Exemplares';
        } finally {
            loadingExemplares.delete(livroId);
            btnExemplares.disabled = false;
        }
    } else {
        exemplaresDiv.style.display = 'none';
        btnExemplares.textContent = 'Ver Exemplares';
    }
}

function mostrarMensagem(elementId, mensagem, tipo) {
    const elemento = document.getElementById(elementId);
    if (elemento) {
        elemento.innerHTML = `<div class="${tipo}">${mensagem}</div>`;
    }
}

function mostrarCarregamento(elementId) {
    const elemento = document.getElementById(elementId);
    if (elemento) {
        elemento.innerHTML = '<div class="loading">Carregando...</div>';
    }
}

function limparMensagem(elementId) {
    const elemento = document.getElementById(elementId);
    if (elemento) {
        elemento.innerHTML = '';
    }
}

const executarBusca = debounce(() => {
    const titulo = document.getElementById('busca-titulo').value.trim();
    if (titulo) {
        buscarLivrosPorTitulo(titulo);
    } else {
        mostrarMensagem('mensagem-busca', 'Digite um título para buscar.', 'error');
    }
}, 300);

function inicializar() {
    if (initialized) return;
    carregarApiLinks();
    const formCadastro = document.getElementById('formulario-cadastro-livro');
    if (formCadastro) {
        formCadastro.addEventListener('submit', function (e) {
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
    }
    const btnBuscar = document.getElementById('btn-buscar');
    if (btnBuscar) {
        btnBuscar.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            executarBusca();
        });
    }
    const campoBusca = document.getElementById('busca-titulo');
    if (campoBusca) {
        campoBusca.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                e.stopPropagation();
                executarBusca();
            }
        });
    }
    initialized = true;
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inicializar);
} else {
    inicializar();
}
