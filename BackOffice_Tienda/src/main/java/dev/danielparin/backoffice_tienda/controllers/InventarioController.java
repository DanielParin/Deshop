package dev.danielparin.backoffice_tienda.controllers;

import dev.danielparin.backoffice_tienda.models.Producto;
import dev.danielparin.backoffice_tienda.repositories.ProductoRepository;
import dev.danielparin.backoffice_tienda.services.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class InventarioController {

    private final ProductoRepository productoRepository = new ProductoRepository(new ApiService("http://localhost:8080/api/"));


    @FXML
    private TextField buscadorCliente;

    @FXML
    private Button SearchButton;

    @FXML
    private Button RefreshButton;

    @FXML
    private TableView<Producto> productosTable;

    @FXML
    private TableColumn<Producto, Long> idProdColumn;

    @FXML
    private TableColumn<Producto, String> NombreColumn;

    @FXML
    private TableColumn<Producto, Double> PrecioColumn;

    @FXML
    private TableColumn<Producto, Integer> CantidadColumn;

    @FXML
    private TableColumn<Producto, String> DescripcionClumn;

    @FXML
    private TableColumn<Producto, String> ImagenColumn;

    @FXML
    private TextField NombreText;

    @FXML
    private TextField PrecioText;

    @FXML
    private TextField CantidadText;

    @FXML
    private TextField DescripcionText;

    @FXML
    private TextField imagenText;

    @FXML
    private ImageView imagenView;

    @FXML
    private Button saveButton;

    @FXML
    private Button DeleteButton;

    private ObservableList<Producto> productosData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idProdColumn.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        NombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        PrecioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        CantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        DescripcionClumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        ImagenColumn.setCellValueFactory(new PropertyValueFactory<>("imagen"));

        refreshData();

        RefreshButton.setOnAction(e -> refreshData());
        SearchButton.setOnAction(event -> buscarProducto());
        saveButton.setOnAction(event -> guardarProducto());
        DeleteButton.setOnAction(event -> eliminarProducto());


        productosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesProducto(newValue)
        );
    }

    @FXML
    private void refreshData() {


        try {
            List<Producto> productos = productoRepository.findAll();
            productosData.clear();
            productosData.addAll(productos);
            productosTable.setItems(productosData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        productosTable.setItems(productosData);
    }

    @FXML
    public void buscarProducto() {
        String nombreBuscado = buscadorCliente.getText().trim();

        if (nombreBuscado.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de búsqueda");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingrese un nombre para buscar.");
            alert.showAndWait();
        } else {
            try {
                // Supongamos que buscarProductosPorNombre devuelve una lista normal (como ArrayList)
                List<Producto> listaProductos = productoRepository.buscarProductosPorNombre(nombreBuscado);

                // Convertir la lista a ObservableList
                productosData = FXCollections.observableArrayList(listaProductos);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Establecer los productos en la TableView
            productosTable.setItems(productosData);
        }
    }

    @FXML
    private void mostrarDetallesProducto(Producto producto) {
        if (producto != null) {
            // Asignar los datos de la fila seleccionada a los controles
            NombreText.setText(producto.getNombre());
            DescripcionText.setText(producto.getDescripcion());
            CantidadText.setText(String.valueOf(producto.getCantidad()));
            PrecioText.setText(String.valueOf(producto.getPrecio()));
            imagenText.setText(producto.getImagen());  // Aquí se coloca la URL de la imagen

            // Cargar la imagen desde la URL si está disponible
            cargarImagenDesdeURL(producto.getImagen());
        }
    }

    @FXML
    public void guardarProducto() {

        String nombre = NombreText.getText().trim();
        String descripcion = DescripcionText.getText().trim();
        String precioText = PrecioText.getText().trim();
        String stockText = CantidadText.getText().trim();
        String imagenUrl = imagenText.getText().trim();

        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioText.isEmpty() || stockText.isEmpty() || imagenUrl.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, complete todos los campos.");
            alert.showAndWait();
            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioText);
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("El precio y la cantidad deben ser un número válido.");
            alert.showAndWait();
            return;
        }

        if (productoSeleccionado != null) {

            productoSeleccionado.setNombre(nombre);
            productoSeleccionado.setDescripcion(descripcion);
            productoSeleccionado.setPrecio(precio);
            productoSeleccionado.setCantidad(stock);
            productoSeleccionado.setImagen(imagenUrl);

            try {
                productoRepository.actualizarProducto(productoSeleccionado.getIdProducto(), new Producto(nombre,precio,stock,descripcion,imagenUrl));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Producto actualizado");
                alert.setHeaderText(null);
                alert.setContentText("El producto ha sido actualizado con éxito.");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hubo un error al actualizar el producto.");
                alert.showAndWait();
            }
        } else {
            // Si no hay un producto seleccionado, significa que es un nuevo producto
            Producto nuevoProducto = new Producto(nombre, precio, stock,descripcion, imagenUrl);

            try {
                // Llamar al repositorio o servicio para guardar el nuevo producto
                productoRepository.crearProducto(nuevoProducto);  // Asumiendo que tienes un método en el repo para guardar
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Producto guardado");
                alert.setHeaderText(null);
                alert.setContentText("El producto ha sido guardado con éxito.");
                alert.showAndWait();

                // Agregar el producto a la tabla para que se actualice la vista
                productosTable.getItems().add(nuevoProducto);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hubo un error al guardar el producto.");
                alert.showAndWait();
            }
        }

        refreshData();
    }

    @FXML
    public void eliminarProducto() {
        Producto productoSeleccionado = productosTable.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            // Si no hay un producto seleccionado, mostrar un alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un producto para eliminar.");
            alert.showAndWait();
        } else {
            // Si hay un producto seleccionado, eliminarlo
            try {
                // Llamar al repositorio o servicio para eliminar el producto
                productoRepository.eliminarProducto(productoSeleccionado.getIdProducto());  // Asumiendo que tienes un método en el repo para eliminar
                productosTable.getItems().remove(productoSeleccionado);  // Eliminarlo de la tabla
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Producto eliminado");
                alert.setHeaderText(null);
                alert.setContentText("El producto ha sido eliminado con éxito.");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hubo un error al eliminar el producto.");
                alert.showAndWait();
            }
        }
    }

    private void cargarImagenDesdeURL(String imagenUrl) {
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            try {
                // Cargar la imagen desde la URL
                Image imagen = new Image(imagenUrl);  // Cargamos la imagen desde la URL
                imagenView.setImage(imagen);  // Asignamos la imagen al ImageView
            } catch (Exception e) {
                // Si hay un error con la URL, mostramos una imagen predeterminada o un aviso
                System.out.println("Error al cargar la imagen: " + e.getMessage());
                imagenView.setImage(null);  // O colocar una imagen predeterminada
            }
        } else {
            // Si la URL está vacía o no existe, se puede mostrar una imagen predeterminada
            imagenView.setImage(null);  // O una imagen predeterminada si lo prefieres
        }
    }
}
