package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por realizar a listagem dos empréstimos por ID, está na camada de caso
 * de uso por não passar por validações.
 */
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
