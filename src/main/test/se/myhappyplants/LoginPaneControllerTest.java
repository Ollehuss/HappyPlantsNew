package se.myhappyplants;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.myhappyplants.client.controller.LoginPaneController;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginPaneControllerTest {

    //KRAV-ID: ANV01F
    //TEST-ID: T01
    @Test
    public void testCheckLoginResponseNotNull_LoginResponseIsNull_DisplaysFailedMessageBox() {
        // Call the method with null loginResponse
        LoginPaneController loginPaneController = new LoginPaneController();
        boolean result = loginPaneController.checkLoginResponseNotNull(null);

        // Check that the appropriate message is returned
        assertEquals(false, result);
    }

    //KRAV-ID: ANV01F
    //TEST-ID: T02
    @Test
    public void testCheckLoginResponseNotNull_LoginResponseIsNull_ReturnsFalse() {
        // Call the method with null loginResponse
        LoginPaneController loginPaneController = new LoginPaneController();
        boolean result = loginPaneController.checkLoginResponseNotNull(null);

        // Check that false is returned
        assertFalse(result, "Expected false when loginResponse is null.");
    }

    //KRAV-ID: ANV01F
    //TEST-ID: T03
//    @Test
//    public void testCheckLoginResponseIsSuccess() {
//        new JFXPanel();
//        LoginPaneController loginPaneController = new LoginPaneController();
//        Message loginResponse = new Message(MessageType.login, new User("test", "test"));
//        String result = loginPaneController.checkLoginResponseIsSuccess(loginResponse);
//        assertEquals("Login response is not successful", result);
//    }


    @Test
    public void testLoginButtonPressed_Invalid() {
        // Mock ServerConnection
        Message loginMessage = new Message(MessageType.login, new User("test@test.com", "testpassword"));

        ServerConnection connection = Mockito.mock(ServerConnection.class);

        Message loginResponse = connection.makeRequest(loginMessage);
        LoginPaneController controller = new LoginPaneController();
        assertFalse(controller.checkLoginResponseNotNull(loginResponse));
    }
}
