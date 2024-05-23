package com.empresa.hito2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final String DATABASE_NAME = "usuarios";
    private final String COLLECTION_NAME = "usuarios";

    @FXML
    void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://diegohuerta:1234@cluster0.len3cit.mongodb.net/")) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("username", username).append("password", password);
            Document user = collection.find(query).first();

            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");

                // Redirigir al usuario a main.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Parent root = loader.load();
                MainController mainController = loader.getController();

                Stage stage = (Stage) usernameField.getScene().getWindow(); // Obtener la ventana actual
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username and password cannot be empty.");
            return;
        }

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://diegohuerta:1234@cluster0.len3cit.mongodb.net/")) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("username", username);
            Document existingUser = collection.find(query).first();

            if (existingUser != null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username already exists.");
            } else {
                Document newUser = new Document("username", username).append("password", password);
                collection.insertOne(newUser);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
