package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface DAOAluno extends ListCrudRepository<Aluno, Long> {


    boolean existsByEmailOrCpf(String email, String cpf);

    boolean existsByMatricula(String matricula);

    Optional<Aluno> findByMatricula(Matricula matricula);
}
