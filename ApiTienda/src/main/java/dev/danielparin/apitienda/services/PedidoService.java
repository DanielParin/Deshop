package dev.danielparin.apitienda.services;

import dev.danielparin.apitienda.models.Pedido;
import dev.danielparin.apitienda.models.PedidoProducto;
import dev.danielparin.apitienda.models.Producto;
import dev.danielparin.apitienda.models.enums.EstadoPedido;
import dev.danielparin.apitienda.repositories.PedidoProductoRepository;
import dev.danielparin.apitienda.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoProductoRepository pedidoProductoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        return pedidoRepository.buscarPorEstado(estado);
    }

    public Pedido guardarPedidoConProductos(Pedido pedido, List<Producto> productos) {

        Pedido pedidoGuardado = pedidoRepository.save(pedido);


        List<PedidoProducto> pedidoProductos = new ArrayList<>();

        for (Producto producto : productos) {

            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setIdPedido(pedidoGuardado.getIdPedido());
            pedidoProducto.setIdProducto(producto.getIdProducto());
            pedidoProductos.add(pedidoProducto);
        }

        pedidoProductoRepository.saveAll(pedidoProductos);

        return pedidoGuardado;
    }


    public Pedido actualizarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    public List<Producto> obtenerProductosDePedido(Long idPedido) {
        return pedidoProductoRepository.obtenerProductosDePedido(idPedido);
    }
}
