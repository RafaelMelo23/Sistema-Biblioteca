package com.poo.projeto_final.domain.model.shared.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Embeddable
public class ExemplarLivroId {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long value;

    protected ExemplarLivroId() {}

    private ExemplarLivroId(Long value) { this.value = value; }

    public static ExemplarLivroId of(Long value) { return new ExemplarLivroId(value); }

    public Long getValue() { return value; }
}
