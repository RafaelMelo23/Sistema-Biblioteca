package com.poo.projeto_final.application.usecase.usuario;

import com.poo.projeto_final.application.dto.DTOProfessor;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOProfessor;

import com.poo.projeto_final.domain.service.UsuarioService;
import org.springframework.stereotype.Component;

@Component
public class CriarProfessorUseCase {

    private final UsuarioService usuarioService;

    public CriarProfessorUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void criarProfessor(DTOProfessor dtoProfessor) {

       usuarioService.criarProfessor(dtoProfessor);
    }
}
