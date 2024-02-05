package se.myhappyplants;

import se.myhappyplants.client.controller.LoginPaneController;
import se.myhappyplants.client.controller.StartClient;
import se.myhappyplants.server.StartServer;

import java.net.UnknownHostException;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TESTNG {

    @Test
    void loginButtonPressed() throws UnknownHostException, SQLException {

        StartServer.initiate();
        StartClient.initiate();
        LoginPaneController loginPaneController = new LoginPaneController();
        Assert.assertEquals(1, loginPaneController.loginButtonPressed());
    }
}
