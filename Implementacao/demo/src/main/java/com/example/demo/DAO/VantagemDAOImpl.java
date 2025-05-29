package com.example.demo.DAO;

import com.example.demo.Model.Vantagem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Collections;

@Repository
public class VantagemDAOImpl implements VantagemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Vantagem findById(Long id) {
        return entityManager.find(Vantagem.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vantagem> findAll() {
        try {
            return entityManager.createQuery(
                "SELECT DISTINCT v FROM Vantagem v LEFT JOIN FETCH v.empresa", 
                Vantagem.class)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public Vantagem save(Vantagem vantagem) {
        if (vantagem.getId() == null) {
            entityManager.persist(vantagem);
            return vantagem;
        } else {
            return entityManager.merge(vantagem);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Vantagem vantagem = findById(id);
        if (vantagem != null) {
            entityManager.remove(vantagem);
        }
    }
}