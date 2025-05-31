package com.example.demo.DAO;

import org.springframework.stereotype.Repository;

import com.example.demo.Model.Professor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProfessorDAOImpl implements ProfessorDAO {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public Professor save(Professor professor) {
      if (professor.getId() == null) {
         entityManager.persist(professor);
         return professor;
      } else {
         return entityManager.merge(professor);
      }
   }

   @Override
   @Transactional(readOnly = true)
   public long count() {
      TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(p) FROM Professor p", Long.class); 
      return query.getSingleResult();
   }
}
