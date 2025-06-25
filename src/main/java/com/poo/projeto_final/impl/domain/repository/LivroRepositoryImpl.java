package com.poo.projeto_final.impl.domain.repository;

import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import com.poo.projeto_final.domain.repository.LivroRepository;
import com.poo.projeto_final.infrastructure.config.persistence.entities.LivroData;
import com.poo.projeto_final.infrastructure.config.persistence.mappers.LivroMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class LivroRepositoryImpl implements LivroRepository {



    @PersistenceContext
    private EntityManager entityManager;
    private final LivroMapper mapper;

    @Override
    public Livro findByExemplarId(Long exemplarId) {

        LivroData livroData =  entityManager.createQuery("""
                            SELECT e.livroData FROM ExemplarLivroData e
                            WHERE e.id = :id
                        """, LivroData.class)
                .setParameter("id", exemplarId)
                .getSingleResult();

        return mapper.toDomain(livroData);
    }

    @Override
    public void salvar(Livro livro) {

        LivroData livroData = mapper.toData(livro);

        if (livroData.getId() == null) {
            entityManager.persist(livroData);
        } else {
            entityManager.merge(livroData);
        }
    }

    @Override
    public boolean existsByTitulo(Titulo titulo) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(l) FROM LivroData l WHERE l.titulo = :titulo", Long.class)
                .setParameter("titulo", titulo)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByIsbn(Isbn isbn) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(l) FROM LivroData l WHERE l.isbn = :isbn", Long.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<Livro> findByTituloContains(Titulo titulo) {
        List<LivroData> livrosData = entityManager.createQuery(
                        "SELECT l FROM LivroData l WHERE LOWER(l.titulo.value) LIKE CONCAT('%', LOWER(:titulo), '%')", LivroData.class)
                .setParameter("titulo", titulo)
                .getResultList();

        return livrosData.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public String findTituloByExemplarId(Long exemplarId) {
        return entityManager.createQuery(
                        "SELECT ex.livroData.titulo FROM ExemplarLivroData ex WHERE ex.id = :exemplarId"
                ).setParameter("exemplarId", exemplarId)
                .getSingleResult().toString();
    }
}
