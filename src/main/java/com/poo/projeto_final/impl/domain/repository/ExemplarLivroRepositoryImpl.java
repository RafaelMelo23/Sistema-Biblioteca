package com.poo.projeto_final.impl.domain.repository;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.repository.ExemplarLivroRepository;
import com.poo.projeto_final.infrastructure.config.persistence.entities.ExemplarLivroData;
import com.poo.projeto_final.infrastructure.config.persistence.mappers.ExemplarLivroMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ExemplarLivroRepositoryImpl implements ExemplarLivroRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ExemplarLivroMapper mapper;

    @Override
    public long contarTodos() {
        return entityManager.createQuery("SELECT COUNT(e) FROM ExemplarLivroData e", Long.class)
                .getSingleResult();
    }

    @Override
    public void salvarAll(List<ExemplarLivro> exemplarLivros) {

        for (int i = 0; i < exemplarLivros.size(); i++) {
            ExemplarLivro exemplar = exemplarLivros.get(i);

            if (exemplar.getId() == null) {
                entityManager.persist(exemplar);
            } else {
                entityManager.merge(exemplar);
            }

            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    @Override
    public void salvar(ExemplarLivro exemplarLivro) {
        if (exemplarLivro.getId() == null) {
            entityManager.persist(exemplarLivro);
        } else {
            entityManager.merge(exemplarLivro);
        }
    }

    @Override
    public void setStatusExemplar(StatusExemplar statusExemplar, Long exemplarId) {
        entityManager.createQuery("""
            UPDATE ExemplarLivroData e SET e.statusExemplar = :status
            WHERE e.id = :id
        """)
                .setParameter("status", statusExemplar)
                .setParameter("id", exemplarId)
                .executeUpdate();
    }

    @Override
    public Optional<Long> buscarExemplarDisponivelIdPorCodigo(String codigoExemplar, StatusExemplar status) {
        return entityManager.createQuery("""
            SELECT e.id FROM ExemplarLivroData e
            WHERE e.codigoExemplar.value = :codigo
            AND e.statusExemplar = :status
        """, Long.class)
                .setParameter("codigo", codigoExemplar)
                .setParameter("status", status)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Long> buscarLivroIdPorExemplarId(Long exemplarLivroId) {
        return entityManager.createQuery("""
            SELECT e.livroData.id FROM ExemplarLivroData e
            WHERE e.id = :id
        """, Long.class)
                .setParameter("id", exemplarLivroId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<ExemplarLivro> buscarPorLivroEStatus(Long livroId, StatusExemplar status) {
        List<ExemplarLivroData> exemplares = entityManager.createQuery("""
            SELECT e FROM ExemplarLivroData e
            WHERE e.livroData.id = :livroId AND e.statusExemplar = :status
        """, ExemplarLivroData.class)
                .setParameter("livroId", livroId)
                .setParameter("status", status)
                .getResultList();

        return exemplares.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void alterarStatusExemplar(List<Long> exemplarIds, StatusExemplar status) {
        entityManager.createQuery("""
            UPDATE ExemplarLivroData e SET e.statusExemplar = :status
            WHERE e.id IN :ids
        """)
                .setParameter("status", status)
                .setParameter("ids", exemplarIds)
                .executeUpdate();
    }

    public CodigoExemplar findCodigoExemplarByExemplarId(Long exemplarId) {
        return entityManager.createQuery("""
        SELECT ex.codigoExemplar FROM ExemplarLivroData ex WHERE ex.id = :exemplarId""", CodigoExemplar.class
        ).getSingleResult();
    }
}
