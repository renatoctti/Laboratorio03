package com.example.demo.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Model.Instituicao;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class InstituicaoDAOImpl implements InstituicaoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Instituicao> findAll() {
        TypedQuery<Instituicao> query = entityManager.createQuery(
            "SELECT i FROM Instituicao i ORDER BY i.nome", Instituicao.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Instituicao> findById(Long id) {
        try {
            Instituicao instituicao = entityManager.find(Instituicao.class, id);
            return Optional.ofNullable(instituicao);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Instituicao save(Instituicao instituicao) {
        if (instituicao.getId() == null) {
            entityManager.persist(instituicao);
            return instituicao;
        } else {
            return entityManager.merge(instituicao);
        }
    }

    @Override
    public void deleteById(Long id) {
        Instituicao instituicao = entityManager.find(Instituicao.class, id);
        if (instituicao != null) {
            entityManager.remove(instituicao);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(i) FROM Instituicao i", Long.class);
        return query.getSingleResult();
    }
}
