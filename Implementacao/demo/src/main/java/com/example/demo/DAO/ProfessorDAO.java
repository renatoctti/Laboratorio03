package com.example.demo.dao;

import com.example.demo.model.Professor;

import java.util.*;

public interface ProfessorDAO {

   Professor save(Professor professor);
   Optional<Professor> findById(Long id);
   Optional<Professor> findByCpf(String cpf);
   List<Professor> findAll();
   void delete(Professor professor); 

} 