package models;

public class Pizza {
    String id;
    String sabor;
    String tamanho;

    public Pizza() {}

    public Pizza(String id, String sabor, String tamanho) {
        this.id = id;
        this.sabor = sabor;
        this.tamanho = tamanho;
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
    
}
