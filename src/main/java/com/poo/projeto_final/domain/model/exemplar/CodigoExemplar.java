package com.poo.projeto_final.domain.model.exemplar;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class CodigoExemplar {

    private String value;

    public CodigoExemplar() {}

    public CodigoExemplar(String value) { this.value = value; }

    public static CodigoExemplar of(String value) { return new CodigoExemplar(value); }

}
