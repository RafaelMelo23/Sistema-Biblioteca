package com.poo.projeto_final.application.usecase;

import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.projection.DTOExemplarLivro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BuscaEspecificaLivroUseCase {

    private final DAOExemplarLivro daoExemplarLivro;

    public BuscaEspecificaLivroUseCase(DAOExemplarLivro daoExemplarLivro) {
        this.daoExemplarLivro = daoExemplarLivro;
    }

    public Page<DTOExemplarLivro> buscarLivrosPorNome(String nome, Pageable pageable) {

       return daoExemplarLivro.findByLivro_Titulo(nome, pageable);
    }
}
