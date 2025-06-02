package com.poo.projeto_final.api.controller;

import com.poo.projeto_final.api.controller.emprestimo.EmprestimoController;
import com.poo.projeto_final.api.controller.livro.LivroController;
import com.poo.projeto_final.api.controller.usuario.UsuarioController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Classe controller responsável por alimentar o front-end com todos os caminhos possíveis das APIs. Feito por meio das classes do Spring Hateoas,
 * retornando um JSON com as URIs das APIs + o link relation delas (criar-aluno por exemplo).
 */
@RestController
@RequestMapping("/api")
public class RootEntryPointController {

    @GetMapping
    public RepresentationModel<?> caminhoRaiz() {

        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(
                // APIs Livro
                linkTo(methodOn(LivroController.class).buscarPorTituloContem(null)).withRel("buscar-por-titulo"),
                linkTo(methodOn(LivroController.class).buscarLivros(null, null)).withRel("buscar-livro-id"),
                linkTo(methodOn(LivroController.class).criarLivro(null)).withRel("criar-livro"),

                // APIs Usuario
                linkTo(methodOn(UsuarioController.class).cadastrarAluno(null)).withRel("criar-aluno"),
                linkTo(methodOn(UsuarioController.class).cadastrarProfessor(null)).withRel("criar-professor"),

                // APIs Emprestimo
                linkTo(methodOn(EmprestimoController.class).listarTodosEmprestimosUsuario(null)).withRel("listar-emprestimos-by-matricula"),
                linkTo(methodOn(EmprestimoController.class).listarEmprestimoEspecifico(null)).withRel("listar-emprestimos-by-emprestimo-id"),
                linkTo(methodOn(EmprestimoController.class).efetuarEmprestimo(null)).withRel("realizar-emprestimo"),
                linkTo(methodOn(EmprestimoController.class).efetuarDevolucao(null)).withRel("realizar-devolucao")
        );

        return model;
    }
}
