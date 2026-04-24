package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionFactory;
import models.Cliente;

public class ClienteRepository {

    public void salvar(Cliente c) throws SQLException {

        String sql = "INSERT INTO cliente (id, nome, telefone, endereco) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getId());
            stmt.setString(2, c.getNome());
            stmt.setString(3, c.getTelefone());
            stmt.setString(4, c.getEndereco());

            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorId(String id) throws SQLException {

        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("endereco")
                );
            }
        }

        return null;
    }

    public List<Cliente> listar() throws SQLException {

        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("endereco")
                ));
            }
        }

        return lista;
    }

    public void deletar(String id) throws SQLException {

        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Cliente c) throws SQLException {

        String sql = "UPDATE cliente SET nome = ?, telefone = ?, endereco = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getTelefone());
            stmt.setString(3, c.getEndereco());
            stmt.setString(4, c.getId());

            stmt.executeUpdate();
        }
    }
}
