package com.example.demo.service;

import com.example.demo.model.Aluno;
import com.example.demo.model.Empresa; // NOVIDADE: Importar Empresa
import com.example.demo.model.Professor;
import com.example.demo.model.Transacao;
import com.example.demo.model.Vantagem; // NOVIDADE: Importar Vantagem
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // Injeta o JavaMailSender configurado pelo Spring

    // Define o remetente do e-mail (usará o username configurado em application.properties)
    private String remetente = "lucasvilelapessoal@gmail.com"; // Deve ser o mesmo do spring.mail.username

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    /**
     * Envia um e-mail de confirmação para o professor após o envio de moedas.
     * @param professor O professor que enviou as moedas.
     * @param aluno O aluno que recebeu as moedas.
     * @param valor O valor das moedas enviadas.
     * @param transacao A transação realizada.
     */
    public void sendCoinTransferConfirmationToProfessor(Professor professor, Aluno aluno, int valor, Transacao transacao) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(professor.getEmail());
            message.setSubject("Confirmação de Envio de Moedas - Plataforma Educacional");

            String formattedDate = transacao.getDataTransacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("pt-BR")));

            String text = String.format(
                    "Olá %s,\n\n" +
                    "Confirmamos que você enviou %d moedas para o aluno %s.\n\n" +
                    "Detalhes da Transação:\n" +
                    "ID da Transação: %d\n" +
                    "Data/Hora: %s\n" +
                    "Valor: %d moedas\n" +
                    "Destinatário: %s\n\n" +
                    "Motivo: %s\n\n" + // Inclui o motivo
                    "Obrigado por utilizar nossa plataforma!\n\n" +
                    "Atenciosamente,\nSua Plataforma Educacional",
                    professor.getNome(), valor, aluno.getNome(),
                    transacao.getId(), formattedDate, valor, aluno.getNome(),
                    transacao.getMotivo() // Obtém o motivo da transação
            );
            message.setText(text);

            mailSender.send(message);
            System.out.println("E-mail de confirmação de envio enviado para o professor: " + professor.getEmail());
        } catch (MailException e) {
            System.err.println("Erro ao enviar e-mail para o professor " + professor.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao enviar e-mail para o professor " + professor.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envia um e-mail de confirmação para o aluno após o recebimento de moedas.
     * @param aluno O aluno que recebeu as moedas.
     * @param professor O professor que enviou as moedas.
     * @param valor O valor das moedas recebidas.
     * @param transacao A transacao realizada.
     */
    public void sendCoinReceptionConfirmationToAluno(Aluno aluno, Professor professor, int valor, Transacao transacao) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(aluno.getEmail());
            message.setSubject("Confirmação de Recebimento de Moedas - Plataforma Educacional");

            String formattedDate = transacao.getDataTransacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("pt-BR")));

            String text = String.format(
                    "Olá %s,\n\n" +
                    "Confirmamos que você recebeu %d moedas!\n\n" +
                    "Detalhes da Transação:\n" +
                    "ID da Transação: %d\n" +
                    "Data/Hora: %s\n" +
                    "Valor: %d moedas\n" +
                    "Remetente: Professor %s\n\n" +
                    "Motivo: %s\n\n" + // Inclui o motivo
                    "Seu saldo atual é de %d moedas.\n\n" +
                    "Obrigado por utilizar nossa plataforma!\n\n" +
                    "Atenciosamente,\nSua Plataforma Educacional",
                    aluno.getNome(), valor,
                    transacao.getId(), formattedDate, valor, professor.getNome(),
                    transacao.getMotivo(), // Obtém o motivo da transação
                    aluno.getMoedas() // Exibe o saldo atual do aluno
            );
            message.setText(text);

            mailSender.send(message);
            System.out.println("E-mail de confirmação de recebimento enviado para o aluno: " + aluno.getEmail());
        } catch (MailException e) {
            System.err.println("Erro ao enviar e-mail para o aluno " + aluno.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao enviar e-mail para o aluno " + aluno.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * NOVO MÉTODO: Envia um e-mail de confirmação para o aluno após comprar uma vantagem.
     * @param aluno O aluno que comprou a vantagem.
     * @param vantagem A vantagem comprada.
     */
    public void sendVantagemConfirmationToAluno(Aluno aluno, Vantagem vantagem) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(aluno.getEmail());
            message.setSubject("Confirmação de Compra de Vantagem - Plataforma Educacional");

            String text = String.format(
                    "Olá %s,\n\n" +
                    "Confirmamos que você comprou a vantagem '%s' por %d moedas!\n\n" +
                    "Detalhes da Vantagem:\n" +
                    "Nome: %s\n" +
                    "Descrição: %s\n" +
                    "Oferecido por: %s\n" +
                    "Custo: %d moedas\n\n" +
                    "Seu novo saldo é de %d moedas.\n\n" +
                    "Obrigado por utilizar nossa plataforma!\n\n" +
                    "Atenciosamente,\nSua Plataforma Educacional",
                    aluno.getNome(), vantagem.getNome(), vantagem.getCustoEmMoedas(),
                    vantagem.getNome(), vantagem.getDescricao(), vantagem.getEmpresaParceira().getNomeFantasia(),
                    vantagem.getCustoEmMoedas(),
                    aluno.getMoedas() // Saldo atual do aluno
            );
            message.setText(text);

            mailSender.send(message);
            System.out.println("E-mail de confirmação de compra de vantagem enviado para o aluno: " + aluno.getEmail());
        } catch (MailException e) {
            System.err.println("Erro ao enviar e-mail de confirmação de compra para o aluno " + aluno.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao enviar e-mail de confirmação de compra para o aluno " + aluno.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * NOVO MÉTODO: Envia uma notificação para a empresa parceira sobre a venda de uma vantagem.
     * @param empresa A empresa parceira que vendeu a vantagem.
     * @param vantagem A vantagem vendida.
     * @param aluno O aluno que comprou a vantagem.
     */
    public void sendVantagemNotificationToEmpresa(Empresa empresa, Vantagem vantagem, Aluno aluno) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(empresa.getEmail());
            message.setSubject("Notificação de Venda de Vantagem - Plataforma Educacional");

            String text = String.format(
                    "Olá %s,\n\n" +
                    "Sua vantagem '%s' foi comprada por um aluno!\n\n" +
                    "Detalhes da Vantagem:\n" +
                    "Nome: %s\n" +
                    "Descrição: %s\n" +
                    "Custo: %d moedas\n\n" +
                    "Detalhes do Comprador:\n" +
                    "Nome do Aluno: %s\n" +
                    "Email do Aluno: %s\n\n" +
                    "Esta vantagem foi marcada como vendida em nossa plataforma.\n\n" +
                    "Atenciosamente,\nSua Plataforma Educacional",
                    empresa.getNomeFantasia(), vantagem.getNome(),
                    vantagem.getNome(), vantagem.getDescricao(), vantagem.getCustoEmMoedas(),
                    aluno.getNome(), aluno.getEmail()
            );
            message.setText(text);

            mailSender.send(message);
            System.out.println("E-mail de notificação de venda enviado para a empresa: " + empresa.getEmail());
        } catch (MailException e) {
            System.err.println("Erro ao enviar e-mail de notificação de venda para a empresa " + empresa.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao enviar e-mail de notificação de venda para a empresa " + empresa.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
