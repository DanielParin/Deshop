package dev.danielparin.backoffice_tienda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Shopr extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Shopr.class.getResource("/dev/danielparin/backoffice_tienda/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Shopr");
        stage.resizableProperty().setValue(Boolean.FALSE);

        String iconPath = "/dev/danielparin/backoffice_tienda/images/Shopr.png";
        Image icon = new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toString());
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}