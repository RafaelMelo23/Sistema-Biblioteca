package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.Embeddable;

@Embeddable
public class Ano {
    private String value;

    protected Ano() {}

    Ano(String value) {
        this.value = value;
    }

    public static Ano of(String value) {
        return new Ano(value);
    }

    public String getValue() {
        return value;
    }
}
