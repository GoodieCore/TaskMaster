module org.example.taskmaster {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.taskmaster to javafx.fxml;
    exports org.example.taskmaster;
    exports projectuser;
    opens projectuser to javafx.fxml;
}