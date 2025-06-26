package com.poo.projeto_final.impl.domain.repository;

import com.poo.projeto_final.application.dto.DTOExemplarLivro;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.repository.ExemplarLivroRepository;
import com.poo.projeto_final.infrastructure.config.persistence.entities.ExemplarLivroData;
import com.poo.projeto_final.infrastructure.config.persistence.mappers.ExemplarLivroMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ExemplarLivroRepositoryImpl implements ExemplarLivroRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ExemplarLivroMapper mapper;

    @Override
    public long contarTodos() {
        return entityManager.createQuery("""
        SELECT COUNT(e) FROM ExemplarLivroData e""", Long.class).getSingleResult();
    }

    @Override
    public void salvar(ExemplarLivro exemplarLivro) {

        ExemplarLivroData exemplarLivroData = mapper.toData(exemplarLivro);

        if (exemplarLivro.getId() == null) {
            entityManager.persist(exemplarLivroData);
        } else {
            entityManager.merge(exemplarLivroData);
        }
    }

    @Override
    public void salvarAll(List<ExemplarLivro> exemplarLivros) {

        for (int i = 0; i < exemplarLivros.size(); i++) {

            ExemplarLivroData exemplarLivroData = mapper.toData(exemplarLivros.get(i));

            if (exemplarLivroData.getId() == null) {
                entityManager.persist(exemplarLivroData);
            } else {
                entityManager.merge(exemplarLivroData);
            }

            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
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

    public List<DTOExemplarLivro> buscarPorLivroEStatus(Long livroId, StatusExemplar status) {
        return entityManager.createQuery("""
        SELECT new com.poo.projeto_final.application.dto.DTOExemplarLivro(e.codigoExemplar.value, e.statusExemplar)
        FROM ExemplarLivroData e
        WHERE e.livroData.id = :livroId AND e.statusExemplar = :status
    """, DTOExemplarLivro.class)
                .setParameter("livroId", livroId)
                .setParameter("status", status)
                .getResultList();
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

    @Override
    public CodigoExemplar findCodigoExemplarByExemplarId(Long exemplarId) {
        return entityManager.createQuery("""
        SELECT ex.codigoExemplar FROM ExemplarLivroData ex WHERE ex.id = :exemplarId
    """, CodigoExemplar.class)
                .setParameter("exemplarId", exemplarId)
                .getSingleResult();
    }

}

