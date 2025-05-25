package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.enums.StatusExemplar;
import jakarta.persistence.*;

@Entity
@Table(name = "exemplar_livro")
public class ExemplarLivro {

    @EmbeddedId
    private ExemplarLivroId id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "codigo_exemplar", nullable = false, unique = true, length = 100))
    private CodigoExemplar codigoExemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_exemplar", nullable = false, length = 30)
    private StatusExemplar statusExemplar;

    // A partir daqui, apenas Value Objects

    @Embeddable
    public static class ExemplarLivroId {

        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long value;

        protected ExemplarLivroId() {}

        private ExemplarLivroId(Long value) { this.value = value; }

        public static ExemplarLivroId of(Long value) { return new ExemplarLivroId(value); }

        public Long getValue() { return value; }
    }

    @Embeddable
    public static class CodigoExemplar {

        private String value;

        protected CodigoExemplar() {}

        private CodigoExemplar(String value) { this.value = value; }

        public static CodigoExemplar of(String value) { return new CodigoExemplar(value); }

        public String getValue() { return value; }
    }
}
