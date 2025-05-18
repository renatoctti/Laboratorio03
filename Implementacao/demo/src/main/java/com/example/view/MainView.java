package com.example.view;

import java.util.Scanner;

public class MainView {
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        menu();
    }
    
    public static void menu() {
        int opcao;
        do {
            System.out.println("\n--- Sistema de Moeda Estudantil ---");
            System.out.println("1. Gestão de Estudantes");
            System.out.println("2. Gestão de Empresas Parceiras");
            System.out.println("0. Sair");
            
            try {
                opcao = Integer.parseInt(sc.nextLine());
                
                switch (opcao) {
                    case 1:
                        AlunoView.menu();
                        break;
                    case 2:
                        EmpresaParceiraView.menu();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
                opcao = -1;
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
            }
        } while (opcao != 0);
        
        sc.close();
    }
}