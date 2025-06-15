package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.AlunoDAO;
import com.example.demo.dao.EmpresaDAO;
import com.example.demo.dao.ProfessorDAO;
import com.example.demo.dao.TransacaoDAO;
import com.example.demo.dao.UsuarioDAO;
import com.example.demo.model.Aluno;
import com.example.demo.model.Empresa;
import com.example.demo.model.Professor;
import com.example.demo.model.Transacao;
import com.example.demo.model.Usuario;
import jakarta.transaction.Transactional; // Manter este import para @Transactional

@Service
public class UsuarioService {
   @Autowired
   private UsuarioDAO usuarioDAO;
   @Autowired
   private AlunoDAO alunoDAO;
   @Autowired
   private EmpresaDAO empresaDAO;
   @Autowired
   private ProfessorDAO professorDAO;
   @Autowired
   private TransacaoDAO transacaoDAO; // Mantenha, para extratos e salvar novas transações

   @Autowired
   private EmailService emailService; // NOVIDADE: Injeta o serviço de e-mail

   @Transactional
   public Usuario cadastrarUsuario(Usuario usuario) {
      return usuarioDAO.save(usuario);
   }

   @Transactional
   public Aluno cadastrarAluno(Aluno aluno) {
      aluno.setRole("ALUNO");
      return alunoDAO.save(aluno);
   }

   @Transactional
   public Empresa cadastrarEmpresa(Empresa empresa) {
      empresa.setRole("EMPRESA");
      return empresaDAO.save(empresa);
   }

   @Transactional
   public Professor cadastrarProfessor(Professor professor) {
      professor.setRole("PROFESSOR");
      return professorDAO.save(professor);
   }

   public Usuario buscarUsuarioPorEmail(String email) {
      Optional<Usuario> usuario = usuarioDAO.findByEmail(email);
      return usuario.orElse(null);
   }

   public Optional<Empresa> findEmpresaByEmail(String email) {
      return empresaDAO.findByEmail(email);
   }

   public Optional<Professor> findProfessorById(Long id) {
      return professorDAO.findById(id);
   }

   public List<Aluno> findAllAlunos() {
      return alunoDAO.findAll();
   }

   public Optional<Aluno> findAlunoById(Long id) {
      return alunoDAO.findById(id);
   }

   @Transactional // Garante que toda a operação (débito/crédito/registro/e-mail) seja atômica
   public void transferirMoedas(Professor professorOrigem, Aluno alunoDestino, int quantidadeMoedas, String motivo) {
      if (quantidadeMoedas <= 0) {
         throw new IllegalArgumentException("A quantidade de moedas deve ser positiva.");
      }
      if (alunoDestino == null || professorOrigem == null) {
         throw new IllegalArgumentException("Aluno e Professor devem ser válidos para a transação.");
      }
      if (professorOrigem.getMoedas() < quantidadeMoedas) {
         throw new RuntimeException("Saldo insuficiente do professor.");
      }
      if (motivo == null || motivo.trim().isEmpty()) {
         throw new IllegalArgumentException("O motivo da transferência é obrigatório.");
      }

      // Subtrai do professor
      professorOrigem.setMoedas(professorOrigem.getMoedas() - quantidadeMoedas);
      professorDAO.save(professorOrigem); // Salva as alterações no professor

      // Adiciona ao aluno
      alunoDestino.setMoedas(alunoDestino.getMoedas() + quantidadeMoedas);
      alunoDAO.save(alunoDestino); // Salva as alterações no aluno

      // Cria e salva a transação
      Transacao transacao = new Transacao(professorOrigem, alunoDestino, quantidadeMoedas, motivo);
      // A dataTransacao já é definida no construtor da Transacao como
      // LocalDateTime.now()
      transacaoDAO.save(transacao); // Salva a transação

      // Envia e-mails de confirmação (agora chamando EmailService diretamente daqui)
      emailService.sendCoinTransferConfirmationToProfessor(professorOrigem, alunoDestino, quantidadeMoedas, transacao);
      emailService.sendCoinReceptionConfirmationToAluno(alunoDestino, professorOrigem, quantidadeMoedas, transacao);
   }

   // Métodos para obter o histórico de transações (já estavam aqui e permanecem)
   public List<Transacao> getExtratoProfessor(Professor professor) {
      return transacaoDAO.findByProfessorOrigemOrderByDataTransacaoDesc(professor);
   }

   public List<Transacao> getExtratoAluno(Aluno aluno) {
      return transacaoDAO.findByAlunoDestinoOrderByDataTransacaoDesc(aluno);
   }
}
