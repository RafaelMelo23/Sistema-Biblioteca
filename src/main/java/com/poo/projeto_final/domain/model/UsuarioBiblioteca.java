package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario_biblioteca")
public class UsuarioBiblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nome", nullable = false, length = 20))
    private Nome nome;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true, length = 11))
    private Cpf cpf;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, length = 320))
    private Email email;

    protected UsuarioBiblioteca() {
    }

    public UsuarioBiblioteca(String nome, String cpf, String email) {
        this.nome = new Nome(nome);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
    }

    public static UsuarioBiblioteca criarAluno(String nome, String cpf, String email, String matricula) {
        return new Aluno(nome, email, cpf, matricula);
    }

    public static UsuarioBiblioteca criarProfessor(String nome, String cpf, String email, String matricula) {
        return new Professor(nome, email, cpf, matricula);
    }

    // A partir daqui apenas value objects.

    @Embeddable
    public static class Nome {
        private String value;

        protected Nome() {
        }

        private Nome(String value) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("Nome não pode ser vazio");
            }
            if (value.length() > 20) {
                throw new IllegalArgumentException("Nome não pode ter mais de 20 caracteres");
            }
            this.value = value;
        }

        public static Nome of(String value) {
            return new Nome(value);
        }

        public String getValue() {
            return value;
        }
    }

    @Embeddable
    public static class Cpf {
        private String value;

        protected Cpf() {
        }

        private Cpf(String value) {
            if (value == null || !value.matches("\\d{11}")) {
                throw new IllegalArgumentException("CPF inválido: deve conter 11 digitos");
            }
            this.value = value;
        }

        public static Cpf of(String value) {
            return new Cpf(value);
        }

        public String getValue() {
            return value;
        }
    }

    @Embeddable
    public static class Email {
        private String value;

        protected Email() {
        }

        private Email(String value) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("Email não pode ser vazio");
            }
            if (value.length() > 320) {
                throw new IllegalArgumentException("Email excede o tamanho máximo");
            }
            if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("Formato de email inválido");
            }
            this.value = value;
        }

        public static Email of(String value) {
            return new Email(value);
        }

        public String getValue() {
            return value;
        }
    }
}
