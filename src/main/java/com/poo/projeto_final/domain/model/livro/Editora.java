package com.poo.projeto_final.domain.model.livro;

import jakarta.persistence.Embeddable;

@Embeddable
public class Editora {
    private String value;

    protected Editora() {}

    Editora(String value) {
        this.value = value;
    }

    public static Editora of(String value) {
        return new Editora(value);
    }

    public String getValue() {
        return value;
    }
}
