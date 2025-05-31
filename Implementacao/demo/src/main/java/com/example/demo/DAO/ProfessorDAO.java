package com.example.demo.DAO;

import com.example.demo.Model.Professor;

public interface ProfessorDAO {

   Professor save(Professor professor);
   long count();

} 