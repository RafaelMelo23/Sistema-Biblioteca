package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import org.springframework.stereotype.Component;

@Component
public class ListarEmprestimoPorIdUseCase {


    private final DAOEmprestimo dAOEmprestimo;

    public ListarEmprestimoPorIdUseCase(DAOEmprestimo dAOEmprestimo) {
        this.dAOEmprestimo = dAOEmprestimo;
    }

    public DTOListagemCompleta listarEmprestimoPorId(Long emprestimoId) {

        return dAOEmprestimo.listarEmprestimosPorId(emprestimoId);
    }

}
