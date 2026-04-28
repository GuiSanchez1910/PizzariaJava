package service;

import java.util.List;
import java.util.UUID;

import exception.ClienteNaoEncontradoException;
import exception.ValidacaoException;
import models.Cliente;
import repository.ClienteRepository;

public class ClienteService {

    private ClienteRepository repo = new ClienteRepository();

    public Cliente criarCliente(Cliente c) throws Exception {

        if (c.getNome() == null || c.getNome().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }

        if (c.getTelefone() == null || c.getTelefone().isEmpty()) {
            throw new ValidacaoException("Telefone é obrigatório");
        }

        if (c.getEndereco() == null || c.getEndereco().isEmpty()) {
            throw new ValidacaoException("Endereço é obrigatório");
        }
        
        c.setId(UUID.randomUUID().toString());

        repo.salvar(c);

        return c;
    }

    public List<Cliente> listarClientes() throws Exception {
        return repo.listar();
    }

    public Cliente buscarPorId(String id) throws Exception {
        Cliente c = repo.buscarPorId(id);
        
        if (c == null) {
            throw new ClienteNaoEncontradoException("Cliente com id " + id + " não encontrado");
        }
        
        return c;
    }

    public void deletar(String id) throws Exception {
        Cliente cliente = repo.buscarPorId(id);
        
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + id + " não encontrado.");
        }
        
        repo.deletar(id);
    }

    public Cliente atualizar(Cliente c) throws Exception {

        if (c.getId() == null) {
            throw new ValidacaoException("ID obrigatório");
        }
        
        Cliente clienteExistente = repo.buscarPorId(c.getId());
        if (clienteExistente == null) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + c.getId() + " não encontrado.");
        }

        repo.atualizar(c);

        return c;
    }
}

