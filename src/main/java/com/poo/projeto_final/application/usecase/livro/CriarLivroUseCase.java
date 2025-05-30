package com.poo.projeto_final.application.usecase.livro;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.service.ExemplarLivroService;
import com.poo.projeto_final.domain.service.LivroService;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para criação de um livro, este é o principal exemplo onde um orquestrador faz sentido, ele faz uso das camadas
 * de serviço correspondentes à operação, porém que passam por diferentes validações e tratam diferentes entidades (Livro e Exemplar)
 * e esta classe apenas orquestra a operação, sem passar por validações ou regras de negócio.
 */
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
