package com.poo.projeto_final.application.usecase.usuario;

import com.poo.projeto_final.application.dto.DTOAluno;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOAluno;

import com.poo.projeto_final.domain.service.UsuarioService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso responsável por criar um usuário do tipo aluno
 */
@Component
public class CriarAlunoUseCase {

    private final UsuarioService usuarioService;

    public CriarAlunoUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Transactional
    public void criarAluno(DTOAluno dtoAluno) {

        usuarioService.criarAluno(dtoAluno);
    }

}
