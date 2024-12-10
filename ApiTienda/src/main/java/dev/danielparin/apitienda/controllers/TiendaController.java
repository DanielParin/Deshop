package dev.danielparin.apitienda.controllers;

import dev.danielparin.apitienda.exceptions.*;
import dev.danielparin.apitienda.models.Cliente;
import dev.danielparin.apitienda.models.Pedido;
import dev.danielparin.apitienda.models.Producto;
import dev.danielparin.apitienda.models.enums.EstadoPedido;
import dev.danielparin.apitienda.services.ClienteService;
import dev.danielparin.apitienda.services.PedidoService;
import dev.danielparin.apitienda.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TiendaController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    // ---- CLIENTES ----

    //Obtener todos los clientes
    @GetMapping("/clientes/all")
    public List<Cliente> getAllClientes(){
        return clienteService.findAll();
    }

    // Buscar clientes por nombre
    @GetMapping("/clientes")
    public List<Cliente> buscarClientesPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        if (clientes.isEmpty()) {
            throw new ClienteNoEncontradoException(-1L);  // Usamos un ID ficticio para indicar que no se encontraron clientes
        }
        return clientes;
    }

    // Buscar cliente por ID
    @GetMapping("/clientes/{id}")
    public Cliente buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
    }

    //Buscar cliente por DNI
    @GetMapping("/clientes/dni")
    public Cliente buscarClientePorDni(@RequestParam String dni) {
        return clienteService.buscarPorDni(dni)
                .orElseThrow(() -> new ClienteNoEncontradoException(-1L));
    }

    // Crear un nuevo cliente
    @PostMapping("/clientes")
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new ValidationException("El nombre del cliente es obligatorio");
        }
        return clienteService.guardarCliente(cliente);
    }

    // Actualizar un cliente existente
    @PutMapping("/clientes/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
        cliente.setIdCliente(clienteExistente.getIdCliente());
        return clienteService.guardarCliente(cliente);
    }

    // Eliminar un cliente
    @DeleteMapping("/clientes/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
        clienteService.eliminarCliente(id);
    }

    // ---- PRODUCTOS ----

    //Obtener todos los clientes
    @GetMapping("/productos/all")
    public List<Producto> getAllProductos(){
        return productoService.findAll();
    }

    // Buscar productos por nombre
    @GetMapping("/productos")
    public List<Producto> buscarProductosPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        if (productos.isEmpty()) {
            throw new ProductoNoEncontradoException(-1L);  // Usamos un ID ficticio para indicar que no se encontraron productos
        }
        return productos;
    }

    // Buscar producto por ID
    @GetMapping("/productos/{id}")
    public Producto buscarProductoPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    // Crear un nuevo producto
    @PostMapping("/productos")
    public Producto crearProducto(@RequestBody Producto producto) {
        if (producto.getNombre() == null || producto.getPrecio() <= 0) {
            throw new ValidationException("El nombre y el precio del producto son obligatorios");
        }
        return productoService.guardarProducto(producto);
    }

    // Actualizar un producto existente
    @PutMapping("/productos/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoExistente = productoService.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
        producto.setIdProducto(productoExistente.getIdProducto());
        return productoService.guardarProducto(producto);
    }

    // Eliminar un producto
    @DeleteMapping("/productos/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
        productoService.eliminarProducto(id);
    }

    // ---- PEDIDOS ----

    //Obtener todos los clientes
    @GetMapping("/pedidos/all")
    public List<Pedido> getAllPedidos(){
        return pedidoService.findAll();
    }

    // Buscar pedido por ID
    @GetMapping("/pedidos/{id}")
    public Pedido buscarPedidoPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    // Obtener todos los pedidos con un estado específico
    @GetMapping("/pedidos")
    public List<Pedido> obtenerPedidosPorEstado(@RequestParam EstadoPedido estado) {
        List<Pedido> pedidos = pedidoService.buscarPorEstado(estado);
        if (pedidos.isEmpty()) {
            throw new PedidoNoEncontradoException(-1L);  // Usamos un ID ficticio para indicar que no se encontraron pedidos
        }
        return pedidos;
    }

    // Crear un nuevo pedido
    @PostMapping("/pedidos")
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        if (pedido.getIdCliente() == null) {
            throw new ValidationException("El ID del cliente es obligatorio para crear un pedido");
        }
        return pedidoService.guardarPedido(pedido);
    }

    // Actualizar un pedido existente
    @PutMapping("/pedidos/{id}")
    public Pedido actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido pedidoExistente = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
        pedido.setIdPedido(pedidoExistente.getIdPedido());
        return pedidoService.guardarPedido(pedido);
    }

    // Eliminar un pedido
    @DeleteMapping("/pedidos/{id}")
    public void eliminarPedido(@PathVariable Long id) {
        pedidoService.buscarPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
        pedidoService.eliminarPedido(id);
    }

    // Obtener todos los productos de un pedido
    @GetMapping("/pedidos/{id}/productos")
    public List<Producto> obtenerProductosDeUnPedido(@PathVariable Long id) {
        pedidoService.buscarPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
        return pedidoService.obtenerProductosDePedido(id);
    }
}
