package repository;

import models.ItemPedido;
import models.Pedido;
import config.ConnectionFactory;

import java.sql.*;
import java.util.*;

public class PedidoRepository {

    private ClienteRepository clienteRepo = new ClienteRepository();
    private ItemPedidoRepository itemRepo = new ItemPedidoRepository();

    public void salvar(Pedido p) throws SQLException {

        String sql = "INSERT INTO pedido (id, cliente_id, valor_total) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getId());
            stmt.setString(2, p.getCliente().getId());
            stmt.setDouble(3, p.getValorTotal());

            stmt.executeUpdate();
        }
    }

    public Pedido buscarPorId(String id) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getString("id"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setCliente(clienteRepo.buscarPorId(rs.getString("cliente_id")));
                p.setItens(itemRepo.buscarPorPedido(id));
                return p;
            }
        }
        return null;
    }

    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getString("id"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setDataHora(rs.getString("data_hora"));
                p.setStatus(rs.getString("status"));

                p.setCliente(clienteRepo.buscarPorId(rs.getString("cliente_id")));

                p.setItens(itemRepo.buscarPorPedido(p.getId()));

                pedidos.add(p);
            }
        }
        return pedidos;
    }

    // FEITO
    public void deletar(String id) throws SQLException {

        String sql = "DELETE FROM pedido WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Pedido p) throws SQLException {
        
        String sqlPedido = "UPDATE pedido SET cliente_id = ?, valor_total = ?, status = ?, data_hora = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                stmt.setString(1, p.getCliente().getId());
                stmt.setDouble(2, p.getValorTotal());
                stmt.setString(3, p.getStatus());
                stmt.setString(4, p.getDataHora());
                stmt.setString(5, p.getId());
                stmt.executeUpdate();
            }
            
            String sqlDeleteItens = "DELETE FROM item_pedido WHERE pedido_id = ?";
            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDeleteItens)) {
                stmtDel.setString(1, p.getId());
                stmtDel.executeUpdate();
            }
            
            String sqlInsertItem = "INSERT INTO item_pedido (id, pizza_id, quantidade, subtotal, pedido_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlInsertItem)) {
                for (ItemPedido ip : p.getItens()) {
                    stmtItem.setString(1, ip.getId());
                    stmtItem.setString(2, ip.getPizza().getId());
                    stmtItem.setInt(3, ip.getQuantidade());
                    stmtItem.setDouble(4, ip.getSubtotal());
                    stmtItem.setString(5, p.getId());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            throw e;
        }
    }
}
