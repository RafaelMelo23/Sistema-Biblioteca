package com.poo.projeto_final.domain.model.livro;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Livro {

    private Long id;
    private final Titulo titulo;
    private final Autor autor;
    private final Isbn isbn;
    private final Ano ano;
    private final Editora editora;

    private Livro(String titulo, String autor, String isbn, String ano, String editora) {
        this.titulo = new Titulo(titulo);
        this.autor = new Autor(autor);
        this.isbn = new Isbn(isbn);
        this.ano = new Ano(ano);
        this.editora = new Editora(editora);
    }

    public Livro(Long id, Titulo titulo, Autor autor, Isbn isbn, Ano ano, Editora editora) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.ano = ano;
        this.editora = editora;
    }

    public static Livro criarLivro(String titulo, String autor, String isbn, String ano, String editora) {
        return new Livro(titulo, autor, isbn, ano, editora);
    }

    public List<ExemplarLivro> criarExemplares(int quantidade, long contagemTotalExemplares) {

        Livro livro = this;
        List<ExemplarLivro> exemplares = new ArrayList<>(quantidade);

        for (int i = 0; i < quantidade; i++) {

            int quantidadeAtual = Math.toIntExact(contagemTotalExemplares + i);

            ExemplarLivro novoExemplar = ExemplarLivro
                    .criarExemplar(livro, CodigoExemplar.of(quantidadeAtual));

            exemplares.add(novoExemplar);
        }

        return exemplares;
    }
}