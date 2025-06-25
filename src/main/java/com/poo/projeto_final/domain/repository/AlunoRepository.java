package com.poo.projeto_final.domain.repository;


import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;

import java.util.Optional;

public interface AlunoRepository {

    void salvar(Aluno aluno);

    boolean existsByEmailOrCpf(Email email, Cpf cpf);

    boolean existsByMatricula(Matricula matricula);

    Optional<Aluno> findByMatricula(Matricula matricula);

    Optional<String> findNameByMatricula(Matricula matricula);
}
