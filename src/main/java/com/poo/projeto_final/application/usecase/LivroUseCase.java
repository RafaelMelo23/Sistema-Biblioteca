package com.poo.projeto_final.application.usecase;

import com.poo.projeto_final.application.dto.DTOExemplarLivro;
import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.model.livro.Isbn;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.livro.Titulo;
import com.poo.projeto_final.domain.repository.ExemplarLivroRepository;
import com.poo.projeto_final.domain.repository.LivroRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class LivroUseCase {
    
    private final LivroRepository livroRepository;
    private final ExemplarLivroRepository exemplarRepository;

    @Transactional
    public void cadastrarLivro(DTOLivro dto) {

        if (livroRepository.existsByTitulo(Titulo.of(dto.titulo()))) {
            throw new IllegalArgumentException("Titulo já cadastrado");
        }

        if (livroRepository.existsByIsbn(Isbn.of(dto.isbn()))) {
            throw new IllegalArgumentException("Isbn já cadastrado");
        }

        Livro livro = Livro.criarLivro(dto.titulo(), dto.autor(),
                dto.isbn(), dto.ano(), dto.editora());

        List<ExemplarLivro> exemplares = livro.criarExemplares(dto.quantidade(), exemplarRepository.contarTodos());

        try {
            livroRepository.salvar(livro);
            exemplarRepository.salvarAll(exemplares);

        } catch (Exception e) {
            log.error("Erro ao cadastrar livro: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao cadastrar livro: " + e.getMessage());
        }

    }

    public List<Livro> listarPorTitulo(String titulo) {

        return livroRepository.findByTituloContains(Titulo.of(titulo));
    }

    public List<DTOExemplarLivro> buscarExemplarPorStatus(Long livroId, StatusExemplar statusExemplar) {

        return exemplarRepository.buscarPorLivroEStatus(livroId, statusExemplar);
    }



}
