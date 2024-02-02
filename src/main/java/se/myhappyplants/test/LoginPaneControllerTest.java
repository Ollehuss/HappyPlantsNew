package se.myhappyplants.test;

import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.LoginPaneController;
import se.myhappyplants.client.controller.StartClient;
import se.myhappyplants.server.StartServer;

import java.net.UnknownHostException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginPaneControllerTest {

    @Test
    void loginButtonPressed() throws UnknownHostException, SQLException {
        System.out.println("skvätt");

        StartServer.initiate();
        StartClient.initiate();
        LoginPaneController loginPaneController = new LoginPaneController();
        assertEquals(1, loginPaneController.loginButtonPressed());
    }
}