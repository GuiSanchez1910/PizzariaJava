package repository;

import models.ItemPedido;
import models.Pizza;
import config.ConnectionFactory;

import java.sql.*;
import java.util.*;

public class ItemPedidoRepository {

    public void salvar(ItemPedido ip, String pedidoId) throws SQLException {

        String sql = "INSERT INTO item_pedido (id, pizza_id, quantidade, subtotal, pedido_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ip.getId());
            stmt.setString(2, ip.getPizza().getId());
            stmt.setInt(3, ip.getQuantidade());
            stmt.setDouble(4, ip.getSubtotal());
            stmt.setString(5, pedidoId);

            stmt.executeUpdate();
        }
    }

    public ItemPedido buscarPorId(String id) throws SQLException {

        String sql = "SELECT * FROM item_pedido WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PizzaRepository pizzaRepo = new PizzaRepository();
                Pizza pizza = pizzaRepo.buscarPorId(rs.getString("pizza_id"));
                return new ItemPedido(
                        rs.getString("id"),
                        pizza,
                        rs.getInt("quantidade"),
                        rs.getDouble("subtotal"));
            }
        }

        return null;
    }

    public List<ItemPedido> buscarPorPedido(String pedidoId) throws SQLException {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido WHERE pedido_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pedidoId);
            ResultSet rs = stmt.executeQuery();
            PizzaRepository pizzaRepo = new PizzaRepository();
            while (rs.next()) {
                Pizza pizza = pizzaRepo.buscarPorId(rs.getString("pizza_id"));
                itens.add(new ItemPedido(rs.getString("id"), pizza, rs.getInt("quantidade"), rs.getDouble("subtotal")));
            }
        }
        return itens;
    }

    public List<ItemPedido> listar() throws SQLException {

        List<ItemPedido> lista = new ArrayList<>();

        String sql = "SELECT * FROM item_pedido";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            PizzaRepository pizzaRepo = new PizzaRepository();

            while (rs.next()) {
                Pizza pizza = pizzaRepo.buscarPorId(rs.getString("pizza_id"));
                lista.add(new ItemPedido(
                        rs.getString("id"),
                        pizza,
                        rs.getInt("quantidade"),
                        rs.getDouble("subtotal")));
            }
        }

        return lista;
    }

    public void deletar(String id) throws SQLException {

        String sql = "DELETE FROM item_pedido WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizar(ItemPedido ip) throws SQLException {

        String sql = "UPDATE item_pedido SET pizza_id = ?, quantidade = ?, subtotal = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ip.getPizza().getId());
            stmt.setInt(2, ip.getQuantidade());
            stmt.setDouble(3, ip.getSubtotal());
            stmt.setString(4, ip.getId());

            stmt.executeUpdate();
        }
    }
}
