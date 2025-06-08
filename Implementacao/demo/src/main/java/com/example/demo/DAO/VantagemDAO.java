package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Aluno;
import com.example.demo.model.Empresa;
import com.example.demo.model.Vantagem;

public interface VantagemDAO {

   Vantagem save(Vantagem vantagem);

   Optional<Vantagem> findById(Long id);

   List<Vantagem> findAll();

   List<Vantagem> findByEmpresaParceira(Empresa empresa);

   List<Vantagem> findByVendidaFalse();

   void delete(Vantagem vantagem);

   List<Vantagem> findByVendidaFalseAndAlunoCompradorIsNull();

   List<Vantagem> findByVendidaTrueAndAlunoCompradorOrderByNomeAsc(Aluno alunoComprador);

   List<Vantagem> findByEmpresaParceiraAndVendidaTrue(Empresa empresaParceira);

   List<Vantagem> findByEmpresaParceiraAndVendidaFalse(Empresa empresaParceira);

}
