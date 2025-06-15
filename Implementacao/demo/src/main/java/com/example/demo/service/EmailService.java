package com.example.demo.service;

import com.example.demo.model.Aluno;
import com.example.demo.model.Professor;
import com.example.demo.model.Transacao;
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

   // Define o remetente do e-mail (usará o username configurado em
   // application.properties)
   // Se quiser um email diferente para FROM, configure uma propriedade customizada
   // e use aqui
   private String remetente = "seu_email@dominio.com"; // Deve ser o mesmo do spring.mail.username

   public void setRemetente(String remetente) {
      this.remetente = remetente;
   }

   /**
    * Envia um e-mail de confirmação para o professor após o envio de moedas.
    * 
    * @param professor O professor que enviou as moedas.
    * @param aluno     O aluno que recebeu as moedas.
    * @param valor     O valor das moedas enviadas.
    * @param transacao A transação realizada.
    */
   public void sendCoinTransferConfirmationToProfessor(Professor professor, Aluno aluno, int valor,
         Transacao transacao) {
      try {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom(remetente);
         message.setTo(professor.getEmail()); // Professor herda email de Usuario
         message.setSubject("Confirmação de Envio de Moedas - Plataforma Educacional");

         String formattedDate = transacao.getDataTransacao()
               .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("pt-BR")));
         String text = String.format(
               "Olá %s,\n\n" +
                     "Confirmamos que você enviou %d moedas para o aluno %s.\n\n" + // RA removido aqui
                     "Detalhes da Transação:\n" +
                     "ID da Transação: %d\n" +
                     "Data/Hora: %s\n" +
                     "Valor: %d moedas\n" +
                     "Destinatário: %s\n\n" + // RA removido aqui
                     "Seu saldo atual é de %d moedas.\n\n"+
                     "Obrigado por utilizar nossa plataforma!\n\n" +
                     "Atenciosamente,\nSua Plataforma Educacional",
               professor.getNome(), valor, aluno.getNome(), // aluno.getRa() removido daqui
               transacao.getId(), formattedDate, valor, aluno.getNome(), professor.getMoedas() // aluno.getRa() removido daqui
         );
         message.setText(text);

         mailSender.send(message);
         System.out.println("E-mail de confirmação de envio enviado para o professor: " + professor.getEmail());
      } catch (MailException e) {
         System.err.println("Erro ao enviar e-mail para o professor " + professor.getEmail() + ": " + e.getMessage());
         e.printStackTrace();
      } catch (Exception e) {
         System.err.println(
               "Erro inesperado ao enviar e-mail para o professor " + professor.getEmail() + ": " + e.getMessage());
         e.printStackTrace();
      }
   }

   /**
    * Envia um e-mail de confirmação para o aluno após o recebimento de moedas.
    * 
    * @param aluno     O aluno que recebeu as moedas.
    * @param professor O professor que enviou as moedas.
    * @param valor     O valor das moedas recebidas.
    * @param transacao A transação realizada.
    */
   public void sendCoinReceptionConfirmationToAluno(Aluno aluno, Professor professor, int valor, Transacao transacao) {
      try {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom(remetente);
         message.setTo(aluno.getEmail()); // Aluno herda email de Usuario
         System.out.println(aluno.getEmail());
         message.setSubject("Confirmação de Recebimento de Moedas - Plataforma Educacional");

         String formattedDate = transacao.getDataTransacao()
               .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("pt-BR")));
         String text = String.format(
               "Olá %s,\n\n" +
                     "Confirmamos que você recebeu %d moedas!\n\n" +
                     "Detalhes da Transação:\n" +
                     "ID da Transação: %d\n" +
                     "Data/Hora: %s\n" +
                     "Valor: %d moedas\n" +
                     "Remetente: Professor %s\n\n" +
                     "Seu saldo atual é de %d moedas.\n\n" +
                     "Obrigado por utilizar nossa plataforma!\n\n" +
                     "Atenciosamente,\nSua Plataforma Educacional",
               aluno.getNome(), valor,
               transacao.getId(), formattedDate, valor, professor.getNome(),
               aluno.getMoedas() // Exibe o saldo atual do aluno
         );
         message.setText(text);

         mailSender.send(message);
         System.out.println("E-mail de confirmação de recebimento enviado para o aluno: " + aluno.getEmail());
      } catch (MailException e) {
         System.err.println("Erro ao enviar e-mail para o aluno " + aluno.getEmail() + ": " + e.getMessage());
         e.printStackTrace();
      } catch (Exception e) {
         System.err
               .println("Erro inesperado ao enviar e-mail para o aluno " + aluno.getEmail() + ": " + e.getMessage());
         e.printStackTrace();
      }
   }
}
