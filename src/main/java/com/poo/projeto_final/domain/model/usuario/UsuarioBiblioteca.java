package com.poo.projeto_final.domain.model.usuario;

import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.professor.Professor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UsuarioBiblioteca {

    @Setter
    private Long id;
    private final Nome nome;
    private final Cpf cpf;
    private final Email email;

    public UsuarioBiblioteca(String nome, String cpf, String email) {
        this.nome = new Nome(nome);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
    }

    protected UsuarioBiblioteca(Long id, Nome nome, Cpf cpf, Email email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public static UsuarioBiblioteca criarAluno(String nome, String cpf, String email, String matricula) {
        return new Aluno(nome, cpf, email, matricula);
    }

    public static UsuarioBiblioteca criarProfessor(String nome, String cpf, String email, String matricula) {
        return new Professor(nome, cpf, email, matricula);
    }
}