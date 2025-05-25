package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "emprestimo")
public class Emprestimo {
    @EmbeddedId
    private EmprestimoId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "exemplar_livro_id", nullable = false))
    private ExemplarLivroId exemplarLivroId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "aluno_id", nullable = false))
    private AlunoId alunoId;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_entrega_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "data_entrega_factual")
    private LocalDate dataFactual;

    public Emprestimo realizarEmprestimo(AlunoId alunoId, ExemplarLivroId exemplarLivroId, LocalDate dataPrevista) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setDataPrevista(dataPrevista);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setAlunoId(alunoId);
        emprestimo.setExemplarLivroId(exemplarLivroId);

        return emprestimo;
    }
}

    @Embeddable
    class EmprestimoId {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long value;

        public EmprestimoId() {
        }

        public EmprestimoId(Long value) {
            this.value = value;
        }

        public static EmprestimoId of(Long value) {
            return new EmprestimoId(value);
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }

    @Embeddable
    class AlunoId {
        private Long value;

        public AlunoId() {
        }

        public AlunoId(Long value) {
            this.value = value;
        }

        public static AlunoId of(Long value) {
            return new AlunoId(value);
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }

    @Embeddable
    class ExemplarLivroId {
        private Long value;

        public ExemplarLivroId() {
        }

        public ExemplarLivroId(Long value) {
            this.value = value;
        }

    public static ExemplarLivroId of(Long value) {
        return new ExemplarLivroId(value);
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}