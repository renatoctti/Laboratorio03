package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Model.Aluno;
import com.example.demo.Model.Professor;
import com.example.demo.Service.AlunoService;
import com.example.demo.Service.ProfessorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/professores")
public class ProfessorController {

   @Autowired
   private ProfessorService professorService;

   @Autowired
   private AlunoService alunoService;


   @GetMapping
   public String listar(Model model) {
      try {
         model.addAttribute("professores", professorService.findAll());
         return "professores/lista";
      } catch (Exception e){
         e.printStackTrace();
         model.addAttribute("erro", "Erro ao carregar professores");
         return "professores/lista";
      }
   }

   @GetMapping("/transacao/{id}")
   public String showTransacaoForm(@PathVariable Long id,Model model) {
      try {
         Professor professor = professorService.findById(id);
         List<Aluno> alunos = alunoService.findAll();
         model.addAttribute("alunos", alunos);
         model.addAttribute("professor", professor);
         return "professores/transacao";
      } catch (Exception e){
         e.printStackTrace();
         return "professores/transacao";
      }
   }
}
