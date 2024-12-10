package dev.danielparin.apitienda.repositories;

import dev.danielparin.apitienda.models.PedidoProducto;
import dev.danielparin.apitienda.models.PedidoProductoId;
import dev.danielparin.apitienda.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, PedidoProductoId> {

    @Query("SELECT prod FROM Producto prod " +
            "JOIN PedidoProducto pp ON prod.idProducto = pp.idProducto " +
            "WHERE pp.idPedido = :idPedido")
    List<Producto> obtenerProductosDePedido(@Param("idPedido") Long idPedido);
}
