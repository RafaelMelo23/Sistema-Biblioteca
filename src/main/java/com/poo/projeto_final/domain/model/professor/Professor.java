package com.poo.projeto_final.domain.model.professor;

import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "professor")
public class Professor extends UsuarioBiblioteca {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "matricula", nullable = false, unique = true, length = 20))
    private Matricula matricula;

    public Professor() {}

    public Professor(String nome, String cpf, String email, String matricula) {
        super(nome, cpf, email);
        this.matricula = new Matricula(matricula);
    }

    public static Professor of(String nome, String cpf, String email, String matricula) {
        return (Professor) criarProfessor(nome, cpf, email, matricula);
    }

}