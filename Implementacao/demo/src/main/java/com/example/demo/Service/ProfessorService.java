package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.ProfessorDAO;
import com.example.demo.Model.Professor;

import jakarta.transaction.Transactional;

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

   public List<Professor> findAll(){
      return professorDAO.findAll();
   }

   @Transactional
   public Professor findById(Long id) {
      try {
         return professorDAO.findById(id);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}
