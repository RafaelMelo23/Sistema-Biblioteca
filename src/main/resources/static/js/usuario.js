function switchTab(nomeAba) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'))
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'))
    event.target.classList.add('active')
    document.getElementById(nomeAba).classList.add('active')
}

function formataCPF(visivel) {
    let v = visivel.value.replace(/\D/g, '')
    v = v.replace(/(\d{3})(\d)/, '$1.$2')
    v = v.replace(/(\d{3})(\d)/, '$1.$2')
    v = v.replace(/(\d{3})(\d{1,2})$/, '$1-$2')
    visivel.value = v
}

document.getElementById('cpfAluno').addEventListener('input', function () {
    formataCPF(this)
})

document.getElementById('cpfProfessor').addEventListener('input', function () {
    formataCPF(this)
})

async function cadastrarAluno() {
    let nomeCampo = document.getElementById('nomeAluno').value.trim()
    let emailCampo = document.getElementById('emailAluno').value.trim()
    let cpfVisual = document.getElementById('cpfAluno').value.trim()
    let matriculaCampo = document.getElementById('matriculaAluno').value.trim()
    let menssageAluno = document.getElementById('messageAluno')
    let botaoAl = document.getElementById('btnCadastrarAluno')

    if (!nomeCampo || !emailCampo || !cpfVisual || !matriculaCampo) {
        exibeMsg(menssageAluno, 'Preencha tudo direitinho', 'error')
        return
    }

    botaoAl.disabled = true
    botaoAl.textContent = 'Registrando...'

    let cpfEnvio = cpfVisual.replace(/\D/g, '')

    try {
        let resp = await fetch('/api/usuario/cadastro/aluno', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                nome: nomeCampo,
                cpf: cpfEnvio,
                email: emailCampo,
                matricula: matriculaCampo
            })
        })

        if (resp.status === 204) {
            exibeMsg(menssageAluno, 'Aluno cadastrado', 'success')
            limparForm('aluno')
        } else if (resp.status === 400) {
            let msg = await resp.text()
            exibeMsg(menssageAluno, msg || 'Dados inválidos', 'error')
        } else if (resp.status === 404) {
            exibeMsg(menssageAluno, 'Nenhum dado retornado', 'error')
        } else if (resp.status === 500) {
            let msg = await resp.text()
            exibeMsg(menssageAluno, msg || 'Erro interno no servidor', 'error')
        } else {
            exibeMsg(menssageAluno, 'Algo deu errado, tenta de novo', 'error')
        }
    } catch (e) {
        exibeMsg(menssageAluno, 'Problema de conexão', 'error')
    } finally {
        botaoAl.disabled = false
        botaoAl.textContent = 'Cadastrar Aluno'
    }
}

async function cadastrarProfessor() {
    let nomeProf = document.getElementById('nomeProfessor').value.trim()
    let emailProf = document.getElementById('emailProfessor').value.trim()
    let cpfVisProf = document.getElementById('cpfProfessor').value.trim()
    let matriculaProf = document.getElementById('matriculaProfessor').value.trim()
    let menssageProf = document.getElementById('messageProfessor')
    let botaoProf = document.getElementById('btnCadastrarProfessor')

    if (!nomeProf || !emailProf || !cpfVisProf || !matriculaProf) {
        exibeMsg(menssageProf, 'Todos os campos precisam ser preenchidos', 'error')
        return
    }

    botaoProf.disabled = true
    botaoProf.textContent = 'Registrando...'

    let cpfEnvioProf = cpfVisProf.replace(/\D/g, '')

    try {
        let resp = await fetch('/api/usuario/cadastro/professor', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                nome: nomeProf,
                cpf: cpfEnvioProf,
                email: emailProf,
                matricula: matriculaProf
            })
        })

        if (resp.status === 204) {
            exibeMsg(menssageProf, 'Professor cadastrado', 'success')
            limparForm('professor')
        } else if (resp.status === 400) {
            let msg = await resp.text()
            exibeMsg(menssageProf, msg || 'Dados inválidos', 'error')
        } else if (resp.status === 404) {
            exibeMsg(menssageProf, 'Nenhum dado retornado', 'error')
        } else if (resp.status === 500) {
            let msg = await resp.text()
            exibeMsg(menssageProf, msg || 'Erro no servidor', 'error')
        } else {
            exibeMsg(menssageProf, 'Algo inesperado aconteceu', 'error')
        }
    } catch (e) {
        exibeMsg(menssageProf, 'Falha na conexão', 'error')
    } finally {
        botaoProf.disabled = false
        botaoProf.textContent = 'Cadastrar Professor'
    }
}

function exibeMsg(el, txt, tipo) {
    el.innerHTML = `<div class="${tipo}">${txt}</div>`
    setTimeout(() => {
        el.innerHTML = ''
    }, 4000)
}

function limparForm(tipo) {
    if (tipo === 'aluno') {
        document.getElementById('nomeAluno').value = ''
        document.getElementById('emailAluno').value = ''
        document.getElementById('cpfAluno').value = ''
        document.getElementById('matriculaAluno').value = ''
    } else if (tipo === 'professor') {
        document.getElementById('nomeProfessor').value = ''
        document.getElementById('emailProfessor').value = ''
        document.getElementById('cpfProfessor').value = ''
        document.getElementById('matriculaProfessor').value = ''
    }
}
