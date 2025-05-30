package com.example.demo.Controller;

import com.example.demo.Model.EmpresaParceira;
import com.example.demo.Model.Vantagem;
import com.example.demo.Service.EmpresaParceiraService;
import com.example.demo.Service.VantagemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/vantagens")
public class VantagemController {

    private final VantagemService vantagemService;
    private final EmpresaParceiraService empresaParceiraService;

    public VantagemController(VantagemService vantagemService,
                              EmpresaParceiraService empresaParceiraService) {
        this.vantagemService = vantagemService;
        this.empresaParceiraService = empresaParceiraService;
    }

    @GetMapping
    public String listarVantagens(Model model) {
        try {
            System.out.println("=== DEBUG Controller: Iniciando listagem de vantagens ===");

            List<Vantagem> vantagens = vantagemService.findAll();
            System.out.println("DEBUG Controller: Vantagens recebidas: " + vantagens.size());

            model.addAttribute("vantagens", vantagens);
            model.addAttribute("totalVantagens", vantagens.size());

            System.out.println("=== DEBUG Controller: Finalizando listagem com sucesso ===");
            return "vantagens/lista";

        } catch (Exception e) {
            System.err.println("=== ERRO CRÍTICO Controller: " + e.getMessage() + " ===");
            e.printStackTrace();

            model.addAttribute("vantagens", new ArrayList<>());
            model.addAttribute("totalVantagens", 0);
            model.addAttribute("erro", "Erro ao carregar vantagens: " + e.getMessage());

            return "vantagens/lista";
        }
    }

    @GetMapping("/cadastrar")
    public String mostrarFormCadastro(Model model) {
        try {
            System.out.println("DEBUG Controller: Carregando formulário de cadastro");

            List<EmpresaParceira> empresas = empresaParceiraService.findAll();
            model.addAttribute("empresas", empresas != null ? empresas : new ArrayList<>());
            model.addAttribute("vantagem", new Vantagem());

            return "vantagens/cadastro";
        } catch (Exception e) {
            System.err.println("ERRO Controller cadastro: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao carregar formulário: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/cadastrar")
    public String processarCadastro(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("custo") String custoStr,
            @RequestParam(value = "urlImagem", required = false) String urlImagem, // Novo parâmetro
            @RequestParam(value = "empresaId", required = false) Long empresaId,
            RedirectAttributes redirectAttributes) {

        try {
            System.out.println("DEBUG Controller: Processando cadastro");
            System.out.println("Nome: " + nome);
            System.out.println("Descrição: " + descricao);
            System.out.println("Custo: " + custoStr);
            System.out.println("URL Imagem: " + urlImagem); // Log do novo parâmetro
            System.out.println("Empresa ID: " + empresaId);

            BigDecimal custoMoedas = parseCusto(custoStr);

            Vantagem vantagem = vantagemService.cadastrarVantagem(nome, descricao, custoMoedas, urlImagem, empresaId); // Passando urlImagem
            System.out.println("DEBUG Controller: Vantagem salva com ID: " + vantagem.getId());

            redirectAttributes.addFlashAttribute("sucesso", "Vantagem '" + nome + "' cadastrada com sucesso!");
            return "redirect:/vantagens";

        } catch (Exception e) {
            System.err.println("ERRO Controller processarCadastro: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/vantagens/cadastrar";
        }
    }

    @GetMapping("/{id}")
    public String detalharVantagem(@PathVariable Long id, Model model) {
        try {
            System.out.println("DEBUG Controller: Detalhando vantagem ID: " + id);

            Vantagem vantagem = vantagemService.findById(id);
            if (vantagem == null) {
                throw new RuntimeException("Vantagem não encontrada");
            }

            model.addAttribute("vantagem", vantagem);
            return "vantagens/detalhes";

        } catch (Exception e) {
            System.err.println("ERRO Controller detalhar: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Vantagem não encontrada");
            return "error";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormEdicao(@PathVariable Long id, Model model) {
        try {
            System.out.println("DEBUG Controller: Carregando edição para ID: " + id);

            Vantagem vantagem = vantagemService.findById(id);
            if (vantagem == null) {
                throw new RuntimeException("Vantagem não encontrada");
            }

            List<EmpresaParceira> empresas = empresaParceiraService.findAll();

            model.addAttribute("vantagem", vantagem);
            model.addAttribute("empresas", empresas != null ? empresas : new ArrayList<>());

            return "vantagens/editar";

        } catch (Exception e) {
            System.err.println("ERRO Controller editar form: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Vantagem não encontrada");
            return "error";
        }
    }

    @PostMapping("/editar/{id}")
    public String processarEdicao(
            @PathVariable Long id,
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("custo") String custoStr,
            @RequestParam(value = "urlImagem", required = false) String urlImagem, // Novo parâmetro
            @RequestParam(value = "empresaId", required = false) Long empresaId,
            RedirectAttributes redirectAttributes) {

        try {
            System.out.println("DEBUG Controller: Processando edição para ID: " + id);

            Vantagem vantagem = vantagemService.findById(id);
            if (vantagem == null) {
                throw new RuntimeException("Vantagem não encontrada");
            }

            BigDecimal custoMoedas = parseCusto(custoStr);

            vantagem.setNome(nome);
            vantagem.setDescricao(descricao);
            vantagem.setCustoMoedas(custoMoedas);
            vantagem.setUrlImagem(urlImagem); // Definindo a URL da imagem

            if (empresaId != null) {
                EmpresaParceira empresa = empresaParceiraService.findById(empresaId)
                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
                vantagem.setEmpresa(empresa);
            } else {
                vantagem.setEmpresa(null);
            }

            vantagemService.save(vantagem);
            redirectAttributes.addFlashAttribute("sucesso", "Vantagem atualizada com sucesso!");
            return "redirect:/vantagens/" + id;

        } catch (Exception e) {
            System.err.println("ERRO Controller processar edição: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            return "redirect:/vantagens/editar/" + id;
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirVantagem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("DEBUG Controller: Excluindo vantagem ID: " + id);

            vantagemService.deleteById(id);
            redirectAttributes.addFlashAttribute("sucesso", "Vantagem excluída com sucesso!");

        } catch (Exception e) {
            System.err.println("ERRO Controller excluir: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
        }

        return "redirect:/vantagens";
    }

    private BigDecimal parseCusto(String custoStr) {
        try {
            if (custoStr == null || custoStr.trim().isEmpty()) {
                throw new RuntimeException("Custo é obrigatório");
            }
            return new BigDecimal(custoStr.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Valor monetário inválido. Use números com ponto ou vírgula decimal.");
        }
    }
}