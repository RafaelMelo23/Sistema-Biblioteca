package com.poo.projeto_final.domain.model.emprestimo;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Emprestimo {

    private Long id;
    private final Long exemplarLivroId;
    private final Matricula matricula;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataPrevista;
    private LocalDate dataFactual;
    private StatusEmprestimo statusEmprestimo;

    private Emprestimo(Matricula matricula, Long exemplarLivroId, LocalDate dataPrevista) {
        this.exemplarLivroId = exemplarLivroId;
        this.matricula = matricula;
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevista = dataPrevista;
        this.statusEmprestimo = StatusEmprestimo.ATIVO;
    }

    public Emprestimo(Long id, Long exemplarLivroId, Matricula matricula, LocalDate dataEmprestimo, LocalDate dataPrevista, LocalDate dataFactual, StatusEmprestimo statusEmprestimo) {
        this.id = id;
        this.exemplarLivroId = exemplarLivroId;
        this.matricula = matricula;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.dataFactual = dataFactual;
        this.statusEmprestimo = statusEmprestimo;
    }

    public static Emprestimo realizarEmprestimo(Matricula matricula, Long exemplarLivroId, LocalDate dataPrevista) {
        return new Emprestimo(matricula, exemplarLivroId, dataPrevista);
    }

    public void finalizar() {
        if(this.statusEmprestimo != StatusEmprestimo.ATIVO) {
            throw new IllegalStateException("Empréstimo não pode ser finalizado pois não está ativo.");
        }
        this.dataFactual = LocalDate.now();
        this.statusEmprestimo = StatusEmprestimo.FINALIZADO;
    }
}