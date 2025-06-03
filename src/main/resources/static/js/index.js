let endpointsAPI = {}

window.onload = function() {
    carregarLinks()
}

function carregarLinks() {
    fetch('/api')
        .then(response => response.json())
        .then(data => {
            if(data._links) {
                endpointsAPI = data._links
                console .log('Links carregados:', endpointsAPI)
            }
        })
        .catch(err => {
            console .error('Falha ao carregar links:', err)
            alert('Não foi possível conectar à API. Verifique o servidor.')
        })
}

function selecionarAba(aba) {
    document.querySelectorAll('.tab').forEach(item => item.classList.remove('active'))
    document.querySelectorAll('.tab-content').forEach(item => item.classList.remove('active'))
    event.target.classList.add('active')
    document.getElementById(aba).classList.add('active')
}

function consultarEmprestimos () {
    var matricula = document.getElementById('matricula').value.trim()

    if(!matricula) {
        exibirErro('resultadosBusca', 'Informe a matrícula do usuário.')
        return
    }

    if(!endpointsAPI['listar-emprestimos-by-matricula']) {
        exibirErro('resultadosBusca', 'Link não disponível.')
        return
    }

    exibirCarregando('resultadosBusca')

    let url = endpointsAPI['listar-emprestimos-by-matricula'].href.replace('{matricula}', matricula)

    fetch(url)
        .then(resp => {
            if(!resp.ok) {
                if(resp.status === 404) throw new Error('Nenhum empréstimo para essa matrícula.')
                else if(resp.status === 400) throw new Error('Matrícula inválida.')
                else throw new Error('Falha ao buscar empréstimos.')
            }
            return resp.json()
        })
        .then(data => {
            mostrarEmprestimos(data._embedded ? data._embedded.dTOListarEmprestimoList : [])
        })
        .catch(e => {
            exibirErro('resultadosBusca', e.message)
        })
}

function mostrarEmprestimos(listaEmprestimos) {
    const container = document.getElementById('resultadosBusca')

    if(!listaEmprestimos || listaEmprestimos.length === 0) {
        container.innerHTML = '<p class="loading">Nenhum empréstimo encontrado.</p>'
        return
    }

    let html = '<h4>Empréstimos Encontrados:</h4><div class="results-grid">'

    listaEmprestimos.forEach(emp => {
        const statusClass = emp.statusEmprestimo === 'ATIVO' ? 'status-ativo' : 'status-devolvido'

        html += `
           <div class="item-card emprestimo-card ${statusClass}">
               <div class="item-info">
                   <div class="info-item">
                       <span class="info-label">Usuário:</span> ${emp.nomeUsuario}
                   </div>
                   <div class="info-item">
                       <span class="info-label">Status:</span> ${emp.statusEmprestimo}
                   </div>
                   <div class="info-item">
                       <span class="info-label">Livro:</span> ${emp.tituloLivro}
                   </div>
                   <div class="info-item">
                       <span class="info-label">Código:</span> ${emp.codigoExemplar}
                   </div>
                   <div class="info-item">
                       <span class="info-label">Data Empréstimo:</span> ${formatarDataBR(emp.dataEmprestimo)}
                   </div>
                   <div class="info-item">
                       <span class="info-label">Data Prevista:</span> ${formatarDataBR(emp.dataPrevista)}
                   </div>
               </div>
               <button onclick="detalhesEmprestimo('${emp._links['emprestimo-especifico'].href}')">Ver Detalhes</button>
           </div>
       `
    })

    html += '</div>'
    container.innerHTML = html
}

function detalhesEmprestimo(url) {
    fetch(url)
        .then(res => {
            if(!res.ok) throw new Error('Falha ao buscar detalhes.')
            return res.json()
        })
        .then(dado => {
            alert(`Detalhes:

Usuário: ${dado.nomeUsuario}
Matrícula: ${dado.matricula}
Livro: ${dado.titulo}
Autor: ${dado.autor}
ISBN: ${dado.isbn}
Ano: ${dado.ano}
Editora: ${dado.editora}
Código Exemplar: ${dado.codigoExemplar}
Data Empréstimo: ${formatarDataBR(dado.dataEmprestimo)}
Data Prevista: ${formatarDataBR(dado.dataPrevista)}
Data Devolução: ${dado.dataDevolucao ? formatarDataBR(dado.dataDevolucao) : 'Não devolvido'}
Status: ${dado.statusEmprestimo}`)
        })
        .catch(err => {
            alert('Falha ao carregar detalhes: ' + err.message)
        })
}

function criarEmprestimo() {
    var matriculaEmp = document.getElementById('matriculaEmprestimo').value.trim()
    var codigo = document.getElementById('codigoExemplar').value.trim()
    var dataPrev = document.getElementById('dataPrevista').value
    var tipoUsu = document.getElementById('tipoUsuario').value

    if(!matriculaEmp || !codigo || !dataPrev || !tipoUsu) {
        exibirErro('resultadoEmprestimo', 'Preencha todos os campos.')
        return
    }

    const linkRealizar = endpointsAPI['realizar-emprestimo']
    if(!linkRealizar) {
        exibirErro('resultadoEmprestimo', 'Link não disponível.')
        return
    }

    let novoEmp = {
        codigoExemplar: codigo,
        matricula: matriculaEmp,
        dataPrevista: dataPrev,
        tipoUsuario: tipoUsu
    }

    exibirCarregando('resultadoEmprestimo')

    fetch(linkRealizar.href, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(novoEmp)
    })
        .then(response => {
            if(!response.ok) {
                if(response.status === 400) throw new Error('Dados inválidos.')
                else if(response.status === 500) throw new Error('Erro interno do servidor.')
                else throw new Error('Falha ao realizar empréstimo.')
            }
            return response.json()
        })
        .then(respData => {
            console .log('Resposta da API (criarEmprestimo):', respData)

            const sucesso = (typeof respData.sucesso === 'boolean')
                ? respData.sucesso
                : (respData.sucesso === 'true' || respData.sucesso === true || respData.sucesso === '1')

            if(sucesso) {
                exibirSucesso('resultadoEmprestimo', 'Empréstimo efetuado com sucesso!')
                resetFormEmprestimo()
            } else {
                if(Array.isArray(respData.emprestimosPendentes) && respData.emprestimosPendentes.length > 0) {
                    exibirPendenciasEmprestimo(respData.emprestimosPendentes)
                } else {
                    exibirErro('resultadoEmprestimo', 'Não foi possível realizar o empréstimo. Verifique os dados informados.')
                }
            }
        })
        .catch(err => {
            console .error('Erro na requisição:', err)
            exibirErro('resultadoEmprestimo', err.message || 'Erro desconhecido.')
        })
}

function exibirPendenciasEmprestimo(pendentes) {
    var container = document.getElementById('resultadoEmprestimo')

    let html = '<div class="error"><h4>Empréstimo não realizado!</h4>'
    html += '<p>O usuário possui as seguintes pendências:</p>'
    html += '<div class="pendencias-grid">'

    pendentes.forEach(emp => {
        const statusClass = emp.statusEmprestimo === 'ATIVO' ? 'status-ativo' : 'status-devolvido'
        const isAtras = emp.statusEmprestimo === 'ATIVO' &&
            new Date(emp.dataPrevista) < new Date()

        html += `
            <div class="pendencia-card ${statusClass} ${isAtras ? 'atrasado' : ''}">
                <div class="pendencia-info">
                    <div class="info-item">
                        <span class="info-label">Livro:</span> ${emp.tituloLivro}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Código:</span> ${emp.codigoExemplar}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Data Empréstimo:</span> ${formatarDataBR(emp.dataEmprestimo)}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Data Prevista:</span> ${formatarDataBR(emp.dataPrevista)}
                    </div>
                    <div class="info-item">
                        <span class="info-label">Status:</span> ${emp.statusEmprestimo} ${isAtras ? ' <span class="texto-atrasado">(ATRASADO)</span>' : ''}
                    </div>
                </div>
            </div>
        `
    })

    html += '</div>'
    html += '<p><strong>Regularize as pendências antes de realizar um novo empréstimo.</strong></p>'
    html += '</div>'

    container.innerHTML = html
}

function processarDevolucao() {
    var matricula = document.getElementById('matriculaDevolucao').value.trim()
    var codigo = document.getElementById('codigoExemplarDevolucao').value.trim()
    var tipoUsuario = document.getElementById('tipoUsuarioDevolucao').value

    if(!matricula || !codigo || !tipoUsuario) {
        exibirErro('resultadoDevolucao', 'Preencha todos os campos.')
        return
    }

    if(!endpointsAPI['realizar-devolucao']) {
        exibirErro('resultadoDevolucao', 'Link não disponível.')
        return
    }

    let dadosRetorno = {
        codigoExemplar: codigo,
        matricula: matricula,
        tipoUsuario: tipoUsuario
    }

    fetch(endpointsAPI['realizar-devolucao'].href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosRetorno)
    })
        .then(res => {
            if(!res.ok) {
                if(res.status === 400) throw new Error('Dados inválidos.')
                else throw new Error('Falha ao processar devolução.')
            }
            exibirSucesso('resultadoDevolucao', 'Devolução realizada com sucesso!')
            resetFormDevolucao()
        })
        .catch(err => {
            exibirErro('resultadoDevolucao', err.message)
        })
}

function exibirErro(containerId, mensagem) {
    const container = document.getElementById(containerId)
    container.innerHTML = `<div class="error">${mensagem}</div>`
}

function exibirSucesso(containerId, mensagem) {
    const container = document.getElementById(containerId)
    container.innerHTML = `<div class="success">${mensagem}</div>`
}

function exibirCarregando(containerId) {
    const container = document.getElementById(containerId)
    container.innerHTML = '<div class="loading">Carregando...</div>'
}

function formatarDataBR(dataString) {
    if(!dataString) return 'N/A'
    const data = new Date(dataString)
    return data.toLocaleDateString('pt-BR')
}

function resetFormEmprestimo() {
    document.getElementById('matriculaEmprestimo').value = ''
    document.getElementById('codigoExemplar').value = ''
    document.getElementById('dataPrevista').value = ''
    document.getElementById('tipoUsuario').value = ''
}

function resetFormDevolucao() {
    document.getElementById('matriculaDevolucao').value = ''
    document.getElementById('codigoExemplarDevolucao').value = ''
    document.getElementById('tipoUsuarioDevolucao').value = ''
}