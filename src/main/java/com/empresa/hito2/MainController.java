package com.empresa.hito2;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.bson.Document;

public class MainController {
    @FXML
    private TextField inputField;
    @FXML
    private ListView<String> listView;

    private DatabaseService dbService = new DatabaseService();

    @FXML
    public void initialize() {
        dbService.connect("your_connection_string", "your_db_name", "your_collection_name");
    }

    @FXML
    public void handleCreate() {
        String input = inputField.getText();
        if (input == null || input.trim().isEmpty()) {
            // Mostrar mensaje de error al usuario
            return;
        }
        Document doc = new Document("name", input);
        try {
            dbService.insert(doc);
            listView.getItems().add(input);
            inputField.clear();
        } catch (Exception e) {
            // Mostrar mensaje de error al usuario
        }
    }

    @FXML
    public void handleDelete() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Implementar eliminación en la base de datos
            listView.getItems().remove(selectedItem);
        }
    }
}