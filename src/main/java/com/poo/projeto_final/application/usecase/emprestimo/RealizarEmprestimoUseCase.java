package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.application.dto.DTOResultadoEmprestimo;
import com.poo.projeto_final.domain.service.EmprestimoService;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por orquestrar a operação de devolução de um livro.
 */
@Component
public class RealizarEmprestimoUseCase {

    private final EmprestimoService servicoEmprestimo;

    public RealizarEmprestimoUseCase(EmprestimoService servicoEmprestimo) {
        this.servicoEmprestimo = servicoEmprestimo;
    }

    public DTOResultadoEmprestimo executarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        return servicoEmprestimo.registrarEmprestimo(dtoEmprestimo);
    }
}
