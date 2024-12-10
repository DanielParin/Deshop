package dev.danielparin.apitienda.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProductoId implements java.io.Serializable {
    private Long idPedido;
    private Long idProducto;
}
