package com.example.demo.DAO;

import com.example.demo.Model.Vantagem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Repository
public class VantagemDAOImpl implements VantagemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Vantagem findById(Long id) { // Se VantagemDAO retornar Vantagem, mantenha assim
        try {
            System.out.println("DEBUG DAO: Buscando vantagem por ID: " + id);
            Vantagem vantagem = entityManager.find(Vantagem.class, id);
            System.out.println("DEBUG DAO: Vantagem encontrada: " + (vantagem != null ? vantagem.getNome() : "null"));
            return vantagem;
        } catch (Exception e) {
            System.err.println("ERRO DAO findById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vantagem> findAll() {
        try {
            System.out.println("DEBUG DAO: Iniciando busca de todas as vantagens...");
            
            String jpql = "SELECT v FROM Vantagem v LEFT JOIN FETCH v.empresa ORDER BY v.id DESC";
            TypedQuery<Vantagem> query = entityManager.createQuery(jpql, Vantagem.class);
            
            List<Vantagem> result = query.getResultList();
            
            if (result == null) {
                result = new ArrayList<>();
            }
            
            System.out.println("DEBUG DAO: Encontradas " + result.size() + " vantagens");
            
            for (Vantagem v : result) {
                System.out.println("DEBUG DAO: Vantagem - ID: " + v.getId() + 
                                 ", Nome: " + v.getNome() + 
                                 ", Descrição: " + v.getDescricao() + 
                                 ", Custo: " + v.getCustoMoedas() +
                                 ", URL Imagem: " + v.getUrlImagem() + // Log do novo atributo
                                 ", Empresa: " + (v.getEmpresa() != null ? v.getEmpresa().getNome() : "null"));
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO DAO findAll: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Vantagem save(Vantagem vantagem) {
        try {
            System.out.println("DEBUG DAO: Salvando vantagem: " + vantagem.getNome());
            
            if (vantagem.getId() == null) {
                entityManager.persist(vantagem);
                entityManager.flush();
                System.out.println("DEBUG DAO: Vantagem persistida com ID: " + vantagem.getId());
            } else {
                vantagem = entityManager.merge(vantagem);
                entityManager.flush();
                System.out.println("DEBUG DAO: Vantagem atualizada com ID: " + vantagem.getId());
            }
            
            return vantagem;
        } catch (Exception e) {
            System.err.println("ERRO DAO save: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar vantagem: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            System.out.println("DEBUG DAO: Deletando vantagem ID: " + id);
            Vantagem vantagem = findById(id);
            if (vantagem != null) {
                entityManager.remove(vantagem);
                entityManager.flush();
                System.out.println("DEBUG DAO: Vantagem removida com sucesso");
            } else {
                System.out.println("DEBUG DAO: Vantagem não encontrada para remoção");
            }
        } catch (Exception e) {
            System.err.println("ERRO DAO deleteById: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar vantagem: " + e.getMessage(), e);
        }
    }
}