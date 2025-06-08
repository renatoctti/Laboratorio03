package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.InstituicaoEnsino;
import com.example.demo.model.Professor;
import com.example.demo.service.InstituicaoEnsinoService;
import com.example.demo.service.UsuarioService;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(
			InstituicaoEnsinoService instituicaoEnsinoService,
			UsuarioService usuarioService 
	) {
		return (args) -> {
			// --- Instituições de Ensino ---
			InstituicaoEnsino ufmg = instituicaoEnsinoService.findByNome("Universidade Federal de Minas Gerais")
					.orElseGet(
							() -> instituicaoEnsinoService.save(new InstituicaoEnsino("Universidade Federal de Minas Gerais",
									"Av. Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG")));

			InstituicaoEnsino pucminas = instituicaoEnsinoService
					.findByNome("Pontifícia Universidade Católica de Minas Gerais")
					.orElseGet(() -> instituicaoEnsinoService
							.save(new InstituicaoEnsino("Pontifícia Universidade Católica de Minas Gerais",
									"Av. Dom José Gaspar, 500 - Coração Eucarístico, Belo Horizonte - MG")));

			InstituicaoEnsino newtonPaiva = instituicaoEnsinoService.findByNome("Centro Universitário Newton Paiva")
					.orElseGet(() -> instituicaoEnsinoService.save(new InstituicaoEnsino("Centro Universitário Newton Paiva",
							"Rua Tomaz Gonzaga, 642 - Lourdes, Belo Horizonte - MG")));

			// --- Professores ---
			// Professor 1
			// Usamos buscarUsuarioPorEmail porque o UsuarioService não tem findByEmail que
			// retorna Professor
			if (usuarioService.buscarUsuarioPorEmail("prof.ana@ufmg.com") == null) {
				Professor profAna = new Professor();
				profAna.setEmail("prof.ana@ufmg.com");
				profAna.setSenha("senha123"); // Senha em texto puro, conforme sua instrução
				profAna.setRole("PROFESSOR");
				profAna.setNome("Ana Silva");
				profAna.setCpf("111.222.333-44");
				profAna.setInstituicaoEnsino(ufmg);
				profAna.setDepartamento("Ciência da Computação");
				profAna.setTitulacao("Doutora");
				usuarioService.cadastrarProfessor(profAna); // Usando UsuarioService
			}

			// Professor 2
			if (usuarioService.buscarUsuarioPorEmail("prof.breno@puc.com") == null) {
				Professor profBreno = new Professor();
				profBreno.setEmail("prof.breno@puc.com");
				profBreno.setSenha("senha123"); // Senha em texto puro
				profBreno.setRole("PROFESSOR");
				profBreno.setNome("Breno Costa");
				profBreno.setCpf("555.666.777-88");
				profBreno.setInstituicaoEnsino(pucminas);
				profBreno.setDepartamento("Engenharia Elétrica");
				profBreno.setTitulacao("Mestre");
				usuarioService.cadastrarProfessor(profBreno); // Usando UsuarioService
			}

			// Professor 3
			if (usuarioService.buscarUsuarioPorEmail("prof.carla@newton.com") == null) {
				Professor profCarla = new Professor();
				profCarla.setEmail("prof.carla@newton.com");
				profCarla.setSenha("senha123"); // Senha em texto puro
				profCarla.setRole("PROFESSOR");
				profCarla.setNome("Carla Dias");
				profCarla.setCpf("999.000.111-22");
				profCarla.setInstituicaoEnsino(newtonPaiva);
				profCarla.setDepartamento("Arquitetura e Urbanismo");
				profCarla.setTitulacao("Especialista");
				usuarioService.cadastrarProfessor(profCarla); // Usando UsuarioService
			}
		};
	}

}
