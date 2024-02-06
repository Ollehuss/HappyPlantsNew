package Testing;

import org.junit.jupiter.api.*;


import se.myhappyplants.client.controller.LoginPaneController;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    private LoginPaneController lpc;

    public static void main(String[] args) {
        JunitTest testFile = new JunitTest();
        testFile.setup();
        testFile.testLoginSuccess();
        testFile.testLoginFailureIncorrectCredentials();
        testFile.testLoginFailureServerConnectionFailed();
    }

    @BeforeEach
    public void setup() {
        this.lpc = new LoginPaneController();
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
}
