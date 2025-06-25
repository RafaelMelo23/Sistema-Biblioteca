package com.poo.projeto_final.infrastructure.config.persistence.mappers;

import com.poo.projeto_final.domain.model.exemplar.ExemplarLivro;
import com.poo.projeto_final.infrastructure.config.persistence.entities.ExemplarLivroData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExemplarLivroMapper {

    private final LivroMapper livroMapper;

    public ExemplarLivro toDomain(ExemplarLivroData data) {
        if (data == null) {
            return null;
        }
        return new ExemplarLivro(
                data.getId(),
                livroMapper.toDomain(data.getLivroData()),
                data.getCodigoExemplar(),
                data.getStatusExemplar()
        );
    }

    public ExemplarLivroData toData(ExemplarLivro domain) {
        if (domain == null) {
            return null;
        }
        ExemplarLivroData data = new ExemplarLivroData();
        data.setId(domain.getId());
        data.setLivroData(livroMapper.toData(domain.getLivro()));
        data.setCodigoExemplar(domain.getCodigoExemplar());
        data.setStatusExemplar(domain.getStatusExemplar());
        return data;
    }
}
