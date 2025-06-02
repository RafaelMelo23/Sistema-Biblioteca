package com.poo.projeto_final.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.poo.projeto_final.domain.enums.TipoUsuario;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DTOEmprestimo(String codigoExemplar,
                            String matricula,
                            LocalDate dataPrevista,
                            TipoUsuario tipoUsuario) {}
