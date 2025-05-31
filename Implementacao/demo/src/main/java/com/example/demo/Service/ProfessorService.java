package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.ProfessorDAO;
import com.example.demo.Model.Professor;

@Service
public class ProfessorService {

   @Autowired
   private ProfessorDAO professorDAO;


   public Professor save(Professor professor) {
      return professorDAO.save(professor);
   }

   public long count(){
      return professorDAO.count();
   }
}
