package se.myhappyplants.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import se.myhappyplants.client.model.*;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.LibraryPlantPane;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.client.view.PopupBox;
import se.myhappyplants.shared.*;
import se.myhappyplants.client.model.SetAvatar;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller with logic used by the "My Plants" tab
 * Created by: Christopher O'Driscoll
 * Updated by: Christopher O'Driscoll, 2021-05-14
 */
public class MyPlantsTabPaneController {

    @FXML
    public ImageView imgNotifications;

    private ArrayList<Plant> currentUserLibrary;

    @FXML
    private MainPaneController mainPaneController;

    @FXML
    private Label lblUsername;

    @FXML
    private Circle imgUserAvatar;

    @FXML
    private ComboBox<SortingOption> cmbSortOption;

    @FXML
    private ListView lstViewUserPlantLibrary;

    @FXML
    private ListView<String> lstViewNotifications;

    @FXML
    private Button btnWaterAll;

    @FXML
    private Button btnExpandAll;

    @FXML
    public Button btnCollapseAll;

    /**
     * Method to initilize the variables
     */

    @FXML
    public void initialize() {
        LoggedInUser loggedInUser = LoggedInUser.getInstance();
        lblUsername.setText(loggedInUser.getUser().getUsername());
        imgUserAvatar.setFill(new ImagePattern(new Image(SetAvatar.setAvatarOnLogin(loggedInUser.getUser().getEmail()))));
        cmbSortOption.setItems(ListSorter.sortOptionsLibrary());
        createCurrentUserLibraryFromDB();
        addCurrentUserLibraryToHomeScreen();
    }


    /**
     * Method to set the mainPaneController
     * @param mainPaneController to set
     */
    public String setMainController(MainPaneController mainPaneController) {
        this.mainPaneController = mainPaneController;
        return "MainPaneController set, MyPlantsTabPaneController";
    }

    /**
     * Getter-method to get the mainPaneController
     * @return MainPaneController
     */
    public MainPaneController getMainPaneController() {
        return mainPaneController;
    }

    /**
     * Method to add a users plants to myPlantsTab
     */
    @FXML
    public String addCurrentUserLibraryToHomeScreen() {
        String librarySize = "0";
        ObservableList<LibraryPlantPane> obsListLibraryPlantPane = FXCollections.observableArrayList();

        if (currentUserLibrary == null) {
            disableButtons();
            obsListLibraryPlantPane.add(new LibraryPlantPane());
            librarySize = "null";
        } else {
            if (currentUserLibrary.size()<1) {
                disableButtons();
                obsListLibraryPlantPane.add(new LibraryPlantPane(this));
                librarySize = "0";
            } else {
                enableButtons();
                for (Plant plant : currentUserLibrary) {
                    obsListLibraryPlantPane.add(new LibraryPlantPane(this, plant));
                }
                librarySize = String.valueOf(currentUserLibrary.size());
            }
        }
        Platform.runLater(() -> {
            lstViewUserPlantLibrary.setItems(obsListLibraryPlantPane);
            sortLibrary();
        });
        return librarySize;
    }

    /**
     * Method to disable the buttons
     */
    private String disableButtons () {
        btnWaterAll.setDisable(true);
        btnExpandAll.setDisable(true);
        btnCollapseAll.setDisable(true);
        return "Buttons has been disabled";
    }

    /**
     * Mehtod to enable the buttons
     */
    private String enableButtons () {
        btnWaterAll.setDisable(false);
        btnExpandAll.setDisable(false);
        btnCollapseAll.setDisable(false);
        return "Buttons has been enabled";
    }

    /**
     * Method to show the notification
     */
    public String showNotifications() {
        ObservableList<String> notificationStrings = NotificationsCreator.getNotificationsStrings(currentUserLibrary, imgNotifications);
        Platform.runLater(() -> lstViewNotifications.setItems(notificationStrings));
        return "Notifications has been shown";
    }

    /**
     * Method to create the logged in users library from the database
     */
    @FXML
    public String createCurrentUserLibraryFromDB() {
        Thread getLibraryThread = new Thread(() -> {
            Message getLibrary = new Message(MessageType.getLibrary, LoggedInUser.getInstance().getUser());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message response = connection.makeRequest(getLibrary);

            if (response.isSuccess()) {
                currentUserLibrary = response.getPlantArray();
                addCurrentUserLibraryToHomeScreen();
                showNotifications();
            } else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
        });
        getLibraryThread.start();
        return "Library has been created";
    }

    /**
     * Method to remove a selected plant from the database
     * @param plant
     */
    @FXML
    public String removePlantFromDB(Plant plant) {
        Platform.runLater(() ->PopupBox.display(MessageText.removePlant.toString()));
        Thread removePlantThread = new Thread(() -> {
            currentUserLibrary.remove(plant);
            addCurrentUserLibraryToHomeScreen();
            Message deletePlant = new Message(MessageType.deletePlant, LoggedInUser.getInstance().getUser(), plant);
            ServerConnection connection = ServerConnection.getClientConnection();
            Message response = connection.makeRequest(deletePlant);

            if (!response.isSuccess()) {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
                createCurrentUserLibraryFromDB();
            }
        });
        removePlantThread.start();
        return plant.getNickname();
    }

    /**
     * Method to add a plant to the logged in users library with a nickname.
     * @param selectedPlant the plant that the user selects
     * @param plantNickname the nickname of the plant that the user chooses
     */
    @FXML
    public String addPlantToCurrentUserLibrary(Plant selectedPlant, String plantNickname) {
        int plantsWithThisNickname = 1;
        String uniqueNickName = plantNickname;
        for (Plant plant : currentUserLibrary) {
            if (plant.getNickname().equals(uniqueNickName)) {
                plantsWithThisNickname++;
                uniqueNickName = plantNickname + plantsWithThisNickname;
            }
        }
        long currentDateMilli = System.currentTimeMillis();
        Date date = new Date(currentDateMilli);
        String imageURL = PictureRandomizer.getRandomPictureURL();
        Plant plantToAdd = new Plant(uniqueNickName, selectedPlant.getPlantId(), date, imageURL);
        PopupBox.display(MessageText.sucessfullyAddPlant.toString());
        addPlantToDB(plantToAdd);
        return "Plant has been added to the logged in users library with a nickname";
    }

    /**
     * Method to save the plant to the database
     * @param plant the selected plant that the user has chosen
     */
    @FXML
    public String addPlantToDB(Plant plant) {
        Thread addPlantThread = new Thread(() -> {
            currentUserLibrary.add(plant);
            Message savePlant = new Message(MessageType.savePlant, LoggedInUser.getInstance().getUser(), plant);
            ServerConnection connection = ServerConnection.getClientConnection();
            Message response = connection.makeRequest(savePlant);
            if (!response.isSuccess()) {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
            createCurrentUserLibraryFromDB();
        });
        addPlantThread.start();
        return "Plant has been saved to the database";
    }

    /**
     * Method to message the right controller-class that the log out-button has been pressed
     * @throws IOException
     */
    @FXML
    private String logoutButtonPressed() throws IOException {
        mainPaneController.logoutButtonPressed();
        return "Logout button has been pressed, MyPlantsTabPaneController";
    }

    /**
     * Method to change last watered date in database, send a request to server and get a boolean respons depending on the result
     *
     * @param plant instance of the plant which to change last watered date
     * @param date  new date to change to
     */
    public String changeLastWateredInDB(Plant plant, LocalDate date) {
        Message changeLastWatered = new Message(MessageType.changeLastWatered, LoggedInUser.getInstance().getUser(), plant, date);
        ServerConnection connection = ServerConnection.getClientConnection();
        Message response = connection.makeRequest(changeLastWatered);
        PopupBox.display(MessageText.sucessfullyChangedDate.toString());
        if (!response.isSuccess()) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
        }
        createCurrentUserLibraryFromDB();
        showNotifications();
        return "Last watered date has been changed in the database";
    }

    /**
     * Method to send to the server to change the nickname of a selected plant in the database.
     * @param plant the selected plant
     * @param newNickname the new nickname of the plant
     * @return if it's successful. true or false
     */
    public String changeNicknameInDB(Plant plant, String newNickname) {
        Message changeNicknameInDB = new Message(MessageType.changeNickname, LoggedInUser.getInstance().getUser(), plant, newNickname);
        ServerConnection connection = ServerConnection.getClientConnection();
        Message response = connection.makeRequest(changeNicknameInDB);

        if (!response.isSuccess()) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "It was not possible to change nickname for your plant. Try again."));
            return "Failed";
        } else {
            plant.setNickname(newNickname);
            sortLibrary();
            PopupBox.display(MessageText.sucessfullyChangedPlant.toString());
            return "Success";
        }
    }


    /**
     * rearranges the library based on selected sorting option
     */
    public String sortLibrary() {
        StringBuilder resultMessage = new StringBuilder();
        SortingOption selectedOption;
        selectedOption = cmbSortOption.getValue();
        if (selectedOption == null)
            selectedOption = SortingOption.nickname;
        lstViewUserPlantLibrary.setItems(ListSorter.sort(selectedOption, lstViewUserPlantLibrary.getItems()));

        resultMessage.append("Library sorted by ").append(selectedOption.toString());
        return resultMessage.toString();
    }

    /**
     * Method to update the users avatar picture
     */

    public String updateAvatar() {
        String resultMessage;
        try {
            imgUserAvatar.setFill(new ImagePattern(new Image(LoggedInUser.getInstance().getUser().getAvatarURL())));
            resultMessage = "Avatar updated successfully.";
        } catch (Exception e) {
            resultMessage = "Failed to update avatar.";
            e.printStackTrace();
        }
        return resultMessage;
    }


    /**
     * Method to send to the server to get extended information about the plant
     * @param plant the selected plant
     * @return an instance of the class PlantDetails
     */
    public PlantDetails getPlantDetails(Plant plant) {
        PlantDetails plantDetails = null;
        Message getInfoSearchedPlant = new Message(MessageType.getMorePlantInfo, plant);
        ServerConnection connection = ServerConnection.getClientConnection();
        Message response = connection.makeRequest(getInfoSearchedPlant);
        if (response != null) {
            plantDetails = response.getPlantDetails();
        }
        return plantDetails;
    }

    /**
     * Method to water all the plant at once
     */
    @FXML
    public String waterAll() {
        btnWaterAll.setDisable(true);
        ObservableList<LibraryPlantPane> libraryPlantPanes = lstViewUserPlantLibrary.getItems();
        changeAllToWateredInDB();
        for (LibraryPlantPane lpp : libraryPlantPanes) {
            lpp.getProgressBar().setProgress(100);
            lpp.setColorProgressBar(100);
        }
        return "All plants have been watered at once";
    }

    /**
     * Method to expand all the plants "flaps" at the same time
     */
    @FXML
    public String expandAll() {
        btnExpandAll.setDisable(true);
        ObservableList<LibraryPlantPane> libraryPlantPanes = lstViewUserPlantLibrary.getItems();
        for (LibraryPlantPane lpp : libraryPlantPanes) {
            if (!lpp.extended)
                lpp.pressInfoButton();
        }
        btnExpandAll.setDisable(false);
        return "All plants flaps have been expanded at once";
    }

    /**
     * Method to collaps att the plants "flaps" at the same time
     */
    @FXML
    public String collapseAll() {
        btnCollapseAll.setDisable(true);
        ObservableList<LibraryPlantPane> libraryPlantPanes = lstViewUserPlantLibrary.getItems();
        for (LibraryPlantPane lpp : libraryPlantPanes) {
            if (lpp.extended)
                lpp.pressInfoButton();
        }
        btnCollapseAll.setDisable(false);
        return "All plants flaps have been collapsed at once";
    }

    /**
     * Method to send a message to the server to change the date of the last watered in the database
     */
    private String changeAllToWateredInDB() {
        Thread waterAllThread = new Thread(() -> {
            Message changeAllToWatered = new Message(MessageType.changeAllToWatered, LoggedInUser.getInstance().getUser());
            ServerConnection connection = ServerConnection.getClientConnection();
            Message response = connection.makeRequest(changeAllToWatered);
            if (!response.isSuccess()) {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
            }
            btnWaterAll.setDisable(false);
            createCurrentUserLibraryFromDB();
            showNotifications();
        });
        waterAllThread.start();
        return "All plants have hade there last watered date changed in the database";
    }

    public String setNewPlantPicture(LibraryPlantPane lpp) {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
        fc.getExtensionFilters().add(fileExtensions);
        File selectedImage = fc.showOpenDialog(null);

        if (selectedImage != null) {
            String imagePath = selectedImage.toString();
            String imageExtension = imagePath.substring(imagePath.indexOf("."));
            File newPictureFile = new File("resources/images/plants/" + lpp.getPlant().getNickname() + imageExtension);
            try {
                try {
                    Files.copy(selectedImage.toPath(), newPictureFile.toPath());
                } catch (FileAlreadyExistsException e) {
                    Files.delete(newPictureFile.toPath());
                    Files.copy(selectedImage.toPath(), newPictureFile.toPath());
                }
                lpp.getPlant().setImageURL(newPictureFile.toURI().toURL().toString());
                lpp.updateImage();
                Thread changePlantPictureThread = new Thread(() -> {
                    Message changePlantPicture = new Message(MessageType.changePlantPicture, LoggedInUser.getInstance().getUser(), lpp.getPlant());
                    ServerConnection connection = ServerConnection.getClientConnection();
                    Message response = connection.makeRequest(changePlantPicture);
                    if (!response.isSuccess()) {
                        Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "The connection to the server has failed. Check your connection and try again."));
                    }
                });
                changePlantPictureThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "New picture has been set";
    }

    public ArrayList<Plant> getCurrentUserLibrary() {
        return currentUserLibrary;
    }

    public void setCurrentUserLibrary(ArrayList<Plant> currentUserLibrary) {
        this.currentUserLibrary = currentUserLibrary;
    }

    public ListView getLstViewUserPlantLibrary() {
        return lstViewUserPlantLibrary;
    }

    public void setLstViewUserPlantLibrary(ListView lstViewUserPlantLibrary) {
        this.lstViewUserPlantLibrary = lstViewUserPlantLibrary;
    }
}
