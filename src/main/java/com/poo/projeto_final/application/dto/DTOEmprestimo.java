package com.poo.projeto_final.application.dto;

import com.poo.projeto_final.domain.enums.TipoUsuario;

import java.time.LocalDate;

public record DTOEmprestimo(String codigoExemplar, String matricula, LocalDate dataPrevista, TipoUsuario tipoUsuario) {}
