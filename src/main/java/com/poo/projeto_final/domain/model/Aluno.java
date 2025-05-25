package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Aluno extends UsuarioBiblioteca {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "matricula", nullable = false, unique = true, length = 20))
    private Matricula matricula;

    public Aluno() {
        super();
    }

    public Aluno(String nome, String email, String cpf, String matricula) {
        super(nome, email, cpf);
        this.matricula = new Matricula(matricula);
    }

    public static Aluno of(String nome, String email, String cpf, String matricula) {
        return (Aluno) criarAluno(nome, email, cpf, matricula);
    }

    @Embeddable
    static class Matricula {
        private String value;

        protected Matricula() {
        }

        private Matricula(String value) {
            this.value = value;
        }

        public static Matricula of(String value) {
            return new Matricula(value);
        }

        public String getValue() {
            return value;
        }
    }
}