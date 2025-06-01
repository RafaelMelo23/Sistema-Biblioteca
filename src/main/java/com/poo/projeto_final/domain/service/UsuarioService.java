package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOAluno;
import com.poo.projeto_final.application.dto.DTOProfessor;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;
import com.poo.projeto_final.domain.repository.DAOAluno;
import com.poo.projeto_final.domain.repository.DAOProfessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de negócio relacionada aos usuários, validações e comunicação com o banco.
 */
@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    /**
     * Interfaces JPA para chamadas ao banco relacionadas à tabela de alunos e professores.
     */
    private final DAOAluno daoAluno;
    private final DAOProfessor daoProfessor;

    public UsuarioService(DAOAluno daoAluno, DAOProfessor daoProfessor) {
        this.daoAluno = daoAluno;
        this.daoProfessor = daoProfessor;
    }

    /**
     * Cadastra um novo aluno baseado no DTO recebido.
     * @param dtoAluno Dados do aluno a serem cadastrados.
     * @throws IllegalArgumentException Caso exista um aluno já cadastrado com algum dos dados informados no DTO.
     */
    public void criarAluno(DTOAluno dtoAluno) {

        if (daoAluno.existsByMatricula(Matricula.of(dtoAluno.matricula()))) {
            throw new IllegalArgumentException("Já existe um aluno registrado com a matrícula: " + dtoAluno.matricula());
        }

        if (daoAluno.existsByEmailOrCpf(Email.of(dtoAluno.email()), Cpf.of(dtoAluno.cpf()))) {
            throw new IllegalArgumentException("Já existe um aluno registrado com a matrícula: " + dtoAluno.matricula());
        }

        try {
            Aluno aluno = Aluno.of(dtoAluno.nome(), dtoAluno.email(), dtoAluno.cpf(), dtoAluno.matricula());

            daoAluno.save(aluno);
        } catch (Exception e) {
            log.error("Erro ao criar aluno: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao criar aluno: " + e.getMessage());
        }
    }

    /**
     * Cadastra um novo professor baseado no DTO recebido.
     * @param dtoProfessor Dados do professor a serem cadastrados.
     * @throws IllegalArgumentException Caso exista um professor já cadastrado com algum dos dados informados no DTO.
     */
    public void criarProfessor(DTOProfessor dtoProfessor) {

        if (daoProfessor.existsByMatricula(Matricula.of(dtoProfessor.matricula()))) {
            throw new IllegalArgumentException("Já existe um professor cadastrado com a mátricula " + dtoProfessor.matricula());
        }

        try {
            Professor professor = Professor.of(dtoProfessor.nome(), dtoProfessor.email(),
                    dtoProfessor.cpf(), dtoProfessor.matricula());

            daoProfessor.save(professor);
        } catch (Exception e) {
            log.error("Erro ao criar professor: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao criar professor: " + e.getMessage());
        }
    }

}
