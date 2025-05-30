package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio relacionada aos exemplares dos livros, validações e comunicação com o banco.
 */
@Service
public class ExemplarLivroService {

    /**
     * Interface JPA para chamadas ao banco relacionadas à tabela de exemplares.
     */
    private final DAOExemplarLivro daoExemplarLivro;

    public ExemplarLivroService(DAOExemplarLivro daoExemplarLivro) {
        this.daoExemplarLivro = daoExemplarLivro;
    }

    /**
     * Cria os exemplares de um livro + seu código único de identificação.
     *
     * @param livro Dados do livro a serem cadastrados.
     * @param quantidade Quantidade de exemplares únicos.
     */
    @Transactional
    public void criarExemplar(Livro livro, int quantidade) {

        List<ExemplarLivro> novosExemplares = new ArrayList<>();
        long contagemAtual = daoExemplarLivro.countExemplarLivroBy();

        for (int i = 1; i <= quantidade ; i++) {

            int quantidadeAtual = Math.toIntExact(contagemAtual + i);

            String codigoExemplar = gerarCodigoExemplar(quantidadeAtual);

            ExemplarLivro novoExemplar = ExemplarLivro.criarExemplar(livro, CodigoExemplar.of(codigoExemplar));

            novosExemplares.add(novoExemplar);
        }

        daoExemplarLivro.saveAll(novosExemplares);
    }

    public String gerarCodigoExemplar(long exemplarAtual) {

        return "LIV-EX-" + (exemplarAtual + 1);
    }
}
