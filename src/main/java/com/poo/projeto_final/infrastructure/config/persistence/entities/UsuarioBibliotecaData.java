package com.poo.projeto_final.infrastructure.config.persistence.entities;

import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;
import com.poo.projeto_final.domain.model.usuario.Nome;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "usuario_biblioteca")
@Getter
@Setter
public class UsuarioBibliotecaData {


    @Id
    @SequenceGenerator(
            name = "usuario_bibl_sequence",
            sequenceName = "USUARIO_BIBL_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usuario_bibl_sequence")
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nome", nullable = false, length = 40))
    private Nome nome;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true, length = 11))
    private Cpf cpf;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, length = 320))
    private Email email;

    protected UsuarioBibliotecaData() {
    }

    public UsuarioBibliotecaData(String nome, String cpf, String email) {
        this.nome = new Nome(nome);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
    }

    public static UsuarioBibliotecaData criarAluno(String nome, String cpf, String email, String matricula) {
        return new AlunoData(nome, email, cpf, matricula);
    }

    public static UsuarioBibliotecaData criarProfessor(String nome, String cpf, String email, String matricula) {
        return new ProfessorData(nome, email, cpf, matricula);
    }

}
