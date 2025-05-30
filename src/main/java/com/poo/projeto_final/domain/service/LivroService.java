package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import com.poo.projeto_final.domain.repository.DAOLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroService {

    private final DAOLivro daoLivro;

    public LivroService(DAOLivro daoLivro) {
        this.daoLivro = daoLivro;
    }

    @Transactional
    public Livro cadastrarLivro(DTOLivro dtoLivro) {

        if (daoLivro.existsByTitulo(Titulo.of(dtoLivro.titulo()))) {
            throw new IllegalArgumentException("Titulo já cadastrado");
        }

        if (daoLivro.existsByIsbn(Isbn.of(dtoLivro.isbn()))) {
            throw new IllegalArgumentException("Isbn já cadastrado");
        }

        Livro livro = Livro.criarLivro(dtoLivro.titulo(), dtoLivro.autor(),
                dtoLivro.isbn(), dtoLivro.ano(), dtoLivro.editora());

        daoLivro.save(livro);

        return livro;
    }

}
