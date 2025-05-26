package com.poo.projeto_final.domain.model.professor;

import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
}