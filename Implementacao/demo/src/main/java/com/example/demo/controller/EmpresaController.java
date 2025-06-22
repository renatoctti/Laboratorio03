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

import com.example.demo.model.Empresa;
import com.example.demo.model.Usuario;
import com.example.demo.model.Vantagem;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.VantagemService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/home/empresa")
public class EmpresaController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private VantagemService vantagemService;

  @GetMapping
  public String homeEmpresa(HttpSession session, Model model) {

    Empresa empresaLogada = validarEAutenticarEmpresa(session, model);
    if (empresaLogada == null) {
      return "redirect:/login";
    }

    List<Vantagem> minhasVantagens = vantagemService.listarVantagensDisponiveisPorEmpresa(empresaLogada);
    model.addAttribute("minhasVantagens", minhasVantagens);

    return "home_empresa";
  }

  @GetMapping("/cadastro-vantagem")
  public String exibirFormularioCadastroVantagem(Model model, HttpSession session,
      RedirectAttributes redirectAttributes) {
    Empresa empresaLogada = validarEAutenticarEmpresa(session, redirectAttributes);
    if (empresaLogada == null) {
      return "redirect:/login";
    }

    Vantagem vantagem = new Vantagem();
    model.addAttribute("vantagem", vantagem);
    return "empresa/cadastro-vantagem";
  }

  @PostMapping("/cadastro-vantagem")
  public String cadastrarVantagem(
      @Valid Vantagem vantagem, // Vantagem agora pode conter imageUrl
      BindingResult result,
      HttpSession session,
      RedirectAttributes redirectAttributes,
      Model model) {
    Empresa empresaLogada = validarEAutenticarEmpresa(session, redirectAttributes);
    if (empresaLogada == null) {
      return "redirect:/login";
    }

    vantagem.setEmpresaParceira(empresaLogada); // Associa a empresa logada à vantagem

    if (result.hasErrors()) {
      model.addAttribute("error", result.getFieldError() != null ? result.getFieldError().getDefaultMessage()
          : "Erro de validação nos campos.");
      model.addAttribute("vantagem", vantagem);
      return "empresa/cadastro-vantagem";
    }

    try {
      vantagemService.cadastrarVantagem(vantagem); // O serviço agora salvará a URL da imagem
      redirectAttributes.addFlashAttribute("success", "Vantagem cadastrada com sucesso!");
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar vantagem: " + e.getMessage());
    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("error", "Erro inesperado ao cadastrar vantagem.");
      System.err.println("Erro inesperado ao cadastrar vantagem: " + e.getMessage());
      e.printStackTrace();
    }

    return "redirect:/home/empresa";
  }

  @GetMapping("/vantagens-vendidas")
  public String vantagensVendidas(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    Empresa empresaLogada = validarEAutenticarEmpresa(session, redirectAttributes);
    if (empresaLogada == null) {
      return "redirect:/login";
    }

    // Lista as vantagens que esta empresa vendeu
    List<Vantagem> vantagensVendidas = vantagemService.listarVantagensVendidasPorEmpresa(empresaLogada);
    model.addAttribute("vantagensVendidas", vantagensVendidas);
    model.addAttribute("usuario", empresaLogada); // Passa a empresa para a tela

    return "empresa/vantagens_vendidas"; // Nome da nova página HTML
  }

  /**
   * Método auxiliar para validar autenticação e autorização da empresa
   * @param session Sessão HTTP
   * @param model Modelo para adicionar mensagens de erro
   * @return Empresa logada ou null se não autorizada
   */
  private Empresa validarEAutenticarEmpresa(HttpSession session, Model model) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

    if (usuario == null || !usuario.getRole().equals("EMPRESA")) {
      return null;
    }

    Optional<Empresa> empresaOpt = usuarioService.findEmpresaByEmail(usuario.getEmail());

    if (!empresaOpt.isPresent()) {
      session.invalidate();
      model.addAttribute("error", "Dados da empresa não encontrados. Faça login novamente.");
      return null;
    }

    Empresa empresaLogada = empresaOpt.get();
    session.setAttribute("usuarioLogado", empresaLogada);
    model.addAttribute("usuario", empresaLogada);

    return empresaLogada;
  }

  /**
   * Método auxiliar para validar autenticação e autorização da empresa (versão com RedirectAttributes)
   * @param session Sessão HTTP
   * @param redirectAttributes Atributos para redirecionamento
   * @return Empresa logada ou null se não autorizada
   */
  private Empresa validarEAutenticarEmpresa(HttpSession session, RedirectAttributes redirectAttributes) {
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

    if (usuario == null || !usuario.getRole().equals("EMPRESA")) {
      redirectAttributes.addFlashAttribute("error", "Acesso negado.");
      return null;
    }

    Optional<Empresa> empresaOpt = usuarioService.findEmpresaByEmail(usuario.getEmail());

    if (!empresaOpt.isPresent()) {
      session.invalidate();
      redirectAttributes.addFlashAttribute("error", "Empresa não encontrada. Faça login novamente.");
      return null;
    }

    Empresa empresaLogada = empresaOpt.get();
    session.setAttribute("usuarioLogado", empresaLogada);

    return empresaLogada;
  }

}
