package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Empresa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class EmpresaDAOImpl implements EmpresaDAO {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   @Transactional
   public Empresa save(Empresa empresa) {
      if (empresa.getId() == null) {
         entityManager.persist(empresa);
         return empresa;
      } else {
         return entityManager.merge(empresa);
      }
   }

   @Override
   public Optional<Empresa> findById(Long id) {
      return Optional.ofNullable(entityManager.find(Empresa.class, id));
   }

   @Override
   public Optional<Empresa> findByCnpj(String cnpj) {
      try {
         return Optional
               .ofNullable(entityManager.createQuery("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj", Empresa.class)
                     .setParameter("cnpj", cnpj)
                     .getSingleResult());
      } catch (NoResultException e) {
         return Optional.empty();
      }
   }

   @Override
   public Optional<Empresa> findByEmail(String email) {
      try {
         return Optional
               .ofNullable(entityManager.createQuery("SELECT e FROM Empresa e WHERE e.email = :email", Empresa.class)
                     .setParameter("email", email)
                     .getSingleResult());
      } catch (NoResultException e) {
         return Optional.empty();
      }
   }

   @Override
   public List<Empresa> findAll() {
      return entityManager.createQuery("SELECT e FROM Empresa e", Empresa.class).getResultList();
   }

   @Override
   @Transactional
   public void delete(Empresa empresa) {
      if (entityManager.contains(empresa)) {
         entityManager.remove(empresa);
      } else {
         entityManager.remove(entityManager.merge(empresa));
      }
   }

}
