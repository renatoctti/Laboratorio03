package com.example.view;

import java.util.Scanner;

import com.example.controller.EstudanteController;
import com.example.model.Estudante;

public class MainView {
   private static EstudanteController controller = new EstudanteController();

   public static void menu() throws Exception {
      Scanner sc = new Scanner(System.in);
      int opcao;
      do {
         System.out.println("\n--- Moeda Estudantil ---");
         System.out.println("1. Cadastrar estudante");
         System.out.println("2. Adicionar moedas");
         System.out.println("3. Consultar saldo");
         System.out.println("4. Listar estudantes");
         System.out.println("0. Sair");
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
         }
      } while (opcao != 0);

      sc.close();
   }
}
