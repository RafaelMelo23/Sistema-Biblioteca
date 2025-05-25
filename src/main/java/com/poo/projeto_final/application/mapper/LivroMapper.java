package com.rafael.lucas.biblioteca.sistema_biblioteca.application.mapper;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.DTOLivro;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Livro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro toEntity(DTOLivro livro);
}
