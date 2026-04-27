package service;

import java.util.List;
import java.util.UUID;

import models.ItemPedido;
import models.Pizza;
import repository.ItemPedidoRepository;

public class ItemPedidoService {
    private ItemPedidoRepository repo = new ItemPedidoRepository();
    private PizzaService pizzaService = new PizzaService();

    public ItemPedido criarItemPedido(ItemPedido ip, String pedidoId) throws Exception {

        if (ip.getPizza() == null || ip.getPizza().getId() == null) {
            throw new Exception("Pizza é obrigatória");
        }

        if (ip.getQuantidade() == null || ip.getQuantidade() <= 0) {
            throw new Exception("Quantidade deve ser maior que zero");
        }

        Pizza pizza = pizzaService.buscarPorId(ip.getPizza().getId());
        if (pizza == null) {
            throw new Exception("Pizza não encontrada");
        }

        ip.setId(UUID.randomUUID().toString());
        ip.setPizza(pizza);
        ip.calcularSubtotal();

        repo.salvar(ip, pedidoId);

        return ip;
    }

    public List<ItemPedido> listarItensPedido() throws Exception {
        return repo.listar();
    }

    public ItemPedido buscarPorId(String id) throws Exception {
        return repo.buscarPorId(id);
    }

    public void deletar(String id) throws Exception {
        repo.deletar(id);
    }

    public ItemPedido atualizar(ItemPedido ip) throws Exception {

        if (ip.getId() == null) {
            throw new Exception("ID obrigatório");
        }

        if (ip.getPizza() == null || ip.getPizza().getId() == null) {
            throw new Exception("Pizza é obrigatória");
        }

        if (ip.getQuantidade() == null || ip.getQuantidade() <= 0) {
            throw new Exception("Quantidade deve ser maior que zero");
        }

        Pizza pizza = pizzaService.buscarPorId(ip.getPizza().getId());
        if (pizza == null) {
            throw new Exception("Pizza não encontrada");
        }

        ip.setPizza(pizza);
        ip.calcularSubtotal();

        repo.atualizar(ip);

        return ip;
    }
}
