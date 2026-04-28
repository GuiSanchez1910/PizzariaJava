package service;

import java.util.List;
import java.util.UUID;

import exception.PedidoNaoEncontradoException;
import exception.ValidacaoException;
import models.Cliente;
import models.ItemPedido;
import models.Pedido;
import models.Pizza;
import repository.ClienteRepository;
import repository.ItemPedidoRepository;
import repository.PedidoRepository;


public class PedidoService {
    
    private PedidoRepository repo = new PedidoRepository();
    private PizzaService pizzaService = new PizzaService();
    private ItemPedidoRepository itemRepo = new ItemPedidoRepository();
    private ClienteRepository clienteRepo = new ClienteRepository();

    public Pedido criarPedido(Pedido p) throws Exception {
        
        Cliente clienteCompleto = clienteRepo.buscarPorId(p.getCliente().getId());

        if (clienteCompleto == null) {
            throw new ValidacaoException("Cliente não encontrado");
        }

        p.setCliente(clienteCompleto);
        
        if (p.getItens() == null || p.getItens().isEmpty()) {
            throw new ValidacaoException("Itens do pedido são obrigatórios");
        }

        p.setId(UUID.randomUUID().toString());
        p.setDataHora(java.time.LocalDateTime.now().toString());
        p.setStatus("PENDENTE");

        for (ItemPedido ip : p.getItens()) {
            if (ip.getPizza() == null || ip.getPizza().getId() == null) {
                throw new ValidacaoException("Pizza é obrigatória");
            }
            if (ip.getQuantidade() == null || ip.getQuantidade() <= 0) {
                throw new ValidacaoException("Quantidade deve ser maior que zero");
            }

            Pizza pizzaNoBanco = pizzaService.buscarPorId(ip.getPizza().getId());
            if (pizzaNoBanco == null) {
                throw new ValidacaoException("Pizza não encontrada: " + ip.getPizza().getId());
            }

            ip.setId(UUID.randomUUID().toString());
            ip.setPizza(pizzaNoBanco);
            ip.calcularSubtotal();
        }

        p.calcularSubtotal();

        repo.salvar(p);

        for (ItemPedido ip : p.getItens()) {
            itemRepo.salvar(ip, p.getId());
        }

        return p;
    }

    public List<Pedido> listarPedidos() throws Exception {
        return repo.listar();
    }

    public Pedido buscarPorId(String id) throws Exception {
        Pedido p = repo.buscarPorId(id);
        
        if (p == null) {
            throw new PedidoNaoEncontradoException("Pedido com id " + id + " não encontrado");
        }
        
        return p;
    }

    public void deletar(String id) throws Exception {
        Pedido pedido = repo.buscarPorId(id);
        
        if (pedido == null) {
            throw new PedidoNaoEncontradoException("Pedido com ID " + id + " não encontrado.");
        }
        
        repo.deletar(id);
    }

    public Pedido atualizar(Pedido p) throws Exception {
        if (p.getId() == null) {
            throw new ValidacaoException("ID obrigatório");
        }

        Pedido pedidoExistente = repo.buscarPorId(p.getId());
        if (pedidoExistente == null) {
            throw new PedidoNaoEncontradoException("Pedido com ID " + p.getId() + " não encontrado.");
        }

        if (p.getItens() == null || p.getItens().isEmpty()) {
            throw new ValidacaoException("Itens do pedido são obrigatórios");
        }

        for (ItemPedido ip : p.getItens()) {
            Pizza pizzaNoBanco = pizzaService.buscarPorId(ip.getPizza().getId());
            if (pizzaNoBanco == null) {
                throw new ValidacaoException("Pizza não encontrada: " + ip.getPizza().getId());
            }

            if (ip.getId() == null) {
                ip.setId(UUID.randomUUID().toString());
            }

            ip.setPizza(pizzaNoBanco);
            ip.calcularSubtotal();
        }

        p.calcularSubtotal();
        repo.atualizar(p);

        return p;
    }
}