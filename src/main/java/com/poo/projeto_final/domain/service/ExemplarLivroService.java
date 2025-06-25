package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.domain.model.livro.Livro;

public interface ExemplarLivroService {

    /**
     * Cria uma quantidade específica de exemplares únicos para um livro.
     *
     * @param livro O livro ao qual os exemplares pertencerão.
     * @param quantidade Número de exemplares a serem criados.
     */
    void criarExemplar(Livro livro, int quantidade);

    /**
     * Gera o código único de um exemplar, baseado na posição atual.
     *
     * @param exemplarAtual O número sequencial atual.
     * @return Código gerado no formato "LIV-EX-{n}".
     */
    String gerarCodigoExemplar(long exemplarAtual);
}

