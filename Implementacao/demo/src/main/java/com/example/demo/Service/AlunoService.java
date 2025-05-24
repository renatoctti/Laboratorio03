package com.example.demo.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.AlunoDAO;
import com.example.demo.Model.Aluno;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoDAO alunoDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Aluno> findAll() {
        return alunoDAO.findAll();
    }

    public Optional<Aluno> findById(Long id) {
        return alunoDAO.findById(id);
    }

    public Optional<Aluno> findByEmail(String email) {
        return alunoDAO.findByEmail(email);
    }

    public Aluno save(Aluno aluno) {
        if (aluno.getId() == null) {
            // Novo aluno - criptografar senha
            aluno.setSenha(passwordEncoder.encode(aluno.getSenha()));
        } else {
            // Para edição, manter a senha existente
            Aluno alunoExistente = alunoDAO.findById(aluno.getId()).orElse(null);
            if (alunoExistente != null) {
                aluno.setSenha(alunoExistente.getSenha());
            }
        }
        return alunoDAO.save(aluno);
    }

    public void deleteById(Long id) {
        alunoDAO.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return alunoDAO.existsByEmail(email);
    }

    public boolean existsByCpf(String cpf) {
        return alunoDAO.existsByCpf(cpf);
    }

    public boolean isEmailAvailable(String email, Long id) {
        Optional<Aluno> aluno = alunoDAO.findByEmail(email);
        return aluno.isEmpty() || aluno.get().getId().equals(id);
    }

    public boolean isCpfAvailable(String cpf, Long id) {
        Optional<Aluno> aluno = alunoDAO.findByCpf(cpf);
        return aluno.isEmpty() || aluno.get().getId().equals(id);
    }

    public long count() {
        return alunoDAO.count();
    }
}
