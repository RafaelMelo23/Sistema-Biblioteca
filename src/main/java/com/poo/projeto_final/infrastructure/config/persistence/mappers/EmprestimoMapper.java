package com.poo.projeto_final.infrastructure.config.persistence.mappers;

import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.infrastructure.config.persistence.entities.EmprestimoData;
import org.springframework.stereotype.Component;

@Component
public class EmprestimoMapper {

    public Emprestimo toDomain(EmprestimoData data) {
        if (data == null) {
            return null;
        }
        return new Emprestimo(
                data.getId(),
                data.getExemplarLivroId(),
                data.getMatricula(),
                data.getDataEmprestimo(),
                data.getDataPrevista(),
                data.getDataFactual(),
                data.getStatusEmprestimo()
        );
    }

    public EmprestimoData toData(Emprestimo domain) {
        if (domain == null) {
            return null;
        }
        EmprestimoData data = new EmprestimoData();
        data.setId(domain.getId());
        data.setExemplarLivroId(domain.getExemplarLivroId());
        data.setMatricula(domain.getMatricula());
        data.setDataEmprestimo(domain.getDataEmprestimo());
        data.setDataPrevista(domain.getDataPrevista());
        data.setDataFactual(domain.getDataFactual());
        data.setStatusEmprestimo(domain.getStatusEmprestimo());
        return data;
    }
}
