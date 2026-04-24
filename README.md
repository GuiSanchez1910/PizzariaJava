# PizzariaJava
Sistema de Gestão de Pedidos - Pizzaria
Este projeto é uma aplicação Java desenvolvida para gerenciar o fluxo de pedidos de uma pizzaria, focando na aplicação de conceitos de Programação Orientada a Objetos e manipulação de Coleções.

# SOBRE O PROJETO
O sistema permite o cadastro de clientes, a manutenção de um cardápio de pizzas e a gestão completa de pedidos, automatizando cálculos de subtotais e totais, além de monitorar o status de cada solicitação.

Estrutura do Modelo
O projeto está dividido em 4 classes principais de modelo:

1. Cliente
Representa o consumidor final.

Atributos: id, nome, telefone, endereco.

Validação: Implementa lógica para garantir que telefone e endereco sejam preenchidos obrigatoriamente.

2. Pizza
Define os produtos disponíveis no cardápio.

Atributos: id, sabor, tamanho (P, M, G), precoBase.

Destaque: Implementação completa de encapsulamento com construtores (cheio/vazio) e métodos acessores.

3. ItemPedido
Classe associativa que vincula uma pizza a um pedido específico.

Atributos: id, pizza, quantidade, subtotal.

Regra de Negócio: Método calcularSubtotal() que processa automaticamente o valor total do item baseado na quantidade.

4. Pedido
A classe central que gerencia a inteligência do negócio.

Atributos: id, cliente, List<ItemPedido>, dataHora, status, valorTotal.

Estados do Pedido: CRIADO, PREPARANDO, ENTREGUE.

# SCHEMA

CREATE DATABASE pizzaria;

```sql
CREATE TABLE pizza (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sabor VARCHAR(255) NOT NULL,
    tamanho VARCHAR(10)
);
```
