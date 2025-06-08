package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.InstituicaoEnsinoDAO;
import com.example.demo.model.InstituicaoEnsino;
import jakarta.transaction.Transactional;

@Service
public class InstituicaoEnsinoService {

   @Autowired
   private InstituicaoEnsinoDAO instituicaoEnsinoDAO;

   public List<InstituicaoEnsino> findAll() {
      return instituicaoEnsinoDAO.findAll();
   }

   public Optional<InstituicaoEnsino> findById(Long id) {
      return instituicaoEnsinoDAO.findById(id);
   }

   @Transactional
   public InstituicaoEnsino save(InstituicaoEnsino instituicao) {
      return instituicaoEnsinoDAO.save(instituicao);
   }

   public Optional<InstituicaoEnsino> findByNome(String nome) {
      return instituicaoEnsinoDAO.findByNome(nome);
   }
}
