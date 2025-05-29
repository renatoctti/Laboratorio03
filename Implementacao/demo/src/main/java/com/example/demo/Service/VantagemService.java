package com.example.demo.Service;

import com.example.demo.DAO.EmpresaParceiraDAO;
import com.example.demo.DAO.VantagemDAOImpl;
import com.example.demo.Model.EmpresaParceira;
import com.example.demo.Model.Vantagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class VantagemService {

   @Autowired
   private VantagemDAOImpl vantagemDAO;

   @Autowired
   private EmpresaParceiraDAO empresaDAO;

   public Vantagem save(Vantagem vantagem) {
      return vantagemDAO.save(vantagem);
   }

   public Vantagem findById(Long id) {
      return vantagemDAO.findById(id);
   }

   public List<Vantagem> findAll() {
      return vantagemDAO.findAll();
   }

   public void deleteById(Long id) {
      vantagemDAO.deleteById(id);
   }

   public Vantagem cadastrarVantagem(Long empresaId, String descricao, BigDecimal custoMoedas) {
      EmpresaParceira empresa = empresaDAO.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada com ID: " + empresaId));

      Vantagem vantagem = new Vantagem();
      vantagem.setDescricao(descricao);
      vantagem.setCustoMoedas(custoMoedas);
      vantagem.setEmpresa(empresa);

      return vantagemDAO.save(vantagem);
   }
}