package models;

public class Pizza {
    private String id;
    private String sabor;
    private String tamanho;
    private Double precoBase;

    public Pizza() {
    }

    public Pizza(String id, String sabor, String tamanho, Double precoBase) {
        this.id = id;
        this.sabor = sabor;
        this.tamanho = tamanho;
        this.precoBase = precoBase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(Double precoBase) {
        this.precoBase = precoBase;
    }
}
