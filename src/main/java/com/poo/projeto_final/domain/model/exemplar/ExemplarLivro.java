package com.poo.projeto_final.domain.model.exemplar;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.shared.vo.ExemplarLivroId;
import jakarta.persistence.*;

@Entity
@Table(name = "exemplar_livro")
public class ExemplarLivro {

    @EmbeddedId
    private ExemplarLivroId id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "codigo_exemplar", nullable = false, unique = true, length = 100))
    private CodigoExemplar codigoExemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_exemplar", nullable = false, length = 30)
    private StatusExemplar statusExemplar;

}
