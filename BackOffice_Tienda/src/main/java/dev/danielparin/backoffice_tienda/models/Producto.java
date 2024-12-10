package dev.danielparin.backoffice_tienda.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class Producto {

    private Long idProducto;
    private String nombre;
    private Double precio;
    private int cantidad;
    private String descripcion;
    private String imagen;


    public Producto(String nombre, Double precio, int cantidad, String descripcion, String imagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }
}
