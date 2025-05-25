package com.rafael.lucas.biblioteca.sistema_biblioteca.application.mapper;

import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.DTOAluno;
import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.DTOProfessor;
import com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.DTOUsuario;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Aluno;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.Professor;
import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.model.UsuarioBiblioteca;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioBiblioteca toEntity(DTOUsuario dtoUsuario);

    Aluno toEntity(DTOAluno dtoAluno);

    Professor toEntity(DTOProfessor dtoProfessor);
}
