package com.example.demo.DAO;


import org.springframework.stereotype.Repository;

import com.example.demo.Model.Transacao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class TransacaoDAOImpl implements TransacaoDAO{

   @PersistenceContext
   private EntityManager entityManager;
   
   @Override
   public Transacao save(Transacao transacao){
      if(transacao.getId() == null){
         entityManager.persist(transacao);
         return transacao;
      } else {
         return entityManager.merge(transacao);
      }
   }

}
