package com.poo.projeto_final.domain.model.emprestimo;

public class EmprestimoId {

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
