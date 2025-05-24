package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.EmpresaParceiraDAO;
import com.example.demo.Model.EmpresaParceira;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaParceiraService {

    @Autowired
    private EmpresaParceiraDAO empresaDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<EmpresaParceira> findAll() {
        return empresaDAO.findAll();
    }

    public Optional<EmpresaParceira> findById(Long id) {
        return empresaDAO.findById(id);
    }

    public Optional<EmpresaParceira> findByEmail(String email) {
        return empresaDAO.findByEmail(email);
    }

    public EmpresaParceira save(EmpresaParceira empresa) {
        if (empresa.getId() == null) {
            // Nova empresa - criptografar senha
            empresa.setSenha(passwordEncoder.encode(empresa.getSenha()));
        } else {
            // Para edição, manter a senha existente
            EmpresaParceira empresaExistente = empresaDAO.findById(empresa.getId()).orElse(null);
            if (empresaExistente != null) {
                empresa.setSenha(empresaExistente.getSenha());
            }
        }
        return empresaDAO.save(empresa);
    }

    public void deleteById(Long id) {
        empresaDAO.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return empresaDAO.existsByEmail(email);
    }

    public boolean existsByCnpj(String cnpj) {
        return empresaDAO.existsByCnpj(cnpj);
    }

    public boolean isEmailAvailable(String email, Long id) {
        Optional<EmpresaParceira> empresa = empresaDAO.findByEmail(email);
        return empresa.isEmpty() || empresa.get().getId().equals(id);
    }

    public boolean isCnpjAvailable(String cnpj, Long id) {
        Optional<EmpresaParceira> empresa = empresaDAO.findByCnpj(cnpj);
        return empresa.isEmpty() || empresa.get().getId().equals(id);
    }

    public long count() {
        return empresaDAO.count();
    }
}
