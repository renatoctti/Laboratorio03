package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList; // NOVIDADE
import java.util.Comparator; // NOVIDADE

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

        // Coleta transações de moedas
        List<Transacao> extratoMoedas = usuarioService.getExtratoAluno(aluno);
        // Coleta vantagens compradas
        List<Vantagem> vantagensCompradas = vantagemService.listarVantagensCompradasPorAluno(aluno);

        // Cria uma lista unificada para o extrato
        List<Object> extratoUnificado = new ArrayList<>();
        extratoUnificado.addAll(extratoMoedas);
        extratoUnificado.addAll(vantagensCompradas);

        // Ordena a lista unificada pela data (mais recente primeiro)
        // Usa uma expressão lambda para comparar a data da transação ou da compra da vantagem
        extratoUnificado.sort(Comparator.comparing((Object item) -> { // AQUI: Adicionado o tipo explícito para 'item'
            if (item instanceof Transacao) {
                return ((Transacao) item).getDataTransacao();
            } else if (item instanceof Vantagem) {
                return ((Vantagem) item).getDataCompra();
            }
            return null; // Caso um tipo inesperado apareça, embora não deva
        }, Comparator.nullsLast(Comparator.reverseOrder()))); // Ordena do mais recente para o mais antigo, nulos por último

        model.addAttribute("extratoUnificado", extratoUnificado); // Envia a lista unificada para o HTML

        return "aluno/extrato";
    }

    @GetMapping("/confirmar-compra/{id}")
    public String mostrarConfirmacaoCompra(
            @PathVariable("id") Long vantagemId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
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
            RedirectAttributes redirectAttributes
    ) {
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
            vantagemService.comprarVantagem(aluno, vantagem);
            redirectAttributes.addFlashAttribute("success", "Vantagem '" + vantagem.getNome() + "' comprada com sucesso! Moedas deduzidas.");
            session.setAttribute("usuarioLogado", usuarioService.findAlunoById(aluno.getId()).get());

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao comprar vantagem: " + e.getMessage());
            System.err.println("Erro ao comprar vantagem: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/home/aluno";
    }

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
        session.setAttribute("usuarioLogado", aluno);

        List<Vantagem> minhasVantagens = vantagemService.listarVantagensCompradasPorAluno(aluno);
        model.addAttribute("minhasVantagens", minhasVantagens);
        model.addAttribute("aluno", aluno);

        return "aluno/minhas_vantagens";
    }
}
