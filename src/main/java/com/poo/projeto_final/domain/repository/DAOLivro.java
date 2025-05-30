package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface DAOLivro extends ListCrudRepository<Livro, Long> {

    boolean existsByTitulo(Titulo titulo);

    boolean existsByIsbn(Isbn isbn);

    List<Livro> findByTituloContains(Titulo titulo);
}
