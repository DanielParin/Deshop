package dev.danielparin.backoffice_tienda.controllers;

import dev.danielparin.backoffice_tienda.models.Cliente;
import dev.danielparin.backoffice_tienda.models.Pedido;
import dev.danielparin.backoffice_tienda.models.Producto;
import dev.danielparin.backoffice_tienda.models.enums.EstadoPedido;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.danielparin.backoffice_tienda.models.Cliente;
import dev.danielparin.backoffice_tienda.models.Pedido;
import dev.danielparin.backoffice_tienda.models.Producto;
import dev.danielparin.backoffice_tienda.models.enums.EstadoPedido;
import dev.danielparin.backoffice_tienda.repositories.ClienteRepository;
import dev.danielparin.backoffice_tienda.repositories.PedidoRepository;
import dev.danielparin.backoffice_tienda.repositories.ProductoRepository;
import dev.danielparin.backoffice_tienda.services.ApiService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InfoController {

    @FXML
    private Label numGanancias_Label;

    @FXML
    private Label numClientes_Label;

    @FXML
    private Label numPedidos_Label;

    @FXML
    private Label numProductos_Label;

    @FXML
    private PieChart pedidosChart;

    @FXML
    private BarChart<String, Number> clientesChart;

    private List<Pedido> pedidos;
    private List<Cliente> clientes;
    private List<Producto> productos;

    private final PedidoRepository pedidoRepository = new PedidoRepository(new ApiService("http://localhost:8080/api/"));
    private final ClienteRepository clienteRepository = new ClienteRepository(new ApiService("http://localhost:8080/api/"));
    private final ProductoRepository productoRepository = new ProductoRepository(new ApiService("http://localhost:8080/api/"));


    public void initialize() {

        try {
            cargarDatos();

        } catch (Exception e) {
            e.printStackTrace();
        }

        actualizarEstadisticas();

        configurarPieChart();

    }

    private void cargarDatos() throws Exception {
        // Reemplaza con tu lógica de carga de datos (backend o base de datos)
        pedidos = pedidoRepository.findAll();
        clientes = clienteRepository.findAll();
        productos = productoRepository.findAll();
    }

    private void actualizarEstadisticas() {
        // Ganancias: suma de pedidos pagados o enviados
        double totalGanancias = pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.PAGADO || p.getEstado() == EstadoPedido.ENVIADO)
                .mapToDouble(Pedido::getCoste)
                .sum();
        numGanancias_Label.setText(String.format("%.2f", totalGanancias));

        // Número de clientes
        numClientes_Label.setText(String.valueOf(clientes.size()));

        // Número de pedidos
        numPedidos_Label.setText(String.valueOf(pedidos.size()));

        // Número de productos
        numProductos_Label.setText(String.valueOf(productos.size()));
    }

    private void configurarPieChart() {
        // Distribución por estado de los pedidos
        Map<EstadoPedido, Long> estadoCounts = pedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getEstado, Collectors.counting()));

        pedidosChart.getData().clear();
        for (Map.Entry<EstadoPedido, Long> entry : estadoCounts.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey().name(), entry.getValue());
            pedidosChart.getData().add(slice);
        }
    }

}
