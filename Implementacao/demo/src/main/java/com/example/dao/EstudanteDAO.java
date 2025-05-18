package com.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Estudante;
import com.example.util.Conexao;

public class EstudanteDAO {

   public void inserir(Estudante e) throws Exception {
      String sql = "INSERT INTO estudante (nome, saldo) VALUES (?, ?)";
      try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, e.getNome());
         stmt.setInt(2, e.getSaldo());
         stmt.execute();
      }
   }

   public List<Estudante> listarTodos() throws Exception {
      List<Estudante> lista = new ArrayList<>();
      String sql = "SELECT * FROM estudante";
      try (Connection conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
         while (rs.next()) {
            Estudante e = new Estudante(
                  rs.getInt("id"),
                  rs.getString("nome"),
                  rs.getInt("saldo"));
            lista.add(e);
         }
      }
      return lista;
   }

   public void adicionarMoedas(int id, int quantidade) throws Exception {
      String sql = "UPDATE estudante SET saldo = saldo + ? WHERE id = ?";
      try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setInt(1, quantidade);
         stmt.setInt(2, id);
         stmt.execute();
      }
   }

   public Estudante buscarPorId(int id) throws Exception {
      String sql = "SELECT * FROM estudante WHERE id = ?";
      try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setInt(1, id);
         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               return new Estudante(
                     rs.getInt("id"),
                     rs.getString("nome"),
                     rs.getInt("saldo"));
            }
         }
      }
      return null;
   }
}
