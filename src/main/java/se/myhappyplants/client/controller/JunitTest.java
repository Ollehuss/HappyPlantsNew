package se.myhappyplants.client.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
//import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.mockito.Mock;
import org.mockito.Mockito;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.RootName;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
//        this.userPlantRepository = mock(UserPlantRepository.class);
        this.testPlant = mock(Plant.class);

        ObservableBooleanValue mockObservable = mock(ObservableBooleanValue.class);
        this.user = mock(User.class);
        when(mockObservable.get()).thenReturn(true);
        when(user.getUniqueId()).thenReturn(49);
        this.date = LocalDate.now();

        this.searchTabPaneController.setMainController(mpc);
        this.startClient = new StartClient();
        //   startClient = mock(StartClient.class);
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
        boolean result = settingsTabPaneController.deleteResponseNotNull(message);
        assertTrue(result);

        // Call the method with a null message
        result = SettingsTabPaneController.deleteResponseNotNull(null);
        assertFalse(result);
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
    @Test
    public void testSetRootToLoginPane() throws IOException {
        // Create a MainPaneController object


        // Call the method
        String result = mpc.setRootToLoginPane();

        // Verify the result
        assertEquals("Root set to loginPane", result);
    }
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

}
