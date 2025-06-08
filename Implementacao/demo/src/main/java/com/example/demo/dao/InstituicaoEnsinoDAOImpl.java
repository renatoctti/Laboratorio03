package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.InstituicaoEnsino;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class InstituicaoEnsinoDAOImpl implements InstituicaoEnsinoDAO {
   @PersistenceContext
   private EntityManager entityManager;

   @Override
   @Transactional
   public InstituicaoEnsino save(InstituicaoEnsino instituicao) {
      if (instituicao.getId() == null) {
         entityManager.persist(instituicao);
         return instituicao;
      } else {
         return entityManager.merge(instituicao);
      }
   }

   @Override
   public Optional<InstituicaoEnsino> findById(Long id) {
      return Optional.ofNullable(entityManager.find(InstituicaoEnsino.class, id));
   }

   @Override
   public Optional<InstituicaoEnsino> findByNome(String nome) {
      try {
         return Optional.ofNullable(entityManager
               .createQuery("SELECT i FROM InstituicaoEnsino i WHERE i.nome = :nome", InstituicaoEnsino.class)
               .setParameter("nome", nome)
               .getSingleResult());
      } catch (NoResultException e) {
         return Optional.empty();
      }
   }

   @Override
   public List<InstituicaoEnsino> findAll() {
      return entityManager.createQuery("SELECT i FROM InstituicaoEnsino i", InstituicaoEnsino.class).getResultList();
   }

   @Override
   @Transactional
   public void delete(InstituicaoEnsino instituicao) {
      if (entityManager.contains(instituicao)) {
         entityManager.remove(instituicao);
      } else {
         entityManager.remove(entityManager.merge(instituicao));
      }
   }
}
