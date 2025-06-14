package com.example.demo.service;

import com.example.demo.dao.VantagemDAO;
import com.example.demo.dao.AlunoDAO;
import com.example.demo.model.Empresa;
import com.example.demo.model.Vantagem;
import com.example.demo.model.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Importar LocalDateTime
import java.util.List;
import java.util.Optional;

@Service
public class VantagemService {

    @Autowired
    private VantagemDAO vantagemDAO;

    @Autowired
    private AlunoDAO alunoDAO;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Vantagem cadastrarVantagem(Vantagem vantagem) {
        if (vantagem.getCustoEmMoedas() <= 0) {
            throw new IllegalArgumentException("O custo da vantagem deve ser positivo.");
        }
        if (vantagem.getEmpresaParceira() == null || vantagem.getEmpresaParceira().getId() == null) {
            throw new IllegalArgumentException("Vantagem deve estar vinculada a uma empresa parceira.");
        }
        vantagem.setVendida(false);
        return vantagemDAO.save(vantagem);
    }

    public List<Vantagem> listarTodasVantagens() {
        return vantagemDAO.findByVendidaFalseAndAlunoCompradorIsNull();
    }

    public Optional<Vantagem> buscarVantagemPorId(Long id) {
        return vantagemDAO.findById(id);
    }

    public List<Vantagem> listarVantagensPorEmpresa(Empresa empresa) {
        return vantagemDAO.findByEmpresaParceira(empresa);
    }

    @Transactional
    public void deletarVantagem(Long id) {
        vantagemDAO.findById(id).ifPresent(vantagemDAO::delete);
    }

    @Transactional
    public void comprarVantagem(Aluno aluno, Vantagem vantagem) {
        if (aluno.getMoedas() < vantagem.getCustoEmMoedas()) {
            throw new RuntimeException("Saldo insuficiente de moedas para comprar esta vantagem.");
        }
        if (vantagem.isVendida()) {
            throw new RuntimeException("Esta vantagem já foi comprada e não está mais disponível.");
        }

        aluno.setMoedas(aluno.getMoedas() - vantagem.getCustoEmMoedas());
        alunoDAO.save(aluno);

        vantagem.setVendida(true);
        vantagem.setAlunoComprador(aluno);
        vantagem.setDataCompra(LocalDateTime.now()); // NOVIDADE: Define a data da compra
        vantagemDAO.save(vantagem);

        emailService.sendVantagemConfirmationToAluno(aluno, vantagem);
        emailService.sendVantagemNotificationToEmpresa(vantagem.getEmpresaParceira(), vantagem, aluno);
    }

    public List<Vantagem> listarVantagensCompradasPorAluno(Aluno aluno) {
        return vantagemDAO.findByVendidaTrueAndAlunoCompradorOrderByNomeAsc(aluno);
    }

    public List<Vantagem> listarVantagensVendidasPorEmpresa(Empresa empresa) {
        return vantagemDAO.findByEmpresaParceiraAndVendidaTrue(empresa);
    }

    public List<Vantagem> listarVantagensDisponiveisPorEmpresa(Empresa empresa) {
        return vantagemDAO.findByEmpresaParceiraAndVendidaFalse(empresa);
    }
}




