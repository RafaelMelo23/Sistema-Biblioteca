package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.service.EmprestimoService;
import org.springframework.stereotype.Component;

@Component
public class DevolucaoEmprestimoUseCase {

    private final EmprestimoService servicoEmprestimo;

    public DevolucaoEmprestimoUseCase(EmprestimoService servicoEmprestimo) {
        this.servicoEmprestimo = servicoEmprestimo;
    }

    public void executarDevolucao(DTOEmprestimo dtoEmprestimo) {

        servicoEmprestimo.registrarEmprestimo(dtoEmprestimo);
    }
}
