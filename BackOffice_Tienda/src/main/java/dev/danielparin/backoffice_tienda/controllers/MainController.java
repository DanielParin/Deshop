package dev.danielparin.backoffice_tienda.controllers;

import dev.danielparin.backoffice_tienda.utils.Paths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Objects;


public class MainController {

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    @FXML
    private Button informacionButton;

    @FXML
    private Button inventarioButton;

    @FXML
    private Button clientesButton;

    @FXML
    private Button pedidosButton;

    @FXML
    private AnchorPane panelView;

    @FXML
    private ImageView imageShop;


    @FXML
    public void initialize() {

        String imagePath = Paths.IMG_PATH+"Shopr.png";
        Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toString());
        imageShop.setImage(image);

        informacionButton.setOnAction(e -> loadView("info-view.fxml"));
        inventarioButton.setOnAction(e -> loadView("inventario-view.fxml"));
        clientesButton.setOnAction(e -> loadView("clientes-view.fxml"));
        pedidosButton.setOnAction(e -> loadView("pedido-view.fxml"));
    }

    public void loadView(String view) {
        String path = Paths.FXML_PATH + view;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane newView = loader.load();

            // Asegurarse de que el controlador de la vista se inicialice correctamente
            Object controller = loader.getController();
            if (controller instanceof InventarioController) {
                ((InventarioController) controller).initialize();
            }

            panelView.getChildren().clear();
            panelView.getChildren().add(newView);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al cargar el archivo fxml", e);
        }
    }


}