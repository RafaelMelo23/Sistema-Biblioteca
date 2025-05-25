package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Aluno;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface DAOAluno extends ListCrudRepository<Aluno, Long> {


    boolean existsByEmailOrCpf(String email, String cpf);

    boolean existsByMatricula(String matricula);

    Optional<Aluno> findByMatricula(String matricula);
}
