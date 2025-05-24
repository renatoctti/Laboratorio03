package com.example.demo.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Model.Aluno;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AlunoDAOImpl implements AlunoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Aluno> findAll() {
        TypedQuery<Aluno> query = entityManager.createQuery(
            "SELECT a FROM Aluno a LEFT JOIN FETCH a.instituicao ORDER BY a.nome", Aluno.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aluno> findById(Long id) {
        try {
            TypedQuery<Aluno> query = entityManager.createQuery(
                "SELECT a FROM Aluno a LEFT JOIN FETCH a.instituicao WHERE a.id = :id", Aluno.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aluno> findByEmail(String email) {
        try {
            TypedQuery<Aluno> query = entityManager.createQuery(
                "SELECT a FROM Aluno a LEFT JOIN FETCH a.instituicao WHERE a.email = :email", Aluno.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aluno> findByCpf(String cpf) {
        try {
            TypedQuery<Aluno> query = entityManager.createQuery(
                "SELECT a FROM Aluno a LEFT JOIN FETCH a.instituicao WHERE a.cpf = :cpf", Aluno.class);
            query.setParameter("cpf", cpf);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Aluno save(Aluno aluno) {
        if (aluno.getId() == null) {
            entityManager.persist(aluno);
            return aluno;
        } else {
            return entityManager.merge(aluno);
        }
    }

    @Override
    public void deleteById(Long id) {
        Aluno aluno = entityManager.find(Aluno.class, id);
        if (aluno != null) {
            entityManager.remove(aluno);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Aluno a WHERE a.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCpf(String cpf) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Aluno a WHERE a.cpf = :cpf", Long.class);
        query.setParameter("cpf", cpf);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Aluno a", Long.class);
        return query.getSingleResult();
    }
}