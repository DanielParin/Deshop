package dev.danielparin.backoffice_tienda.controllers;

import dev.danielparin.backoffice_tienda.models.Cliente;
import dev.danielparin.backoffice_tienda.models.Producto;
import dev.danielparin.backoffice_tienda.repositories.ClienteRepository;
import dev.danielparin.backoffice_tienda.services.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;


public class ClientesController {

    private final ClienteRepository clienteRepository= new ClienteRepository(new ApiService("http://localhost:8080/api/"));

    @FXML
    private TextField buscadorDni;

    @FXML
    private Button searchButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private TableColumn<Cliente, String> dniColumn;

    @FXML
    private TableColumn<Cliente, String> nombreColumn;

    @FXML
    private TableColumn<Cliente, String> apellidosColumn;

    @FXML
    private TableColumn<Cliente, String> fechaColumn;

    @FXML
    private TableColumn<Cliente, String> correoColumn;

    @FXML
    private TableColumn<Cliente, String> telefonoColumn;

    @FXML
    private TableColumn<Cliente, String> direccionColumn;

    @FXML
    private TextField dniText;

    @FXML
    private TextField nombreText;

    @FXML
    private TextField apellidosText;

    @FXML
    private TextField fechaText;

    @FXML
    private TextField correoText;

    @FXML
    private TextField telefonoText;

    @FXML
    private TextField direccionText;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    private ObservableList<Cliente> clientesData = FXCollections.observableArrayList();


    @FXML
    private void initialize() {

        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fNacimiento"));
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        saveButton.setOnAction(event -> guardarCliente());
        deleteButton.setOnAction(event -> eliminarCliente());
        searchButton.setOnAction(event -> buscarCliente());
        refreshButton.setOnAction(event -> refreshData());

        tablaClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalle(newValue)
        );

        refreshData();

    }


    @FXML
    public void refreshData() {

        try {
            List<Cliente> clientesObtenidos = clienteRepository.findAll();
            clientesData.clear();
            clientesData.addAll(clientesObtenidos);
            tablaClientes.setItems(clientesData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void guardarCliente() {
        String dni = dniText.getText().trim();
        String nombre = nombreText.getText().trim();
        String apellidos = apellidosText.getText().trim();
        String fechaNacimiento = fechaText.getText().trim();
        String correo = correoText.getText().trim();
        String telefono = telefonoText.getText().trim();
        String direccion = direccionText.getText().trim();

        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty() || correo.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, complete todos los campos.");
            alert.showAndWait();
            return;
        }

        if (!fechaNacimiento.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("La fecha debe estar en el formato yyyy-MM-dd.");
            alert.showAndWait();
            return;
        }

        if (clienteSeleccionado != null) {
            clienteSeleccionado.setDni(dni);
            clienteSeleccionado.setNombre(nombre);
            clienteSeleccionado.setApellidos(apellidos);
            clienteSeleccionado.setFNacimiento(LocalDate.parse(fechaNacimiento));
            clienteSeleccionado.setCorreo(correo);
            clienteSeleccionado.setTelefono(telefono);
            clienteSeleccionado.setDireccion(direccion);

            try {
                clienteRepository.actualizarCliente(clienteSeleccionado.getIdCliente(), clienteSeleccionado);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cliente actualizado");
                alert.setHeaderText(null);
                alert.setContentText("El cliente ha sido actualizado con éxito.");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hubo un error al actualizar el cliente.");
                alert.showAndWait();
            }
        } else {
            Cliente nuevoCliente = new Cliente(dni, nombre, apellidos, LocalDate.parse(fechaNacimiento), correo, telefono, direccion);

            try {
                clienteRepository.crearCliente(nuevoCliente);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cliente guardado");
                alert.setHeaderText(null);
                alert.setContentText("El cliente ha sido guardado con éxito.");
                alert.showAndWait();

                tablaClientes.getItems().add(nuevoCliente);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hubo un error al guardar el cliente.");
                alert.showAndWait();
            }
        }

        refreshData();
    }

    @FXML
    public void eliminarCliente() {
        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un cliente para eliminar.");
            alert.showAndWait();
            return;
        }

        try {
            clienteRepository.eliminarCliente(clienteSeleccionado.getIdCliente());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cliente eliminado");
            alert.setHeaderText(null);
            alert.setContentText("El cliente ha sido eliminado con éxito.");
            alert.showAndWait();

            tablaClientes.getItems().remove(clienteSeleccionado);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hubo un error al eliminar el cliente.");
            alert.showAndWait();
        }
    }

    @FXML
    private void mostrarDetalle(Cliente newValue) {

        if (newValue != null) {
            dniText.setText(newValue.getDni());
            nombreText.setText(newValue.getNombre());
            apellidosText.setText(newValue.getApellidos());
            fechaText.setText(newValue.getFNacimiento().toString());
            correoText.setText(newValue.getCorreo());
            telefonoText.setText(newValue.getTelefono());
            direccionText.setText(newValue.getDireccion());
        }
    }

    @FXML
    private void buscarCliente() {
        String dniBusqueda = buscadorDni.getText().trim();

        if (dniBusqueda.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de búsqueda");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingrese un DNI para buscar.");
            alert.showAndWait();
        } else {

            try {
                Cliente clientesEncontrados = clienteRepository.buscarClientePorDni(dniBusqueda);
                if(clientesEncontrados != null) {
                    clientesData = FXCollections.observableArrayList(clientesEncontrados);
                    tablaClientes.setItems(clientesData);
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Búsqueda sin resultados");
                    alert.setHeaderText(null);
                    alert.setContentText("No se encontró un cliente con el DNI ingresado.");
                    alert.showAndWait();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
