package com.poo.projeto_final.domain.service;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.ExemplarLivroId;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.DAOAluno;
import com.poo.projeto_final.domain.repository.DAOEmprestimo;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import com.poo.projeto_final.domain.repository.DAOProfessor;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    private final DAOAluno daoAluno;
    private final DAOExemplarLivro daoExemplarLivro;
    private final DAOProfessor dAOProfessor;
    private final DAOEmprestimo dAOEmprestimo;

    public EmprestimoService(DAOAluno daoAluno, DAOExemplarLivro daoExemplarLivro, DAOProfessor dAOProfessor, DAOEmprestimo dAOEmprestimo) {
        this.daoAluno = daoAluno;
        this.daoExemplarLivro = daoExemplarLivro;
        this.dAOProfessor = dAOProfessor;
        this.dAOEmprestimo = dAOEmprestimo;
    }

    public Emprestimo salvarEmprestimo(DTOEmprestimo dtoEmprestimo) {

        Matricula matricula;

        ExemplarLivroId exemplarId = daoExemplarLivro.findByCodigoExemplarValueIs(dtoEmprestimo.codigoExemplar(), StatusExemplar.DISPONIVEL)
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível encontrar o livro, ou ele não está disponível"));

        switch (dtoEmprestimo.tipoUsuario()) {
            case ALUNO:
                Aluno aluno = daoAluno
                        .findByMatricula(Matricula.of(dtoEmprestimo.matricula()))
                        .orElseThrow(() ->
                                new IllegalArgumentException("Aluno com a matrícula informada não existe")
                        );

                matricula = aluno.getMatricula();
                break;

            case PROFESSOR:
                Professor professor = dAOProfessor
                        .findByMatricula(Matricula.of(dtoEmprestimo.matricula()))
                        .orElseThrow(() ->
                                new IllegalArgumentException("Professor com a matrícula informada não existe")
                        );
                matricula = professor.getMatricula();
                break;

            default:
                throw new IllegalArgumentException("O tipo de usuário não foi informado");
        }

        Emprestimo emprestimo = Emprestimo.realizarEmprestimo(matricula, exemplarId, dtoEmprestimo.dataPrevista());

        return dAOEmprestimo.save(emprestimo);
    }

}
