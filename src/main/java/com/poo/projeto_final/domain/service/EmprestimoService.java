package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoService {

    List<Emprestimo> buscarAtrasos(Matricula matricula);

    Emprestimo realizarEmprestimo(Matricula matricula, String codigoExemplar, LocalDate dataPrevista);

    void finalizarEmprestimo(Matricula matricula, String codigoExemplar);

    void atualizarAtrasos();
}
