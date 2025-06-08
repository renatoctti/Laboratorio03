package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Usuario;
import com.example.demo.model.Aluno;
import com.example.demo.model.Vantagem;
import com.example.demo.model.Transacao;
import com.example.demo.service.VantagemService;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home/aluno")
public class AlunoController {

  @Autowired
  VantagemService vantagemService;

  @Autowired
  UsuarioService usuarioService;

  @GetMapping
  public String homeAluno(HttpSession session, Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("ALUNO")) {
      return "redirect:/login";
    }

    Optional<Aluno> alunoOpt = usuarioService.findAlunoById(usuarioLogado.getId());
    if (!alunoOpt.isPresent()) {
      session.invalidate();
      return "redirect:/login?error=Aluno+nao+encontrado";
    }
    Aluno aluno = alunoOpt.get();

    model.addAttribute("aluno", aluno);
    session.setAttribute("usuarioLogado", aluno);

    // O listarTodasVantagens agora filtra as não vendidas E sem comprador
    List<Vantagem> todasVantagens = vantagemService.listarTodasVantagens();
    model.addAttribute("todasVantagens", todasVantagens);

    return "home_aluno";
  }

  @GetMapping("/extrato")
  public String exibirExtratoAluno(HttpSession session, Model model) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("ALUNO")) {
      return "redirect:/login";
    }

    Optional<Aluno> alunoOpt = usuarioService.findAlunoById(usuarioLogado.getId());
    if (!alunoOpt.isPresent()) {
      session.invalidate();
      return "redirect:/login?error=Aluno+nao+encontrado";
    }
    Aluno aluno = alunoOpt.get();
    session.setAttribute("usuarioLogado", aluno);

    model.addAttribute("aluno", aluno);

    List<Transacao> extrato = usuarioService.getExtratoAluno(aluno);
    model.addAttribute("extrato", extrato);

    return "aluno/extrato";
  }

  @GetMapping("/confirmar-compra/{id}")
  public String mostrarConfirmacaoCompra(
      @PathVariable("id") Long vantagemId,
      HttpSession session,
      Model model,
      RedirectAttributes redirectAttributes) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("ALUNO")) {
      return "redirect:/login";
    }

    Optional<Aluno> alunoOpt = usuarioService.findAlunoById(usuarioLogado.getId());
    if (!alunoOpt.isPresent()) {
      session.invalidate();
      redirectAttributes.addFlashAttribute("error", "Aluno não encontrado.");
      return "redirect:/login";
    }
    Aluno aluno = alunoOpt.get();
    session.setAttribute("usuarioLogado", aluno);

    Optional<Vantagem> vantagemOpt = vantagemService.buscarVantagemPorId(vantagemId);
    if (!vantagemOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("error", "Vantagem não encontrada.");
      return "redirect:/home/aluno";
    }
    Vantagem vantagem = vantagemOpt.get();

    if (vantagem.isVendida()) {
      redirectAttributes.addFlashAttribute("error", "Esta vantagem já foi comprada e não está mais disponível.");
      return "redirect:/home/aluno";
    }
    if (aluno.getMoedas() < vantagem.getCustoEmMoedas()) {
      redirectAttributes.addFlashAttribute("error", "Moedas insuficientes para comprar esta vantagem.");
      return "redirect:/home/aluno";
    }

    model.addAttribute("aluno", aluno);
    model.addAttribute("vantagem", vantagem);

    return "aluno/confirmar_compra";
  }

  @PostMapping("/comprar-vantagem")
  public String comprarVantagem(
      @RequestParam("vantagemId") Long vantagemId,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("ALUNO")) {
      redirectAttributes.addFlashAttribute("error", "Acesso negado.");
      return "redirect:/login";
    }

    Optional<Aluno> alunoOpt = usuarioService.findAlunoById(usuarioLogado.getId());
    if (!alunoOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("error", "Aluno não encontrado.");
      session.invalidate();
      return "redirect:/login";
    }
    Aluno aluno = alunoOpt.get();

    Optional<Vantagem> vantagemOpt = vantagemService.buscarVantagemPorId(vantagemId);
    if (!vantagemOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("error", "Vantagem não encontrada.");
      return "redirect:/home/aluno";
    }
    Vantagem vantagem = vantagemOpt.get();

    try {
      // A lógica de setar o alunoComprador está no VantagemService.comprarVantagem
      vantagemService.comprarVantagem(aluno, vantagem);
      redirectAttributes.addFlashAttribute("success",
          "Vantagem '" + vantagem.getNome() + "' comprada com sucesso! Moedas deduzidas.");
      // Atualiza o aluno na sessão com o novo saldo (importante para exibir saldo
      // atualizado)
      session.setAttribute("usuarioLogado", usuarioService.findAlunoById(aluno.getId()).get());

    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("error", "Erro ao comprar vantagem: " + e.getMessage());
      System.err.println("Erro ao comprar vantagem: " + e.getMessage());
      e.printStackTrace();
    }

    return "redirect:/home/aluno";
  }

  // NOVIDADE AQUI: Endpoint para listar as vantagens compradas pelo aluno
  @GetMapping("/minhas-vantagens")
  public String minhasVantagens(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null || !usuarioLogado.getRole().equals("ALUNO")) {
      redirectAttributes.addFlashAttribute("error", "Acesso negado.");
      return "redirect:/login";
    }

    Optional<Aluno> alunoOpt = usuarioService.findAlunoById(usuarioLogado.getId());
    if (!alunoOpt.isPresent()) {
      session.invalidate();
      redirectAttributes.addFlashAttribute("error", "Aluno não encontrado.");
      return "redirect:/login";
    }
    Aluno aluno = alunoOpt.get();
    session.setAttribute("usuarioLogado", aluno); // Atualiza o objeto na sessão

    // Busca as vantagens compradas por este aluno
    List<Vantagem> minhasVantagens = vantagemService.listarVantagensCompradasPorAluno(aluno);
    model.addAttribute("minhasVantagens", minhasVantagens);
    model.addAttribute("aluno", aluno); // Passa o aluno para a tela

    return "aluno/minhas_vantagens"; // Nome da nova página HTML
  }
}
