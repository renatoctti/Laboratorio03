package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Professor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class ProfessorDAOImpl implements ProfessorDAO {
   @PersistenceContext
   private EntityManager entityManager;

   @Override
   @Transactional
   public Professor save(Professor professor) {
      if (professor.getId() == null) {
         entityManager.persist(professor);
         return professor;
      } else {
         return entityManager.merge(professor);
      }
   }

   @Override
   public Optional<Professor> findById(Long id) {
      return Optional.ofNullable(entityManager.find(Professor.class, id));
   }

   @Override
   public Optional<Professor> findByCpf(String cpf) {
      try {
         return Optional
               .ofNullable(entityManager.createQuery("SELECT p FROM Professor p WHERE p.cpf = :cpf", Professor.class)
                     .setParameter("cpf", cpf)
                     .getSingleResult());
      } catch (NoResultException e) {
         return Optional.empty();
      }
   }

   @Override
   public List<Professor> findAll() {
      return entityManager.createQuery("SELECT p FROM Professor p", Professor.class).getResultList();
   }

   @Override
   @Transactional
   public void delete(Professor professor) {
      if (entityManager.contains(professor)) {
         entityManager.remove(professor);
      } else {
         entityManager.remove(entityManager.merge(professor));
      }
   }
}
