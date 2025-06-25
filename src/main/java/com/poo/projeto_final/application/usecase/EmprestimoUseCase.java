package com.poo.projeto_final.application.usecase;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.application.dto.DTOResultadoEmprestimo;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import com.poo.projeto_final.domain.repository.*;
import com.poo.projeto_final.domain.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmprestimoUseCase {

    private final ExemplarLivroRepository exemplarLivroRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository profRepo;
    private final LivroRepository livroRepositoryImpl;
    private final UsuarioUseCase usuarioUseCase;
    private final EmprestimoRepository emprestimoRepositoryImpl;
    private final EmprestimoService emprestimoService;

    @Transactional
    public DTOResultadoEmprestimo registrarEmprestimo(DTOEmprestimo in) {

        if (in.dataPrevista().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data prevista anterior ao dia atual");
        }

        Matricula matricula = Matricula.of(in.matricula());
        UsuarioBiblioteca usuario = usuarioUseCase.validarExistencia(matricula);

        List<Emprestimo> emprestimosAtrasados = emprestimoService.buscarAtrasos(matricula);
        if (!emprestimosAtrasados.isEmpty()) {
            var dtos = emprestimosAtrasados.stream().map(e -> new DTOListarEmprestimo(

                    e.getId(),
                    livroRepositoryImpl.findTituloByExemplarId(e.getExemplarLivroId()),
                    in.codigoExemplar(),
                    e.getDataEmprestimo(),
                    e.getDataPrevista(),
                    e.getDataFactual(),
                    e.getStatusEmprestimo(),
                    usuario.getNome().getValue()

            )).collect(Collectors.toList());

            return new DTOResultadoEmprestimo(false, dtos);
        }

        emprestimoService.realizarEmprestimo(matricula, in.codigoExemplar(), in.dataPrevista());

        return new DTOResultadoEmprestimo(true, List.of());
    }

    @Transactional
    public void devolverEmprestimo(DTOEmprestimo in) {

        Matricula matricula = Matricula.of(in.matricula());

        boolean usuarioExists = switch (in.tipoUsuario()) {
            case ALUNO -> alunoRepository.existsByMatricula(matricula);
            case PROFESSOR -> profRepo.existsByMatricula(matricula);
        };

        if (!usuarioExists) throw new IllegalArgumentException("Usuário inválido");

        emprestimoService.finalizarEmprestimo(matricula, in.codigoExemplar());
    }

    /* Puxei de forma singular cada dado necessário, a fim de seguir DDD de forma mais pura, obviamente poderia fazer uma projection
    puxando todos os dados de uma vez só.
     */
    public DTOListagemCompleta listarEmprestimoPorId(Long emprestimoId) {

        Emprestimo emprestimo = emprestimoRepositoryImpl.listarEmprestimoPorId(emprestimoId);

        String nomeUsuario = usuarioUseCase.buscarNome(emprestimo.getMatricula());

        Livro livro = livroRepositoryImpl.findByExemplarId(emprestimo.getExemplarLivroId());

        CodigoExemplar codigoExemplar = exemplarLivroRepository.findCodigoExemplarByExemplarId(emprestimo.getExemplarLivroId());

        return new DTOListagemCompleta(
                nomeUsuario,
                livro.getTitulo().getValue(),
                livro.getAutor().getValue(),
                livro.getIsbn().getValue(),
                livro.getAno().getValue(),
                livro.getEditora().getValue(),
                codigoExemplar.getValue(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevista(),
                emprestimo.getDataFactual(),
                emprestimo.getStatusEmprestimo(),
                emprestimo.getMatricula().getValue()
        );
    }

    public List<DTOListarEmprestimo> listarEmprestimoPorId(Matricula matricula) {

        List<Emprestimo> emprestimosPorUsuario = emprestimoRepositoryImpl.listarEmprestimosUsuario(matricula);

        String nomeUsuario = usuarioUseCase.buscarNome(matricula);

        return emprestimosPorUsuario.stream()
                .map(emprestimo -> {
                    Livro livro = livroRepositoryImpl.findByExemplarId(emprestimo.getExemplarLivroId());
                    CodigoExemplar codigoExemplar = exemplarLivroRepository.findCodigoExemplarByExemplarId(emprestimo.getExemplarLivroId());

                    return new DTOListarEmprestimo(
                            emprestimo.getId(),
                            livro.getTitulo().getValue(),
                            codigoExemplar.getValue(),
                            emprestimo.getDataEmprestimo(),
                            emprestimo.getDataPrevista(),
                            emprestimo.getDataFactual(),
                            emprestimo.getStatusEmprestimo(),
                            nomeUsuario
                    );
                })
                .toList();
    }

}
