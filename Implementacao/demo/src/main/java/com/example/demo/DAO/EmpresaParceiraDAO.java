package com.example.demo.DAO;

import java.util.List;
import java.util.Optional;

import com.example.demo.Model.EmpresaParceira;


public interface EmpresaParceiraDAO {
    List<EmpresaParceira> findAll();
    Optional<EmpresaParceira> findById(Long id);
    Optional<EmpresaParceira> findByEmail(String email);
    Optional<EmpresaParceira> findByCnpj(String cnpj);
    EmpresaParceira save(EmpresaParceira empresa);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
    long count();
}