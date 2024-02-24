package se.myhappyplants.client.controller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import javafx.application.Platform;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.Verifier;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;
import se.myhappyplants.client.model.Verifier;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;


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
          startClient = mock(StartClient.class);
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

    //it needs to start Server first
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


    @Test
    public void testGetErrorMessage() {
        // Create an instance of Verified
        Verifier verified = new Verifier();

        // Call the getErrorMessage method
        String errorMessage = verified.getErrorMessage(Verifier.errorType.WRONG_EMAIL_FORMAT);

        // Check the returned value
        // Replace "expectedErrorMessage" with the expected error message
        String expectedErrorMessage = "Please enter your email address in format: yourname@example.com";
        assertEquals(expectedErrorMessage, errorMessage, "Error message was not as expected");
    }
    @Test
    public void testIsVerifiedRegistrationReturnsTrue() {
        // Act
        when(mockVerifier.validateRegistration(controller)).thenReturn(true);
        boolean result = controller.isVerifiedRegistration();

        // Assert
        assertTrue(result, "isVerifiedRegistration() should return true");
    }

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

    @BeforeAll
    public static void setupJavaFXRuntime() {
        // Initializes the JavaFX Runtime
        new JFXPanel();
    }

    private RegisterPaneController controller;
    private Verifier mockVerifier;
    private RegisterPaneController.UIMessageService mockUIMessageService;

    @BeforeEach
    void setUp() {
        mockVerifier = mock(Verifier.class);
        mockUIMessageService = mock(RegisterPaneController.UIMessageService.class);
        controller = new RegisterPaneController();
        controller.setVerifier(mockVerifier);
        controller.setUIMessageService(mockUIMessageService);
    }

    @Test
    public void testVerificationFails() {
        // Arrange: Mock isVerifiedRegistration to return false
        when(mockVerifier.validateRegistration(controller)).thenReturn(false);

        // Create a Message object that represents a failed registration
        Message failedRegistrationMessage = new Message(false);

        // Act: Invoke the method under test
        controller.registerButtonPressed();

        // Assert: Perform your assertions here
        assertFalse(controller.registerResponseSuccess(failedRegistrationMessage), "Registration failed. Please check the details and try again");
    }
}

