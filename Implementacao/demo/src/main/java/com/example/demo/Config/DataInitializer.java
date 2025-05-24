package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.Model.Instituicao;
import com.example.demo.Service.InstituicaoService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InstituicaoService instituicaoService;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar dados básicos se necessário
        if (instituicaoService.count() == 0) {
            // Criar instituições de exemplo
            instituicaoService.save(new Instituicao("PUC Minas", "Av. Dom José Gaspar, 500 - Coração Eucarístico, Belo Horizonte - MG"));
            instituicaoService.save(new Instituicao("UFMG", "Av. Pres. Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG"));
            instituicaoService.save(new Instituicao("CEFET-MG", "Av. Amazonas, 7675 - Nova Gameleira, Belo Horizonte - MG"));
            
            System.out.println("Dados iniciais criados com sucesso!");
        }
    }
}
