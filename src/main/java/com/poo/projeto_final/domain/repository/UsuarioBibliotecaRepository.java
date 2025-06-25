package com.poo.projeto_final.domain.repository;


public interface UsuarioBibliotecaRepository {

    boolean existsByEmailOrCpf(String email, String cpf);
}
