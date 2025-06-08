package com.example.demo.dao;

import com.example.demo.model.Aluno;
import com.example.demo.model.Empresa;
import com.example.demo.model.Vantagem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional

import java.util.List;
import java.util.Optional;

@Repository // Marca esta classe como um componente de repositório Spring
public class VantagemDAOImpl implements VantagemDAO {

    @PersistenceContext // Injeta o EntityManager gerenciado pelo Spring/JPA
    private EntityManager entityManager;

    @Override
    @Transactional // Garante que a operação de persistência ocorra dentro de uma transação
    public Vantagem save(Vantagem vantagem) {
        if (vantagem.getId() == null) {
            // Se o ID é nulo, é uma nova vantagem, então persistimos
            entityManager.persist(vantagem);
        } else {
            // Se o ID existe, a vantagem já existe e precisa ser mesclada para atualização
            vantagem = entityManager.merge(vantagem);
        }
        return vantagem;
    }

    @Override
    public Optional<Vantagem> findById(Long id) {
        // Busca uma vantagem pelo ID; retorna Optional para lidar com a ausência
        return Optional.ofNullable(entityManager.find(Vantagem.class, id));
    }

    @Override
    public List<Vantagem> findAll() {
        // Retorna todas as vantagens
        return entityManager.createQuery("SELECT v FROM Vantagem v", Vantagem.class)
                .getResultList();
    }

    @Override
    public List<Vantagem> findByEmpresaParceira(Empresa empresaParceira) {
        // Busca vantagens filtradas pela empresa parceira
        return entityManager.createQuery("SELECT v FROM Vantagem v WHERE v.empresaParceira = :empresa", Vantagem.class)
                .setParameter("empresa", empresaParceira)
                .getResultList();
    }

    @Override
    public List<Vantagem> findByVendidaFalse() {
        // Busca vantagens onde 'vendida' é falso (não vendidas)
        return entityManager.createQuery("SELECT v FROM Vantagem v WHERE v.vendida = false", Vantagem.class)
                .getResultList();
    }

    public List<Vantagem> findByVendidaFalseAndAlunoCompradorIsNull() {
        // NOVO: Busca vantagens onde 'vendida' é falso E 'alunoComprador' é nulo
        // Isso garante que apenas vantagens realmente disponíveis sejam listadas
        return entityManager.createQuery(
                "SELECT v FROM Vantagem v WHERE v.vendida = false AND v.alunoComprador IS NULL", Vantagem.class)
                .getResultList();
    }

    @Override
    public List<Vantagem> findByVendidaTrueAndAlunoCompradorOrderByNomeAsc(Aluno alunoComprador) {
        // NOVO: Busca vantagens que foram vendidas, pertencem a um aluno específico e
        // ordena por nome
        return entityManager.createQuery(
                "SELECT v FROM Vantagem v WHERE v.vendida = true AND v.alunoComprador = :aluno ORDER BY v.nome ASC",
                Vantagem.class)
                .setParameter("aluno", alunoComprador)
                .getResultList();
    }

    @Override
    public List<Vantagem> findByEmpresaParceiraAndVendidaTrue(Empresa empresaParceira) {
        return entityManager.createQuery(
                "SELECT v FROM Vantagem v WHERE v.empresaParceira = :empresa AND v.vendida = true", Vantagem.class)
                .setParameter("empresa", empresaParceira)
                .getResultList();
    }

    public List<Vantagem> findByEmpresaParceiraAndVendidaFalse(Empresa empresaParceira) {
        return entityManager.createQuery(
                "SELECT v FROM Vantagem v WHERE v.empresaParceira = :empresa AND v.vendida = false", Vantagem.class)
                .setParameter("empresa", empresaParceira)
                .getResultList();
    }

    @Override
    @Transactional // Garante que a operação de remoção ocorra dentro de uma transação
    public void delete(Vantagem vantagem) {
        // Remove a vantagem. O 'merge' é para garantir que a entidade esteja gerenciada
        entityManager.remove(entityManager.contains(vantagem) ? vantagem : entityManager.merge(vantagem));
    }

}
