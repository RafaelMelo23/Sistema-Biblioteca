package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.domain.service.EmprestimoService;
import org.springframework.stereotype.Component;

@Component
public class RealizarEmprestimoUseCase {

    private final EmprestimoService servicoEmprestimo;

    public RealizarEmprestimoUseCase(EmprestimoService servicoEmprestimo) {
        this.servicoEmprestimo = servicoEmprestimo;
    }

    public void executarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        servicoEmprestimo.atualizarEmprestimo(dtoEmprestimo);
    }
}
