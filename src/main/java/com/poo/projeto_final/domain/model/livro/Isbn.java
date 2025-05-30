package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.Embeddable;

@Embeddable
public class Isbn {
    private String value;

    protected Isbn() {}

    Isbn(String value) {
        this.value = value;
    }

    public static Isbn of(String value) {
        return new Isbn(value);
    }

    public String getValue() {
        return value;
    }
}
