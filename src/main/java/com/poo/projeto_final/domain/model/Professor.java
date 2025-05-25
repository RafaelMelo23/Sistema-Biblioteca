package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "professor")
public class Professor extends UsuarioBiblioteca {

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "matricula", nullable = false, unique = true, length = 20))
    private Matricula matricula;

    public Professor() {}

    public Professor(String nome, String email, String cpf, String matricula) {
        super(nome, email, cpf);
        this.matricula = new Matricula(matricula);
    }

    public static Professor of(String nome, String email, String cpf, String matricula) {
        return (Professor) criarProfessor(nome, email, cpf, matricula);
    }

    @Getter
    @Embeddable
    static class Matricula {
        private String valor;

        protected Matricula() {
        }

        private Matricula(String valor) {
            this.valor = valor;
        }

        public static Matricula of(String valor) {
            return new Matricula(valor);
        }

    }
}