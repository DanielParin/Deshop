module dev.danielparin.backoffice_tienda {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires static lombok;
    requires java.logging;
    requires java.desktop;
    requires kernel;
    requires layout;

    opens dev.danielparin.backoffice_tienda.models to com.google.gson, javafx.base;
    opens dev.danielparin.backoffice_tienda.models.enums;
    opens dev.danielparin.backoffice_tienda to javafx.fxml;
    exports dev.danielparin.backoffice_tienda;
    exports dev.danielparin.backoffice_tienda.controllers;
    opens dev.danielparin.backoffice_tienda.controllers to javafx.fxml;
}