package com.poo.projeto_final.domain.model.exemplar;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class CodigoExemplar {

    private String value;

    public CodigoExemplar() {}

    public CodigoExemplar(String value) { this.value = value; }

    /*
    Os códigos dos livros seguem o padrão númerico do banco, ou seja, se há 50 exemplares registrados, o próximo será LIV-EX-51
     */
    public static CodigoExemplar of(long contagemTotal) {

        String value = "LIV-EX-" + (contagemTotal + 1);
        return new CodigoExemplar(value);
    }
}
