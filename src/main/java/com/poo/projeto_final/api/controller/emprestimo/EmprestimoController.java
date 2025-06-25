package com.poo.projeto_final.api.controller.emprestimo;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.application.dto.DTOResultadoEmprestimo;
import com.poo.projeto_final.application.usecase.EmprestimoUseCase;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/emprestimo")
@RequiredArgsConstructor
public class EmprestimoController {

    private static final Logger logger = LoggerFactory.getLogger(EmprestimoController.class);
    private final EmprestimoUseCase emprestimoUseCase;

    @Operation(summary = "Listar todos os empréstimos de um usuário")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Empréstimos encontrados"), @ApiResponse(responseCode = "404", description = "Nenhum empréstimo encontrado"), @ApiResponse(responseCode = "400", description = "Matrícula inválida"), @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @GetMapping("/listar")
    public CollectionModel<EntityModel<DTOListarEmprestimo>> listarTodosEmprestimosUsuario(@RequestParam String matricula) {

        try {
            List<DTOListarEmprestimo> emprestimoDTO = emprestimoUseCase.listarEmprestimoPorId(Matricula.of(matricula));

            if (emprestimoDTO.isEmpty()) {
                logger.warn("Nenhum empréstimo encontrado para a matrícula {}", matricula);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum empréstimo encontrado");
            }

            List<EntityModel<DTOListarEmprestimo>> emprestimoModel = emprestimoDTO.stream()
                    .map(dto -> EntityModel.of(dto,
                            linkTo(methodOn(EmprestimoController.class)
                                    .listarTodosEmprestimosUsuario(matricula)).withSelfRel(),
                            linkTo(methodOn(EmprestimoController.class)
                                    .listarEmprestimoEspecifico(dto.emprestimoid())).withRel("emprestimo-especifico"))).toList();

            return CollectionModel.of(emprestimoModel, linkTo(
                    methodOn(EmprestimoController.class)
                            .listarTodosEmprestimosUsuario(matricula)).withSelfRel());

        } catch (IllegalArgumentException e) {
            logger.error("Erro de argumento ao listar empréstimos para matrícula {}: {}", matricula, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi encontrado empréstimos com a matrícula informada");
        } catch (ResponseStatusException e){
            throw e;
        } catch (Exception e) {
            logger.error("Erro interno ao listar empréstimos para matrícula {}: {}", matricula, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Houve um erro ao listar empréstimos");
        }
    }

    @Operation(summary = "Listar empréstimo específico por ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Empréstimo encontrado"), @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"), @ApiResponse(responseCode = "400", description = "ID inválido"), @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @GetMapping("/listar/{id}")
    public EntityModel<DTOListagemCompleta> listarEmprestimoEspecifico(@PathVariable Long id) {

        try {
            DTOListagemCompleta dto = emprestimoUseCase.listarEmprestimoPorId(id);

            if (dto == null) {
                logger.warn("Nenhum empréstimo encontrado com ID {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o empréstimo com o ID informado");
            }

            return EntityModel.of(dto, linkTo(methodOn(EmprestimoController.class)
                    .listarEmprestimoEspecifico(id))
                    .withSelfRel(), linkTo(
                            methodOn(EmprestimoController.class).listarTodosEmprestimosUsuario(dto.matricula())).withRel("todos-emprestimos"));

        } catch (IllegalArgumentException e) {
            logger.error("ID inválido fornecido: {} - {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erro interno ao buscar empréstimo com ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Efetuar um empréstimo")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Empréstimo realizado com sucesso"), @ApiResponse(responseCode = "400", description = "Dados inválidos"), @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @PostMapping("/efetuar")
    public ResponseEntity<DTOResultadoEmprestimo> efetuarEmprestimo(@RequestBody @Valid DTOEmprestimo dtoEmprestimo) {

        try {
            return ResponseEntity.ok(emprestimoUseCase.registrarEmprestimo(dtoEmprestimo));

        } catch (IllegalArgumentException e) {
            logger.error("Erro de argumento ao efetuar empréstimo: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Erro interno ao efetuar empréstimo: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Operation(summary = "Realizar devolução de livro")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Devolução realizada com sucesso"), @ApiResponse(responseCode = "400", description = "Dados inválidos"), @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @PostMapping("/devolucao")
    public ResponseEntity<?> efetuarDevolucao(@RequestBody DTOEmprestimo dtoEmprestimo) {

        try {
            emprestimoUseCase.devolverEmprestimo(dtoEmprestimo);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Erro de argumento ao efetuar devolução: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Erro interno ao efetuar devolução: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
