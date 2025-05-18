package com.example.view;

import java.util.Scanner;

import com.example.controller.EmpresaParceiraController;
import com.example.model.EmpresaParceira;

public class EmpresaParceiraView {
    private static EmpresaParceiraController controller = new EmpresaParceiraController();
    private static Scanner sc = new Scanner(System.in);
    
    public static void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Empresas Parceiras ---");
            System.out.println("1. Cadastrar empresa parceira");
            System.out.println("2. Listar empresas parceiras");
            System.out.println("3. Buscar empresa por ID");
            System.out.println("4. Atualizar dados de empresa");
            System.out.println("5. Remover empresa");
            System.out.println("0. Voltar ao menu principal");
            
            try {
                opcao = Integer.parseInt(sc.nextLine());
                
                switch (opcao) {
                    case 1:
                        cadastrarEmpresa();
                        break;
                    case 2:
                        listarEmpresas();
                        break;
                    case 3:
                        buscarEmpresa();
                        break;
                    case 4:
                        atualizarEmpresa();
                        break;
                    case 5:
                        removerEmpresa();
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
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
    }
    
    private static void cadastrarEmpresa() throws Exception {
        System.out.println("\n--- Cadastro de Empresa Parceira ---");
        
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        
        System.out.print("CNPJ: ");
        String cnpj = sc.nextLine();
        
        System.out.print("Email: ");
        String email = sc.nextLine();
        
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        
        System.out.print("Endereço: ");
        String endereco = sc.nextLine();
        
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        
        controller.cadastrar(nome, cnpj, email, telefone, endereco, senha);
        System.out.println("Empresa cadastrada com sucesso!");
    }
    
    private static void listarEmpresas() throws Exception {
        System.out.println("\n--- Lista de Empresas Parceiras ---");
        
        for (EmpresaParceira empresa : controller.listar()) {
            System.out.println("ID: " + empresa.getId());
            System.out.println("Nome: " + empresa.getNome());
            System.out.println("CNPJ: " + empresa.getCnpj());
            System.out.println("Email: " + empresa.getEmail());
            System.out.println("Telefone: " + empresa.getTelefone());
            System.out.println("Endereço: " + empresa.getEndereco());
            System.out.println("------------------------");
        }
    }
    
    private static void buscarEmpresa() throws Exception {
        System.out.println("\n--- Buscar Empresa por ID ---");
        
        System.out.print("ID da empresa: ");
        int id = Integer.parseInt(sc.nextLine());
        
        EmpresaParceira empresa = controller.buscarPorId(id);
        if (empresa != null) {
            System.out.println("ID: " + empresa.getId());
            System.out.println("Nome: " + empresa.getNome());
            System.out.println("CNPJ: " + empresa.getCnpj());
            System.out.println("Email: " + empresa.getEmail());
            System.out.println("Telefone: " + empresa.getTelefone());
            System.out.println("Endereço: " + empresa.getEndereco());
        } else {
            System.out.println("Empresa não encontrada.");
        }
    }
    
    private static void atualizarEmpresa() throws Exception {
        System.out.println("\n--- Atualizar Dados de Empresa ---");
        
        System.out.print("ID da empresa: ");
        int id = Integer.parseInt(sc.nextLine());
        
        EmpresaParceira empresa = controller.buscarPorId(id);
        if (empresa == null) {
            System.out.println("Empresa não encontrada.");
            return;
        }
        
        System.out.println("Dados atuais:");
        System.out.println("Nome: " + empresa.getNome());
        System.out.println("CNPJ: " + empresa.getCnpj());
        System.out.println("Email: " + empresa.getEmail());
        System.out.println("Telefone: " + empresa.getTelefone());
        System.out.println("Endereço: " + empresa.getEndereco());
        
        System.out.println("\nDigite os novos dados (deixe em branco para manter o valor atual):");
        
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        if (nome.isEmpty()) nome = empresa.getNome();
        
        System.out.print("CNPJ: ");
        String cnpj = sc.nextLine();
        if (cnpj.isEmpty()) cnpj = empresa.getCnpj();
        
        System.out.print("Email: ");
        String email = sc.nextLine();
        if (email.isEmpty()) email = empresa.getEmail();
        
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        if (telefone.isEmpty()) telefone = empresa.getTelefone();
        
        System.out.print("Endereço: ");
        String endereco = sc.nextLine();
        if (endereco.isEmpty()) endereco = empresa.getEndereco();
        
        System.out.print("Nova senha (deixe em branco para manter a atual): ");
        String senha = sc.nextLine();
        
        controller.atualizar(id, nome, cnpj, email, telefone, endereco, senha);
        System.out.println("Dados da empresa atualizados com sucesso!");
    }
    
    private static void removerEmpresa() throws Exception {
        System.out.println("\n--- Remover Empresa ---");
        
        System.out.print("ID da empresa a remover: ");
        int id = Integer.parseInt(sc.nextLine());
        
        EmpresaParceira empresa = controller.buscarPorId(id);
        if (empresa == null) {
            System.out.println("Empresa não encontrada.");
            return;
        }
        
        System.out.println("Dados da empresa:");
        System.out.println("Nome: " + empresa.getNome());
        System.out.println("CNPJ: " + empresa.getCnpj());
        
        System.out.print("Tem certeza que deseja remover esta empresa? (S/N): ");
        String confirmacao = sc.nextLine();
        
        if (confirmacao.equalsIgnoreCase("S")) {
            controller.remover(id);
            System.out.println("Empresa removida com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}