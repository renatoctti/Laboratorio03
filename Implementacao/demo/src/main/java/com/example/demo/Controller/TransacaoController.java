package com.example.demo.Controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Model.Aluno;
import com.example.demo.Model.Professor;
import com.example.demo.Model.Transacao;
import com.example.demo.Service.AlunoService;
import com.example.demo.Service.ProfessorService;
import com.example.demo.Service.TransacaoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {

   @Autowired
   private ProfessorService professorService;

   @Autowired
   private AlunoService alunoService;

   @Autowired
   private TransacaoService transacaoService;

   @PostMapping("/salvar")
   public String processarTransferenciaMoedas(
         @RequestParam("professorId") Long professorId,
         @RequestParam("alunoId") Long alunoId,
         @RequestParam("quantidadeMoedas") BigDecimal quantidadeMoedas,
         @RequestParam("motivo") String motivo,
         RedirectAttributes redirectAttributes,
         Model model) {
      Professor professor = professorService.findById(professorId);
      Optional<Aluno> alunoOptional = alunoService.findById(alunoId);

      if (professor.getId() == null) {
         redirectAttributes.addFlashAttribute("erro", "Professor não encontrado.");
         return "redirect:/professores";
      }
      if (alunoOptional.isEmpty()) {
         redirectAttributes.addFlashAttribute("erro", "Aluno não encontrado.");
         return "redirect:/professores/transacao/" + professorId;
      }

      Aluno aluno = alunoOptional.get();

      if (professor.getSaldoMoedas().compareTo(quantidadeMoedas) < 0) {
         redirectAttributes.addFlashAttribute("erro", "Saldo insuficiente para a transferência.");
         return "redirect:/professores/transacao/" + professorId;
      }

      professor.setSaldoMoedas(professor.getSaldoMoedas().subtract(quantidadeMoedas));
      aluno.setSaldoMoedas(aluno.getSaldoMoedas().add(quantidadeMoedas));

      professorService.save(professor);
      alunoService.save(aluno);

      Transacao transacao = new Transacao();
      transacao.setValor(quantidadeMoedas);
      transacao.setAluno(aluno);
      transacao.setProfessor(professor);
      transacao.setMotivo(motivo);

      transacaoService.save(transacao);

      redirectAttributes.addFlashAttribute("sucesso", "Moedas transferidas com sucesso!");
      return "redirect:/professores"; 
   }

}
