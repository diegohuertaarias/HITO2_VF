module com.empresa.hito2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mongodb.driver;
    requires org.mongodb.bson;

    opens com.empresa.hito2 to javafx.fxml;
    exports com.empresa.hito2;
}