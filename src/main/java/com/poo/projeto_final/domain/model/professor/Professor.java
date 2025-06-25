package com.poo.projeto_final.domain.model.professor;

import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;
import com.poo.projeto_final.domain.model.usuario.Nome;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import lombok.Getter;

@Getter
public class Professor extends UsuarioBiblioteca {

    private final Matricula matricula;

    public Professor(String nome, String cpf, String email, String matricula) {
        super(nome, cpf, email);
        this.matricula = new Matricula(matricula);
    }

    public Professor(Long id, Nome nome, Cpf cpf, Email email, Matricula matricula) {
        super(id, nome, cpf, email);
        this.matricula = matricula;
    }

    public static Professor of(String nome, String cpf, String email, String matricula) {
        return (Professor) criarProfessor(nome, cpf, email, matricula);
    }
}