package com.poo.projeto_final.domain.repository;

import com.poo.projeto_final.application.dto.DTOEmprestimoAtrasado;
import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DAOEmprestimo extends ListCrudRepository<Emprestimo, Long> {

    @Modifying
    @Query("UPDATE Emprestimo em SET em.statusEmprestimo = :statusEmprestimo " +
            "WHERE em.matricula = :matricula")
    void setStatusEmprestimo(@Param("statusEmprestimo") StatusEmprestimo statusEmprestimo,
                             @Param("matricula") Matricula matricula);

    @Query("SELECT COUNT(e) " +
            "FROM Emprestimo e " +
            "JOIN ExemplarLivro ex ON ex.id = e.exemplarLivroId " +
            "WHERE e.matricula = :matricula " +
            "  AND ex.livro.id = :livroId " +
            "  AND (e.statusEmprestimo = com.poo.projeto_final.domain.enums.StatusEmprestimo.ATIVO  " +
            "    OR e.statusEmprestimo = com.poo.projeto_final.domain.enums.StatusEmprestimo.ATRASADO)")
    long contarEmprestimosPorStatusPendente(@Param("matricula") Matricula matricula,
                                            @Param("livroId") Long livroId);

    @Query("""
            SELECT new com.poo.projeto_final.application.dto.DTOListarEmprestimo(emp.id,
                                 liv.titulo.value,\s
                                 ex.codigoExemplar.value, emp.dataEmprestimo, emp.dataPrevista, emp.dataFactual,
                                 emp.statusEmprestimo, COALESCE(al.nome.value, prof.nome.value))
                                                         FROM Emprestimo emp
                                                         JOIN ExemplarLivro ex ON emp.exemplarLivroId = ex.id
                                                         JOIN Livro liv on ex.livro.id = liv.id
                                                         LEFT JOIN Aluno al ON al.matricula.value = emp.matricula.value
                                                         LEFT JOIN Professor prof ON prof.matricula.value = emp.matricula.value         \s
                                                         WHERE emp.matricula.value = :matricula""")
    List<DTOListarEmprestimo> listarEmprestimosUsuario(@Param("matricula") String matricula);

    @Query("""
            SELECT new com.poo.projeto_final.application.dto.DTOListarEmprestimo(emp.id,
                                 liv.titulo.value,\s
                                 ex.codigoExemplar.value, emp.dataEmprestimo, emp.dataPrevista, emp.dataFactual,
                                 emp.statusEmprestimo, COALESCE(al.nome.value, prof.nome.value))
                                                         FROM Emprestimo emp
                                                         JOIN ExemplarLivro ex ON emp.exemplarLivroId = ex.id
                                                         JOIN Livro liv on ex.livro.id = liv.id
                                                         LEFT JOIN Aluno al ON al.matricula.value = emp.matricula.value
                                                         LEFT JOIN Professor prof ON prof.matricula.value = emp.matricula.value         \s
                                                         WHERE emp.matricula.value = :matricula
                                                         AND emp.statusEmprestimo = :statusEmprestimo""")
    List<DTOListarEmprestimo> detalhesEmprestimoAtrasado(@Param("matricula") String matricula,
                                                         @Param("statusEmprestimo") StatusEmprestimo statusEmprestimo);

    boolean existsByMatriculaAndStatusEmprestimo(Matricula matricula, StatusEmprestimo statusEmprestimo);

    @Query("""
            SELECT new com.poo.projeto_final.application.dto.DTOListagemCompleta(COALESCE(al.nome.value, prof.nome.value),
                                 liv.titulo.value, liv.autor.value, liv.isbn.value, liv.ano.value, liv.editora.value,
                                 ex.codigoExemplar.value, emp.dataEmprestimo, emp.dataPrevista, emp.dataFactual,
                                 emp.statusEmprestimo, emp.matricula.value)
                                                       FROM Emprestimo emp
                                                       JOIN ExemplarLivro ex ON emp.exemplarLivroId = ex.id
                                                       JOIN Livro liv on ex.livro.id = liv.id
                                                       LEFT JOIN Aluno al ON al.matricula.value = emp.matricula.value
                                                       LEFT JOIN Professor prof ON prof.matricula.value = emp.matricula.value        \s
                                                       WHERE emp.id = :emprestimoId""")
    DTOListagemCompleta listarEmprestimosPorId(@Param("emprestimoId") Long emprestimoId);

    @Query("""
            SELECT new com.poo.projeto_final.application.dto.DTOEmprestimoAtrasado(
                                    emp.id,
                                    emp.exemplarLivroId,
                                    emp.matricula.value)
                                    FROM Emprestimo emp
                                    WHERE emp.statusEmprestimo = :statusEmprestimo
                                    AND emp.dataPrevista <= :data
                                    AND emp.dataFactual = null""")
    List<DTOEmprestimoAtrasado> emprestimosAtrasados(@Param("data") Date dataReferencia,
                                                     @Param("statusEmprestimo") StatusEmprestimo status);

    @Modifying
    @Query("UPDATE Emprestimo e SET e.statusEmprestimo = :statusEmprestimo WHERE e.id IN :emprestimoId")
    void alterarStatusEmprestimo(@Param("emprestimoId") List<Long> emprestimoId,
                                 @Param("statusEmprestimo") StatusEmprestimo status);


}
