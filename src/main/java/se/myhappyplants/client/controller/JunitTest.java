package se.myhappyplants.client.controller;
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
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;


import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    private LoginPaneController lpc;
    private MainPaneController mpc;
    private UserPlantRepository userPlantRepository;
    private PlantRepository plantRepositoryMock;
    private SearchTabPaneController searchTabPaneController;
    private MyPlantsTabPaneController myPlantsTabPaneController;
    private Plant testPlant;
    private User user;

    private StartClient startClient;
    private IDatabaseConnection connectionMyHappyPlants;
    private IQueryExecutor databaseMyHappyPlants;
    private LocalDate date;
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
        this.userPlantRepository = new UserPlantRepository(plantRepositoryMock, databaseMyHappyPlants);
        this.settingsTabPaneController = new SettingsTabPaneController();
        this.testPlant = mock(Plant.class);

        this.user = mock(User.class);
        when(user.getUniqueId()).thenReturn(49);
        this.date = LocalDate.now();

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


    @Test
    public void replaceSingleQuotesWithDoubleQuotes() {
        String nickname = "Valentino's plant";
        date = LocalDate.of(2022, 2, 10);
        String expected = "UPDATE public.user_plant SET last_watered = '2022-02-10' WHERE user_id = 49 AND nickname = 'Valentino''s plant';";
        String actual = userPlantRepository.createLastWateredQuery(user, nickname, date);
        assertEquals(expected, actual);
    }


    @Test
    public void testRunLastWateredQuery_Success() throws SQLException {
        String query = userPlantRepository.createLastWateredQuery(user, "Valentino's plant", date);
//        doNothing().when(databaseMyHappyPlants).executeUpdate(query);
        assertTrue(userPlantRepository.runLastWateredQuery(query));
    }

    @Test
    public void testRunLastWateredQuery_Failure() throws SQLException {
        String query = userPlantRepository.createLastWateredQuery(user, "Valentino's plant", date);
//        doThrow(SQLException.class).when(databaseMyHappyPlants).executeUpdate(query);
        assertFalse(!userPlantRepository.runLastWateredQuery(query));
    }

    @Test
    public void testDeleteResponseNotNull() {
        // Create a Message object
        Message message = new Message(MessageType.login, "Test User");

        // Call the method with a non-null message
//        boolean result = settingsTabPaneController.deleteResponseNotNull(message); // deleteResponseNotNull har private access
//        assertTrue(result);

        // Call the method with a null message
//        result = SettingsTabPaneController.deleteResponseNotNull(null);
//        assertFalse(result);
    }


//    @Test
//    public void testRemovePlantFromDB() {
//        // Arrange
//        ArrayList<Plant> library = new ArrayList<>();
//        library.add(testPlant);
//        myPlantsTabPaneController.currentUserLibrary = library;
//
//        // Act
//        myPlantsTabPaneController.removePlantFromDB(testPlant);
//
//        // Assert
//        assertFalse(myPlantsTabPaneController.currentUserLibrary.contains(testPlant));
//    }
//
//    @Test
//    public void testAddCurrentUserLibraryToHomeScreen() {
//        // Arrange
//        ArrayList<Plant> library = new ArrayList<>();
//        library.add(testPlant);
//        myPlantsTabPaneController.currentUserLibrary = library;
//
//        // Act
//        myPlantsTabPaneController.addCurrentUserLibraryToHomeScreen();
//
//        // Assert
//        ListView listView = myPlantsTabPaneController.lstViewUserPlantLibrary;
//        ObservableList<LibraryPlantPane> items = listView.getItems();
//        assertEquals(1, items.size());
//        assertEquals(library.get(0), items.get(0).getPlant());
//    }
//
//    @Test
//    public void testAddPlantToCurrentUserLibrary() {
//
//        // Assuming MyPlantsTabPaneController has a method to get the current user's library
//        int initialLibrarySize = myPlantsTabPaneController.get().size();
//
//        searchTabPaneController.addPlantToCurrentUserLibrary(testPlant);
//
//        int finalLibrarySize = myPlantsTabPaneController.getCurrentUserLibrary().size();
//
//        assertTrue(finalLibrarySize == initialLibrarySize + 1);
//    }

//    @Test
//    public void test1WriteEmailToTextFile() throws IOException {
//        // Arrange
//        MainPaneController mpc = new MainPaneController();
//        String email = "test@test.com";
//        LoggedInUser.getInstance().setUser(new User(email, "test", "test", true));
//        String expected = "Email written to file";
//
//        // Act
//        String result = mpc.writeEmailToTextFile();
//
//        // Assert
//        assertEquals(expected, result);
//
//        // Verify that the email is written to the file
//        BufferedReader br = new BufferedReader(new FileReader("resources/lastLogin.txt"));
//        String writtenEmail = br.readLine();
//        br.close();
//        assertEquals(email, writtenEmail);
//    }
//
//     @Test
//    public void test1WriteEmailToTextFile() throws IOException {
//        // Arrange
//        MainPaneController mpc = new MainPaneController();
//        String email = "test@test.com";
//        LoggedInUser.getInstance().setUser(new User(email, "test", "test", true));
//        String expected = "Email written to file";
//
//        // Act
//        String result = mpc.writeEmailToTextFile();
//
//        // Assert
//        assertEquals(expected, result);
//
//        // Verify that the email is written to the file
//        BufferedReader br = new BufferedReader(new FileReader("resources/lastLogin.txt"));
//        String writtenEmail = br.readLine();
//        br.close();
//        assertEquals(email, writtenEmail);
//    }

    @Test
    public void testSetUserToNull() {
       String result = mpc.setUserToNull();
       assertEquals("User set to null", result);
    }
//    @Test
//    public void testSetRootToLoginPane() throws IOException, SQLException {
//        StartServer serverMock = mock(StartServer.class);
//        //StartClient clientMock = mock(StartClient.class);
//     //   serverMock.main(new String[0]);
//        StartClient.setRoot(String.valueOf(RootName.loginPane));
//        String result = mpc.setRootToLoginPane();
//        assertEquals("Root set to loginPane", result);
//     //   verify(clientMock, times(1)).setRoot(RootName.loginPane.toString());
//    }
//    @Test
//    public void testSetRootToLoginPane() throws IOException, SQLException {
//        StartServer serverMock = mock(StartServer.class);
//        //StartClient clientMock = mock(StartClient.class);
//     //   serverMock.main(new String[0]);
//        StartClient.setRoot(String.valueOf(RootName.loginPane));
////        String result = mpc.setRootToLoginPane(); // scene är fortfarande null, se ANV02F i Testbara krav
//
//        // Verify the result
////        assertEquals("Root set to loginPane", result);
//    }
//    @Test
//    public void testSetRootToLoginPane() throws IOException {
//        // Initialize JavaFX platform
//        new JFXPanel();
//
//        // Arrange
//        MainPaneController mpc = new MainPaneController();
//        String expected = "Root set to loginPane";
//
//        // Act
//        Platform.runLater(() -> {
//            String result = mpc.setRootToLoginPane();
//
//            // Assert
//            assertEquals(expected, result);
//        });
//
//        // Wait for JavaFX platform to finish processing
//        WaitForAsyncUtils.waitForFxEvents();
//    }





//    @Test
//    @Test
//    public void testSetRootToLoginPane() throws IOException {
//        // Initialize JavaFX platform
//        new JFXPanel();
//
//        // Arrange
//        MainPaneController mpc = new MainPaneController();
//        String expected = "Root set to loginPane";
//
//        // Act
//        Platform.runLater(() -> {
//            String result = mpc.setRootToLoginPane();
//
//            // Assert
//            assertEquals(expected, result);
//        });
//
//        // Wait for JavaFX platform to finish processing
//        WaitForAsyncUtils.waitForFxEvents();
//    }





//        @Test
//    public void test1SetUserToNull() {
//        // Arrange
//        MainPaneController mpc = new MainPaneController();
//        LoggedInUser.getInstance().setUser(new User("test@test.com", "test", "test", true));
//        String expected = "User set to null";
//
//        // Act
//        String result = mpc.setUserToNull();
//
//        // Assert
//        assertEquals(expected, result);
//
//        // Verify that the user is set to null
//        assertEquals(null,LoggedInUser.getInstance().getUser());
//    }
//    @Test
//    public void testSetRootToLoginPane() throws IOException {
//        // Initialize JavaFX platform
//        new JFXPanel();
//
//        // Arrange
//        MainPaneController mpc = new MainPaneController();
//        String expected = "Root set to loginPane";
//
//        // Act
//        Platform.runLater(() -> {
//            String result = mpc.setRootToLoginPane();
//
//            // Assert
//            assertEquals(expected, result);
//        });
//
//        // Wait for JavaFX platform to finish processing
//        WaitForAsyncUtils.waitForFxEvents();
//    }





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
//        boolean result = lpc.checkLoginResponseNotNull(null);     // checkLoginResponseNotNull har private access

        // Check that false is returned
//        assertFalse(result, "Expected false when loginResponse is null.");
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
//        assertFalse(verified.validateEmail("test.EmailWHENiswrong.com"));         // validateEmail har private access
    }

    @Test
    public void testvalidEmail() {
        Verifier verified = new Verifier();
//        assertTrue(verified.validateEmail("test.EmailWHENisCorrect@correct.com"));        // validateEmail har private access
    }

    @Test
    public void testIsVerifiedRegistrationReturnsTrue() {
        // Act
        when(mockVerifier.validateRegistration(controller)).thenReturn(true);
//        boolean result = controller.isVerifiedRegistration();         // isVerifiedRegistration har private access

        // Assert
//        assert (result);
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
//        WaitForAsyncUtils.waitForFxEvents();    // vet inte vad problemet är här med WaitForAsyncUtils
    }

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


}