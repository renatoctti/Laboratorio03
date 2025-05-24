package com.example.demo.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Model.EmpresaParceira;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class EmpresaParceiraDAOImpl implements EmpresaParceiraDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaParceira> findAll() {
        TypedQuery<EmpresaParceira> query = entityManager.createQuery(
            "SELECT e FROM EmpresaParceira e ORDER BY e.nome", EmpresaParceira.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpresaParceira> findById(Long id) {
        try {
            TypedQuery<EmpresaParceira> query = entityManager.createQuery(
                "SELECT e FROM EmpresaParceira e LEFT JOIN FETCH e.vantagens WHERE e.id = :id", EmpresaParceira.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpresaParceira> findByEmail(String email) {
        try {
            TypedQuery<EmpresaParceira> query = entityManager.createQuery(
                "SELECT e FROM EmpresaParceira e WHERE e.email = :email", EmpresaParceira.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpresaParceira> findByCnpj(String cnpj) {
        try {
            TypedQuery<EmpresaParceira> query = entityManager.createQuery(
                "SELECT e FROM EmpresaParceira e WHERE e.cnpj = :cnpj", EmpresaParceira.class);
            query.setParameter("cnpj", cnpj);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public EmpresaParceira save(EmpresaParceira empresa) {
        if (empresa.getId() == null) {
            entityManager.persist(empresa);
            return empresa;
        } else {
            return entityManager.merge(empresa);
        }
    }

    @Override
    public void deleteById(Long id) {
        EmpresaParceira empresa = entityManager.find(EmpresaParceira.class, id);
        if (empresa != null) {
            entityManager.remove(empresa);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM EmpresaParceira e WHERE e.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCnpj(String cnpj) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM EmpresaParceira e WHERE e.cnpj = :cnpj", Long.class);
        query.setParameter("cnpj", cnpj);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM EmpresaParceira e", Long.class);
        return query.getSingleResult();
    }
}