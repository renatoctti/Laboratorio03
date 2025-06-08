package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO {
   @PersistenceContext
   private EntityManager entityManager;

   @Override
   @Transactional
   public Usuario save(Usuario usuario) {
      if (usuario.getId() == null) {
         entityManager.persist(usuario);
         return usuario;
      } else {
         return entityManager.merge(usuario);
      }
   }

   @Override
   public Optional<Usuario> findById(Long id) {
      return Optional.ofNullable(entityManager.find(Usuario.class, id));
   }

   @Override
   public Optional<Usuario> findByEmail(String email) {
      try {
         return Optional
               .ofNullable(entityManager.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                     .setParameter("email", email)
                     .getSingleResult());
      } catch (NoResultException e) {
         return Optional.empty();
      }
   }

   @Override
   public List<Usuario> findAll() {
      return entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
   }

   @Override
   @Transactional
   public void delete(Usuario usuario) {
      if (entityManager.contains(usuario)) {
         entityManager.remove(usuario);
      } else {
         entityManager.remove(entityManager.merge(usuario));
      }
   }
}
