package dev.danielparin.backoffice_tienda.models;

import dev.danielparin.backoffice_tienda.models.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    private Long idPedido;
    private Cliente cliente;
    private LocalDate fPedido;
    private Double coste;
    private EstadoPedido estado;

}
