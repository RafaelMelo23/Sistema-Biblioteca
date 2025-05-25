package com.rafael.lucas.biblioteca.sistema_biblioteca.application.dto;

import java.time.LocalDate;

public record DTOEmprestimo(String codigoExemplar, String matricula, LocalDate dataPrevista) {}
