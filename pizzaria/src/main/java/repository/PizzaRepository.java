package repository;

import models.Pizza;
import config.ConnectionFactory;

import java.sql.*;
import java.util.*;

public class PizzaRepository {

    public void salvar(Pizza p) throws SQLException {

        String sql = "INSERT INTO pizza (id, sabor, tamanho, preco_base) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getId());
            stmt.setString(2, p.getSabor());
            stmt.setString(3, p.getTamanho());
            stmt.setDouble(4, p.getPrecoBase());

            stmt.executeUpdate();
        }
    }

    public Pizza buscarPorId(String id) throws SQLException {

        String sql = "SELECT * FROM pizza WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Pizza(
                        rs.getString("id"),
                        rs.getString("sabor"),
                        rs.getString("tamanho"),
                        rs.getDouble("preco_base"));
            }
        }

        return null;
    }

    public List<Pizza> listar() throws SQLException {

        List<Pizza> lista = new ArrayList<>();

        String sql = "SELECT * FROM pizza";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Pizza(
                        rs.getString("id"),
                        rs.getString("sabor"),
                        rs.getString("tamanho"),
                        rs.getDouble("preco_base")));
            }
        }

        return lista;
    }

    public void deletar(String id) throws SQLException {

        String sql = "DELETE FROM pizza WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Pizza p) throws SQLException {

        String sql = "UPDATE pizza SET sabor = ?, tamanho = ?, preco_base = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getSabor());
            stmt.setString(2, p.getTamanho());
            stmt.setDouble(3, p.getPrecoBase());
            stmt.setString(4, p.getId());

            stmt.executeUpdate();
        }
    }

}
