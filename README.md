# Moeda Estudantil - Sistema de Mérito Acadêmico

## Introdução
Este projeto é um sistema web desenvolvido em Java com Spring Boot para gerenciar um programa de mérito estudantil baseado em moedas virtuais. Alunos recebem moedas por desempenho e comportamento, podendo trocá-las por vantagens oferecidas por empresas parceiras. Professores distribuem moedas e empresas cadastram benefícios. O sistema inclui autenticação, notificações por email e integração com banco de dados MySQL.

## Configuração do Ambiente

### Pré-requisitos
- Java 21 ou superior
- MySQL 8+
- Git

### Clonando o repositório
```bash
git clone https://github.com/renatoctti/Laboratorio03.git
cd Laboratorio03/Implementacao/demo
```

### Configuração do Banco de Dados
1. Crie um banco de dados MySQL chamado `moeda_estudantil`.
2. Altere as credenciais do banco no arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moeda_estudantil?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### Configuração de Email
No mesmo arquivo, configure um email válido para envio de notificações:
```properties
spring.mail.username=SEU_EMAIL@gmail.com
spring.mail.password=SUA_SENHA_DE_APLICATIVO
```
## Build e Execução

### Usando Maven Wrapper (recomendado)
No Windows:
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```
No Linux/Mac:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

O sistema estará disponível em `http://localhost:8080`.

---
# Diagrama de Caso de Uso
![Diagrama de caso de uso](Laboratorio03/Projeto/CasoDeUso/CasosdeUso.png)

# Diagrama de classes
![Diagrama de classes](Laboratorio03/Projeto/Classe/CasosdeUso.png)

# Diagrama de componentes
![Diagrama de componentes](Laboratorio03/Projeto/Componentes/DiagramaDeComponentes.png)

# Diagrama de implantação
![Diagrama de implantação](Laboratorio03/Projeto/Implantacao/DiagramaDeImpplantacap.png)

---

# Histórias de Usuário

## Aluno

### 01 - Cadastro de Aluno
**Como** um aluno  
**Quero** me cadastrar no sistema  
**Para** poder participar do programa de mérito estudantil.

### 02 - Consulta de Extrato
**Como** um aluno  
**Quero** consultar o meu extrato de moedas  
**Para** saber quantas moedas recebi e quantas usei.

### 03 - Troca de Moedas por Vantagens
**Como** um aluno  
**Quero** trocar minhas moedas por vantagens cadastradas  
**Para** aproveitar os benefícios oferecidos pelas empresas parceiras.

### 04 - Recebimento de Notificação
**Como** um aluno  
**Quero** receber um email quando ganhar moedas  
**Para** saber que fui reconhecido por um professor.

### 05 - Recebimento de Cupom
**Como** um aluno  
**Quero** receber por email um cupom com código ao resgatar uma vantagem  
**Para** poder utilizá-lo na empresa parceira.

---

## Professor

### 06 - Distribuição de Moedas
**Como** um professor  
**Quero** distribuir moedas para alunos  
**Para** reconhecer seu desempenho e comportamento.

### 07 - Consulta de Extrato
**Como** um professor  
**Quero** consultar meu extrato de transações  
**Para** saber quantas moedas distribuí e ainda posso distribuir.

---

## Empresa Parceira

### 08 - Cadastro de Empresa
**Como** uma empresa parceira  
**Quero** me cadastrar no sistema  
**Para** oferecer vantagens em troca das moedas estudantis.

### 09 - Cadastro de Vantagens
**Como** uma empresa parceira  
**Quero** cadastrar produtos e descontos com fotos e descrições  
**Para** atrair alunos interessados nas vantagens.

### 10 - Recebimento de Notificação de Troca
**Como** uma empresa parceira  
**Quero** receber um email com o código da vantagem resgatada  
**Para** poder validar a troca presencialmente.

---

## Sistema

### 11 - Autenticação
**Como** um usuário (aluno, professor ou empresa)  
**Quero** fazer login no sistema  
**Para** acessar minhas funcionalidades específicas.
