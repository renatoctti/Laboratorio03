package com.example.demo.DAO;

import java.util.List;

import com.example.demo.Model.Professor;

public interface ProfessorDAO {

   List<Professor> findAll();
   Professor save(Professor professor);
   long count();
   Professor findById(Long id);

} 