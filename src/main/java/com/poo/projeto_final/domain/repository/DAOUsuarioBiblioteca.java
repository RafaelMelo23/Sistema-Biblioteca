package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.UsuarioBiblioteca;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOUsuarioBiblioteca extends ListCrudRepository<UsuarioBiblioteca, Long> {

    boolean existsByEmailOrCpf(String email, String cpf);
}
