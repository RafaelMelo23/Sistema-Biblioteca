package com.rafael.lucas.biblioteca.sistema_biblioteca.application.usecase;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.DTOAluno;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Aluno;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository.DAOAluno;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CriarAlunoUseCase {

    private final DAOAluno daoAluno;

    public CriarAlunoUseCase(DAOAluno daoAluno) {
        this.daoAluno = daoAluno;
    }

    @Transactional
    public void criarAluno(DTOAluno dtoAluno) {

         if (daoAluno.existsByMatricula(dtoAluno.matricula())) {
             throw new IllegalArgumentException("Já existe um aluno registrado com a matrícula: " + dtoAluno.matricula());
         }

        Aluno aluno = Aluno.of(dtoAluno.nome(), dtoAluno.email(), dtoAluno.cpf(), dtoAluno.matricula());

        daoAluno.save(aluno);
    }

}
