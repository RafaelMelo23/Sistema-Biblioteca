package com.poo.projeto_final.application.usecase;

import com.poo.projeto_final.application.dto.DTOAluno;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.repository.DAOAluno;

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
