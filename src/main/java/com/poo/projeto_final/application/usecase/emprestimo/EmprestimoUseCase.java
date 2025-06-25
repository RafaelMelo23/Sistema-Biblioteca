package com.poo.projeto_final.application.usecase.emprestimo;

import com.poo.projeto_final.application.dto.DTOEmprestimo;
import com.poo.projeto_final.application.dto.DTOListagemCompleta;
import com.poo.projeto_final.application.dto.DTOListarEmprestimo;
import com.poo.projeto_final.application.dto.DTOResultadoEmprestimo;
import com.poo.projeto_final.domain.impl.domain.repository.*;
import com.poo.projeto_final.domain.impl.domain.service.EmprestimoServiceImpl;
import com.poo.projeto_final.domain.model.emprestimo.Emprestimo;
import com.poo.projeto_final.domain.model.exemplar.CodigoExemplar;
import com.poo.projeto_final.domain.model.livro.Livro;
import com.poo.projeto_final.domain.model.shared.vo.Matricula;
import com.poo.projeto_final.domain.model.usuario.UsuarioBiblioteca;
import com.poo.projeto_final.domain.service.EmprestimoService;
import com.poo.projeto_final.impl.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmprestimoUseCase {

    private final AlunoRepositoryImpl alunoRepository;
    private final ProfessorRepositoryImpl profRepo;
    private final LivroRepositoryImpl livroRepositoryImpl;
    private final UsuarioService usuarioService;
    private final EmprestimoService emprestimoService;
    private final EmprestimoServiceImpl emprestimoServiceImpl;
    private final EmprestimoRepositoryImpl emprestimoRepositoryImpl;
    private final ExemplarLivroRepositoryImpl exemplarLivroRepositoryImpl;

    @Transactional
    public DTOResultadoEmprestimo registrarEmprestimo(DTOEmprestimo in) {

        if (in.dataPrevista().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data prevista anterior ao dia atual");
        }

        Matricula matricula = Matricula.of(in.matricula());
        UsuarioBiblioteca usuario = usuarioService.validarExistencia(matricula);

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

        emprestimoServiceImpl.realizarEmprestimo(matricula, in.codigoExemplar(), in.dataPrevista());

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

        emprestimoServiceImpl.devolverEmprestimo(matricula, in.codigoExemplar());
    }

    /* Puxei de forma singular cada dado necessário, a fim de seguir DDD de forma mais pura, obviamente poderia fazer uma projection
    puxando todos os dados de uma vez só.
     */
    public DTOListagemCompleta listarEmprestimoPorId(Long emprestimoId) {

        Emprestimo emprestimo = emprestimoRepositoryImpl.listarEmprestimoPorId(emprestimoId);

        String nomeUsuario = usuarioService.buscarNome(emprestimo.getMatricula());

        Livro livro = livroRepositoryImpl.findByExemplarId(emprestimo.getExemplarLivroId());

        CodigoExemplar codigoExemplar = exemplarLivroRepositoryImpl.findCodigoExemplarByExemplarId(emprestimo.getExemplarLivroId());

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

        String nomeUsuario = usuarioService.buscarNome(matricula);

        return emprestimosPorUsuario.stream()
                .map(emprestimo -> {
                    Livro livro = livroRepositoryImpl.findByExemplarId(emprestimo.getExemplarLivroId());
                    CodigoExemplar codigoExemplar = exemplarLivroRepositoryImpl.findCodigoExemplarByExemplarId(emprestimo.getExemplarLivroId());

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
