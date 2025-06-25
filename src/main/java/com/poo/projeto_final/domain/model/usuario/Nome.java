package com.poo.projeto_final.domain.model.usuario;

import jakarta.persistence.Embeddable;

@Embeddable
public class Nome {

    private String value;

    public Nome() {
    }

    public Nome(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (value.length() > 20) {
            throw new IllegalArgumentException("Nome não pode ter mais de 20 caracteres");
        }
        this.value = value;
    }

    public static Nome of(String value) {
        return new Nome(value);
    }

    public String getValue() {
        return value;
    }
}
