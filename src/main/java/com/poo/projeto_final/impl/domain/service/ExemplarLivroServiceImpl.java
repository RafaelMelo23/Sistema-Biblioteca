package com.poo.projeto_final.impl.domain.service;

import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.service.ExemplarLivroService;
import com.poo.projeto_final.impl.domain.repository.ExemplarLivroRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio relacionada aos exemplares dos livros, validações e comunicação com o banco.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExemplarLivroServiceImpl implements ExemplarLivroService {

    /**
     * Interface JPA para chamadas ao banco relacionadas à tabela de exemplares.
     */
    private final ExemplarLivroRepositoryImpl exemplarLivroRepository;

    /**
     * Cria os exemplares de um livro + seu código único de identificação.
     * @Transactional garante que as mudanças no banco só sejam commitadas caso o método tenha sucesso, caso contrário, são revertidas
     * @param livro Dados do livro a serem cadastrados.
     * @param quantidade Quantidade de exemplares únicos.
     */
    @Transactional
    public void criarExemplar(Livro livro, int quantidade) {

        List<ExemplarLivro> novosExemplares = new ArrayList<>();
        long contagemAtual = exemplarLivroRepository.countExemplarLivroBy();

        try {

            for (int i = 0; i < quantidade; i++) {

                int quantidadeAtual = Math.toIntExact(contagemAtual + i);

                String codigoExemplar = gerarCodigoExemplar(quantidadeAtual);

                ExemplarLivro novoExemplar = ExemplarLivro.criarExemplar(livro, CodigoExemplar.of(codigoExemplar));

                novosExemplares.add(novoExemplar);
            }

            exemplarLivroRepository.saveAll(novosExemplares);
        } catch (Exception e) {
            log.error("Erro ao criar exemplar: {}", e.getMessage(), e);
        }
    }

    public String gerarCodigoExemplar(long exemplarAtual) {

        return "LIV-EX-" + (exemplarAtual + 1);
    }
}
