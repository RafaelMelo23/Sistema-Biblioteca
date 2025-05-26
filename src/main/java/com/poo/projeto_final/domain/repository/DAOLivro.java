package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.livro.Livro;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOLivro extends ListCrudRepository<Livro, Long> {

}
