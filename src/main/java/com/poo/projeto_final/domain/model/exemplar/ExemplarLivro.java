package com.poo.projeto_final.domain.model.exemplar;

import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "exemplar_livro")
public class ExemplarLivro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "codigo_exemplar", nullable = false, unique = true, length = 100))
    private CodigoExemplar codigoExemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_exemplar", nullable = false, length = 30)
    private StatusExemplar statusExemplar;

    public static ExemplarLivro criarExemplar(Livro livro, CodigoExemplar codigoExemplar) {
        ExemplarLivro novoExemplar = new ExemplarLivro();

        novoExemplar.setLivro(livro);
        novoExemplar.setCodigoExemplar(codigoExemplar);
        novoExemplar.setStatusExemplar(StatusExemplar.DISPONIVEL);

        return novoExemplar;
    }

}
