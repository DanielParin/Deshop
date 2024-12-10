package dev.danielparin.apitienda.repositories;

import dev.danielparin.apitienda.models.Pedido;
import dev.danielparin.apitienda.models.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.idPedido = :id")
    Pedido buscarPorId(@Param("id") Long id);

    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    List<Pedido> buscarPorEstado(@Param("estado") EstadoPedido estado);
}
