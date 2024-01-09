module com.cgvsu {
    requires javafx.controls;
    requires javafx.fxml;
    requires vecmath;
    requires java.desktop;
    requires org.junit.jupiter.api;


    opens com.cgvsu to javafx.fxml;
    exports com.cgvsu;
    exports com.cgvsu.table_models_cell;
    opens com.cgvsu.table_models_cell to javafx.fxml;
}