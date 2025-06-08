package com.example.demo.dao;

import java.util.*;

import com.example.demo.model.Empresa;

public interface EmpresaDAO {

   Empresa save(Empresa empresa);
   Optional<Empresa> findByEmail(String email);
   Optional<Empresa> findById(Long id);
   Optional<Empresa> findByCnpj(String cnpj);
   List<Empresa> findAll();
   void delete(Empresa empresa);
   
} 