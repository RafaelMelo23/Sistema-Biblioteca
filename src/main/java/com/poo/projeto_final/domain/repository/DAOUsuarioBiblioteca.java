package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOUsuarioBiblioteca extends ListCrudRepository<UsuarioBiblioteca, Long> {

    boolean existsByEmailOrCpf(String email, String cpf);
}
