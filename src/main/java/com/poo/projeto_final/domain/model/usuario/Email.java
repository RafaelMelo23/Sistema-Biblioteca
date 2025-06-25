package com.poo.projeto_final.domain.model.usuario;

import jakarta.persistence.Embeddable;

@Embeddable
public class Email {
    private String value;

    public Email() {
    }

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (value.length() > 320) {
            throw new IllegalArgumentException("Email excede o tamanho máximo");
        }
        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.value = value;
    }

    public static Email of(String value) {
        return new Email(value);
    }

    public String getValue() {
        return value;
    }
}