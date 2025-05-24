package com.example.demo.DAO;

import java.util.List;
import java.util.Optional;

import com.example.demo.Model.Aluno;


public interface AlunoDAO {
    List<Aluno> findAll();
    Optional<Aluno> findById(Long id);
    Optional<Aluno> findByEmail(String email);
    Optional<Aluno> findByCpf(String cpf);
    Aluno save(Aluno aluno);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    long count();
}