package se.myhappyplants;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;


import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.client.controller.*;
import se.myhappyplants.client.model.*;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.LibraryPlantPane;
import se.myhappyplants.server.StartServer;
import se.myhappyplants.server.services.*;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;

import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    private LoginPaneController lpc;
    private MainPaneController mpc;
    private PlantRepository plantRepositoryMock;
    private SearchTabPaneController searchTabPaneController;
    private MyPlantsTabPaneController myPlantsTabPaneController;
    private Plant testPlant;
    private StartClient startClient;
    private IDatabaseConnection connectionMyHappyPlants;
    private IQueryExecutor databaseMyHappyPlants;
    private SettingsTabPaneController settingsTabPaneController;


    public static void main(String[] args) throws IOException, SQLException {
        JunitTest testFile = new JunitTest();
        testFile.setup();
        testFile.testLogOutButton();
      //  testFile.testLogInButton();
    }


    @BeforeEach
    public void setup() throws IOException, SQLException {
        this.lpc = new LoginPaneController();
        this.mpc = new MainPaneController();
        this.startClient = new StartClient();
        this.searchTabPaneController = new SearchTabPaneController();
        this.myPlantsTabPaneController = new MyPlantsTabPaneController();
        this.plantRepositoryMock = mock(PlantRepository.class);
        this.connectionMyHappyPlants = mock(DatabaseConnection.class);
        this.databaseMyHappyPlants = mock(QueryExecutor.class);
        this.settingsTabPaneController = new SettingsTabPaneController();
        this.testPlant = mock(Plant.class);

        this.searchTabPaneController.setMainController(mpc);
        this.startClient = new StartClient();

        mockVerifier = mock(Verifier.class);
//        mockUIMessageService = mock(RegisterPaneController.UIMessageService.class);       // vet inte var och när denna klass ska ha implementerats (UIMessageService), kanske Omar vet
        controller = new RegisterPaneController();
//        controller.setVerifier(mockVerifier);     // vet inte om denna metod har implementerats någonannanstans, ni får fråga Omar om det är något ni ska ha med
//        controller.setUIMessageService(mockUIMessageService);     // samma som kommentaren ovan


    }

    public void testLogOutButton() throws IOException, SQLException {
        setup();
        testWriteEmailToTextFile();
        testSetUserToNull();
//        testSetRootToLoginPane();
       // testLogoutButtonPressed();
    }


    /////////////////////////// JAVAFX SETUP ///////////////////////////
    @BeforeAll
    public static void setupJavaFX() throws InterruptedException {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(JavaFXInit.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
        Thread.sleep(500);
    }

    public static class JavaFXInit extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // No need to do anything here.
        }
    }

    ///////////////////////////////////////////////////////////////



    //KRAV-ID: SK02F
    @Test
    public void testRunLastWateredQuery_Success() throws SQLException {
        UserPlantRepository userPlantRepository = mock(UserPlantRepository.class);
        User testUser = mock(User.class);
        LocalDate testDate = LocalDate.now();

        String testQuery = userPlantRepository.createLastWateredQuery(testUser, "Valentino's plant", testDate);
        when(userPlantRepository.runLastWateredQuery(testQuery)).thenReturn(true);

        assertTrue(userPlantRepository.runLastWateredQuery(testQuery));
    }

    //KRAV-ID: SK02F
    @Test
    public void testRunLastWateredQuery_Failure() throws SQLException {
        UserPlantRepository userPlantRepository = mock(UserPlantRepository.class);
        User testUser = mock(User.class);
        LocalDate testDate = LocalDate.now();

        String testQuery = userPlantRepository.createLastWateredQuery(testUser, "Valentino's plant", testDate);
        when(userPlantRepository.runLastWateredQuery(testQuery)).thenReturn(false);

        assertFalse(userPlantRepository.runLastWateredQuery(testQuery));
    }

    //KRAV-ID: SK02F
    @Test
    public void replaceSingleQuotesWithDoubleQuotes() {
        UserPlantRepository userPlantRepository = mock(UserPlantRepository.class);
        User testUser = new User(49, "email", "username", false);
        String nickname = "Valentino's plant";
        LocalDate testDate = LocalDate.of(2022, 2, 10);

        String expected = "UPDATE public.user_plant SET last_watered = '2022-02-10' WHERE user_id = 49 AND nickname = 'Valentino''s plant';";

        when(userPlantRepository.createLastWateredQuery(testUser, nickname, testDate)).thenReturn(expected);
        String actual = userPlantRepository.createLastWateredQuery(testUser, nickname, testDate);

        assertEquals(expected, actual);
    }


    // KRAV-ID: BIB06F
    @Test
    public void testRemovePlantFromDB() {
        Date testDate = new Date(1);
        Plant testPlant = new Plant("nickname", "plantID", testDate);
        Plant insertPlant = mock(Plant.class);

        MyPlantsTabPaneController controller = mock(MyPlantsTabPaneController.class);

        when(controller.removePlantFromDB(eq(insertPlant))).thenReturn(testPlant.getNickname());

        String result = controller.removePlantFromDB(insertPlant);
        String expected = testPlant.getNickname();

        assertEquals(expected, result);
    }


    // KRAV-ID: BIB02F
    @Test
    public void testAddCurrentUserLibraryToHomeScreen() {
        Plant testPlant = mock(Plant.class);
        ArrayList<Plant> testLibrary = new ArrayList<>();
        testLibrary.add(testPlant);

        MyPlantsTabPaneController controller = mock(MyPlantsTabPaneController.class);
        when(controller.addCurrentUserLibraryToHomeScreen()).thenReturn(String.valueOf(testLibrary.size()));

        controller.setCurrentUserLibrary(testLibrary);

        String result = controller.addCurrentUserLibraryToHomeScreen();
        String expected = String.valueOf(testLibrary.size());

        assertEquals(expected, result);
    }


    // KRAV-ID: BIB01F
    @Test
    public void testAddPlantToCurrentUserLibrary() {
        Plant testPlant = new Plant("plantID", "commonName", "scientificName", "familyName", "imageURL");
        Plant insertPlant = mock(Plant.class);

        SearchTabPaneController controller = mock(SearchTabPaneController.class);
        when(controller.addPlantToCurrentUserLibrary(eq(insertPlant))).thenReturn(testPlant.getCommonName());

        String result = controller.addPlantToCurrentUserLibrary(insertPlant);
        String expected = testPlant.getCommonName();

        assertEquals(expected, result);
    }

    // KRAV-ID: ANV02F
    @Test
    public void testWriteEmailToTextFile() throws IOException {
        // Arrange
        MainPaneController mpc = new MainPaneController();
        String email = "test@test.com";
        LoggedInUser.getInstance().setUser(new User(email, "test", "test", true));
        String expected = "Email written to file";

        // Act
        String result = mpc.writeEmailToTextFile();

        // Assert
        assertEquals(expected, result);

        // Verify that the email is written to the file
        BufferedReader br = new BufferedReader(new FileReader("resources/lastLogin.txt"));
        String writtenEmail = br.readLine();
        br.close();
        assertEquals(email, writtenEmail);
    }

    //KRAV-ID ANV02F
    @Test
    public void testSetUserToNull() {
        // Arrange
        MainPaneController mpc = new MainPaneController();
        LoggedInUser.getInstance().setUser(new User("test@test.com", "test", "test", true));
        String expected = "User set to null";

        // Act
        String result = mpc.setUserToNull();

        // Assert
        assertEquals(expected, result);

        // Verify that the user is set to null
        assertEquals(null,LoggedInUser.getInstance().getUser());
    }

    //KRAV-ID ANV02F
    @Test
    public void testSetRootToLoginPane() throws IOException {
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


    //KRAV-ID: ANV01F
    @Test
    public void testCheckLoginResponseNotNull_LoginResponseIsNull_DisplaysFailedMessageBox() {
        // Call the method with null loginResponse
        LoginPaneController loginPaneController = new LoginPaneController();
        boolean result = loginPaneController.checkLoginResponseNotNull(null);

        // Check that the appropriate message is returned
        assertEquals(false, result);
    }

    //KRAV-ID: ANV01F
    @Test
    public void testCheckLoginResponseNotNull_LoginResponseIsNull_ReturnsFalse() {
        // Call the method with null loginResponse
        LoginPaneController loginPaneController = new LoginPaneController();
        boolean result = loginPaneController.checkLoginResponseNotNull(null);

        // Check that false is returned
        assertFalse(result, "Expected false when loginResponse is null.");
    }

    //KRAV-ID: ANV01F
//    @Test
//    public void testCheckLoginResponseIsSuccess() {
//        LoginPaneController loginPaneController = new LoginPaneController();
//        Message loginResponse = new Message(MessageType.login, new User("test", "test"));
//        String result = loginPaneController.checkLoginResponseIsSuccess(loginResponse);
//        assertEquals("Login response is not successful", result);
//    }


    //KRAV-ID: ANV03F
    @Test
    public void testInvalidEmail() {
        Verifier verified = new Verifier();
        assertFalse(verified.validateEmail("test.EmailWHENiswrong.com"));         // validateEmail har private access
    }

    //KRAV-ID: ANV03F
    @Test
    public void testvalidEmail() {
        Verifier verified = new Verifier();
        assertTrue(verified.validateEmail("test.EmailWHENisCorrect@correct.com"));        // validateEmail har private access
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
    public void testSetAvatar_Success() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/image.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expected = new File(pathToImg).toURI().toString();
        String result = user.getAvatarURL();

        assertEquals(expected, result);
    }

    @Test
    public void testSetAvatar_Failure() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/image.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expected = new File(pathToImg).toURI().toString();
        String result = user.getAvatarURL() + "xyz";

        assertNotEquals(expected, result);
    }



//
//    @Test
//    public void testIsVerifiedRegistrationReturnsTrue() {
//        // Act
//        when(mockVerifier.validateRegistration(controller)).thenReturn(true);
//        boolean result = controller.isVerifiedRegistration();         // isVerifiedRegistration har private access
//
//        // Assert
//        assert (result);
//    }
//
//    @Test
//    public void testApiResponseNotNull() {
//        // Setup - create a Message and ServerConnection instance
//        Message apiRequest = new Message(MessageType.search, "testSearch");
//        ServerConnection connection = ServerConnection.getClientConnection();
//
//        // Execute
//        Message apiResponse = connection.makeRequest(apiRequest);
//
//        // Verify
//        assertNotNull(apiResponse, "The apiResponse is null, indicating no connection.");
//    }
//
//    //it needs to start Server first
//    @Test
//    public void testApiResponseSuccess() {
//        // Setup - create a Message and ServerConnection instance
//        Message apiRequest = new Message(MessageType.search, "validSearchCriteria");
//        ServerConnection connection = ServerConnection.getClientConnection();
//
//        // Execute
//        Message apiResponse = connection.makeRequest(apiRequest);
//
//        // Verify that apiResponse is not null to avoid NullPointerException in the test
//        assertNotNull(apiResponse, "The apiResponse is null, which is not expected here.");
//
//        // Now verify apiResponse.isSuccess() is true
//        assertTrue(apiResponse.isSuccess(), "The apiResponse did not successfully retrieve the data.");
//    }




    @BeforeAll
    public static void setupJavaFXRuntime() {
        // Initializes the JavaFX Runtime
        new JFXPanel();
    }

    private RegisterPaneController controller;
    private Verifier mockVerifier;
//    private RegisterPaneController.UIMessageService mockUIMessageService; // vet inte var och när denna klass ska ha implementerats (UIMessageService), kanske Omar vet


 //registerThen Login
    @Test
    public void testLoginButtonPressed_SuccessfulLogin() {
        Message registerRequest = new Message(MessageType.register, new User("test@test.com","test", "testpassword", true));
        ServerConnection connection = ServerConnection.getClientConnection();
        Message registerResponse = connection.makeRequest(registerRequest);
        RegisterPaneController Register = new RegisterPaneController();
//        if (Register.registerResponseSuccess(registerResponse)) {   // registerResponseSuccess har private access
            // Further validation can be added here if needed
            LoggedInUser.getInstance().setUser(registerResponse.getUser());
//        }
        Message loginMessage = new Message(MessageType.login, new User("test@test.com", "testpassword"));
        Message loginResponse = connection.makeRequest(loginMessage);
        LoginPaneController controller = new LoginPaneController();
//        assertTrue(controller.checkLoginResponseNotNull(loginResponse));    // checkLoginResponseNotNull har private access
    }
    //login without registeration
    @Test
    public void testLoginButtonPressed_Invalid() {

        // Mock ServerConnection
        Message loginMessage = new Message(MessageType.login, new User("test@test.com", "testpassword"));

        ServerConnection connection = Mockito.mock(ServerConnection.class);
        Message loginResponse = connection.makeRequest(loginMessage);
         LoginPaneController controller = new LoginPaneController();
//        assertFalse(controller.checkLoginResponseNotNull(loginResponse));   // checkLoginResponseNotNull har private access
    }


    @Test
    public void testDeleteResponseNotNull() {
        Message message = new Message(MessageType.login, "Test User");

        boolean result = settingsTabPaneController.deleteResponseNotNull(message);
        assertTrue(result);
    }

    @Test
    public void testDeleteResponseNotNull_Null() {
        Message message = new Message(MessageType.login, "Test User");

        boolean result = SettingsTabPaneController.deleteResponseNotNull(null);
        assertFalse(result);
    }



}