package com.poo.projeto_final.impl.domain.repository;


import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;
import com.poo.projeto_final.domain.repository.AlunoRepository;
import com.poo.projeto_final.infrastructure.config.persistence.entities.AlunoData;
import com.poo.projeto_final.infrastructure.config.persistence.mappers.AlunoMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class AlunoRepositoryImpl implements AlunoRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final AlunoMapper mapper;

    @Override
    public Optional<String> findNameByMatricula(Matricula matricula) {
        return entityManager.createQuery("""
        SELECT al.nome FROM AlunoData al WHERE al.matricula = :matricula""", String.class)
                .setParameter("matricula", matricula)
                .getSingleResult().describeConstable();
    }

    @Override
    public void salvar(Aluno aluno) {

        AlunoData data = mapper.toData(aluno);

        if (data.getId() == null) {
            entityManager.persist(data);
        } else {
            entityManager.merge(data);
        }
    }

    @Override
    public boolean existsByEmailOrCpf(Email email, Cpf cpf) {
        var query = entityManager.createQuery("""
                SELECT COUNT(a) FROM AlunoData a
                WHERE a.email = :email OR a.cpf = :cpf
                """, Long.class);
        query.setParameter("email", email);
        query.setParameter("cpf", cpf);
        return query.getSingleResult() > 0;
    }

    @Override
    public boolean existsByMatricula(Matricula matricula) {
        var query = entityManager.createQuery("""
                SELECT COUNT(a) FROM AlunoData a
                WHERE a.matricula = :matricula
                """, Long.class);
        query.setParameter("matricula", matricula);
        return query.getSingleResult() > 0;
    }

    @Override
    public Optional<Aluno> findByMatricula(Matricula matricula) {
        var query = entityManager.createQuery("""
                SELECT a FROM AlunoData a
                WHERE a.matricula = :matricula
                """, AlunoData.class);
        query.setParameter("matricula", matricula);

        Optional<AlunoData> alunoOpt = query.getResultStream().findFirst();

        return alunoOpt.map(mapper::toDomain);
    }
}
