package com.example.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.TransacaoDAO;
import com.example.demo.Model.Transacao;

@Service
public class TransacaoService {
   
   @Autowired
   TransacaoDAO transacaoDAO;

   public Transacao save (Transacao transacao){
      return transacaoDAO.save(transacao);
   }
}
