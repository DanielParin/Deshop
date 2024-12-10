package dev.danielparin.apitienda.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(PedidoProductoId.class)
public class PedidoProducto {

    @Id
    private Long idPedido;

    @Id
    private Long idProducto;
}
