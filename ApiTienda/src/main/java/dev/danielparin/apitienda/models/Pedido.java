package dev.danielparin.apitienda.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.danielparin.apitienda.models.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "id_Cliente", nullable = false)
    private Cliente cliente;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fPedido")
    private LocalDate fPedido;

    private Double coste;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;


    public Long getIdCliente() {
        return cliente != null ? cliente.getIdCliente() : null;
    }
}
