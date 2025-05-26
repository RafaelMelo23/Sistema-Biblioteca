package com.poo.projeto_final.domain.model.aluno;

import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
}