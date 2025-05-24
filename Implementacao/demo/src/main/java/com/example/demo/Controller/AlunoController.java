package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.Aluno;
import com.example.demo.Model.Instituicao;
import com.example.demo.Service.AlunoService;
import com.example.demo.Service.InstituicaoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    
    @Autowired
    private AlunoService alunoService;
    
    @Autowired
    private InstituicaoService instituicaoService;
    
    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute("alunos", alunoService.findAll());
            return "alunos/lista";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao carregar lista de alunos: " + e.getMessage());
            return "alunos/lista";
        }
    }
    
    @GetMapping("/novo")
    public String novo(Model model) {
        try {
            model.addAttribute("aluno", new Aluno());
            model.addAttribute("instituicoes", instituicaoService.findAll());
            return "alunos/form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao carregar formulário: " + e.getMessage());
            return "redirect:/alunos";
        }
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            return alunoService.findById(id)
                .map(aluno -> {
                    model.addAttribute("aluno", aluno);
                    model.addAttribute("instituicoes", instituicaoService.findAll());
                    return "alunos/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Aluno não encontrado!");
                    return "redirect:/alunos";
                });
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao carregar aluno: " + e.getMessage());
            return "redirect:/alunos";
        }
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Aluno aluno, 
                        @RequestParam("instituicaoId") Long instituicaoId,
                        BindingResult result, 
                        Model model, 
                        RedirectAttributes redirectAttributes) {
        
        try {
            // Buscar e definir a instituição
            if (instituicaoId != null) {
                Instituicao instituicao = instituicaoService.findById(instituicaoId).orElse(null);
                if (instituicao != null) {
                    aluno.setInstituicao(instituicao);
                } else {
                    result.rejectValue("instituicao", "error.aluno", "Instituição não encontrada");
                }
            } else {
                result.rejectValue("instituicao", "error.aluno", "Instituição é obrigatória");
            }
            
            // Validações customizadas
            if (!alunoService.isEmailAvailable(aluno.getEmail(), aluno.getId())) {
                result.rejectValue("email", "error.aluno", "Email já está em uso");
            }
            
            if (!alunoService.isCpfAvailable(aluno.getCpf(), aluno.getId())) {
                result.rejectValue("cpf", "error.aluno", "CPF já está em uso");
            }
            
            if (result.hasErrors()) {
                model.addAttribute("instituicoes", instituicaoService.findAll());
                model.addAttribute("instituicaoSelecionada", instituicaoId);
                return "alunos/form";
            }
            
            alunoService.save(aluno);
            redirectAttributes.addFlashAttribute("sucesso", "Aluno salvo com sucesso!");
            return "redirect:/alunos";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar aluno: " + e.getMessage());
            model.addAttribute("instituicoes", instituicaoService.findAll());
            model.addAttribute("instituicaoSelecionada", instituicaoId);
            return "alunos/form";
        }
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            alunoService.deleteById(id);
            redirectAttributes.addFlashAttribute("sucesso", "Aluno excluído com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir aluno: " + e.getMessage());
        }
        return "redirect:/alunos";
    }
    
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            return alunoService.findById(id)
                .map(aluno -> {
                    model.addAttribute("aluno", aluno);
                    return "alunos/detalhes";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Aluno não encontrado!");
                    return "redirect:/alunos";
                });
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao carregar detalhes: " + e.getMessage());
            return "redirect:/alunos";
        }
    }
}
