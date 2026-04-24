package service;

import java.util.List;
import java.util.UUID;

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
        
        p.setId(UUID.randomUUID().toString());

        repo.salvar(p);

        return p;
    }

    public List<Pizza> listarPizzas() throws Exception {
        return repo.listar();
    }

    public Pizza buscarPorId(String id) throws Exception {
        return repo.buscarPorId(id);
    }

    public void deletar(String id) throws Exception {
        repo.deletar(id);
    }

    public Pizza atualizar(Pizza p) throws Exception {

        if (p.getId() == null) {
            throw new Exception("ID obrigatório");
        }

        repo.atualizar(p);

        return p;
    }
}
