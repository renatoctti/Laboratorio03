package com.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.model.EmpresaParceira;
import com.example.util.Conexao;

public class EmpresaParceiraDAO {

    // CREATE - Inserir uma nova empresa parceira
    public void inserir(EmpresaParceira empresa) throws Exception {
        String sql = "INSERT INTO empresa_parceira (nome, cnpj, email, telefone, endereco, senha) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getCnpj());
            stmt.setString(3, empresa.getEmail());
            stmt.setString(4, empresa.getTelefone());
            stmt.setString(5, empresa.getEndereco());
            stmt.setString(6, empresa.getSenha());
            stmt.executeUpdate();
            
            // Obter o ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empresa.setId(rs.getInt(1));
                }
            }
        }
    }
    
    // READ - Listar todas as empresas parceiras
    public List<EmpresaParceira> listarTodas() throws Exception {
        List<EmpresaParceira> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa_parceira";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EmpresaParceira empresa = new EmpresaParceira(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cnpj"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("endereco"),
                    rs.getString("senha")
                );
                lista.add(empresa);
            }
        }
        return lista;
    }
    
    // READ - Buscar empresa por ID
    public EmpresaParceira buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM empresa_parceira WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmpresaParceira(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco"),
                        rs.getString("senha")
                    );
                }
            }
        }
        return null;
    }
    
    // READ - Buscar empresa por CNPJ
    public EmpresaParceira buscarPorCnpj(String cnpj) throws Exception {
        String sql = "SELECT * FROM empresa_parceira WHERE cnpj = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmpresaParceira(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco"),
                        rs.getString("senha")
                    );
                }
            }
        }
        return null;
    }
    
    // READ - Buscar empresa por email (para login)
    public EmpresaParceira buscarPorEmail(String email) throws Exception {
        String sql = "SELECT * FROM empresa_parceira WHERE email = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmpresaParceira(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco"),
                        rs.getString("senha")
                    );
                }
            }
        }
        return null;
    }
    
    // UPDATE - Atualizar dados da empresa
    public void atualizar(EmpresaParceira empresa) throws Exception {
        String sql = "UPDATE empresa_parceira SET nome = ?, cnpj = ?, email = ?, telefone = ?, endereco = ?, senha = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getCnpj());
            stmt.setString(3, empresa.getEmail());
            stmt.setString(4, empresa.getTelefone());
            stmt.setString(5, empresa.getEndereco());
            stmt.setString(6, empresa.getSenha());
            stmt.setInt(7, empresa.getId());
            stmt.executeUpdate();
        }
    }
    
    // DELETE - Remover empresa
    public void remover(int id) throws Exception {
        String sql = "DELETE FROM empresa_parceira WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    // Verificar credenciais para login
    public EmpresaParceira autenticar(String email, String senha) throws Exception {
        String sql = "SELECT * FROM empresa_parceira WHERE email = ? AND senha = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmpresaParceira(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco"),
                        rs.getString("senha")
                    );
                }
            }
        }
        return null;
    }
}