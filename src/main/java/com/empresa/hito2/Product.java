package com.empresa.hito2;

public class Product {
    private String nombre;
    private String unidades;
    private String precio;

    public Product(String nombre, String unidades, String precio) {
        this.nombre = nombre;
        this.unidades = unidades;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
