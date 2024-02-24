package se.myhappyplants.client.controller;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static se.myhappyplants.client.controller.RegisterPaneController.registerResponseSuccess;

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
    public void testVALIDSetAvatar() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/image.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expectedAvatarURL = "file:/Users/omar/Documents/kurser/Termin_4/DA489A_Systemutveckling_II/GitHub/HappyPlantsNew/path/to/image.jpg";
        assertEquals(expectedAvatarURL, user.getAvatarURL());
    }

    @Test
    public void testNOTVALIDSetAvatar() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/imae.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expectedAvatarURL = "file:/Users/omar/Documents/kurser/Termin_4/DA489A_Systemutveckling_II/GitHub/HappyPlantsNew/path/to/image.jpg";
        assertNotEquals(expectedAvatarURL, user.getAvatarURL());
    }


    @Test
    public void testInvalidEmail() {
        Verifier verified = new Verifier();
        assertFalse(verified.validateEmail("test.EmailWHENiswrong.com"));
    }

    @Test
    public void testvalidEmail() {
        Verifier verified = new Verifier();
        assertTrue(verified.validateEmail("test.EmailWHENisCorrect@correct.com"));
    }

    @Test
    public void testIsVerifiedRegistrationReturnsTrue() {
        // Act
        when(mockVerifier.validateRegistration(controller)).thenReturn(true);
        boolean result = controller.isVerifiedRegistration();

        // Assert
        assert (result);
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

 //registerThen Login
    @Test
    public void testLoginButtonPressed_SuccessfulLogin() {
        Message registerRequest = new Message(MessageType.register, new User("test@test.com","test", "testpassword", true));
        ServerConnection connection = ServerConnection.getClientConnection();
        Message registerResponse = connection.makeRequest(registerRequest);
        RegisterPaneController Register = new RegisterPaneController();
        if (Register.registerResponseSuccess(registerResponse)) {
            // Further validation can be added here if needed
            LoggedInUser.getInstance().setUser(registerResponse.getUser());
        }
        Message loginMessage = new Message(MessageType.login, new User("test@test.com", "testpassword"));
        Message loginResponse = connection.makeRequest(loginMessage);
        LoginPaneController controller = new LoginPaneController();
        assertTrue(controller.checkLoginResponseNotNull(loginResponse));
    }
    //login without registeration
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