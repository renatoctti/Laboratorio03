package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.InstituicaoDAO;
import com.example.demo.Model.Instituicao;

import java.util.List;
import java.util.Optional;

@Service
public class InstituicaoService {
    
    @Autowired
    private InstituicaoDAO instituicaoDAO;
    
    public List<Instituicao> findAll() {
        return instituicaoDAO.findAll();
    }
    
    public Optional<Instituicao> findById(Long id) {
        return instituicaoDAO.findById(id);
    }
    
    public Instituicao save(Instituicao instituicao) {
        return instituicaoDAO.save(instituicao);
    }
    
    public void deleteById(Long id) {
        instituicaoDAO.deleteById(id);
    }
    
    public long count() {
        return instituicaoDAO.count();
    }
}
