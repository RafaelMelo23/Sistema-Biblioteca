package com.poo.projeto_final.application.dto;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;

import java.time.LocalDate;

public record DTOListagemCompleta(String nomeUsuario, String titulo, String autor, String isbn, String ano, String editora,
                                  String codigoExemplar, LocalDate dataEmprestimo, LocalDate dataPrevista,
                                  LocalDate dataDevolucao, StatusEmprestimo statusEmprestimo, String matricula) {
}
