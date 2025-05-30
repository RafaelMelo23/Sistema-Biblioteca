package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.ExemplarLivroId;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOAluno;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import com.poo.projeto_final.domain.repository.DAOProfessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmprestimoService {

    private final DAOAluno daoAluno;
    private final DAOExemplarLivro daoExemplarLivro;
    private final DAOProfessor daoProfessor;
    private final DAOEmprestimo daoEmprestimo;

    public EmprestimoService(DAOAluno daoAluno, DAOExemplarLivro daoExemplarLivro, DAOProfessor daoProfessor, DAOEmprestimo daoEmprestimo) {
        this.daoAluno = daoAluno;
        this.daoExemplarLivro = daoExemplarLivro;
        this.daoProfessor = daoProfessor;
        this.daoEmprestimo = daoEmprestimo;
    }

    @Transactional
    public void registrarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        Matricula matricula;

        ExemplarLivroId exemplarId = daoExemplarLivro.findByCodigoExemplarValueIs(dtoEmprestimo.codigoExemplar(), StatusExemplar.DISPONIVEL)
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

        Emprestimo emprestimo = Emprestimo.realizarEmprestimo(matricula, exemplarId, dtoEmprestimo.dataPrevista(), StatusEmprestimo.PENDENTE);

        daoEmprestimo.save(emprestimo);
    }

    @Transactional
    public void atualizarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        ExemplarLivroId exemplarId = daoExemplarLivro.
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
