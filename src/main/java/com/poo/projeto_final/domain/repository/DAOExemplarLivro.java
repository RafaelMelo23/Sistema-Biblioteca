package com.poo.projeto_final.domain.repository;

import com.poo.projeto_final.application.dto.projection.DTOExemplarLivro;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.model.shared.vo.ExemplarLivroId;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DAOExemplarLivro extends ListCrudRepository<ExemplarLivro, Long> {

    long countExemplarLivroBy();

    @Modifying
    @Query("UPDATE ExemplarLivro ex SET ex.statusExemplar = :statusExemplar " +
            "WHERE ex.id = :exemplarId")
    void setStatusExemplar(@Param("statusExemplar") StatusExemplar statusExemplar,
                           @Param("exemplarId") ExemplarLivroId exemplarId);

    @Query("SELECT e.id FROM ExemplarLivro e " +
            "WHERE e.codigoExemplar.value = :codigoExemplar " +
            "AND e.statusExemplar = :statusExemplar")
    Optional<ExemplarLivroId> findByCodigoExemplarValueIs(@Param("codigoExemplar") String codigoExemplarValue,
                                                          @Param("statusExemplar") StatusExemplar statusExemplar);

    @Query("SELECT e.livro.id FROM ExemplarLivro e WHERE e.id = :exemplarId")
    Optional<Long> findLivroIdByExemplarLivroId(@Param("exemplarId") ExemplarLivroId exemplarLivroId);

    List<DTOExemplarLivro> findByLivro_IdAndStatusExemplar(Long livroId, StatusExemplar statusExemplar);
}
