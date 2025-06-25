package com.poo.projeto_final.infrastructure.config.persistence.entities;

import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "aluno")
public class AlunoData extends UsuarioBibliotecaData {

    /* Na regra de negócio fictícia, a matrícula não é um número gerado pela biblioteca, e sim reaproveitado. O aluno já teria esse número,
    tal qual o RGM por exemplo. Por isso não existe método para geração automática e sequencial da matrícula.
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "matricula", nullable = false, unique = true, length = 20))
    private Matricula matricula;

    public AlunoData() {
        super();
    }

    public AlunoData(String nome, String email, String cpf, String matricula) {
        super(nome, cpf, email);
        this.matricula = new Matricula(matricula);
    }

    public static AlunoData of(String nome, String email, String cpf, String matricula) {
        return (AlunoData) criarAluno(nome, cpf, email, matricula);
    }
}