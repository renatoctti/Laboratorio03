package com.example.controller;

import java.util.List;

import com.example.dao.EmpresaParceiraDAO;
import com.example.model.EmpresaParceira;

public class EmpresaParceiraController {
    private EmpresaParceiraDAO dao = new EmpresaParceiraDAO();
    
    // Cadastrar nova empresa parceira
    public void cadastrar(String nome, String cnpj, String email, String telefone, String endereco, String senha) throws Exception {
        // Verificar se já existe empresa com este CNPJ
        if (dao.buscarPorCnpj(cnpj) != null) {
            throw new Exception("Já existe uma empresa cadastrada com este CNPJ.");
        }
        
        // Verificar se já existe empresa com este email
        if (dao.buscarPorEmail(email) != null) {
            throw new Exception("Já existe uma empresa cadastrada com este email.");
        }
        
        EmpresaParceira empresa = new EmpresaParceira(nome, cnpj, email, telefone, endereco, senha);
        dao.inserir(empresa);
    }
    
    // Listar todas as empresas
    public List<EmpresaParceira> listar() throws Exception {
        return dao.listarTodas();
    }
    
    // Buscar empresa por ID
    public EmpresaParceira buscarPorId(int id) throws Exception {
        return dao.buscarPorId(id);
    }
    
    // Atualizar dados da empresa
    public void atualizar(int id, String nome, String cnpj, String email, String telefone, String endereco, String senha) throws Exception {
        EmpresaParceira empresa = dao.buscarPorId(id);
        if (empresa == null) {
            throw new Exception("Empresa não encontrada.");
        }
        
        // Verificar se o CNPJ já está em uso por outra empresa
        EmpresaParceira empresaExistente = dao.buscarPorCnpj(cnpj);
        if (empresaExistente != null && empresaExistente.getId() != id) {
            throw new Exception("Este CNPJ já está em uso por outra empresa.");
        }
        
        // Verificar se o email já está em uso por outra empresa
        empresaExistente = dao.buscarPorEmail(email);
        if (empresaExistente != null && empresaExistente.getId() != id) {
            throw new Exception("Este email já está em uso por outra empresa.");
        }
        
        empresa.setNome(nome);
        empresa.setCnpj(cnpj);
        empresa.setEmail(email);
        empresa.setTelefone(telefone);
        empresa.setEndereco(endereco);
        
        // Atualizar senha apenas se uma nova senha for fornecida
        if (senha != null && !senha.isEmpty()) {
            empresa.setSenha(senha);
        }
        
        dao.atualizar(empresa);
    }
    
    // Remover empresa
    public void remover(int id) throws Exception {
        EmpresaParceira empresa = dao.buscarPorId(id);
        if (empresa == null) {
            throw new Exception("Empresa não encontrada.");
        }
        
        // Aqui poderia verificar se existem vantagens cadastradas pela empresa
        // e decidir se remove ou não, ou se remove em cascata
        
        dao.remover(id);
    }
    
    // Autenticar empresa (login)
    public EmpresaParceira autenticar(String email, String senha) throws Exception {
        EmpresaParceira empresa = dao.autenticar(email, senha);
        if (empresa == null) {
            throw new Exception("Email ou senha inválidos.");
        }
        return empresa;
    }
}