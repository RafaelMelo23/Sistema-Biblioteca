package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;


public interface DAOProfessor extends ListCrudRepository<Professor, Long> {

    boolean existsByMatricula(Matricula matricula);

    Optional<Professor> findByMatricula(Matricula matricula);
}
