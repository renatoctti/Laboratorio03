package com.example.demo.Controller;

import com.example.demo.Model.EmpresaParceira;
import com.example.demo.Model.Vantagem;
import com.example.demo.Service.EmpresaParceiraService;
import com.example.demo.Service.VantagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/vantagens")
public class VantagemController {

   private final VantagemService vantagemService;
   private final EmpresaParceiraService empresaParceiraService;

   @Autowired
   public VantagemController(VantagemService vantagemService,
         EmpresaParceiraService empresaParceiraService) {
      this.vantagemService = vantagemService;
      this.empresaParceiraService = empresaParceiraService;
   }

   // Lista todas as vantagens
   @GetMapping
   public String listarVantagens(Model model) {
      try {
         System.out.println("DEBUG: Iniciando busca por vantagens..."); // Log
         List<Vantagem> vantagens = vantagemService.findAll();
         System.out.println("DEBUG: Vantagens encontradas: " + vantagens.size()); // Log

         model.addAttribute("vantagens", vantagens);
         return "vantagens/lista";
      } catch (Exception e) {
         System.err.println("ERRO GRAVE: " + e.getMessage()); // Log vermelho
         e.printStackTrace(); // Stacktrace completo
         model.addAttribute("erro", "Sistema temporariamente indisponível");
         return "error"; // Página de erro genérica
      }
   }

   // Formulário de cadastro
   @GetMapping("/cadastrar")
   public String mostrarFormCadastro(Model model) {
      model.addAttribute("empresas", empresaParceiraService.findAll());
      model.addAttribute("vantagem", new Vantagem());
      return "vantagens/cadastro";
   }

   // Processa o cadastro
   @PostMapping("/cadastrar")
   public String processarCadastro(
         @RequestParam("empresaId") Long empresaId,
         @RequestParam("descricao") String descricao,
         @RequestParam("custo") String custoStr, // Recebe como String para tratamento seguro
         RedirectAttributes redirectAttributes) {

      try {
         BigDecimal custoMoedas = parseCusto(custoStr);
         EmpresaParceira empresa = empresaParceiraService.findById(empresaId)
               .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

         Vantagem vantagem = new Vantagem();
         vantagem.setDescricao(descricao);
         vantagem.setCustoMoedas(custoMoedas);
         vantagem.setEmpresa(empresa);

         vantagemService.save(vantagem);
         redirectAttributes.addFlashAttribute("sucesso", "Vantagem cadastrada com sucesso!");
      } catch (Exception e) {
         redirectAttributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
      }

      return "redirect:/vantagens/cadastrar";
   }

   // Detalhes de uma vantagem@GetMapping("/{id}")
   public String detalharVantagem(@PathVariable Long id, Model model) {
      Vantagem vantagem = vantagemService.findById(id);
      if (vantagem == null) {
         throw new RuntimeException("Vantagem não encontrada");
      }
      model.addAttribute("vantagem", vantagem);
      return "vantagens/detalhes";
   }

   // Formulário de edição
   @GetMapping("/editar/{id}")
   public String mostrarFormEdicao(@PathVariable Long id, Model model) {
      Vantagem vantagem = vantagemService.findById(id);
      if (vantagem == null) {
         throw new RuntimeException("Vantagem não encontrada");
      }
      model.addAttribute("vantagem", vantagem);
      model.addAttribute("empresas", empresaParceiraService.findAll());
      return "vantagens/editar";
   }

   // Processa a edição
   @PostMapping("/editar/{id}")
   public String processarEdicao(
         @PathVariable Long id,
         @ModelAttribute Vantagem vantagem,
         RedirectAttributes redirectAttributes) {
      try {
         vantagem.setId(id);
         vantagemService.save(vantagem);
         redirectAttributes.addFlashAttribute("sucesso", "Vantagem atualizada com sucesso!");
      } catch (Exception e) {
         redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
      }
      return "redirect:/vantagens/" + id;
   }

   // Exclui uma vantagem
   @PostMapping("/excluir/{id}")
   public String excluirVantagem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
      try {
         vantagemService.deleteById(id);
         redirectAttributes.addFlashAttribute("sucesso", "Vantagem excluída com sucesso!");
      } catch (Exception e) {
         redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
      }
      return "redirect:/vantagens";
   }

   // Método auxiliar para parse seguro do valor monetário
   private BigDecimal parseCusto(String custoStr) {
      try {
         return new BigDecimal(custoStr.replace(",", "."));
      } catch (NumberFormatException e) {
         throw new RuntimeException("Valor monetário inválido. Use números com ponto ou vírgula decimal.");
      }
   }
}