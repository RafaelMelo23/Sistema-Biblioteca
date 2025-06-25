package com.poo.projeto_final.infrastructure.config.persistence.mappers;

import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.infrastructure.config.persistence.entities.LivroData;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {

    public Livro toDomain(LivroData data) {
        if (data == null) {
            return null;
        }
        return new Livro(
                data.getId(),
                data.getTitulo(),
                data.getAutor(),
                data.getIsbn(),
                data.getAno(),
                data.getEditora()
        );
    }

    public LivroData toData(Livro domain) {
        if (domain == null) {
            return null;
        }
        LivroData data = new LivroData();
        data.setId(domain.getId());
        data.setTitulo(domain.getTitulo());
        data.setAutor(domain.getAutor());
        data.setIsbn(domain.getIsbn());
        data.setAno(domain.getAno());
        data.setEditora(domain.getEditora());
        return data;
    }
}
