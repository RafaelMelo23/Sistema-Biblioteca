package com.poo.projeto_final.domain.repository;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;

import java.util.List;
import java.util.Optional;

public interface ExemplarLivroRepository {

    long contarTodos();

    void salvar(ExemplarLivro exemplarLivro);

    void salvarAll(List<ExemplarLivro> exemplarLivros);

    void setStatusExemplar(StatusExemplar statusExemplar, Long exemplarId);

    Optional<Long> buscarExemplarDisponivelIdPorCodigo(String codigoExemplar, StatusExemplar status);

    Optional<Long> buscarLivroIdPorExemplarId(Long exemplarLivroId);

    List<ExemplarLivro> buscarPorLivroEStatus(Long livroId, StatusExemplar status);

    void alterarStatusExemplar(List<Long> exemplarIds, StatusExemplar status);

    CodigoExemplar findCodigoExemplarByExemplarId(Long exemplarId);
}
