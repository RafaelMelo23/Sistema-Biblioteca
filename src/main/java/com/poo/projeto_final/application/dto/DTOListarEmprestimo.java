package com.poo.projeto_final.application.dto;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;

import java.time.LocalDate;

public record DTOListarEmprestimo(Long emprestimoid,
                                  String tituloLivro,
                                  String codigoExemplar,
                                  LocalDate dataEmprestimo,
                                  LocalDate dataPrevista,
                                  LocalDate dataDevolucao,
                                  StatusEmprestimo statusEmprestimo,
                                  String nomeUsuario) {}
