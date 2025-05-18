package com.example.controller;

import java.util.List;

import com.example.dao.EstudanteDAO;
import com.example.model.Estudante;

public class EstudanteController {
   private EstudanteDAO dao = new EstudanteDAO();

   public void cadastrar(String nome) throws Exception {
      Estudante e = new Estudante(nome);
      dao.inserir(e);
   }

   public void adicionarMoedas(int id, int qtd) throws Exception {
      dao.adicionarMoedas(id, qtd);
   }

   public Estudante consultar(int id) throws Exception {
      return dao.buscarPorId(id);
   }

   public List<Estudante> listar() throws Exception {
      return dao.listarTodos();
   }

   public void remover(int id) throws Exception {
    dao.remover(id);
}
}