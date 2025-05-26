package com.poo.projeto_final.application.usecase;

import com.poo.projeto_final.application.dto.DTOProfessor;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.repository.DAOProfessor;

import org.springframework.stereotype.Component;

@Component
public class CriarProfessorUseCase {

    private final DAOProfessor daoProfessor;

    public CriarProfessorUseCase(DAOProfessor daoProfessor) {
        this.daoProfessor = daoProfessor;
    }

    public void criarProfessor(DTOProfessor dtoProfessor) {

       if (daoProfessor.existsByMatricula(dtoProfessor.matricula())) {
            throw new IllegalArgumentException("Já existe um professor cadastrado com a mátricula " + dtoProfessor.matricula());
        }

       Professor professor = Professor.of(dtoProfessor.nome(), dtoProfessor.email(),
               dtoProfessor.cpf(), dtoProfessor.matricula());

       daoProfessor.save(professor);
    }
}
