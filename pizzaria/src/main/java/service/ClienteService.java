package service;

import java.util.List;
import java.util.UUID;

import models.Cliente;
import repository.ClienteRepository;

public class ClienteService {

    private ClienteRepository repo = new ClienteRepository();

    public Cliente criarCliente(Cliente c) throws Exception {

        if (c.getNome() == null || c.getNome().isEmpty()) {
            throw new Exception("Nome é obrigatório");
        }

        if (c.getTelefone() == null || c.getTelefone().isEmpty()) {
            throw new Exception("Telefone é obrigatório");
        }

        if (c.getEndereco() == null || c.getEndereco().isEmpty()) {
            throw new Exception("Endereço é obrigatório");
        }
        
        c.setId(UUID.randomUUID().toString());

        repo.salvar(c);

        return c;
    }

    public List<Cliente> listarClientes() throws Exception {
        return repo.listar();
    }

    public Cliente buscarPorId(String id) throws Exception {
        return repo.buscarPorId(id);
    }

    public void deletar(String id) throws Exception {
        repo.deletar(id);
    }

    public Cliente atualizar(Cliente c) throws Exception {

        if (c.getId() == null) {
            throw new Exception("ID obrigatório");
        }

        repo.atualizar(c);

        return c;
    }
}
