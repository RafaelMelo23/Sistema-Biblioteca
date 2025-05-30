package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso responsável por realizar a listagem dos empréstimos por matrícula de usuário, está na camada de caso
 * de uso por não passar por validações.
 */
@Component
public class ListarEmprestimosUseCase {

    private final DAOEmprestimo daoEmprestimo;

    public ListarEmprestimosUseCase(DAOEmprestimo daoEmprestimo) {
        this.daoEmprestimo = daoEmprestimo;
    }

    public List<DTOListarEmprestimo> listarEmpretimosUsuarioUseCase(String matricula) {

        return daoEmprestimo.listarEmprestimosUsuario(Matricula.of(matricula));
    }
}
