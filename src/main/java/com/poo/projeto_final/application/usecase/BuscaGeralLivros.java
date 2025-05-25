package com.rafael.lucas.biblioteca.sistema_biblioteca.application.usecase;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.projection.DTOExemplarLivro;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.enums.StatusExemplar;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository.DAOExemplarLivro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BuscaGeralLivros {

    private final DAOExemplarLivro daoExemplarLivro;

    public BuscaGeralLivros(DAOExemplarLivro daoExemplarLivro) {
        this.daoExemplarLivro = daoExemplarLivro;
    }

    public Page<DTOExemplarLivro> buscarTodosLivrosPorStatus(StatusExemplar status, int pagina, int quantidade) {

        Pageable paginacao = PageRequest.of(pagina, quantidade);

        return daoExemplarLivro.findByStatusExemplarIs(status, paginacao);
    }
}
