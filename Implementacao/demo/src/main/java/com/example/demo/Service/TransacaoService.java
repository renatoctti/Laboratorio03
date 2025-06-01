package com.example.demo.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.TransacaoDAO;
import com.example.demo.Model.Professor;
import com.example.demo.Model.Transacao;

@Service
public class TransacaoService {

   @Autowired
   TransacaoDAO transacaoDAO;

   @Autowired
   ProfessorService professorService;

   public Transacao save(Transacao transacao) {
      return transacaoDAO.save(transacao);
   }

   public List<Transacao> getTodasTransacoesDoProfessor(Long professorId) {

      Professor professor = professorService.findById(professorId);

      if (professor != null) {
         return transacaoDAO.getTransacoesByProfessor(professor);
      }
      return Collections.emptyList();
   }
}
