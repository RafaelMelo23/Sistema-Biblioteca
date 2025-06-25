package com.poo.projeto_final.infrastructure.config.persistence.entities;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "exemplar_livro")
public class ExemplarLivroData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private LivroData livroData;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "codigo_exemplar", nullable = false, unique = true, length = 100))
    private CodigoExemplar codigoExemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_exemplar", nullable = false, length = 30)
    private StatusExemplar statusExemplar;

    public static ExemplarLivroData criarExemplar(LivroData livroData, CodigoExemplar codigoExemplar) {
        ExemplarLivroData novoExemplar = new ExemplarLivroData();

        novoExemplar.setLivroData(livroData);
        novoExemplar.setCodigoExemplar(codigoExemplar);
        novoExemplar.setStatusExemplar(StatusExemplar.DISPONIVEL);

        return novoExemplar;
    }

}
