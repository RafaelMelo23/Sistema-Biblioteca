package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.Embeddable;

@Embeddable
public class Autor {
    private String value;

    protected Autor() {}

    public Autor(String value) {
        this.value = value;
    }

    public static Autor of(String value) {
        return new Autor(value);
    }

    public String getValue() {
        return value;
    }
}
