package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Aluno;

public interface AlunoDAO {

   Aluno save(Aluno aluno);
   Optional<Aluno> findById(Long id);
   Optional<Aluno> findByCpf(String cpf);
   List<Aluno> findAll();
   void delete (Aluno aluno);
   
}
