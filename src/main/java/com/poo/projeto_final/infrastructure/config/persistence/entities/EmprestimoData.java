package com.poo.projeto_final.infrastructure.config.persistence.entities;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "emprestimo")
public class EmprestimoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exemplar_livro_id", nullable = false)
    private Long exemplarLivroId;

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

    public static EmprestimoData realizarEmprestimo(Matricula Matricula, Long exemplarLivroId, LocalDate dataPrevista, StatusEmprestimo statusEmprestimo) {
        EmprestimoData emprestimoData = new EmprestimoData();
        emprestimoData.setDataPrevista(dataPrevista);
        emprestimoData.setDataEmprestimo(LocalDate.now());
        emprestimoData.setMatricula(Matricula);
        emprestimoData.setExemplarLivroId(exemplarLivroId);
        emprestimoData.setStatusEmprestimo(statusEmprestimo);

        return emprestimoData;
    }
}