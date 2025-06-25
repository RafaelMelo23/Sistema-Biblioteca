package com.poo.projeto_final.impl.domain.service;

import com.poo.projeto_final.domain.enums.StatusEmprestimo;
import com.poo.projeto_final.domain.enums.StatusExemplar;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.repository.EmprestimoRepository;
import com.poo.projeto_final.domain.repository.ExemplarLivroRepository;
import com.poo.projeto_final.domain.service.EmprestimoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepo;
    private final ExemplarLivroRepository exemplarRepo;

    @Override
    public List<Emprestimo> buscarAtrasos(Matricula matricula) {

        return emprestimoRepo
                .emprestimosAtrasados(
                        LocalDate.now().minusDays(5),
                        StatusEmprestimo.ATIVO);
    }

    @Override
    public Emprestimo realizarEmprestimo(Matricula matricula, String codigoExemplar, LocalDate dataPrevista) {

        Long exemplarId = exemplarRepo
                .buscarExemplarDisponivelIdPorCodigo(codigoExemplar, StatusExemplar.DISPONIVEL)
                .orElseThrow(() -> new IllegalArgumentException("Exemplar indisponível"));

        Long livroId = exemplarRepo
                .buscarLivroIdPorExemplarId(exemplarId)
                .orElseThrow(() -> new IllegalArgumentException("Exemplar sem livro ou não encontrado"));

        if (emprestimoRepo.contarEmprestimosPorStatusPendente(matricula, livroId) > 0) {
            throw new IllegalStateException("O usuário já tem empréstimo(s) pendente(s) do mesmo livro");
        }

        exemplarRepo.setStatusExemplar(StatusExemplar.EMPRESTADO, exemplarId);

        Emprestimo emprestimoRealizado = Emprestimo.realizarEmprestimo(matricula, exemplarId, dataPrevista);

        return emprestimoRepo.salvar(emprestimoRealizado);
    }

    @Override
    public void finalizarEmprestimo(Matricula matricula, String codigoExemplar) {

        Long exemplarId = exemplarRepo
                .buscarExemplarDisponivelIdPorCodigo(codigoExemplar, StatusExemplar.EMPRESTADO)
                .orElseThrow(() -> new IllegalArgumentException("Exemplar não está emprestado"));

        exemplarRepo.setStatusExemplar(StatusExemplar.DISPONIVEL, exemplarId);
        emprestimoRepo.setStatusEmprestimo(StatusEmprestimo.FINALIZADO, matricula);
    }

    @Override
    public void aplicarStatusAtrasadoAoEmprestimo() {
        var dataAtualMenosCinco = LocalDate.now().minusDays(5);

        var atrasos = emprestimoRepo.emprestimosAtrasados(dataAtualMenosCinco, StatusEmprestimo.ATIVO);

        var idsEmp = atrasos.stream()
                .map(Emprestimo::getId)
                .toList();

        var idsEx = atrasos.stream()
                .map(Emprestimo::getExemplarLivroId)
                .toList();

        emprestimoRepo.alterarStatusEmprestimo(idsEmp, StatusEmprestimo.ATRASADO);
        exemplarRepo.alterarStatusExemplar(idsEx, StatusExemplar.PERDIDO);
    }
}
