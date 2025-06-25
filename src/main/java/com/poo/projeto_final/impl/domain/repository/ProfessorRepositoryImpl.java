package com.poo.projeto_final.impl.domain.repository;

import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.ProfessorRepository;
import com.poo.projeto_final.infrastructure.config.persistence.entities.ProfessorData;
import com.poo.projeto_final.infrastructure.config.persistence.mappers.ProfessorMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProfessorRepositoryImpl implements ProfessorRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ProfessorMapper mapper;

    @Override
    public boolean existsByMatricula(Matricula matricula) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(p) FROM ProfessorData p WHERE p.matricula = :matricula", Long.class)
                .setParameter("matricula", matricula)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void salvar(Professor professor) {
        ProfessorData professorData = mapper.toData(professor);

        if (professorData.getId() == null) {
            entityManager.persist(professorData);
        } else
            entityManager.merge(professorData);
    }


    @Override
    public Optional<Professor> findByMatricula(Matricula matricula) {
        var query = entityManager.createQuery(
                "SELECT p FROM ProfessorData p WHERE p.matricula = :matricula", ProfessorData.class);
        query.setParameter("matricula", matricula);

        Optional<ProfessorData> professorOpt = query.getResultStream().findFirst();

        return professorOpt.map(mapper::toDomain);
    }

    @Override
    public Optional<String> findNameByMatricula(Matricula matricula) {
        return entityManager.createQuery("""
        SELECT pro.nome FROM ProfessorData pro WHERE pro.matricula = :matricula""", String.class)
                .setParameter("matricula", matricula)
                .getSingleResult().describeConstable();
    }
}
