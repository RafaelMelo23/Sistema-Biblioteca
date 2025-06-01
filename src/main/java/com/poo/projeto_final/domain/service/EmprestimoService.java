package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOAluno;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import com.poo.projeto_final.domain.repository.DAOProfessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela lógica de negócio relacionada aos empréstimos, validações e comunicação com o banco.
 */
@Service
public class EmprestimoService {

    private final DAOAluno daoAluno;
    private final DAOExemplarLivro daoExemplarLivro;
    private final DAOProfessor daoProfessor;
    private final DAOEmprestimo daoEmprestimo;
    Logger logger = LoggerFactory.getLogger(EmprestimoService.class);

    public EmprestimoService(DAOAluno daoAluno, DAOExemplarLivro daoExemplarLivro, DAOProfessor daoProfessor, DAOEmprestimo daoEmprestimo) {
        this.daoAluno = daoAluno;
        this.daoExemplarLivro = daoExemplarLivro;
        this.daoProfessor = daoProfessor;
        this.daoEmprestimo = daoEmprestimo;
    }

    /**
     * Realiza um empréstimo com base no DTO (Data Transfer Object) recebido.
     *
     * @param dtoEmprestimo Dados do emprestimo a ser registrado.
     * @throws IllegalArgumentException Caso o usuário tente registrar um emprestimo de um livro
     * com outro emprestimo pendente do mesmo livro.
     */
    @Transactional
    public void registrarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        Matricula matricula;

        Long exemplarId = daoExemplarLivro.findByCodigoExemplarValueIs(dtoEmprestimo.codigoExemplar(), StatusExemplar.DISPONIVEL)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível encontrar o livro, ou ele não está disponível"));

        Long livroId = daoExemplarLivro.findLivroIdByExemplarLivroId(exemplarId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível encontrar um livro associado ao exemplar"));

        switch (dtoEmprestimo.tipoUsuario()) {
            case ALUNO:
                Aluno aluno = daoAluno
                        .findByMatricula(Matricula.of(dtoEmprestimo.matricula()))
                        .orElseThrow(() -> new IllegalArgumentException("Aluno com a matrícula informada não existe")
                        );

                matricula = aluno.getMatricula();
                break;

            case PROFESSOR:
                Professor professor = daoProfessor
                        .findByMatricula(Matricula.of(dtoEmprestimo.matricula()))
                        .orElseThrow(() -> new IllegalArgumentException("Professor com a matrícula informada não existe")
                        );

                matricula = professor.getMatricula();
                break;

            default:
                throw new IllegalArgumentException("O tipo de usuário não foi informado");
        }

        if (daoEmprestimo.contarEmprestimosPorStatusPendente(matricula, livroId) > 0) {
            throw new IllegalArgumentException("Não é possível ter um emprestimo pendente do mesmo livro.");
        }

        daoExemplarLivro.setStatusExemplar(StatusExemplar.EMPRESTADO, exemplarId);

        try {
            Emprestimo emprestimo = Emprestimo.realizarEmprestimo(matricula, exemplarId, dtoEmprestimo.dataPrevista(), StatusEmprestimo.ATIVO);

            daoEmprestimo.save(emprestimo);
        } catch (Exception e) {
            logger.error("Erro ao registrar emprestimo: {}", e.getMessage(), e);
        }
    }

    /**
     * Atualiza (devolução) de um empréstimo já pendente.
     *
     * @param dtoEmprestimo Dados do emprestimo a ser atualizado.
     * @throws IllegalArgumentException Caso não for possível encontrar o livro e/ou no status adequado.
     * @throws IllegalArgumentException Caso não for possível encontrar o usuário com a matrícula informada.
     */
    @Transactional
    public void atualizarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        Long exemplarId = daoExemplarLivro.
                findByCodigoExemplarValueIs(dtoEmprestimo.codigoExemplar(), StatusExemplar.EMPRESTADO)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível encontrar o livro, ou ele não está emprestado"));

        boolean isUsuarioValido = switch (dtoEmprestimo.tipoUsuario()) {
            case ALUNO -> daoAluno.existsByMatricula(Matricula.of(dtoEmprestimo.matricula()));
            case PROFESSOR -> daoProfessor.existsByMatricula(Matricula.of(dtoEmprestimo.matricula()));
        };

        if (!isUsuarioValido) throw new IllegalArgumentException("Usuário não existente com a matrícula informada");

        daoExemplarLivro.setStatusExemplar(StatusExemplar.DISPONIVEL, exemplarId);
        daoEmprestimo.setStatusEmprestimo(StatusEmprestimo.FINALIZADO, Matricula.of(dtoEmprestimo.matricula()));
    }
}
