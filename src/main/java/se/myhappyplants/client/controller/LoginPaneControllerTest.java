package se.myhappyplants.client.controller;

import static org.junit.jupiter.api.Assertions.*;

class LoginPaneControllerTest {

    @org.junit.jupiter.api.Test
    void loginButtonPressed() {
        LoginPaneController loginPaneController = new LoginPaneController();
        assertEquals(1, loginPaneController.loginButtonPressed());
    }
}