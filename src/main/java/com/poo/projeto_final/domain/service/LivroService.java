package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import com.poo.projeto_final.domain.repository.DAOLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela lógica de negócio relacionada aos livros, validações e comunicação com o banco.
 */
@Service
public class LivroService {

    /**
     * Interface JPA para chamadas ao banco relacionadas à tabela de livros.
     */
    private final DAOLivro daoLivro;

    public LivroService(DAOLivro daoLivro) {
        this.daoLivro = daoLivro;
    }

    /**
     * Cadastra um novo livro baseado no DTO recebido.
     *
     * @param dtoLivro Dados do livro a serem cadastrados.
     * @return Livro cadastrado.
     * @throws IllegalArgumentException Caso o título ou ISBN já estejam cadastrados.
     */
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
