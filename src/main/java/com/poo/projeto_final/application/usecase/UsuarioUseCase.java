package com.poo.projeto_final.application.usecase;

import com.poo.projeto_final.application.dto.DTOAluno;
import com.poo.projeto_final.application.dto.DTOProfessor;
import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.Cpf;
import com.poo.projeto_final.domain.model.usuario.Email;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import com.poo.projeto_final.domain.repository.AlunoRepository;
import com.poo.projeto_final.domain.repository.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela lógica de negócio relacionada aos usuários, validações e comunicação com o banco.
 */
@Service
@AllArgsConstructor
public class UsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(com.poo.projeto_final.impl.domain.service.UsuarioService.class);

    /**
     * Interfaces JPA para chamadas ao banco relacionadas à tabela de alunos e professores.
     */
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    /**
     * Cadastra um novo aluno baseado no DTO recebido.
     * @param dtoAluno Dados do aluno a serem cadastrados.
     * @Transactional garante que as mudanças no banco só sejam commitadas caso o método tenha sucesso, caso contrário, são revertidas
     * @throws IllegalArgumentException Caso exista um aluno já cadastrado com algum dos dados informados no DTO.
     */
    @Transactional
    public void criarAluno(DTOAluno dtoAluno) {

        if (alunoRepository.existsByMatricula(Matricula.of(dtoAluno.matricula()))) {
            throw new IllegalArgumentException("Já existe um aluno registrado com a matrícula: " + dtoAluno.matricula());
        }

        if (alunoRepository.existsByEmailOrCpf(Email.of(dtoAluno.email()), Cpf.of(dtoAluno.cpf()))) {
            throw new IllegalArgumentException("Já existe um aluno registrado com a matrícula: " + dtoAluno.matricula());
        }

        Aluno aluno = Aluno.of(dtoAluno.nome(), dtoAluno.cpf(), dtoAluno.email(), dtoAluno.matricula());

        try {
            alunoRepository.salvar(aluno);
        } catch (Exception e) {
            log.error("Erro ao criar aluno: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao criar aluno: " + e.getMessage());
        }
    }

    /**
     * Cadastra um novo professor baseado no DTO recebido.
     * @param dtoProfessor Dados do professor a serem cadastrados.
     * @Transactional garante que as mudanças no banco só sejam commitadas caso o método tenha sucesso, caso contrário, são revertidas
     * @throws IllegalArgumentException Caso exista um professor já cadastrado com algum dos dados informados no DTO.
     */
    @Transactional
    public void criarProfessor(DTOProfessor dtoProfessor) {

        if (professorRepository.existsByMatricula(Matricula.of(dtoProfessor.matricula()))) {
            throw new IllegalArgumentException("Já existe um professor cadastrado com a mátricula " + dtoProfessor.matricula());
        }

        try {
            Professor professor = Professor.of(dtoProfessor.nome(), dtoProfessor.email(),
                    dtoProfessor.cpf(), dtoProfessor.matricula());

            professorRepository.salvar(professor);
        } catch (Exception e) {
            log.error("Erro ao criar professor: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Erro ao criar professor: " + e.getMessage());
        }
    }

    public UsuarioBiblioteca validarExistencia(Matricula matricula) {

        return alunoRepository.findByMatricula(matricula)
                .map(aluno -> (UsuarioBiblioteca) aluno)
                .orElseGet(() ->
                        professorRepository.findByMatricula(matricula)
                                .map(prof -> (UsuarioBiblioteca) prof)
                                .orElseThrow(() -> new IllegalArgumentException("Usuário com a matricula informada não existe")));
    }

    public String buscarNome(Matricula matricula) {

        return alunoRepository.findNameByMatricula(matricula)
                .or(() -> professorRepository.findNameByMatricula(matricula))
                .orElseThrow(() -> new IllegalArgumentException("Usuário com a matricula informada não existe"));
    }
}
