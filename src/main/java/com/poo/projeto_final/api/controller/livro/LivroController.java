package com.poo.projeto_final.api.controller.livro;

import com.poo.projeto_final.application.dto.DTOLivro;
import com.poo.projeto_final.application.dto.projection.DTOExemplarLivro;
import com.poo.projeto_final.application.usecase.livro.BuscaExemplarPorStatusUseCase;
import com.poo.projeto_final.application.usecase.livro.BuscaLivroPorTituloUseCase;
import com.poo.projeto_final.application.usecase.livro.CriarLivroUseCase;
import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/livro")
public class LivroController {

    private final CriarLivroUseCase criarLivroUseCase;
    private final BuscaExemplarPorStatusUseCase buscaExemplarPorStatusUseCase;
    private final BuscaLivroPorTituloUseCase buscaLivroPorTituloUseCase;

    public LivroController(CriarLivroUseCase criarLivroUseCase, BuscaExemplarPorStatusUseCase buscaExemplarPorStatusUseCase, BuscaLivroPorTituloUseCase buscaLivroPorTituloUseCase) {
        this.criarLivroUseCase = criarLivroUseCase;
        this.buscaExemplarPorStatusUseCase = buscaExemplarPorStatusUseCase;
        this.buscaLivroPorTituloUseCase = buscaLivroPorTituloUseCase;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarLivro(DTOLivro dto) {

        try {
            criarLivroUseCase.criarLivro(dto);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro ao criar livro, informações inválidas");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar livro");
        }
    }

    @GetMapping("/buscar/exemplares")
    public List<DTOExemplarLivro> buscarLivros(@RequestParam Long livroId, @RequestParam StatusExemplar statusExemplar) {

        return buscaExemplarPorStatusUseCase.buscarExemplarPorStatus(livroId, statusExemplar);
    }

    @GetMapping("/buscar/titulo")
    public CollectionModel<EntityModel<Livro>> buscarPorTituloContem(@RequestParam String titulo) {

        List<Livro> livros = buscaLivroPorTituloUseCase.buscaLivroPorTituloContem(titulo);

        List<EntityModel<Livro>> livrosComLinks = livros.stream().map(livro -> {
            EntityModel<Livro> recurso = EntityModel.of(livro);

            Link exemplaresDisponiveis = linkTo(methodOn(LivroController.class)
                    .buscarLivros(livro.getId(), StatusExemplar.DISPONIVEL))
                    .withRel("exemplares-disponiveis");

            recurso.add(exemplaresDisponiveis);

            return recurso;
        }).toList();

        return CollectionModel.of(livrosComLinks);
    }



}
