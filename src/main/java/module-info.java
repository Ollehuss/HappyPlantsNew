//Isn't used right now

module demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    //requires com.microsoft.sqlserver.jdbc;
    requires org.postgresql.jdbc;
    requires org.junit.jupiter.api;
    requires org.testng;

    opens  se.myhappyplants.client.controller to javafx.fxml;
    exports se.myhappyplants.client.view to javafx.fxml;
    exports se.myhappyplants.client.controller;
}