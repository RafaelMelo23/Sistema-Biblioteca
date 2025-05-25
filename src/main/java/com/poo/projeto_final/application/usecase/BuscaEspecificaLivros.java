package com.rafael.lucas.biblioteca.sistema_biblioteca.application.usecase;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.projection.DTOExemplarLivro;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository.DAOExemplarLivro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BuscaEspecificaLivros {

    private final DAOExemplarLivro daoExemplarLivro;

    public BuscaEspecificaLivros(DAOExemplarLivro daoExemplarLivro) {
        this.daoExemplarLivro = daoExemplarLivro;
    }

    public Page<DTOExemplarLivro> buscarLivrosPorNome(String nome, Pageable pageable) {

       return daoExemplarLivro.findByLivro_Titulo(nome, pageable);
    }
}
