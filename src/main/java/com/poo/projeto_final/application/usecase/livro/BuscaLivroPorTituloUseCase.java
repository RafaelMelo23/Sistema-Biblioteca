package com.poo.projeto_final.application.usecase.livro;

import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.repository.DAOLivro;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso responsável por buscar livros a partir do título informado, está na camada de caso de uso
 * por não passar por validações
 */
@Component
public class BuscaLivroPorTituloUseCase {

    private final DAOLivro daoLivro;

    public BuscaLivroPorTituloUseCase(DAOLivro daoLivro) {
        this.daoLivro = daoLivro;
    }

    public List<Livro> buscaLivroPorTituloContem(String titulo) {

        return daoLivro.findByTituloContains(titulo);
    }

}
