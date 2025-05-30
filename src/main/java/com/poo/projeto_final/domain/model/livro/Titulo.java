package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.Embeddable;

@Embeddable
public class Titulo {
    private String value;

    protected Titulo() {}

    Titulo(String value) {
        this.value = value;
    }

    public static Titulo of(String value) {
        return new Titulo(value);
    }

    public String getValue() {
        return value;
    }
}
