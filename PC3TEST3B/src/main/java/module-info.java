module com.example.pc3test3b {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires javafx.media;
    requires com.google.protobuf;
    requires junit;
    requires org.junit.jupiter.api;


    opens com.example.pc3test3b to javafx.fxml;
    opens com.example.pc3test3b.model to javafx.base;

    exports com.example.pc3test3b;
    exports com.example.pc3test3b.model;
    exports com.example.pc3test3b.controller to junit;
}
