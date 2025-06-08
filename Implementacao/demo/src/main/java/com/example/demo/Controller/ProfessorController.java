// src/main/java/com/example/demo/controller/ProfessorController.java
package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.TransferenciaMoedasDTO;
import com.example.demo.model.Aluno;
import com.example.demo.model.Professor;
import com.example.demo.model.Transacao; // Importar a classe Transacao
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/home/professor")
public class ProfessorController {

  @Autowired
  private UsuarioService usuarioService;

  @GetMapping
  public String homeProfessor(HttpSession session, Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("PROFESSOR")) {
      return "redirect:/login";
    }

    Optional<Professor> professorOpt = usuarioService.findProfessorById(usuarioLogado.getId());
    if (!professorOpt.isPresent()) {
      session.invalidate();
      return "redirect:/login?error=Professor+nao+encontrado";
    }
    Professor professor = professorOpt.get();
    session.setAttribute("usuarioLogado", professor);

    model.addAttribute("professor", professor);

    return "home_professor";
  }

  @GetMapping("/transferir-moedas")
  public String mostrarFormTransferenciaMoedas(HttpSession session, Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("PROFESSOR")) {
      return "redirect:/login";
    }

    Optional<Professor> professorOpt = usuarioService.findProfessorById(usuarioLogado.getId());
    if (!professorOpt.isPresent()) {
      session.invalidate();
      return "redirect:/login?error=Usuario+nao+encontrado";
    }
    Professor professor = professorOpt.get();
    session.setAttribute("usuarioLogado", professor);

    model.addAttribute("professor", professor);

    List<Aluno> alunos = usuarioService.findAllAlunos();
    model.addAttribute("alunos", alunos);

    model.addAttribute("transferenciaMoedasDTO", new TransferenciaMoedasDTO());

    return "professor/transferir_moedas";
  }

  @PostMapping("/transferir-moedas")
  public String transferirMoedas(
      @Valid TransferenciaMoedasDTO transferenciaMoedasDTO,
      BindingResult result,
      HttpSession session,
      RedirectAttributes redirectAttributes,
      Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioLogado == null || !usuarioLogado.getRole().equals("PROFESSOR")) {
      redirectAttributes.addFlashAttribute("error", "Acesso negado.");
      return "redirect:/login";
    }

    Optional<Professor> professorOpt = usuarioService.findProfessorById(usuarioLogado.getId());
    if (!professorOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("error", "Professor não encontrado.");
      session.invalidate();
      return "redirect:/login";
    }
    Professor professor = professorOpt.get();

    if (result.hasErrors()) {
      model.addAttribute("professor", professor);
      model.addAttribute("alunos", usuarioService.findAllAlunos());
      model.addAttribute("error", result.getFieldError() != null ? result.getFieldError().getDefaultMessage()
          : "Erro de validação nos campos.");
      // Mantém o DTO no modelo para que os valores preenchidos sejam mantidos no
      // formulário
      model.addAttribute("transferenciaMoedasDTO", transferenciaMoedasDTO);
      return "professor/transferir_moedas";
    }

    int quantidade = transferenciaMoedasDTO.getQuantidade();
    Long alunoId = transferenciaMoedasDTO.getAlunoId();
    String motivo = transferenciaMoedasDTO.getMotivo(); // NOVIDADE AQUI: Obtém o motivo

    if (professor.getMoedas() < quantidade) {
      redirectAttributes.addFlashAttribute("error", "Saldo insuficiente de moedas.");
      return "redirect:/home/professor/transferir-moedas";
    }
    if (quantidade <= 0) {
      redirectAttributes.addFlashAttribute("error", "A quantidade de moedas deve ser positiva.");
      return "redirect:/home/professor/transferir-moedas";
    }

    Optional<Aluno> alunoOpt = usuarioService.findAlunoById(alunoId);
    if (!alunoOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("error", "Aluno não encontrado.");
      return "redirect:/home/professor/transferir-moedas";
    }
    Aluno aluno = alunoOpt.get();

    try {
      usuarioService.transferirMoedas(professor, aluno, quantidade, motivo); // Passa o motivo
      redirectAttributes.addFlashAttribute("success",
          quantidade + " moedas transferidas com sucesso para " + aluno.getNome() + "!");

      session.setAttribute("usuarioLogado", usuarioService.findProfessorById(professor.getId()).get());

    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("error", "Erro ao transferir moedas: " + e.getMessage());
    }

    return "redirect:/home/professor/transferir-moedas";
  }

  // NOVO MÉTODO: Exibir o extrato de transações do professor
  @GetMapping("/extrato")
  public String exibirExtratoProfessor(HttpSession session, Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("PROFESSOR")) {
      return "redirect:/login";
    }

    Optional<Professor> professorOpt = usuarioService.findProfessorById(usuarioLogado.getId());
    if (!professorOpt.isPresent()) {
      session.invalidate();
      return "redirect:/login?error=Professor+nao+encontrado";
    }
    Professor professor = professorOpt.get();
    session.setAttribute("usuarioLogado", professor); // Atualiza o objeto na sessão

    model.addAttribute("professor", professor); // Professor completo com saldo

    // Obtém o histórico de transações deste professor
    List<Transacao> extrato = usuarioService.getExtratoProfessor(professor);
    model.addAttribute("extrato", extrato); // Adiciona o extrato ao modelo

    return "professor/extrato"; // Nome do novo arquivo HTML para o extrato
  }
}