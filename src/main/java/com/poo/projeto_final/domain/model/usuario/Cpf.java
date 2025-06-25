package com.poo.projeto_final.domain.model.usuario;

import jakarta.persistence.Embeddable;

@Embeddable
public class Cpf {

    private String value;

    public Cpf() {
    }

    public Cpf(String value) {
        if (value == null || !value.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido: deve conter 11 digitos");
        }
        this.value = value;
    }

    public static Cpf of(String value) {
        return new Cpf(value);
    }

    public String getValue() {
        return value;
    }
}
