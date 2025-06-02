package com.poo.projeto_final.domain.model.aluno;

import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "aluno")
public class Aluno extends UsuarioBiblioteca {

    /* Na regra de negócio fictícia, a matrícula não é um número gerado pela biblioteca, e sim reaproveitado. O aluno já teria esse número,
    tal qual o RGM por exemplo. Por isso não existe método para geração automática e sequencial da matrícula.
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "matricula", nullable = false, unique = true, length = 20))
    private Matricula matricula;

    public Aluno() {
        super();
    }

    public Aluno(String nome, String email, String cpf, String matricula) {
        super(nome, cpf, email);
        this.matricula = new Matricula(matricula);
    }

    public static Aluno of(String nome, String email, String cpf, String matricula) {
        return (Aluno) criarAluno(nome, cpf, email, matricula);
    }
}