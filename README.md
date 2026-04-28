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

- **Atributos:** `id`, `nome`, `telefone`, `endereco`.
- **Relacionamento:** Possui múltiplos `Pedidos` (1:N).

### 2. Entidade Pizza

Define os produtos e especificações do catálogo.

- **Atributos:** `id`, `sabor`, `tamanho`, `precoBase`.
- **Relacionamento:** Referenciada em múltiplos `ItemPedidos` (1:N).

### 3. Entidade ItemPedido

Classe de associação que gerencia a relação entre produtos e vendas.

- **Atributos:** `id`, `pedido`, `pizza`, `quantidade`, `subtotal`.
- **Lógica de Cálculo:**
  $$subtotal = precoBase \times quantidade$$
- **Relacionamento:** Pertence a um `Pedido` (N:1) e referencia uma `Pizza` (N:1).

### 4. Entidade Pedido

Classe centralizadora da inteligência de negócio.

- **Atributos:** `id`, `cliente`, `List<ItemPedido>`, `dataHora`, `status`, `valorTotal`.
- **Relacionamento:** Pertence a um `Cliente` (N:1) e possui múltiplos `ItemPedidos` (1:N).

---

## Regras de Negócio e Implementação

| Funcionalidade        | Descrição Técnica                                             |
| :-------------------- | :------------------------------------------------------------ |
| **Persistência**      | Implementada via JDBC com suporte a transações relacionais.   |
| **Cálculo de Totais** | Iteração sobre coleções (`List`) para soma de subtotais.      |
| **Gestão de Tempo**   | Registro preciso de transações utilizando `LocalDateTime`.    |
| **Validação**         | Camada de verificação pré-persistência para dados de contato. |

---

## Configuração do Ambiente de Desenvolvimento

### Requisitos de Software

- Java Development Kit (JDK) 17+
- MySQL Server 8.0+
- Maven ou IDE compatível (IntelliJ/Eclipse)

# SCHEMA

```sql
CREATE DATABASE pizzaria;
USE pizzaria;

CREATE TABLE pizza (
    id VARCHAR(50) PRIMARY KEY,
    sabor VARCHAR(255) NOT NULL,
    tamanho VARCHAR(10) NOT NULL,
    preco_base DOUBLE NOT NULL
);

CREATE TABLE cliente (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    endereco VARCHAR(255) NOT NULL
);

CREATE TABLE pedido (
    id VARCHAR(50) PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL,
    data_hora VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    valor_total DOUBLE NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
);

CREATE TABLE item_pedido (
    id VARCHAR(50) PRIMARY KEY,
    pedido_id VARCHAR(50) NOT NULL,
    pizza_id VARCHAR(50) NOT NULL,
    quantidade INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (pizza_id) REFERENCES pizza(id) ON DELETE CASCADE
);
```

### API Endpoints

### PIZZA

| Método   | Endpoint       | Descrição                             |
| :------- | :------------- | :------------------------------------ |
| `GET`    | `/pizzas`      | Lista todas as pizzas cadastradas     |
| `GET`    | `/pizzas/{id}` | Busca os detalhes de uma pizza por ID |
| `POST`   | `/pizzas`      | Cria um novo registro de pizza        |
| `PUT`    | `/pizzas/{id}` | Atualiza as informações de uma pizza  |
| `DELETE` | `/pizzas/{id}` | Remove uma pizza permanentemente      |

### CLIENTE

| Método   | Endpoint         | Descrição                              |
| :------- | :--------------- | :------------------------------------- |
| `GET`    | `/clientes`      | Lista todas os clientes cadastradas    |
| `GET`    | `/clientes/{id}` | Busca os detalhes de um cliente por ID |
| `POST`   | `/clientes`      | Cria um novo registro de cliente       |
| `PUT`    | `/clientes/{id}` | Atualiza as informações de um cliente  |
| `DELETE` | `/clientes/{id}` | Remove um cliente permanentemente      |

### ITEM_PEDIDO

| Método   | Endpoint             | Descrição                                                      |
| :------- | :------------------- | :------------------------------------------------------------- |
| `GET`    | `/itens-pedido`      | Lista todos os itens de pedido cadastrados                     |
| `GET`    | `/itens-pedido/{id}` | Busca os detalhes de um item de pedido por ID                  |
| `POST`   | `/itens-pedido`      | Cria um novo item de pedido (calcula subtotal automaticamente) |
| `PUT`    | `/itens-pedido/{id}` | Atualiza as informações de um item de pedido                   |
| `DELETE` | `/itens-pedido/{id}` | Remove um item de pedido permanentemente                       |

### PEDIDO

| Método   | Endpoint             | Descrição                                                      |
| :------- | :------------------- | :------------------------------------------------------------- |
| `GET`    | `/pedidos`      | Lista todos os pedidos cadastrados                             |
| `GET`    | `/pedidos/{id}` | Busca os detalhes de um pedido por ID                          |
| `POST`   | `/pedidos`      | Cria um novo pedido                                            |
| `PUT`    | `/pedidos/{id}` | Atualiza as informações de um  pedido                          |
| `DELETE` | `/pedidos/{id}` | Remove um pedido permanentemente                               |
