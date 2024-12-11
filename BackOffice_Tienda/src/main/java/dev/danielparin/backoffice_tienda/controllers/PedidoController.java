package dev.danielparin.backoffice_tienda.controllers;


import dev.danielparin.backoffice_tienda.models.Pedido;
import dev.danielparin.backoffice_tienda.models.enums.EstadoPedido;
import dev.danielparin.backoffice_tienda.repositories.PedidoRepository;
import dev.danielparin.backoffice_tienda.repositories.ProductoRepository;
import dev.danielparin.backoffice_tienda.services.ApiService;
import dev.danielparin.backoffice_tienda.utils.PedidoFile;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class PedidoController {

    private final PedidoRepository pedidoRepository = new PedidoRepository(new ApiService("http://localhost:8080/api/"));

    @FXML
    private TableView<Pedido> pedidosTable;
    @FXML
    private TableColumn<Pedido, Long> idPedidoColumn;
    @FXML
    private TableColumn<Pedido, Long> idClienteColumn;
    @FXML
    private TableColumn<Pedido, String> fechaPedidoColumn;
    @FXML
    private TableColumn<Pedido, Double> costeColumn;
    @FXML
    private TableColumn<Pedido, String> estadoColumn;
    @FXML
    private TextField buscadorPedido;

    @FXML
    private TextField pedidoField, clienteField, fechaField, costeField, estadoField;
    @FXML
    private Button buscarButton, refreshButton, sendButton, downloadButton, deleteButton;

    private ObservableList<Pedido> pedidosData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Configuración de columnas
        idPedidoColumn.setCellValueFactory(new PropertyValueFactory<>("idPedido"));

        idClienteColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getCliente().getIdCliente()).asObject());

        fechaPedidoColumn.setCellValueFactory(new PropertyValueFactory<>("fPedido"));
        costeColumn.setCellValueFactory(new PropertyValueFactory<>("coste"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Acciones de los botones
        refreshButton.setOnAction(e -> refreshData());
        buscarButton.setOnAction(e -> buscarPedido());
        sendButton.setOnAction(e -> enviarPedido());
        downloadButton.setOnAction(e -> descargarPedido());
        deleteButton.setOnAction(e -> eliminarPedido());

        pedidosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesPedido(newValue)
        );

        // Refrescar los datos al inicializar
        refreshData();
    }

    @FXML
    private void refreshData() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll();
            System.out.println(pedidos);
            pedidosData.clear();
            pedidosData.addAll(pedidos);
            pedidosTable.setItems(pedidosData);
        } catch (Exception e) {
            mostrarAlerta("Error al cargar pedidos", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void buscarPedido() {
        String idTexto = buscadorPedido.getText().trim();
        if (idTexto.isEmpty()) {
            mostrarAlerta("Error de búsqueda", "Por favor, ingrese un ID de pedido válido.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Long idPedido = Long.parseLong(idTexto);
            Pedido pedido = pedidoRepository.buscarPedidoPorId(idPedido);
            if (pedido != null) {
                pedidosData.clear();
                pedidosData.add(pedido);
                pedidosTable.setItems(pedidosData);
            } else {
                mostrarAlerta("Pedido no encontrado", "No se encontró un pedido con el ID proporcionado.", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de búsqueda", "El ID debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error al buscar pedido", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void enviarPedido() {
        Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un pedido para marcarlo como enviado.", Alert.AlertType.WARNING);
            return;
        }

        if (pedidoSeleccionado.getEstado() == EstadoPedido.ENVIADO) {
            mostrarAlerta("Pedido ya enviado", "El pedido seleccionado ya está marcado como enviado.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            pedidoSeleccionado.setEstado(EstadoPedido.ENVIADO);
            pedidoRepository.actualizarPedido(pedidoSeleccionado.getIdPedido(), pedidoSeleccionado);
            refreshData();
        } catch (Exception e) {
            mostrarAlerta("Error al actualizar pedido", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarPedido() {
        Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un pedido para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            pedidoRepository.eliminarPedido(pedidoSeleccionado.getIdPedido());
            refreshData();
        } catch (Exception e) {
            mostrarAlerta("Error al eliminar pedido", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void mostrarDetallesPedido(Pedido pedido) {
        if (pedido != null) {
            pedidoField.setText(String.valueOf(pedido.getIdPedido()));
            clienteField.setText(String.valueOf(pedido.getCliente().getIdCliente()));
            fechaField.setText(pedido.getFPedido().toString());
            costeField.setText(String.valueOf(pedido.getCoste()));
            estadoField.setText(pedido.getEstado().toString());
        } else {
            pedidoField.clear();
            clienteField.clear();
            fechaField.clear();
            costeField.clear();
            estadoField.clear();
        }
    }

    @FXML
    private void descargarPedido() {
        Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Seleccione un pedido para descargar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Pedido");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                PedidoFile.generarPDF(file, pedidoSeleccionado, pedidoRepository.obtenerProductosDeUnPedido(pedidoSeleccionado.getIdPedido()));
                mostrarAlerta("Archivo guardado", "El archivo PDF se ha generado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error al generar PDF", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}