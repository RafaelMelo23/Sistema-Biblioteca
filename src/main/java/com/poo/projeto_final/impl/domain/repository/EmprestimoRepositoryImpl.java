package com.poo.projeto_final.impl.domain.repository;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.EmprestimoRepository;
import com.poo.projeto_final.infrastructure.config.persistence.entities.EmprestimoData;
import com.poo.projeto_final.infrastructure.config.persistence.mappers.EmprestimoMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class EmprestimoRepositoryImpl implements EmprestimoRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final EmprestimoMapper mapper;

    public Emprestimo salvar(Emprestimo emprestimo) {

        EmprestimoData emprestimoData = mapper.toData(emprestimo);

        EmprestimoData managedEmprestimo;

        if (emprestimoData.getId() == null) {

            entityManager.persist(emprestimoData);
            managedEmprestimo = emprestimoData;
        } else {
            managedEmprestimo = entityManager.merge(emprestimoData);
        }

        return mapper.toDomain(managedEmprestimo);
    }

    @Override
    public void setStatusEmprestimo(StatusEmprestimo statusEmprestimo, Matricula matricula) {
        entityManager.createQuery("""
            UPDATE EmprestimoData e SET e.statusEmprestimo = :status
            WHERE e.matricula = :matricula
        """)
                .setParameter("status", statusEmprestimo)
                .setParameter("matricula", matricula)
                .executeUpdate();
    }

    @Override
    public long contarEmprestimosPorStatusPendente(Matricula matricula, Long livroId) {
        return entityManager.createQuery("""
            SELECT COUNT(e)
            FROM EmprestimoData e
            JOIN ExemplarLivroData ex ON ex.id = e.exemplarLivroId
            WHERE e.matricula = :matricula
              AND ex.livroData.id = :livroId
              AND (e.statusEmprestimo = :ativo OR e.statusEmprestimo = :atrasado)""", Long.class)

                .setParameter("matricula", matricula)
                .setParameter("livroId", livroId)
                .setParameter("ativo", StatusEmprestimo.ATIVO)
                .setParameter("atrasado", StatusEmprestimo.ATRASADO)
                .getSingleResult();
    }

    @Override
    public List<Emprestimo> listarEmprestimosUsuario(Matricula matricula) {
        List<EmprestimoData> resultadosEmprestimos = entityManager.createQuery("""
            SELECT e FROM EmprestimoData e
            WHERE e.matricula = :matricula
            ORDER BY e.dataEmprestimo DESC
        """, EmprestimoData.class)
                .setParameter("matricula", matricula)
                .getResultList();

        return resultadosEmprestimos.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> detalhesEmprestimoAtrasado(Matricula matricula, StatusEmprestimo statusEmprestimo) {
        List<EmprestimoData> resultadosEmprestimos = entityManager.createQuery("""
            SELECT e FROM EmprestimoData e
            WHERE e.matricula = :matricula AND e.statusEmprestimo = :status
        """, EmprestimoData.class)
                .setParameter("matricula", matricula)
                .setParameter("status", statusEmprestimo)
                .getResultList();

        return resultadosEmprestimos.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByMatriculaAndStatusEmprestimo(Matricula matricula, StatusEmprestimo statusEmprestimo) {
        Long count = entityManager.createQuery("""
            SELECT COUNT(e)
            FROM EmprestimoData e
            WHERE e.matricula = :matricula AND e.statusEmprestimo = :status
        """, Long.class)
                .setParameter("matricula", matricula)
                .setParameter("status", statusEmprestimo)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Emprestimo listarEmprestimoPorId(Long emprestimoId) {
        EmprestimoData emprestimoData = entityManager.find(EmprestimoData.class, emprestimoId);

        return mapper.toDomain(emprestimoData);
    }

    @Override
    public List<Emprestimo> emprestimosAtrasados(LocalDate dataReferencia, StatusEmprestimo status) {
        List<EmprestimoData> resultadosEmprestimos = entityManager.createQuery("""
            SELECT e FROM EmprestimoData e
            WHERE e.statusEmprestimo = :status
              AND e.dataPrevista <= :dataReferencia
              AND e.dataFactual IS NULL
        """, EmprestimoData.class)
                .setParameter("status", status)
                .setParameter("dataReferencia", dataReferencia)
                .getResultList();

        return resultadosEmprestimos.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void alterarStatusEmprestimo(List<Long> emprestimoId, StatusEmprestimo status) {
        entityManager.createQuery("""
            UPDATE EmprestimoData e SET e.statusEmprestimo = :status
            WHERE e.id IN :ids
        """)
                .setParameter("status", status)
                .setParameter("ids", emprestimoId)
                .executeUpdate();
    }
}
