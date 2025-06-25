package com.poo.projeto_final.domain.model.aluno;

import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;
import com.poo.projeto_final.domain.model.usuario.Nome;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import lombok.Getter;

@Getter
public class Aluno extends UsuarioBiblioteca {

    private final Matricula matricula;

    public Aluno(String nome, String cpf, String email, String numeroMatricula) {
        super(nome, cpf, email);
        this.matricula = new Matricula(numeroMatricula);
    }

    public Aluno(String nome, String cpf, String email, Matricula matricula) {
        super(nome, cpf, email);
        this.matricula = matricula;
    }

    public Aluno(Long id, Nome nome, Cpf cpf, Email email, Matricula matricula) {
        super(id, nome, cpf, email);
        this.matricula = matricula;
    }
}