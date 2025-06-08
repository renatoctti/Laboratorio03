package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.model.Aluno;
import com.example.demo.model.Empresa;
import com.example.demo.model.InstituicaoEnsino;
import com.example.demo.model.Professor;
import com.example.demo.model.Usuario; // Manter este import para Usuario, para o login e buscarUsuarioPorEmail
import com.example.demo.service.InstituicaoEnsinoService;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

   @Autowired
   private UsuarioService usuarioService;

   @Autowired
   private InstituicaoEnsinoService instituicaoEnsinoService;

   @GetMapping("")
   public String showLoginForm(Model model) {
      model.addAttribute("usuario", new Usuario());
      return "login";
   }

   @PostMapping("/login") // Certifique-se de que este "path" está correto no seu formulário de login
   public String processLogin(@RequestParam String email, @RequestParam String senha, HttpSession session,
         Model model) {
      Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);

      if (usuario != null && usuario.getSenha().equals(senha)) {
         session.setAttribute("usuarioLogado", usuario);
         return "redirect:/home/" + usuario.getRole().toLowerCase();
      } else {
         model.addAttribute("error", "Email ou senha invalidos");
         return "login";
      }
   }

   @GetMapping("/cadastro")
   public String showRegistrationForm(Model model) {
      // 'usuario' pode ser um objeto genérico para preencher o formulário no lado do
      // Thymeleaf
      model.addAttribute("usuario", new Usuario());
      List<InstituicaoEnsino> instituicoes = instituicaoEnsinoService.findAll();
      model.addAttribute("instituicoes", instituicoes);
      return "cadastro";
   }

   @PostMapping("/cadastro")
   public String processRegistration(
         @RequestParam String email,
         @RequestParam String senha,
         @RequestParam String role,
         @RequestParam(required = false) String nome,
         @RequestParam(required = false) String cpf,
         @RequestParam(required = false) String rg,
         @RequestParam(required = false) String endereco,
         @RequestParam(required = false) Long instituicaoEnsinoId,
         @RequestParam(required = false) String curso,
         @RequestParam(required = false) String cnpj,
         @RequestParam(required = false) String nomeFantasia,
         @RequestParam(required = false) String departamento,
         @RequestParam(required = false) String titulacao,
         Model model) {

      // Verifica se o email já está cadastrado antes de prosseguir
      if (usuarioService.buscarUsuarioPorEmail(email) != null) {
         model.addAttribute("error", "Email já cadastrado");
         List<InstituicaoEnsino> instituicoes = instituicaoEnsinoService.findAll();
         model.addAttribute("instituicoes", instituicoes);
         return "cadastro";
      }

      try {
         switch (role.toUpperCase()) {
            case "ALUNO":
               // Validação de campos específicos de Aluno
               if (nome == null || nome.isEmpty() || cpf == null || cpf.isEmpty() || instituicaoEnsinoId == null
                     || curso == null || curso.isEmpty()) {
                  throw new IllegalArgumentException("Todos os campos de Aluno são obrigatórios.");
               }
               Aluno aluno = new Aluno();
               // Define os campos herdados de Usuario diretamente no objeto Aluno
               aluno.setEmail(email);
               aluno.setSenha(senha);
               aluno.setRole(role.toUpperCase()); // A role será sobrescrita no service, mas é bom para consistência

               // Define os campos específicos de Aluno
               aluno.setNome(nome);
               aluno.setCpf(cpf);
               aluno.setRg(rg);
               aluno.setEndereco(endereco);

               // Busca e define a InstituicaoEnsino
               Optional<InstituicaoEnsino> instituicaoEnsinoOpt = instituicaoEnsinoService
                     .findById(instituicaoEnsinoId);
               if (instituicaoEnsinoOpt.isEmpty()) {
                  throw new IllegalArgumentException("Instituição de Ensino para o Aluno não encontrada.");
               }
               aluno.setInstituicaoEnsino(instituicaoEnsinoOpt.get());
               aluno.setCurso(curso);

               usuarioService.cadastrarAluno(aluno); // Chama o serviço para cadastrar o Aluno
               break;

            case "EMPRESA":
               // Validação de campos específicos de Empresa
               if (cnpj == null || cnpj.isEmpty() || nomeFantasia == null || nomeFantasia.isEmpty()) {
                  throw new IllegalArgumentException("Todos os campos de Empresa são obrigatórios.");
               }

               Empresa empresa = new Empresa();
               // Define os campos herdados de Usuario diretamente no objeto Empresa
               empresa.setEmail(email);
               empresa.setSenha(senha);
               empresa.setRole(role.toUpperCase()); // A role será sobrescrita no service

               // Define os campos específicos de Empresa
               empresa.setCnpj(cnpj);
               empresa.setNomeFantasia(nomeFantasia);

               usuarioService.cadastrarEmpresa(empresa); // Chama o serviço para cadastrar a Empresa
               break;

            case "PROFESSOR":
               // Validação de campos específicos de Professor
               if (nome == null || nome.isEmpty() || cpf == null || cpf.isEmpty() || instituicaoEnsinoId == null
                     || departamento == null || departamento.isEmpty()) {
                  throw new IllegalArgumentException("Todos os campos de Professor são obrigatórios");
               }
               Professor professor = new Professor();
               // Define os campos herdados de Usuario diretamente no objeto Professor
               professor.setEmail(email);
               professor.setSenha(senha);
               professor.setRole(role.toUpperCase()); // A role será sobrescrita no service

               // Define os campos específicos de Professor
               professor.setNome(nome);
               professor.setCpf(cpf);

               // Busca e define a InstituicaoEnsino
               Optional<InstituicaoEnsino> instituicaoProfessorOpt = instituicaoEnsinoService
                     .findById(instituicaoEnsinoId);
               if (instituicaoProfessorOpt.isEmpty()) {
                  throw new IllegalArgumentException("Instituição de Ensino para Professor não encontrada.");
               }
               professor.setInstituicaoEnsino(instituicaoProfessorOpt.get());
               professor.setDepartamento(departamento);
               professor.setTitulacao(titulacao);

               usuarioService.cadastrarProfessor(professor); // Chama o serviço para cadastrar o Professor
               break;

            default:
               model.addAttribute("error", "Tipo de usuário inválido.");
               List<InstituicaoEnsino> instituicoes = instituicaoEnsinoService.findAll();
               model.addAttribute("instituicoes", instituicoes);
               return "cadastro";
         }
      } catch (IllegalArgumentException e) {
         // Captura exceções de validação e exibe a mensagem de erro
         model.addAttribute("error", e.getMessage());
         List<InstituicaoEnsino> instituicoes = instituicaoEnsinoService.findAll();
         model.addAttribute("instituicoes", instituicoes);
         return "cadastro";
      } catch (Exception e) {
         // Captura outras exceções inesperadas e exibe uma mensagem genérica (e loga a
         // stack trace)
         e.printStackTrace(); // Imprime o stack trace no console para depuração
         model.addAttribute("error", "Erro ao cadastrar: " + e.getMessage()); // Mensagem mais útil para o usuário
         List<InstituicaoEnsino> instituicoes = instituicaoEnsinoService.findAll();
         model.addAttribute("instituicoes", instituicoes);
         return "cadastro";
      }

      model.addAttribute("success", "Cadastro realizado com sucesso! Faça login.");
      return "login";
   }

   @GetMapping("/logout")
   public String logout() {
      return "/login";
   }
}