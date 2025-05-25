package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.EmpresaParceira;
import com.example.demo.Service.EmpresaParceiraService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empresas")
public class EmpresaParceiraController {
    
    @Autowired
    private EmpresaParceiraService empresaService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresas", empresaService.findAll());
        return "empresa/lista";
    }
    
    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("empresa", new EmpresaParceira());
        return "empresa/form";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return empresaService.findById(id)
            .map(empresa -> {
                model.addAttribute("empresa", empresa);
                return "empresa/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("erro", "Empresa não encontrada!");
                return "redirect:/empresa";
            });
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute EmpresaParceira empresa, BindingResult result, 
                        Model model, RedirectAttributes redirectAttributes) {
        
        // Validações customizadas
        if (!empresaService.isEmailAvailable(empresa.getEmail(), empresa.getId())) {
            result.rejectValue("email", "error.empresa", "Email já está em uso");
        }
        
        if (!empresaService.isCnpjAvailable(empresa.getCnpj(), empresa.getId())) {
            result.rejectValue("cnpj", "error.empresa", "CNPJ já está em uso");
        }
        
        if (result.hasErrors()) {
            return "empresas/form";
        }
        
        try {
            empresaService.save(empresa);
            redirectAttributes.addFlashAttribute("sucesso", "Empresa salva com sucesso!");
            return "redirect:/empresas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar empresa: " + e.getMessage());
            return "redirect:/empresas";
        }
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            empresaService.deleteById(id);
            redirectAttributes.addFlashAttribute("sucesso", "Empresa excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir empresa: " + e.getMessage());
        }
        return "redirect:/empresas";
    }
    
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return empresaService.findById(id)
            .map(empresa -> {
                model.addAttribute("empresa", empresa);
                return "empresas/detalhes";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("erro", "Empresa não encontrada!");
                return "redirect:/empresas";
            });
    }
}
