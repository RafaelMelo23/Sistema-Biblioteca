package com.poo.projeto_final.api.controller.usuario;

import com.poo.projeto_final.application.dto.DTOAluno;
import com.poo.projeto_final.application.dto.DTOProfessor;
import com.poo.projeto_final.application.usecase.UsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @Operation(summary = "Cadastrar um novo aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aluno cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Informações inválidas",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Requisição sem corpo"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao cadastrar aluno",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/cadastro/aluno")
    public ResponseEntity<?> cadastrarAluno(@Parameter(description = "Dados do aluno a ser cadastrado")
                                                @RequestBody DTOAluno aluno) {

        try {
            if (aluno == null) {
                return ResponseEntity.badRequest().body("O corpo está vazio");
            }

            usuarioUseCase.criarAluno(aluno);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado ao cadastrar aluno");
        }
    }

    @Operation(summary = "Cadastrar um novo professor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Professor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Informações inválidas",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Requisição sem corpo"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao cadastrar professor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/cadastro/professor")
    public ResponseEntity<?> cadastrarProfessor(@Parameter(description = "Dados do professor a ser cadastrado")
                                                    @RequestBody DTOProfessor professor) {

        try {
            if (professor == null) {
                return ResponseEntity.badRequest().body("O corpo está vazio");
            }

            usuarioUseCase.criarProfessor(professor);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado ao cadastrar professor");
        }
    }

}
