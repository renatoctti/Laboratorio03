package com.example.view;

import java.util.Scanner;

import com.example.controller.EstudanteController;
import com.example.model.Estudante;

public class AlunoView {
   private static EstudanteController controller = new EstudanteController();

   public static void menu() throws Exception {
      Scanner sc = new Scanner(System.in);
      int opcao;
      do {
         System.out.println("\n--- Alunos ---");
         System.out.println("1. Cadastrar estudante");
         System.out.println("2. Adicionar moedas");
         System.out.println("3. Consultar saldo");
         System.out.println("4. Listar estudantes");
         System.out.println("5. Remover estudante"); 
         System.out.println("0. Voltar ao menu principal");
         opcao = sc.nextInt();

         switch (opcao) {
            case 1:
               System.out.print("Nome: ");
               sc.nextLine(); // limpar buffer
               String nome = sc.nextLine();
               controller.cadastrar(nome);
               break;
            case 2:
               System.out.print("ID do estudante: ");
               int id = sc.nextInt();
               System.out.print("Quantidade de moedas: ");
               int qtd = sc.nextInt();
               controller.adicionarMoedas(id, qtd);
               break;
            case 3:
               System.out.print("ID do estudante: ");
               Estudante e = controller.consultar(sc.nextInt());
               if (e != null)
                  System.out.println(e.getNome() + " possui " + e.getSaldo() + " moedas.");
               else
                  System.out.println("Estudante não encontrado.");
               break;
            case 4:
               for (Estudante est : controller.listar())
                  System.out.println(est.getId() + " - " + est.getNome() + " (" + est.getSaldo() + " moedas)");
               break;
            case 5: // New case for removing a student
               System.out.print("ID do estudante a remover: ");
               int idRemover = sc.nextInt();
               // Optionally, confirm before removing
               System.out.print("Tem certeza que deseja remover este estudante? (S/N): ");
               sc.nextLine(); // limpar buffer
               String confirmacao = sc.nextLine();
               if (confirmacao.equalsIgnoreCase("S")) {
                  controller.remover(idRemover);
                  System.out.println("Estudante removido com sucesso.");
               } else {
                  System.out.println("Operação cancelada.");
               }
               break;
            case 0:
               System.out.println("Voltando ao menu principal...");
               return; // Retorna ao menu principal sem fechar o Scanner
            default:
               System.out.println("Opção inválida!");
         }
      } while (opcao != 0);
      
      // Removido o sc.close() para não fechar o Scanner que será usado pelo menu principal
   }
}