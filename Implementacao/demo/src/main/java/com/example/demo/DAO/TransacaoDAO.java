package com.example.demo.dao;

import com.example.demo.model.Aluno;
import com.example.demo.model.Professor;
import com.example.demo.model.Transacao;

import java.util.List;
import java.util.Optional;

public interface TransacaoDAO {

   Transacao save(Transacao transacao);

   Optional<Transacao> findById(Long id);

   List<Transacao> findAll();

   List<Transacao> findByProfessorOrigemOrderByDataTransacaoDesc(Professor professorOrigem);

   List<Transacao> findByAlunoDestinoOrderByDataTransacaoDesc(Aluno alunoDestino);

   void delete(Transacao transacao);
}
