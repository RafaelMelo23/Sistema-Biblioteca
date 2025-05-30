package com.poo.projeto_final.application.usecase.livro;

import com.poo.projeto_final.application.dto.projection.DTOExemplarLivro;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.repository.DAOExemplarLivro;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso responsável por buscar exemplares por id do livro, e status do exemplar, está na camada de caso
 * de uso por não passar por validações.
 */
@Component
public class BuscaExemplarPorStatusUseCase {

    private final DAOExemplarLivro dAOExemplarLivro;

    public BuscaExemplarPorStatusUseCase(DAOExemplarLivro dAOExemplarLivro) {
        this.dAOExemplarLivro = dAOExemplarLivro;
    }

    public List<DTOExemplarLivro> buscarExemplarPorStatus(Long livroId, StatusExemplar status) {

        return dAOExemplarLivro.findByLivro_IdAndStatusExemplar(livroId, status);
    }

}
