package com.poo.projeto_final.domain.model.usuario;

import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario_biblioteca")
@Getter
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

}
