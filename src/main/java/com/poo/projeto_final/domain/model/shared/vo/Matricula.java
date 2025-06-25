package com.poo.projeto_final.domain.model.shared.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public class Matricula {

    private String value;

    public Matricula() {
    }

    public Matricula(String value) {
        this.value = value;
    }

    public static Matricula of(String value) {
        return new Matricula(value);
    }

    public String getValue() {
        return value;
    }
}
