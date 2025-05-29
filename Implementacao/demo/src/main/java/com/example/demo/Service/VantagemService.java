package com.example.demo.Service;

import com.example.demo.DAO.EmpresaParceiraDAO;
import com.example.demo.DAO.VantagemDAO;
import com.example.demo.Model.EmpresaParceira;
import com.example.demo.Model.Vantagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Service
public class VantagemService {

    @Autowired
    private VantagemDAO vantagemDAO;

    @Autowired
    private EmpresaParceiraDAO empresaDAO;

    @Transactional
    public Vantagem save(Vantagem vantagem) {
        try {
            System.out.println("DEBUG Service: Salvando vantagem: " + vantagem.getNome());
            
            // Validações básicas
            if (vantagem.getNome() == null || vantagem.getNome().trim().isEmpty()) {
                throw new RuntimeException("Nome da vantagem é obrigatório");
            }
            if (vantagem.getDescricao() == null || vantagem.getDescricao().trim().isEmpty()) {
                throw new RuntimeException("Descrição da vantagem é obrigatória");
            }
            if (vantagem.getCustoMoedas() == null || vantagem.getCustoMoedas().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Custo deve ser maior que zero");
            }
            
            return vantagemDAO.save(vantagem);
        } catch (Exception e) {
            System.err.println("ERRO Service save: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar vantagem: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Vantagem findById(Long id) {
        try {
            System.out.println("DEBUG Service: Buscando vantagem por ID: " + id);
            return vantagemDAO.findById(id);
        } catch (Exception e) {
            System.err.println("ERRO Service findById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Vantagem> findAll() {
        try {
            System.out.println("DEBUG Service: Buscando todas as vantagens...");
            List<Vantagem> vantagens = vantagemDAO.findAll();
        
            // Garantir que nunca retorne null
            if (vantagens == null) {
                System.out.println("DEBUG Service: DAO retornou null, criando lista vazia");
                vantagens = new ArrayList<>();
            }
        
            System.out.println("DEBUG Service: Retornando " + vantagens.size() + " vantagens");
            return vantagens;
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO Service findAll: " + e.getMessage());
            e.printStackTrace();
            // Em caso de erro, retornar lista vazia ao invés de lançar exceção
            return new ArrayList<>();
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            System.out.println("DEBUG Service: Deletando vantagem ID: " + id);
            vantagemDAO.deleteById(id);
        } catch (Exception e) {
            System.err.println("ERRO Service deleteById: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar vantagem: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Vantagem cadastrarVantagem(String nome, String descricao, BigDecimal custoMoedas, Long empresaId) {
        try {
            System.out.println("DEBUG Service: Cadastrando nova vantagem: " + nome);
            
            EmpresaParceira empresa = null;
            if (empresaId != null) {
                empresa = empresaDAO.findById(empresaId)
                    .orElseThrow(() -> new RuntimeException("Empresa não encontrada com ID: " + empresaId));
            }

            Vantagem vantagem = new Vantagem();
            vantagem.setNome(nome);
            vantagem.setDescricao(descricao);
            vantagem.setCustoMoedas(custoMoedas);
            vantagem.setEmpresa(empresa);

            return save(vantagem);
        } catch (Exception e) {
            System.err.println("ERRO Service cadastrarVantagem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar vantagem: " + e.getMessage(), e);
        }
    }
}
