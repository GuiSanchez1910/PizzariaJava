package service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import exception.PizzaNaoEncontradaException;

import models.Pizza;
import repository.PizzaRepository;

public class PizzaService {
    private PizzaRepository repo = new PizzaRepository();

    public Pizza criarPizza(Pizza p) throws Exception {

        if (p.getSabor() == null || p.getSabor().isEmpty()) {
            throw new Exception("Sabor é obrigatório");
        }

        if (p.getTamanho() == null || p.getTamanho().isEmpty()) {
            throw new Exception("Tamanho é obrigatório");
        }

        if (p.getPrecoBase() == null || p.getPrecoBase() <= 0) {
            throw new Exception("Preço base deve ser maior que zero");
        }

        p.setId(UUID.randomUUID().toString());

        repo.salvar(p);

        return p;
    }

    public List<Pizza> listarPizzas() throws Exception {
        return repo.listar();
    }

    public Pizza buscarPorId(String id) throws Exception {
        Pizza p = repo.buscarPorId(id);

        if(p == null) {
            throw new PizzaNaoEncontradaException("Pizza com id " + id + " não encontrada");
        }

        return p;
    }

    public void deletar(String id) {

        try {
            Pizza pizza = repo.buscarPorId(id);

            if (pizza == null) {
                throw new PizzaNaoEncontradaException("Pizza com ID " + id + " não encontrada.");
            }

            repo.deletar(id);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar o banco de dados.", e);
        }
    }

    public Pizza atualizar(Pizza p) throws Exception {

        System.out.println("ID recebido: [" + p.getId() + "]");

        if (p.getId() == null || p.getId().trim().isEmpty()) {
            throw new Exception("ID é obrigatório");
        }

        if (p.getSabor() == null || p.getSabor().isEmpty()) {
            throw new Exception("Sabor é obrigatório");
        }

        if (p.getTamanho() == null || p.getTamanho().isEmpty()) {
            throw new Exception("Tamanho é obrigatório");
        }

        if (p.getPrecoBase() == null || p.getPrecoBase() <= 0) {
            throw new Exception("Preço base deve ser maior que zero");
        }

        try {
            repo.atualizar(p);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pizza no banco", e);
        }

        return p;
    }
}
