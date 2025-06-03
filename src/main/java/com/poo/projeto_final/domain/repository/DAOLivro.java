package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DAOLivro extends ListCrudRepository<Livro, Long> {

    boolean existsByTitulo(Titulo titulo);

    boolean existsByIsbn(Isbn isbn);

    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo.value) LIKE CONCAT('%', LOWER(:titulo), '%')")
    List<Livro> findByTituloContains(@Param("titulo") String titulo);
}
