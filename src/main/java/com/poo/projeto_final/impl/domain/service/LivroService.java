package com.poo.projeto_final.impl.domain.service;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import com.poo.projeto_final.domain.repository.LivroRepository;
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
    private final LivroRepository livroRepository;
    Logger logger = LoggerFactory.getLogger(LivroService.class);

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    /**
     * Cadastra um novo livro baseado no DTO recebido.
     *
     * @param dtoLivro Dados do livro a serem cadastrados.
     * @Transactional garante que as mudanças no banco só sejam commitadas caso o método tenha sucesso, caso contrário, são revertidas
     * @return Livro cadastrado.
     * @throws IllegalArgumentException Caso o título ou ISBN já estejam cadastrados.
     */
    @Transactional
    public Livro cadastrarLivro(DTOLivro dtoLivro) {

        if (livroRepository.existsByTitulo(Titulo.of(dtoLivro.titulo()))) {
            throw new IllegalArgumentException("Titulo já cadastrado");
        }

        if (livroRepository.existsByIsbn(Isbn.of(dtoLivro.isbn()))) {
            throw new IllegalArgumentException("Isbn já cadastrado");
        }

        try {
            Livro livro = Livro.criarLivro(dtoLivro.titulo(), dtoLivro.autor(),
                    dtoLivro.isbn(), dtoLivro.ano(), dtoLivro.editora());

            return livroRepository.save(livro);

        } catch (Exception e) {
            logger.error("Erro ao cadastrar livro: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao cadastrar livro: " + e.getMessage());
        }

    }
}
