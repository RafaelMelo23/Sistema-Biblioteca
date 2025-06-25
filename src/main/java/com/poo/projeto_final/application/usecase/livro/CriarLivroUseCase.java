package com.poo.projeto_final.application.usecase.livro;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.impl.domain.service.ExemplarLivroServiceImpl;
import com.poo.projeto_final.impl.domain.service.LivroService;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para criação de um livro, este é o principal exemplo onde um orquestrador faz sentido, ele faz uso das camadas
 * de serviço correspondentes à operação, porém que passam por diferentes validações e tratam diferentes entidades (Livro e Exemplar)
 * e esta classe apenas orquestra a operação, sem passar por validações ou regras de negócio.
 */
@Component
public class CriarLivroUseCase {

    private final LivroService livroService;
    private final ExemplarLivroServiceImpl exemplarLivroServiceImpl;


    public CriarLivroUseCase(LivroService livroService, ExemplarLivroServiceImpl exemplarLivroServiceImpl) {
        this.livroService = livroService;
        this.exemplarLivroServiceImpl = exemplarLivroServiceImpl;
    }

    public void criarLivro(DTOLivro dto) {

        Livro novoLivro = livroService.cadastrarLivro(dto);

        exemplarLivroServiceImpl.criarExemplar(novoLivro, dto.quantidade());
    }
}
