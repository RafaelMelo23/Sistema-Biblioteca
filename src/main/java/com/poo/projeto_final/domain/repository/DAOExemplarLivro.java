package com.rafael.lucas.biblioteca.sistema_biblioteca.domain.repository;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.projection.DTOExemplarLivro;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.enums.StatusExemplar;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.ExemplarLivro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface DAOExemplarLivro extends ListCrudRepository<ExemplarLivro, Long> {

    long countByLivro_Id(Long idLivro);

    Page<DTOExemplarLivro> findByStatusExemplarIs(StatusExemplar statusExemplar, Pageable pageable);

    Page<DTOExemplarLivro> findByLivro_Titulo(String titulo, Pageable pageable);

    Optional<ExemplarLivro> findByCodigoExemplar(String codigoExemplar);
}
