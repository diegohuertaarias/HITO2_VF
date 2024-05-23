import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> productsCollection;

    public App() {
        // Conectar a MongoDB Atlas
        client = MongoClients.create("mongodb+srv://<username>:<password>@cluster0.len3cit.mongodb.net/");
        database = client.getDatabase("myDatabase");

        // Obtener las colecciones de usuarios y productos
        usersCollection = database.getCollection("users");
        productsCollection = database.getCollection("products");
    }

    public void createUser(String username, String password) {
        // Crear un nuevo usuario
        Document user = new Document("username", username)
                .append("password", password);
        usersCollection.insertOne(user);
    }

    public boolean authenticateUser(String username, String password) {
        // Autenticar un usuario
        Document user = usersCollection.find(new Document("username", username)).first();
        return user != null && user.getString("password").equals(password);
    }

    public void createProduct(String name, String description, double price) {
        // Crear un nuevo producto
        Document product = new Document("name", name)
                .append("description", description)
                .append("price", price);
        productsCollection.insertOne(product);
    }

    public void updateProduct(String name, String description, double price) {
        // Actualizar un producto existente
        Document product = new Document("name", name)
                .append("description", description)
                .append("price", price);
        productsCollection.updateOne(new Document("name", name), new Document("$set", product));
    }

    public void deleteProduct(String name) {
        // Eliminar un producto
        productsCollection.deleteOne(new Document("name", name));
    }
}
