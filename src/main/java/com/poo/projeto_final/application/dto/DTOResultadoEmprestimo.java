package com.poo.projeto_final.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DTOResultadoEmprestimo(boolean sucesso,
                                     List<DTOListarEmprestimo> emprestimosPendentes) {
}
