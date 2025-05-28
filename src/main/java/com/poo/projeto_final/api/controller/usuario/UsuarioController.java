package com.poo.projeto_final.api.controller.usuario;

import com.poo.projeto_final.application.dto.DTOAluno;
import com.poo.projeto_final.application.dto.DTOProfessor;
import com.poo.projeto_final.application.usecase.usuario.CriarAlunoUseCase;
import com.poo.projeto_final.application.usecase.usuario.CriarProfessorUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarProfessorUseCase criarProfessorUseCase;

    public UsuarioController(CriarAlunoUseCase criarAlunoUseCase, CriarProfessorUseCase criarProfessorUseCase) {
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarProfessorUseCase = criarProfessorUseCase;
    }

    @PostMapping("/cadastro/aluno")
    public ResponseEntity<?> cadastrarAluno(@RequestBody DTOAluno aluno) {

        try {
            if (aluno == null) {
                return ResponseEntity.notFound().build();
            }

            criarAlunoUseCase.criarAluno(aluno);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Informações inválidas");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado ao cadastrar aluno");
        }
    }

    @PostMapping("/cadastro/professor")
    public ResponseEntity<?> cadastrarProfessor(@RequestBody DTOProfessor professor) {

        try {
            if (professor == null) {
                return ResponseEntity.notFound().build();
            }

            criarProfessorUseCase.criarProfessor(professor);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Informações inválidas");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado ao cadastrar professor");
        }
    }

}
