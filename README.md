# PizzariaJava
> **Sistema de Gestão de Pedidos**
> 
> Aplicação robusta em Java para automação de fluxos comerciais em pizzarias, com foco em Programação Orientada a Objetos (POO) e manipulação dinâmica de coleções.

---

## Sobre o Projeto
O sistema faz o gerenciamento de clientes e cardápio e o fechamento financeiro do pedido. A solução automatiza cálculos de subtotal por item e consolida o valor total do pedido, com estados logísticos.

---

## Arquitetura do Modelo de Dados
A lógica de domínio está estruturada em quatro entidades fundamentais, organizadas para garantir integridade e baixo acoplamento.

### 1. Entidade Cliente
Responsável pela identificação e localização do consumidor.
* **Atributos:** `id`, `nome`, `telefone`, `endereco`.

### 2. Entidade Pizza
Define os produtos e especificações do catálogo.
* **Atributos:** `id`, `sabor`, `tamanho` (P, M, G), `precoBase`.

### 3. Entidade ItemPedido
Classe de associação que gerencia a relação entre produtos e vendas.
* **Atributos:** `id`, `pizza`, `quantidade`, `subtotal`.
* **Lógica de Cálculo:**
    $$subtotal = precoBase \times quantidade$$

### 4. Entidade Pedido
Classe centralizadora da inteligência de negócio.
* **Atributos:** `id`, `cliente`, `List<ItemPedido>`, `dataHora`, `status`, `valorTotal`.
* **Fluxo de Status:** `CRIADO` → `PREPARANDO` → `ENTREGUE`.

---

## Regras de Negócio e Implementação

| Funcionalidade | Descrição Técnica |
| :--- | :--- |
| **Persistência** | Implementada via JDBC com suporte a transações relacionais. |
| **Cálculo de Totais** | Iteração sobre coleções (`List`) para soma de subtotais. |
| **Gestão de Tempo** | Registro preciso de transações utilizando `LocalDateTime`. |
| **Validação** | Camada de verificação pré-persistência para dados de contato. |

---

## Configuração do Ambiente de Desenvolvimento

### Requisitos de Software
* Java Development Kit (JDK) 17+
* MySQL Server 8.0+
* Maven ou IDE compatível (IntelliJ/Eclipse)

# SCHEMA

```sql
CREATE DATABASE pizzaria;
```

```sql
CREATE TABLE pizza (
    id VARCHAR (50) PRIMARY KEY,
    sabor VARCHAR(255) NOT NULL,
    tamanho VARCHAR(10)
);
```
### API Endpoints 

### PIZZA

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/pizzas` | Lista todas as pizzas cadastradas |
| `GET` | `/pizzas/{id}` | Busca os detalhes de uma pizza por ID |
| `POST` | `/pizzas` | Cria um novo registro de pizza |
| `PUT` | `/pizzas/{id}` | Atualiza as informações de uma pizza |
| `DELETE` | `/pizzas/{id}` | Remove uma pizza permanentemente |

---
