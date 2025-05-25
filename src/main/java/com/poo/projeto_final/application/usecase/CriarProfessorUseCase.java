package com.rafael.lucas.biblioteca.sistema_biblioteca.application.usecase;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.DTOProfessor;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Professor;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository.DAOProfessor;
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
