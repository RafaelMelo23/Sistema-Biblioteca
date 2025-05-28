package com.poo.projeto_final.domain.model.emprestimo;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.model.shared.vo.ExemplarLivroId;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
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
    @AttributeOverride(name = "value", column = @Column(name = "matricula_id", nullable = false))
    private Matricula matricula;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_entrega_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "data_entrega_factual")
    private LocalDate dataFactual;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_emprestimo", nullable = false, length = 12)
    private StatusEmprestimo statusEmprestimo;

    public static Emprestimo realizarEmprestimo(Matricula Matricula, ExemplarLivroId exemplarLivroId, LocalDate dataPrevista, StatusEmprestimo statusEmprestimo) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setDataPrevista(dataPrevista);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setMatricula(Matricula);
        emprestimo.setExemplarLivroId(exemplarLivroId);
        emprestimo.setStatusEmprestimo(statusEmprestimo);

        return emprestimo;
    }
}