package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "titulo", nullable = false, unique = true, length = 250))
    private Titulo titulo;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "autor", nullable = false, length = 300))
    private Autor autor;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "isbn", nullable = false, unique = true, length = 13))
    private Isbn isbn;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ano", nullable = false, length = 4))
    private Ano ano;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "editora", nullable = false, length = 280))
    private Editora editora;

    public Livro() {}

    // Construtor que pega os parametros de String e converte nos value objects.
    public Livro(String titulo, String autor, String isbn, String ano, String editora) {
        this.titulo = new Titulo(titulo);
        this.autor = new Autor(autor);
        this.isbn = new Isbn(isbn);
        this.ano = new Ano(ano);
        this.editora = new Editora(editora);
    }

    // Método de domínio para criação de um livro, retorna o objeto.
    public static Livro criarLivro(String titulo, String autor, String isbn, String ano, String editora) {
        return new Livro(titulo, autor, isbn, ano, editora);
    }
}
