package models;
import java.util.List;

public class Pedido {
    private String id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private String dataHora;
    private String status;
    private Double valorTotal;

    public Pedido() {
    }

    public Pedido(String id, Cliente cliente, List<ItemPedido> itens, String dataHora, String status, Double valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.itens = itens;
        this.dataHora = dataHora;
        this.status = status;
        this.valorTotal = valorTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void calcularSubtotal() {
        if (this.itens != null) {
            this.valorTotal = this.itens.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
        }
    }
}
