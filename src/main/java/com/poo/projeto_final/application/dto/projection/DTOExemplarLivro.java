package com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto.projection;

import com.rafael.lucas.biblioteca.sistema_biblioteca.domain.enums.StatusExemplar;

public interface DTOExemplarLivro {

    String codigoExemplar();
    StatusExemplar statusExemplar();
    DTOLivro dtoLivro();
}
