package com.poo.projeto_final.domain.repository;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.domain.model.shared.vo.ExemplarLivroId;
import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.projection.DTOExemplarLivro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DAOExemplarLivro extends ListCrudRepository<ExemplarLivro, Long> {

    long countByLivro_Id(Long idLivro);

    Page<DTOExemplarLivro> findByStatusExemplarIs(StatusExemplar statusExemplar, Pageable pageable);

    Page<DTOExemplarLivro> findByLivro_Titulo(String titulo, Pageable pageable);

    Optional<ExemplarLivro> findByCodigoExemplar(String codigoExemplar);

    @Query("SELECT e.id FROM ExemplarLivro e " +
            "WHERE e.codigoExemplar.value = :codigoExemplar " +
            "AND e.statusExemplar = :statusExemplar")
    Optional<ExemplarLivroId> findByCodigoExemplarValueIs(@Param("codigoExemplar")
                                                                        String codigoExemplarValue,
                                                          @Param("statusExemplar")
                                                                        StatusExemplar statusExemplar);
}
