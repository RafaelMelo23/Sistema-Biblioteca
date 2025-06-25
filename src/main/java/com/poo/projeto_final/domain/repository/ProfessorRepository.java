package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;

import java.util.Optional;


public interface ProfessorRepository {

    boolean existsByMatricula(Matricula matricula);

    Optional<Professor> findByMatricula(Matricula matricula);

    Optional<String> findNameByMatricula(Matricula matricula);
}
