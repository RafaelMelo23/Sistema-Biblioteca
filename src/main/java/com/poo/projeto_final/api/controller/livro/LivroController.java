package com.poo.projeto_final.api.controller.livro;

import com.poo.projeto_final.application.dto.DTOExemplarLivro;
import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.application.usecase.LivroUseCase;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/livro")
@RequiredArgsConstructor
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);
    private final LivroUseCase livroUseCase;

    @Operation(summary = "Criar um novo livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Informações inválidas",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/criar")
    public ResponseEntity<?> criarLivro(@Parameter(description = "DTO contendo os dados do livro") @RequestBody DTOLivro dto) {

        try {
            livroUseCase.cadastrarLivro(dto);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao criar livro, provável corpo inválido. {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao criar livro, informações inválidas");
        } catch (Exception e) {
            logger.error("Erro ao criar livro, erro interno. " + e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao criar livro");
        }
    }

    @Operation(summary = "Buscar exemplares de um livro por status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exemplares retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = DTOExemplarLivro.class)))
    })
    @GetMapping("/buscar/exemplares")
    public List<DTOExemplarLivro> buscarLivros(@Parameter(description = "ID do livro") @RequestParam Long livroId,
                                               @Parameter(description = "Status do exemplar") @RequestParam StatusExemplar statusExemplar) {

        return livroUseCase.buscarExemplarPorStatus(livroId, statusExemplar);
    }

    @Operation(summary = "Buscar livros cujo título contenha o termo informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros encontrada",
                    content = @Content(schema = @Schema(implementation = Livro.class)))
    })
    @GetMapping("/buscar/titulo")
    public CollectionModel<EntityModel<Livro>> buscarPorTituloContem(@Parameter(description = "Parte do título a ser buscada")
                                                                     @RequestParam String titulo) {

        List<Livro> livros = livroUseCase.listarPorTitulo(titulo);

        List<EntityModel<Livro>> livrosComLinks = livros.stream().map(livro -> {
            EntityModel<Livro> recurso = EntityModel.of(livro);

            Link exemplaresDisponiveis = linkTo(methodOn(LivroController.class)
                    .buscarLivros(livro.getId(), StatusExemplar.DISPONIVEL))
                    .withRel("exemplares-disponiveis");
            Link exemplaresEmprestados = linkTo(methodOn(LivroController.class)
                    .buscarLivros(livro.getId(), StatusExemplar.EMPRESTADO))
                    .withRel("exemplares-emprestados");
            Link exemplaresPerdidos = linkTo(methodOn(LivroController.class)
                    .buscarLivros(livro.getId(), StatusExemplar.PERDIDO))
                    .withRel("exemplares-perdidos");

            recurso.add(exemplaresDisponiveis, exemplaresEmprestados, exemplaresPerdidos);

            return recurso;
        }).toList();

        return CollectionModel.of(livrosComLinks);
    }


}
