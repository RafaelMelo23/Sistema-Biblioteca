package com.poo.projeto_final.infrastructure.config.persistence.mappers;

import com.poo.projeto_final.domain.model.professor.Professor;
import com.poo.projeto_final.infrastructure.config.persistence.entities.ProfessorData;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    public Professor toDomain(ProfessorData data) {
        if (data == null) {
            return null;
        }
        return new Professor(
                data.getId(),
                data.getNome(),
                data.getCpf(),
                data.getEmail(),
                data.getMatricula()
        );
    }

    public ProfessorData toData(Professor domain) {
        if (domain == null) {
            return null;
        }
        ProfessorData data = new ProfessorData();
        data.setId(domain.getId());
        data.setNome(domain.getNome());
        data.setCpf(domain.getCpf());
        data.setEmail(domain.getEmail());
        data.setMatricula(domain.getMatricula());
        return data;
    }
}
