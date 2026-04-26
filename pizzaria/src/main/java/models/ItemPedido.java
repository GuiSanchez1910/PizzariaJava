package models;

public class ItemPedido {
    private String id;
    private Pizza pizza;
    private Integer quantidade;
    private Double subtotal;

    public ItemPedido() {
    }

    public ItemPedido(String id, Pizza pizza, Integer quantidade, Double subtotal) {
        this.id = id;
        this.pizza = pizza;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public void calcularSubtotal() {
        if (this.pizza != null && this.pizza.getPrecoBase() != null && this.quantidade != null) {
            this.subtotal = this.pizza.getPrecoBase() * this.quantidade;
        }
    }
}
