package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;

import java.util.List;

public interface LivroRepository {

    Livro salvar(Livro livro);

    boolean existsByTitulo(Titulo titulo);

    boolean existsByIsbn(Isbn isbn);

    List<Livro> findByTituloContains(Titulo titulo);

    String findTituloByExemplarId(Long exemplarId);

    Livro findByExemplarId(Long exemplarId);


}
