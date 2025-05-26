package com.poo.projeto_final.domain.repository;

import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOEmprestimo extends ListCrudRepository<Emprestimo, Long> {


}
