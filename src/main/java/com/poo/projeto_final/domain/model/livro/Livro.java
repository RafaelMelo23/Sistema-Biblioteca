package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.*;

@Entity
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

    // Construtor que pega os parametros de String e converte em Objetos
    public Livro(String titulo, String autor, String isbn, String ano, String editora) {
        this.titulo = new Titulo(titulo);
        this.autor = new Autor(autor);
        this.isbn = new Isbn(isbn);
        this.ano = new Ano(ano);
        this.editora = new Editora(editora);
    }

    public Livro() {}

    // Método de domínio para criação de um livro, retorna o objeto.
    public Livro criarLivro(String titulo, String autor, String isbn, String ano, String editora) {
        return new Livro(titulo, autor, isbn, ano, editora);
    }
    
    // A partir daqui, apenas value objects

    @Embeddable
    public static class Titulo {
        private String value;

        protected Titulo() {}

        private Titulo(String value) {
            this.value = value;
        }

        public static Titulo of(String value) {
            return new Titulo(value);
        }

        public String getValue() {
            return value;
        }
    }

    @Embeddable
    public static class Autor {
        private String value;

        protected Autor() {}

        private Autor(String value) {
            this.value = value;
        }

        public static Autor of(String value) {
            return new Autor(value);
        }

        public String getValue() {
            return value;
        }
    }

    @Embeddable
    public static class Isbn {
        private String value;

        protected Isbn() {}

        private Isbn(String value) {
            this.value = value;
        }

        public static Isbn of(String value) {
            return new Isbn(value);
        }

        public String getValue() {
            return value;
        }
    }

    @Embeddable
    public static class Ano {
        private String value;

        protected Ano() {}

        private Ano(String value) {
            this.value = value;
        }

        public static Ano of(String value) {
            return new Ano(value);
        }

        public String getValue() {
            return value;
        }
    }

    @Embeddable
    public static class Editora {
        private String value;

        protected Editora() {}

        private Editora(String value) {
            this.value = value;
        }

        public static Editora of(String value) {
            return new Editora(value);
        }

        public String getValue() {
            return value;
        }
    }
}
