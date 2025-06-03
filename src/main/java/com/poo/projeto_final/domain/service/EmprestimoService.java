package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.*;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
     * @return O método retorna um DTOResultadoEmprestimo, que tem como objetivo indicar o sucesso ou não da operação, e os empréstimos atrasados caso o usuário tenha
     * @throws IllegalArgumentException Caso o usuário tente registrar um emprestimo de um livro com outro emprestimo pendente do mesmo livro.
     * @throws IllegalArgumentException Caso o exemplar não exista ou não esteja no status disponível.
     * @throws IllegalArgumentException Caso a matricula não seja encontrada no sistema ou tipo de usuário não seja informado.
     * @Transactional garante que as mudanças no banco só sejam commitadas caso o método tenha sucesso, caso contrário, são revertidas.
     */
    @Transactional
    public DTOResultadoEmprestimo registrarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        Matricula matricula;

        if (dtoEmprestimo.dataPrevista().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de entrega não pode ser antes do dia atual");
        }

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
            return new DTOResultadoEmprestimo(false, Collections.emptyList());
        }


        boolean sucesso = false;
        try {
            List<DTOListarEmprestimo> emprestimosAtrasados = new ArrayList<>();
            if (daoEmprestimo.existsByMatriculaAndStatusEmprestimo(matricula, StatusEmprestimo.ATRASADO)) {
                emprestimosAtrasados = daoEmprestimo.
                        detalhesEmprestimoAtrasado(matricula.getValue(), StatusEmprestimo.ATRASADO);

            }

            if (emprestimosAtrasados.isEmpty()) {

                daoExemplarLivro.setStatusExemplar(StatusExemplar.EMPRESTADO, exemplarId);
                daoEmprestimo.save(Emprestimo.realizarEmprestimo(matricula, exemplarId,
                        dtoEmprestimo.dataPrevista(), StatusEmprestimo.ATIVO));

                sucesso = true;
            }
            return new DTOResultadoEmprestimo(sucesso, emprestimosAtrasados);

        } catch (Exception e) {
            logger.error("Erro ao registrar emprestimo: {}", e.getMessage(), e);

            List<DTOListarEmprestimo> dtoEmprestimosAtrasados = daoEmprestimo.
                    detalhesEmprestimoAtrasado(matricula.getValue(), StatusEmprestimo.ATRASADO);

            return new DTOResultadoEmprestimo(false, dtoEmprestimosAtrasados);
        }
    }

    /**
     * Atualiza (devolução) de um empréstimo já pendente.
     *
     * @param dtoEmprestimo Dados do emprestimo a ser atualizado.
     * @throws IllegalArgumentException Caso não for possível encontrar o livro e/ou no status adequado.
     * @throws IllegalArgumentException Caso não for possível encontrar o usuário com a matrícula informada.
     * @Transactional garante que as mudanças no banco só sejam commitadas caso o método tenha sucesso, caso contrário, são revertidas.
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

    /**
     * Método para atualizar os status de empréstimos ainda não entregues há 5 dias para atrasado/perdido, é utilizado dentro de um @Scheduled, para rodar periodicamente meia noite diariamente.
     */
    public void executarEmprestimoAtrasado() {

        Date dataAtualMenosCinco = Date.from(
                LocalDate.now()
                        .minusDays(5)
                        .atStartOfDay().toInstant(ZoneOffset.of(ZoneId.systemDefault().getId()))
        );

        List<DTOEmprestimoAtrasado> dtoAtrasado = daoEmprestimo.emprestimosAtrasados(dataAtualMenosCinco, StatusEmprestimo.ATIVO);

        List<Long> emprestimoIds = new ArrayList<>();
        List<Long> exemplarIds = new ArrayList<>();

        for (DTOEmprestimoAtrasado dtoEmprestimo : dtoAtrasado) {
            emprestimoIds.add(dtoEmprestimo.emprestimoId());
            exemplarIds.add(dtoEmprestimo.exemplarLivroId());
        }

        daoEmprestimo.alterarStatusEmprestimo(emprestimoIds, StatusEmprestimo.ATRASADO);
        daoExemplarLivro.alterarStatusExemplar(exemplarIds, StatusExemplar.PERDIDO);

    }
}
