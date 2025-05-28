package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import org.springframework.stereotype.Component;

import java.util.List;

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
