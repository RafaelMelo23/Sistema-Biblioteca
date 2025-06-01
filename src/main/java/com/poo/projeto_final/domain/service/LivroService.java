package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import com.poo.projeto_final.domain.repository.DAOLivro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(LivroService.class);

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

        try {
            Livro livro = Livro.criarLivro(dtoLivro.titulo(), dtoLivro.autor(),
                    dtoLivro.isbn(), dtoLivro.ano(), dtoLivro.editora());

            return daoLivro.save(livro);

        } catch (Exception e) {
            logger.error("Erro ao cadastrar livro: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao cadastrar livro: " + e.getMessage());
        }

    }
}
