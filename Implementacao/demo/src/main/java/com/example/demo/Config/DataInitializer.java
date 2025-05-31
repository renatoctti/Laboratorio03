package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.Model.Instituicao;
import com.example.demo.Model.Professor;
import com.example.demo.Service.InstituicaoService;
import com.example.demo.Service.ProfessorService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InstituicaoService instituicaoService;

    @Autowired
    private ProfessorService professorService;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar dados básicos se necessário
        if (instituicaoService.count() == 0) {
            // Criar instituições de exemplo
            instituicaoService.save(new Instituicao("PUC Minas", "Av. Dom José Gaspar, 500 - Coração Eucarístico, Belo Horizonte - MG"));
            instituicaoService.save(new Instituicao("UFMG", "Av. Pres. Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG"));
            instituicaoService.save(new Instituicao("CEFET-MG", "Av. Amazonas, 7675 - Nova Gameleira, Belo Horizonte - MG"));
            
            System.out.println("instituicoes criadas com sucesso!");
        }

        Instituicao PUC = instituicaoService.findById((long) 1).orElse(null);
        Instituicao UFMG = instituicaoService.findById((long) 2).orElse(null);
        Instituicao CEFET = instituicaoService.findById((long) 3).orElse(null);

        if (professorService.count() == 0) {
            professorService.save(new Professor("Ana Silva", "111.222.333-44", "Matemática", "senha123", PUC));
            professorService.save(new Professor("Bruno Costa", "555.666.777-88", "Física", "senha123", UFMG));
            professorService.save(new Professor("Carla Dias", "999.000.111-22", "Química", "senha123", PUC));
            professorService.save(new Professor("Daniel Rocha", "333.444.555-66", "Letras", "senha123", CEFET));
            professorService.save(new Professor("Eduarda Souza", "777.888.999-00", "Ciência da Computação", "senha123", UFMG));

            System.out.println("Professores criados com sucesso!");
        }
    }
}
