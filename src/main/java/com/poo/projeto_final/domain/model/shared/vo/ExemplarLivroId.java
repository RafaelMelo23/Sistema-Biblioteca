package com.poo.projeto_final.domain.model.shared.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ExemplarLivroId {

    @Column(name = "id", nullable = false)
    private Long value;

    protected ExemplarLivroId() {}

    private ExemplarLivroId(Long value) { this.value = value; }

    public static ExemplarLivroId of(Long value) { return new ExemplarLivroId(value); }

    public Long getValue() { return value; }
}
