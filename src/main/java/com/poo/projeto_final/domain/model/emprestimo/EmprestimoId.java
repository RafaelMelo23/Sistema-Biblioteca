package com.poo.projeto_final.domain.model.emprestimo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Embeddable
public class EmprestimoId {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long value;

    public EmprestimoId() {
    }

    public EmprestimoId(Long value) {
        this.value = value;
    }

    public static EmprestimoId of(Long value) {
        return new EmprestimoId(value);
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
