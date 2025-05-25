package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Professor;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOProfessor extends ListCrudRepository<Professor, Long> {

    boolean existsByEmailOrCpf(String email, String cpf);

    boolean existsByMatricula(String matricula);
}
