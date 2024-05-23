module com.empresa.hito2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mongo.java.driver;

    opens com.empresa.hito2 to javafx.fxml;
    exports com.empresa.hito2;
}