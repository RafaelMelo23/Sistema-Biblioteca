package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Emprestimo;
import org.springframework.data.repository.ListCrudRepository;

public interface DAOEmprestimo extends ListCrudRepository<Emprestimo, Long> {


}
