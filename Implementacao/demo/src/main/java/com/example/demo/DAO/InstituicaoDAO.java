package com.example.demo.DAO;

import java.util.List;
import java.util.Optional;

import com.example.demo.Model.Instituicao;


public interface InstituicaoDAO {
    List<Instituicao> findAll();
    Optional<Instituicao> findById(Long id);
    Instituicao save(Instituicao instituicao);
    void deleteById(Long id);
    long count();
}