package se.myhappyplants.client.controller;

import javafx.application.Platform;
import org.junit.jupiter.api.*;


import org.mockito.Mockito;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


import javafx.embed.swing.JFXPanel;

public class JunitTest {
    private LoginPaneController lpc;
    private MainPaneController mpc;

    private StartClient startClient;


    public static void main(String[] args) throws IOException, SQLException {
        JunitTest testFile = new JunitTest();
        testFile.setup();
        testFile.testLogOutButton();
      //  testFile.testLogInButton();
    }
    public void testLogOutButton() throws IOException, SQLException {
        setup();
        testWriteEmailToTextFile();
        testSetUserToNull();
        testSetRootToLoginPane();
       // testLogoutButtonPressed();
    }
    public void testLogInButton() throws IOException {
        setup();
     //   checkLoginResponseNotNull_LoginResponseIsNull_DisplaysFailedMessageBox();
      //  checkLoginResponseIsSuccess();
    }

    @BeforeEach
    public void setup() throws IOException {
        this.lpc = new LoginPaneController();
        this.mpc = new MainPaneController();
        this.startClient = new StartClient();
     //   startClient = mock(StartClient.class);
    }
    @Test
    public void testSetUserToNull() {
       String result = mpc.setUserToNull();
       assertEquals("User set to null", result);
    }

    @Test
    public void testWriteEmailToTextFile() throws IOException {
        String email = "test@test.com";
        LoggedInUser.getInstance().setUser(new User(email, "test", "test", true));
        String result = mpc.writeEmailToTextFile();
        assertEquals("Email written to file", result);
    }
 /*   @Test
    public void checkLoginResponseNotNull_LoginResponseIsNull_DisplaysFailedMessageBox() {
        // Call the method with null loginResponse
        String result = lpc.checkLoginResponseNotNull(null);

        // Check that the appropriate message is returned
        assertEquals("Login response is null", result);
    }

  */
  /*  @Test
    public void checkLoginResponseIsSuccess() {
        Message loginResponse = new Message(MessageType.login, new User("test", "test"));
        String result = lpc.checkLoginResponseIsSuccess(loginResponse);
        assertEquals("Login response is not successful", result);
    }
   */

    @Test
    public void checkLoginResponseNotNull_LoginResponseIsNull_ReturnsFalse() {
        // Call the method with null loginResponse
        boolean result = lpc.checkLoginResponseNotNull(null);

        // Check that false is returned
        assertFalse(result, "Expected false when loginResponse is null.");
    }
    //it need to start Server first
    @Test
    public void testMakeRequestApiResponseNotNull() {
        // Mock ServerConnection
        ServerConnection connection = Mockito.mock(ServerConnection.class);
        Message mockResponse = new Message(true); // Adapt as necessary

        // When makeRequest is called on the mock, return the mockResponse
        Mockito.when(connection.makeRequest(Mockito.any(Message.class))).thenReturn(mockResponse);

        // Create a mock or real Message object as needed for the request
        Message apiRequest = new Message(true); // Adapt constructor as necessary

        // Execute
        Message apiResponse = connection.makeRequest(apiRequest);

        // Verify
        assertNotNull(apiResponse, "apiResponse should not be null to indicate a successful connection.");
    }
    @Test
    public void testSetAvatar() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/image.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expectedAvatarURL = new File(pathToImg).toURI().toString();
        assertEquals(expectedAvatarURL, user.getAvatarURL(), "Avatar URL was not set correctly");
    }
    /*
    @Test
    public void testGetErrorMessage() {
        // Create an instance of Verified
        Verified verified = new Verified();

        // Call the getErrorMessage method
        String errorMessage = verified.getErrorMessage();

        // Check the returned value
        // Replace "expectedErrorMessage" with the expected error message
        String expectedErrorMessage = "expectedErrorMessage";
        assertEquals(expectedErrorMessage, errorMessage, "Error message was not as expected");
    }*/
    /*@Test
    public void testIsVerifiedRegistrationReturnsTrue() {
        // Arrange
        RegisterPaneController controller = new RegisterPaneController();

        // Act
        boolean result = controller.isVerifiedRegistration();

        // Assert
        assertTrue(result, "isVerifiedRegistration() should return true");
    }*/
    @Test
    public void testApiResponseNotNull() {
        // Setup - create a Message and ServerConnection instance
        Message apiRequest = new Message(MessageType.search, "testSearch");
        ServerConnection connection = ServerConnection.getClientConnection();

        // Execute
        Message apiResponse = connection.makeRequest(apiRequest);

        // Verify
        assertNotNull(apiResponse, "The apiResponse is null, indicating no connection.");
    }
    //it needs to start Server first
    @Test
    public void testApiResponseSuccess() {
        // Setup - create a Message and ServerConnection instance
        Message apiRequest = new Message(MessageType.search, "validSearchCriteria");
        ServerConnection connection = ServerConnection.getClientConnection();

        // Execute
        Message apiResponse = connection.makeRequest(apiRequest);

        // Verify that apiResponse is not null to avoid NullPointerException in the test
        assertNotNull(apiResponse, "The apiResponse is null, which is not expected here.");

        // Now verify apiResponse.isSuccess() is true
        assertTrue(apiResponse.isSuccess(), "The apiResponse did not successfully retrieve the data.");
    }


    @Test
    public void testSetRootToLoginPane() throws IOException {
        //System.out.println(System.getProperty("java.class.path"));
        // Initialize JavaFX platform
        new JFXPanel();

        // Arrange
        Platform.runLater(() -> {
            MainPaneController mpc = new MainPaneController();
            String expected = "Root set to loginPane";

            // Act
            String result = null;
            try {
                result = mpc.setRootToLoginPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Assert
            assertEquals(expected, result);
        });

        // Wait for JavaFX platform to finish processing
        WaitForAsyncUtils.waitForFxEvents();
    }
}
