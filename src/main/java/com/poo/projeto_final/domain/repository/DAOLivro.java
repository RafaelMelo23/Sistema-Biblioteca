package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Livro;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOLivro extends ListCrudRepository<Livro, Long> {

}
