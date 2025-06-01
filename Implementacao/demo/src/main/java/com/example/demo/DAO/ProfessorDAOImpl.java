package com.example.demo.DAO;

import java.util.List;

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

   @Override
   @Transactional(readOnly = true)
   public List<Professor> findAll() {
      TypedQuery<Professor> query = entityManager.createQuery(
         "SELECT p FROM Professor p LEFT JOIN FETCH p.instituicao ORDER BY p.nome", Professor.class
      );
      return query.getResultList();
   }

   @Override
   @Transactional(readOnly = true)
   public Professor findById (Long id){
      try {
         Professor professor = entityManager.find(Professor.class, id);
         System.out.println("Professor encontrado: " + (professor != null ? professor.getNome() : null));
         return professor;
      } catch (Exception e){
         e.printStackTrace();
         return null;
      }
   }
}
