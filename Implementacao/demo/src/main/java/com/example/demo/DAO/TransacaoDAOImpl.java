package com.example.demo.dao;

import com.example.demo.model.Aluno;
import com.example.demo.model.Professor;
import com.example.demo.model.Transacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional

import java.util.List;
import java.util.Optional;

@Repository // Marca esta classe como um componente de repositório Spring
public class TransacaoDAOImpl implements TransacaoDAO {

   @PersistenceContext // Injeta o EntityManager gerenciado pelo Spring/JPA
   private EntityManager entityManager;

   @Override
   @Transactional // Garante que a operação de persistência ocorra dentro de uma transação
   public Transacao save(Transacao transacao) {
      if (transacao.getId() == null) {
         // Se o ID é nulo, é uma nova transação, então persistimos
         entityManager.persist(transacao);
      } else {
         // Se o ID existe, a transação já existe e precisa ser mesclada para atualização
         transacao = entityManager.merge(transacao);
      }
      return transacao;
   }

   @Override
   public Optional<Transacao> findById(Long id) {
      // Busca uma transação pelo ID; retorna Optional para lidar com a ausência
      return Optional.ofNullable(entityManager.find(Transacao.class, id));
   }

   @Override
   public List<Transacao> findAll() {
      // Retorna todas as transações, ordenadas pela data mais recente
      return entityManager.createQuery("SELECT t FROM Transacao t ORDER BY t.dataTransacao DESC", Transacao.class)
            .getResultList();
   }

   @Override
   public List<Transacao> findByProfessorOrigemOrderByDataTransacaoDesc(Professor professorOrigem) {
      // Busca transações filtradas pelo professor de origem e ordenadas pela data
      return entityManager
            .createQuery("SELECT t FROM Transacao t WHERE t.professorOrigem = :professor ORDER BY t.dataTransacao DESC",
                  Transacao.class)
            .setParameter("professor", professorOrigem) // Define o parâmetro professor
            .getResultList();
   }

   @Override
   public List<Transacao> findByAlunoDestinoOrderByDataTransacaoDesc(Aluno alunoDestino) {
      // Busca transações filtradas pelo aluno de destino e ordenadas pela data
      return entityManager
            .createQuery("SELECT t FROM Transacao t WHERE t.alunoDestino = :aluno ORDER BY t.dataTransacao DESC",
                  Transacao.class)
            .setParameter("aluno", alunoDestino) // Define o parâmetro aluno
            .getResultList();
   }

   @Override
   @Transactional // Garante que a operação de remoção ocorra dentro de uma transação
   public void delete(Transacao transacao) {
      // Remove a transação. O 'merge' é para garantir que a entidade esteja
      // gerenciada
      entityManager.remove(entityManager.contains(transacao) ? transacao : entityManager.merge(transacao));
   }
}
