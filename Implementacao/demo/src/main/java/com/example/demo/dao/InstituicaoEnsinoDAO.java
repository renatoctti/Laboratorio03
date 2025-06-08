package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.InstituicaoEnsino;

public interface InstituicaoEnsinoDAO {

   InstituicaoEnsino save(InstituicaoEnsino instituicao);
   Optional<InstituicaoEnsino> findById(Long id);
   Optional<InstituicaoEnsino> findByNome(String nome);
   List<InstituicaoEnsino> findAll();
   void delete(InstituicaoEnsino instituicao);
   
}
