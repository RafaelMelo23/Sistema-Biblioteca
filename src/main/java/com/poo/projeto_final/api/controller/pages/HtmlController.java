package com.poo.projeto_final.api.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    @GetMapping("/livro")
    public String paginaLivro() {
        return "redirect:/livro.html";
    }

    @GetMapping("/usuario")
    public String paginaUsuario() {
        return "redirect:/usuario.html";
    }

}
