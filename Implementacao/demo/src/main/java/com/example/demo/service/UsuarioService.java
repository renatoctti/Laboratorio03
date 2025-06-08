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
import com.example.demo.model.Usuario; // Mantenha o import de Usuario, pois buscarUsuarioPorEmail ainda retorna Usuario
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
   @Autowired
   private UsuarioDAO usuarioDAO; // Ainda útil para buscar por email
   @Autowired
   private AlunoDAO alunoDAO;
   @Autowired
   private EmpresaDAO empresaDAO;
   @Autowired
   private ProfessorDAO professorDAO;
   @Autowired
   private TransacaoDAO transacaoDAO;

   @Transactional
   public Usuario cadastrarUsuario(Usuario usuario) {
      return usuarioDAO.save(usuario);
   }

   @Transactional
   public Aluno cadastrarAluno(Aluno aluno) {
      aluno.setRole("ALUNO"); // Garante que a role esteja correta antes de salvar
      return alunoDAO.save(aluno); // O Hibernate cuidará da persistência em ambas as tabelas
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

   @Transactional
   public void transferirMoedas(Professor professorOrigem, Aluno alunoDestino, int quantidade, String motivo) { // Adicionado
                                                                                                                // 'motivo'
      if (professorOrigem.getMoedas() < quantidade) {
         throw new RuntimeException("Saldo insuficiente do professor.");
      }
      if (quantidade <= 0) {
         throw new RuntimeException("A quantidade deve ser positiva.");
      }
      if (motivo == null || motivo.trim().isEmpty()) { // Validação do motivo
         throw new RuntimeException("O motivo da transferência é obrigatório.");
      }

      // Subtrai do professor
      professorOrigem.setMoedas(professorOrigem.getMoedas() - quantidade);
      professorDAO.save(professorOrigem);

      // Adiciona ao aluno
      alunoDestino.setMoedas(alunoDestino.getMoedas() + quantidade);
      alunoDAO.save(alunoDestino);

      // NOVIDADE AQUI: Registrar a transação
      Transacao transacao = new Transacao(professorOrigem, alunoDestino, quantidade, motivo);
      transacaoDAO.save(transacao); // Salva a transação no banco de dados
   }

   // NOVO MÉTODO: Para obter o histórico de transações de um professor
   public List<Transacao> getExtratoProfessor(Professor professor) {
      return transacaoDAO.findByProfessorOrigemOrderByDataTransacaoDesc(professor);
   }

   public List<Transacao> getExtratoAluno(Aluno aluno){
      return transacaoDAO.findByAlunoDestinoOrderByDataTransacaoDesc(aluno);
   }
}