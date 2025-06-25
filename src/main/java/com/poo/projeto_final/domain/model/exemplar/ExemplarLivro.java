package com.poo.projeto_final.domain.model.exemplar;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import lombok.Getter;

@Getter
public class ExemplarLivro {

    private Long id;
    private final Livro livro;
    private final CodigoExemplar codigoExemplar;
    private StatusExemplar statusExemplar;

    private ExemplarLivro(Livro livro, CodigoExemplar codigoExemplar) {
        this.livro = livro;
        this.codigoExemplar = codigoExemplar;
        this.statusExemplar = StatusExemplar.DISPONIVEL;
    }

    public ExemplarLivro(Long id, Livro livro, CodigoExemplar codigoExemplar, StatusExemplar statusExemplar) {
        this.id = id;
        this.livro = livro;
        this.codigoExemplar = codigoExemplar;
        this.statusExemplar = statusExemplar;
    }

    public static ExemplarLivro criarExemplar(Livro livro, CodigoExemplar codigoExemplar) {
        return new ExemplarLivro(livro, codigoExemplar);
    }
}