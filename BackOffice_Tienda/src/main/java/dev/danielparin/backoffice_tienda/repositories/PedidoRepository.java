package dev.danielparin.backoffice_tienda.repositories;

import dev.danielparin.backoffice_tienda.models.Pedido;
import dev.danielparin.backoffice_tienda.models.Producto;
import dev.danielparin.backoffice_tienda.services.ApiService;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Arrays;

@AllArgsConstructor
public class PedidoRepository {
    private final ApiService apiService;


    public List<Pedido> findAll() throws Exception {
        return List.of(apiService.get("pedidos/all", Pedido[].class));
    }
    public Pedido buscarPedidoPorId(Long id) throws Exception {
        return apiService.get("pedidos/" + id, Pedido.class);
    }

    public List<Pedido> obtenerPedidosPorEstado(String estado) throws Exception {
        return Arrays.asList(apiService.get("pedidos?estado=" + estado, Pedido[].class));
    }

    public Pedido crearPedido(Pedido pedido) throws Exception {
        return apiService.post("pedidos", pedido, Pedido.class);
    }

    public Pedido actualizarPedido(Long id, Pedido pedido) throws Exception {
        return apiService.put("pedidos/" + id, pedido, Pedido.class);
    }

    public void eliminarPedido(Long id) throws Exception {
        apiService.delete("pedidos/" + id);
    }

    public List<Producto> obtenerProductosDeUnPedido(Long id) throws Exception {
        return Arrays.asList(apiService.get("pedidos/" + id + "/productos", Producto[].class));
    }
}

