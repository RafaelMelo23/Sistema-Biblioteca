package com.poo.projeto_final.api.controller.emprestimo;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.application.usecase.emprestimo.DevolucaoEmprestimoUseCase;
import com.poo.projeto_final.application.usecase.emprestimo.ListarEmprestimoPorIdUseCase;
import com.poo.projeto_final.application.usecase.emprestimo.ListarEmprestimosUseCase;
import com.poo.projeto_final.application.usecase.emprestimo.RealizarEmprestimoUseCase;
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
public class EmprestimoController {

    private final ListarEmprestimosUseCase listarEmprestimosUseCase;
    private final RealizarEmprestimoUseCase realizarEmprestimoUseCase;
    private final DevolucaoEmprestimoUseCase devolucaoEmprestimoUseCase;
    private final ListarEmprestimoPorIdUseCase listarEmprestimoPorIdUseCase;

    public EmprestimoController(ListarEmprestimosUseCase listarEmprestimosUseCase, RealizarEmprestimoUseCase realizarEmprestimoUseCase, DevolucaoEmprestimoUseCase devolucaoEmprestimoUseCase, ListarEmprestimoPorIdUseCase listarEmprestimoPorIdUseCase) {
        this.listarEmprestimosUseCase = listarEmprestimosUseCase;
        this.realizarEmprestimoUseCase = realizarEmprestimoUseCase;
        this.devolucaoEmprestimoUseCase = devolucaoEmprestimoUseCase;
        this.listarEmprestimoPorIdUseCase = listarEmprestimoPorIdUseCase;
    }

    @GetMapping("/listar")
    public CollectionModel<EntityModel<DTOListarEmprestimo>> listarTodosEmprestimosUsuario(@RequestParam String matricula) {

        try {
            List<DTOListarEmprestimo> emprestimoDTO = listarEmprestimosUseCase.listarEmpretimosUsuarioUseCase(matricula);

            if (emprestimoDTO.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum empréstimo encontrado");
            }

            List<EntityModel<DTOListarEmprestimo>> emprestimoModel = emprestimoDTO.stream()
                    .map(dto -> EntityModel.of(dto,
                            linkTo(methodOn(EmprestimoController.class)
                                    .listarTodosEmprestimosUsuario(matricula))
                                    .withSelfRel(),
                            linkTo(methodOn(EmprestimoController.class)
                                    .listarEmprestimoEspecifico(dto.emprestimoid()))
                                    .withRel("emprestimo-especifico")
                    ))
                    .toList();

            return CollectionModel.of(emprestimoModel,
                    linkTo(methodOn(EmprestimoController.class).listarTodosEmprestimosUsuario(matricula)).withSelfRel());

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi encontrado empréstimos com a matrícula informada");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Houve um erro ao listar empréstimos");
        }
    }

    @GetMapping("/listar/{id}")
    public EntityModel<DTOListagemCompleta> listarEmprestimoEspecifico(@PathVariable Long id) {

        try {
            DTOListagemCompleta dto = listarEmprestimoPorIdUseCase.listarEmprestimoPorId(id);

            if (dto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi encontrado o empréstimo com o ID informado");
            }

            EntityModel<DTOListagemCompleta> emprestimomodel = EntityModel.of(dto,
                    linkTo(methodOn(EmprestimoController.class)
                            .listarEmprestimoEspecifico(id)).withSelfRel(),
                    linkTo(methodOn(EmprestimoController.class)
                            .listarTodosEmprestimosUsuario(dto.matricula())).withRel("todos-emprestimos")
            );

            return emprestimomodel;

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/efetuar")
    public ResponseEntity<?> efetuarEmprestimo(@RequestBody DTOEmprestimo dtoEmprestimo) {

        try {
            realizarEmprestimoUseCase.executarEmprestimo(dtoEmprestimo);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/devolucao")
    public ResponseEntity<?> efetuarDevolucao(@RequestBody DTOEmprestimo dtoEmprestimo) {

        try {
            devolucaoEmprestimoUseCase.executarDevolucao(dtoEmprestimo);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
