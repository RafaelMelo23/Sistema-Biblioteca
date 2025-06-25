package com.poo.projeto_final.infrastructure.config.persistence.mappers;

import com.poo.projeto_final.domain.model.aluno.Aluno;
import com.poo.projeto_final.infrastructure.config.persistence.entities.AlunoData;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    public Aluno toDomain(AlunoData data) {
        if (data == null) {
            return null;
        }
        return new Aluno(
                data.getId(),
                data.getNome(),
                data.getCpf(),
                data.getEmail(),
                data.getMatricula()
        );
    }

    public AlunoData toData(Aluno domain) {
        if (domain == null) {
            return null;
        }
        AlunoData data = new AlunoData();
        data.setId(domain.getId());
        data.setNome(domain.getNome());
        data.setCpf(domain.getCpf());
        data.setEmail(domain.getEmail());
        data.setMatricula(domain.getMatricula());
        return data;
    }
}

