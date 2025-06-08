package com.example.demo.dao;

import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Aluno;

@Repository
public class AlunoDAOImpl implements AlunoDAO{
   
   @PersistenceContext
   private EntityManager entityManager;

   @Override
   @Transactional
   public Aluno save(Aluno aluno){
      if (aluno.getId() == null){
         entityManager.persist(aluno);
         return aluno;
      } else {
         return entityManager.merge(aluno);
      }
   }

   @Override
   public Optional<Aluno> findById(Long id){
      return Optional.ofNullable(entityManager.find(Aluno.class, id));
   }

   @Override
   public Optional<Aluno> findByCpf(String cpf){
      try{
         return Optional.ofNullable(entityManager.createQuery("SELECT a FROM Aluno a WHERE a.cpf = :cpf", Aluno.class)
         .setParameter("cpf", cpf)
         .getSingleResult());
      } catch (NoResultException e){
         return Optional.empty();
      }
   }

   @Override
   public List<Aluno> findAll (){
      return entityManager.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultList();
   }

   @Override
   @Transactional
   public void delete(Aluno aluno){
      if (entityManager.contains(aluno)){
         entityManager.remove(aluno);
      } else {
         entityManager.remove(entityManager.merge(aluno));
      }
   }

}
