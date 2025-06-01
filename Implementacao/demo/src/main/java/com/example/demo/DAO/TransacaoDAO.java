package com.example.demo.DAO;


import java.util.List;

import com.example.demo.Model.Professor;
import com.example.demo.Model.Transacao;

public interface TransacaoDAO {
   Transacao save(Transacao transacao);
   List<Transacao> getTransacoesByProfessor(Professor professor);
}
