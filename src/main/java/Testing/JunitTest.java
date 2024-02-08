package Testing;

import org.junit.jupiter.api.*;


import se.myhappyplants.client.controller.LoginPaneController;
import se.myhappyplants.client.controller.MainPaneController;
import se.myhappyplants.client.controller.SettingsTabPaneController;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    private LoginPaneController lpc;
    private MainPaneController mpc;


    public static void main(String[] args) throws IOException {
        JunitTest testFile = new JunitTest();
        testFile.setup();
      //  testFile.testLoginSuccess();
      //  testFile.testLoginFailureIncorrectCredentials();
      //  testFile.testLoginFailureServerConnectionFailed();
        testFile.testLogout();
    }

    @BeforeEach
    public void setup() {
        this.lpc = new LoginPaneController();
        this.mpc = new MainPaneController();
    }


    @Test
    public void testLoginSuccess() {
        String result = lpc.loginButtonPressed();
        assertEquals("Login failed", result);
    }

    @Test
    public void testLoginFailureIncorrectCredentials() {
        LoginPaneController objUnderTest = new LoginPaneController();
        String result = objUnderTest.loginButtonPressed();
        assertEquals("Login failed: Incorrect email or password", result);
    }

    @Test
    public void testLoginFailureServerConnectionFailed() {
        LoginPaneController objUnderTest = new LoginPaneController();
        // Mock ServerConnection to simulate a failure
        String result = objUnderTest.loginButtonPressed();
        assertEquals("Login failed: Connection to the server failed", result);
    }
    @Test
    public void testLogout() throws IOException {
        MainPaneController objUnderTest = new MainPaneController();
        String result = objUnderTest.logoutButtonPressed();
        assertEquals("You have been logged out successfully!", result);
    }
}
