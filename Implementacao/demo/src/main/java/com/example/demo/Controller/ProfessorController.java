package com.example.demo.controller;

import com.example.demo.dto.TransferenciaMoedasDTO;
import com.example.demo.model.*;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home/professor")
public class ProfessorController {

    // Constantes para strings repetitivas
    private static final String USUARIO_LOGADO = "usuarioLogado";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String PROFESSOR_ATTR = "professor";
    private static final String ERROR_ATTR = "error";
    private static final String TRANSFERENCIA_DTO = "transferenciaMoedasDTO";
    private static final String ALUNOS_ATTR = "alunos";
    private static final String EXTRATO_ATTR = "extrato";

    private final UsuarioService usuarioService;

    @Autowired
    public ProfessorController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Métodos auxiliares reutilizáveis
    private boolean isProfessorNaoAutenticado(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
        return usuario == null || !"PROFESSOR".equals(usuario.getRole());
    }

    private Optional<Professor> getProfessorDaSessao(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
        return usuarioService.findProfessorById(usuario.getId());
    }

    private void configurarModelComProfessor(HttpSession session, Model model) {
        Professor professor = getProfessorDaSessao(session).orElseThrow();
        session.setAttribute(USUARIO_LOGADO, professor);
        model.addAttribute(PROFESSOR_ATTR, professor);
    }

    // Endpoints
    @GetMapping
    public String mostrarHomeProfessor(HttpSession session, Model model) {
        if (isProfessorNaoAutenticado(session)) {
            return REDIRECT_LOGIN;
        }

        Optional<Professor> professorOpt = getProfessorDaSessao(session);
        if (professorOpt.isEmpty()) {
            session.invalidate();
            return REDIRECT_LOGIN + "?error=Professor+nao+encontrado";
        }

        configurarModelComProfessor(session, model);
        return "home_professor";
    }

    @GetMapping("/transferir-moedas")
    public String mostrarFormularioTransferencia(HttpSession session, Model model) {
        if (isProfessorNaoAutenticado(session)) {
            return REDIRECT_LOGIN;
        }

        Optional<Professor> professorOpt = getProfessorDaSessao(session);
        if (professorOpt.isEmpty()) {
            session.invalidate();
            return REDIRECT_LOGIN + "?error=Usuario+nao+encontrado";
        }

        configurarModelComProfessor(session, model);
        model.addAttribute(ALUNOS_ATTR, usuarioService.findAllAlunos());
        model.addAttribute(TRANSFERENCIA_DTO, new TransferenciaMoedasDTO());

        return "professor/transferir_moedas";
    }

    @PostMapping("/transferir-moedas")
    public String processarTransferencia(
            @Valid TransferenciaMoedasDTO transferenciaDTO,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (isProfessorNaoAutenticado(session)) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Acesso negado.");
            return REDIRECT_LOGIN;
        }

        Optional<Professor> professorOpt = getProfessorDaSessao(session);
        if (professorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Professor não encontrado.");
            session.invalidate();
            return REDIRECT_LOGIN;
        }

        Professor professor = professorOpt.get();

        if (bindingResult.hasErrors()) {
            return handleErrosDeValidacao(professor, transferenciaDTO, bindingResult, model);
        }

        return executarTransferencia(professor, transferenciaDTO, redirectAttributes, session);
    }

    private String handleErrosDeValidacao(Professor professor, 
                                        TransferenciaMoedasDTO dto,
                                        BindingResult result,
                                        Model model) {
        model.addAttribute(PROFESSOR_ATTR, professor);
        model.addAttribute(ALUNOS_ATTR, usuarioService.findAllAlunos());
        model.addAttribute(ERROR_ATTR, result.getFieldError() != null 
                ? result.getFieldError().getDefaultMessage() 
                : "Erro de validação nos campos.");
        model.addAttribute(TRANSFERENCIA_DTO, dto);
        return "professor/transferir_moedas";
    }

    private String executarTransferencia(Professor professor,
                                      TransferenciaMoedasDTO dto,
                                      RedirectAttributes redirectAttributes,
                                      HttpSession session) {
        if (!validarTransferencia(professor, dto, redirectAttributes)) {
            return "redirect:/home/professor/transferir-moedas";
        }

        Optional<Aluno> alunoOpt = usuarioService.findAlunoById(dto.getAlunoId());
        if (alunoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Aluno não encontrado.");
            return "redirect:/home/professor/transferir-moedas";
        }

        try {
            Aluno aluno = alunoOpt.get();
            usuarioService.transferirMoedas(professor, aluno, dto.getQuantidade(), dto.getMotivo());
            
            redirectAttributes.addFlashAttribute("success",
                    String.format("%d moedas transferidas com sucesso para %s!", 
                    dto.getQuantidade(), aluno.getNome()));

            atualizarSessao(session, professor.getId());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, 
                    "Erro ao transferir moedas: " + e.getMessage());
        }

        return "redirect:/home/professor/transferir-moedas";
    }

    private boolean validarTransferencia(Professor professor, 
                                       TransferenciaMoedasDTO dto,
                                       RedirectAttributes redirectAttributes) {
        if (professor.getMoedas() < dto.getQuantidade()) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, "Saldo insuficiente de moedas.");
            return false;
        }
        if (dto.getQuantidade() <= 0) {
            redirectAttributes.addFlashAttribute(ERROR_ATTR, 
                    "A quantidade de moedas deve ser positiva.");
            return false;
        }
        return true;
    }

    private void atualizarSessao(HttpSession session, Long professorId) {
        usuarioService.findProfessorById(professorId)
            .ifPresent(prof -> session.setAttribute(USUARIO_LOGADO, prof));
    }

    @GetMapping("/extrato")
    public String mostrarExtrato(HttpSession session, Model model) {
        if (isProfessorNaoAutenticado(session)) {
            return REDIRECT_LOGIN;
        }

        Optional<Professor> professorOpt = getProfessorDaSessao(session);
        if (professorOpt.isEmpty()) {
            session.invalidate();
            return REDIRECT_LOGIN + "?error=Professor+nao+encontrado";
        }

        configurarModelComProfessor(session, model);
        model.addAttribute(EXTRATO_ATTR, 
                usuarioService.getExtratoProfessor(professorOpt.get()));

        return "professor/extrato";
    }
}