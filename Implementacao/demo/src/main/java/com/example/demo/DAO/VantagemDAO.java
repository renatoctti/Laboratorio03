package com.example.demo.DAO;

import com.example.demo.Model.Vantagem;
import java.util.List;

public interface VantagemDAO {
    Vantagem save(Vantagem vantagem);
    Vantagem findById(Long id);
    List<Vantagem> findAll();
    void deleteById(Long id);
}
