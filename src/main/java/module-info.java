module com.latidude99 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.latidude99 to javafx.fxml;
    exports com.latidude99;
}