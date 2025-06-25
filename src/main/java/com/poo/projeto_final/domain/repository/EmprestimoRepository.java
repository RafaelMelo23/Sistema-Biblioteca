package com.poo.projeto_final.domain.repository;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository {

    void setStatusEmprestimo(StatusEmprestimo statusEmprestimo, Matricula matricula);

    long contarEmprestimosPorStatusPendente(Matricula matricula, Long livroId);

    List<Emprestimo> listarEmprestimosUsuario(Matricula matricula);

    List<Emprestimo> detalhesEmprestimoAtrasado(Matricula matricula, StatusEmprestimo statusEmprestimo);

    boolean existsByMatriculaAndStatusEmprestimo(Matricula matricula, StatusEmprestimo statusEmprestimo);

    Emprestimo listarEmprestimoPorId(Long emprestimoId);

    List<Emprestimo> emprestimosAtrasados(LocalDate dataReferencia, StatusEmprestimo status);

    void alterarStatusEmprestimo(List<Long> emprestimoId, StatusEmprestimo status);

    Emprestimo salvar(Emprestimo emprestimo);

}
