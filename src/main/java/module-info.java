//Isn't used right now

module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires com.microsoft.sqlserver.jdbc;

    opens  se.myhappyplants.client.controller to javafx.fxml;
    exports se.myhappyplants.client.controller;
}