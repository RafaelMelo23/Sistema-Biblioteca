package com.poo.projeto_final.application.usecase.livro;

import com.poo.projeto_final.application.dto.projection.DTOExemplarLivro;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BuscaGeralLivroUseCase {

    private final DAOExemplarLivro daoExemplarLivro;

    public BuscaGeralLivroUseCase(DAOExemplarLivro daoExemplarLivro) {
        this.daoExemplarLivro = daoExemplarLivro;
    }

    public Page<DTOExemplarLivro> buscarTodosLivrosPorStatus(StatusExemplar status, int pagina, int quantidade) {

        Pageable paginacao = PageRequest.of(pagina, quantidade);

        return daoExemplarLivro.findByStatusExemplarIs(status, paginacao);
    }
}
