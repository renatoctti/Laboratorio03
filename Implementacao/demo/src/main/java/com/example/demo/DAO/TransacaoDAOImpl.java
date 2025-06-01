package com.example.demo.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Model.Professor;
import com.example.demo.Model.Transacao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class TransacaoDAOImpl implements TransacaoDAO {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public Transacao save(Transacao transacao) {
      if (transacao.getId() == null) {
         entityManager.persist(transacao);
         return transacao;
      } else {
         return entityManager.merge(transacao);
      }
   }

   @Override
   public List<Transacao> getTransacoesByProfessor(Professor professor) {
      String jpql = "SELECT t FROM Transacao t WHERE t.professor = :professor ORDER BY t.dataTransacao DESC";

      TypedQuery<Transacao> query = entityManager.createQuery(jpql, Transacao.class);

      query.setParameter("professor", professor);

      return query.getResultList();
   }

}
