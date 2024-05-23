package com.empresa.hito2;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.util.Optional;

public class MainController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> unitsColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField unitsField;

    @FXML
    private TextField priceField;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private Button applyFilterButton;

    private final String CONNECTION_STRING = "mongodb+srv://diegohuerta:1234@cluster0.len3cit.mongodb.net/";
    private final String DATABASE_NAME = "Productos_Deportivos";
    private final String COLLECTION_NAME = "Productos";


    @FXML
    void initialize() {
        // Configurar las celdas de la tabla
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        unitsColumn.setCellValueFactory(new PropertyValueFactory<>("unidades"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Inicializar la elección de filtros
        filterChoiceBox.getItems().clear(); // Limpia las opciones existentes para evitar duplicados
        filterChoiceBox.getItems().addAll("Precio alto", "Orden alfabético");

        // Cargar los datos desde la base de datos
        loadData();
    }

    @FXML
    void addProduct() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            String name = nameField.getText();
            String units = unitsField.getText();
            String price = priceField.getText();

            Document document = new Document("nombre", name)
                    .append("unidades", units)
                    .append("precio", price);
            collection.insertOne(document);

            loadData(); // Recargar los datos después de agregar un nuevo producto

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Producto insertado exitosamente!");

            // Limpiar los campos de texto después de agregar el producto
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    private void loadData() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            productTable.getItems().clear();
            FindIterable<Document> iterable = collection.find();
            for (Document doc : iterable) {
                String name = doc.getString("nombre");
                String units = doc.getString("unidades");
                String price = doc.getString("precio");
                Product product = new Product(name, units, price);
                productTable.getItems().add(product);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error al cargar datos", "Error al cargar datos desde la base de datos: " + e.getMessage());
        }
    }

    @FXML
    void updateProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Mostrar un diálogo para obtener los nuevos valores
            TextInputDialog nameDialog = new TextInputDialog(selectedProduct.getNombre());
            nameDialog.setTitle("Editar Producto");
            nameDialog.setHeaderText("Ingrese el nuevo nombre del producto:");
            nameDialog.setContentText("Nombre:");
            Optional<String> nameResult = nameDialog.showAndWait();
            String name = nameResult.orElse("");

            TextInputDialog unitsDialog = new TextInputDialog(selectedProduct.getUnidades());
            unitsDialog.setTitle("Editar Producto");
            unitsDialog.setHeaderText("Ingrese las nuevas unidades del producto:");
            unitsDialog.setContentText("Unidades:");
            Optional<String> unitsResult = unitsDialog.showAndWait();
            String units = unitsResult.orElse("");

            TextInputDialog priceDialog = new TextInputDialog(selectedProduct.getPrecio());
            priceDialog.setTitle("Editar Producto");
            priceDialog.setHeaderText("Ingrese el nuevo precio del producto:");
            priceDialog.setContentText("Precio:");
            Optional<String> priceResult = priceDialog.showAndWait();
            String price = priceResult.orElse("");

            // Crear un filtro para encontrar el documento del producto a actualizar
            Document filter = new Document("nombre", selectedProduct.getNombre());

            // Crear un documento con los nuevos valores
            Document update = new Document("$set", new Document("nombre", name)
                    .append("unidades", units)
                    .append("precio", price));

            try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                // Ejecutar la actualización en la base de datos
                UpdateResult updateResult = collection.updateOne(filter, update);

                // Verificar si la actualización fue exitosa
                if (updateResult.getModifiedCount() > 0) {
                    // Recargar los datos en la tabla
                    loadData();

                    // Mostrar un mensaje de éxito
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Producto actualizado exitosamente!");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Advertencia", "No se encontró ningún producto para actualizar.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar el producto: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se ha seleccionado ningún producto para actualizar.");
        }
    }

    @FXML
    void deleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Crear un filtro para encontrar el documento del producto a eliminar
            Document filter = new Document("nombre", selectedProduct.getNombre());

            try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

                // Ejecutar la eliminación del documento de la base de datos
                DeleteResult deleteResult = collection.deleteOne(filter);

                // Verificar si la eliminación fue exitosa
                if (deleteResult.getDeletedCount() > 0) {
                    // Recargar los datos en la tabla
                    loadData();

                    // Mostrar un mensaje de éxito
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "¡Producto eliminado exitosamente!");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Advertencia", "No se encontró ningún producto para eliminar.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar el producto: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se ha seleccionado ningún producto para eliminar.");
        }
    }

    @FXML
    void applyFilter() {
        String selectedFilter = filterChoiceBox.getValue();
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            productTable.getItems().clear(); // Limpiar la tabla antes de cargar los datos filtrados

            FindIterable<Document> filteredDocs;
            switch (selectedFilter) {
                case "Precio alto":
                    filteredDocs = collection.find().sort(new Document("unidades", -1)); // Orden descendente
                    break;
                case "Orden alfabético":
                    filteredDocs = collection.find().sort(new Document("nombre", 1)); // Orden ascendente
                    break;
                default:
                    filteredDocs = collection.find();
                    break;
            }
            loadFilteredData(filteredDocs);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al filtrar los productos: " + e.getMessage());
        }
    }

    private void loadFilteredData(FindIterable<Document> documents) {
        for (Document doc : documents) {
            String name = doc.getString("nombre");
            String units = doc.getString("unidades");
            String price = doc.getString("precio");
            Product product = new Product(name, units, price);
            productTable.getItems().add(product);
        }
    }

    private void clearFields() {
        nameField.clear();
        unitsField.clear();
        priceField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
