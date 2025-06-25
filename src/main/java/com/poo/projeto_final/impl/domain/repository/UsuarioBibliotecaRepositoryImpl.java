package com.poo.projeto_final.impl.domain.repository;


import com.poo.projeto_final.domain.repository.UsuarioBibliotecaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UsuarioBibliotecaRepositoryImpl implements UsuarioBibliotecaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsByEmailOrCpf(String email, String cpf) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM UsuarioBibliotecaData u WHERE u.email.value = :email OR u.cpf.value = :cpf", Long.class)
                .setParameter("email", email)
                .setParameter("cpf", cpf)
                .getSingleResult();
        return count > 0;
    }

}
