package com.empresa.hito2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        // Configurar la escena
        Scene scene = new Scene(root);
        // Configurar el escenario principal
        primaryStage.setScene(scene);
        primaryStage.setTitle("Inicio de sesión");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
