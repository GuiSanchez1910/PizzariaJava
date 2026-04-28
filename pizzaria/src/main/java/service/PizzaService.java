package service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import exception.PizzaNaoEncontradaException;
import exception.ValidacaoException;
import exception.ErroOperacaoBancoException;

import models.Pizza;
import repository.PizzaRepository;

public class PizzaService {
    private PizzaRepository repo = new PizzaRepository();

    public Pizza criarPizza(Pizza p) throws Exception {

        if (p.getSabor() == null || p.getSabor().isEmpty()) {
            throw new ValidacaoException("Sabor é obrigatório");
        }

        if (p.getTamanho() == null || p.getTamanho().isEmpty()) {
            throw new ValidacaoException("Tamanho é obrigatório");
        }

        if (p.getPrecoBase() == null || p.getPrecoBase() <= 0) {
            throw new ValidacaoException("Preço base deve ser maior que zero");
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
            throw new ErroOperacaoBancoException("Erro ao deletar pizza no banco de dados.", e);
        }
    }

    public Pizza atualizar(Pizza p) throws Exception {

        if (p.getId() == null || p.getId().trim().isEmpty()) {
            throw new ValidacaoException("ID é obrigatório");
        }

        if (p.getSabor() == null || p.getSabor().isEmpty()) {
            throw new ValidacaoException("Sabor é obrigatório");
        }

        if (p.getTamanho() == null || p.getTamanho().isEmpty()) {
            throw new ValidacaoException("Tamanho é obrigatório");
        }

        if (p.getPrecoBase() == null || p.getPrecoBase() <= 0) {
            throw new ValidacaoException("Preço base deve ser maior que zero");
        }

        try {
            repo.atualizar(p);
        } catch (SQLException e) {
            throw new ErroOperacaoBancoException("Erro ao atualizar pizza no banco de dados.", e);
        }

        return p;
    }
}
