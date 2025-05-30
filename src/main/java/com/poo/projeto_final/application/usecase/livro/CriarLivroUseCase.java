package com.poo.projeto_final.application.usecase.livro;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.service.ExemplarLivroService;
import com.poo.projeto_final.domain.service.LivroService;
import org.springframework.stereotype.Component;

@Component
public class CriarLivroUseCase {

    private final LivroService livroService;
    private final ExemplarLivroService exemplarLivroService;


    public CriarLivroUseCase(LivroService livroService, ExemplarLivroService exemplarLivroService) {
        this.livroService = livroService;
        this.exemplarLivroService = exemplarLivroService;
    }

    public void criarLivro(DTOLivro dto) {

        Livro novoLivro = livroService.cadastrarLivro(dto);

        exemplarLivroService.criarExemplar(novoLivro, dto.quantidade());
    }
}
