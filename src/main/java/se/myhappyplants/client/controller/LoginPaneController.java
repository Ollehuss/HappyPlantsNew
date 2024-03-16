package se.myhappyplants.client.controller;

import java.io.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.RootName;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.client.view.PopupBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controls the inputs from a user that hasn't logged in
 * Created by: Eric Simonsson, Christopher O'Driscoll
 * Updated by: Linn Borgström, 2021-05-13
 */
public class LoginPaneController {

    @FXML
    public Hyperlink registerLink;
    @FXML
    private TextField txtFldEmail;
    @FXML
    private PasswordField passFldPassword;


    /**
     * Switches to 'logged in' scene
     *
     * @throws IOException
     */
    @FXML
    public String initialize() throws IOException {
        String lastLoggedInUser;

        File file = new File("resources/lastLogin.txt");
        if (!file.exists()) {
            file.createNewFile();
            return "Last login file not found, created a new file";
        }
        else if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader("resources/lastLogin.txt"));) {
                lastLoggedInUser = br.readLine();
                txtFldEmail.setText(lastLoggedInUser);
                return "Last login file found, read from file";
            }
            catch (IOException e) {
                e.printStackTrace();
                return "Error reading from file";
            }
        }
        return "Something went wrong with the last login file";
    }

    /**
     * Method which tries to log in user. If it's successful, it changes scene
     *
     * @throws IOException
     */
    @FXML
    private String loginButtonPressed() {
        Thread loginThread = new Thread(() -> {
            Message loginMessage = new Message(MessageType.login, new User(txtFldEmail.getText(), passFldPassword.getText()));
            ServerConnection connection = ServerConnection.getClientConnection();
            Message loginResponse = connection.makeRequest(loginMessage);

            if(checkLoginResponseNotNull(loginResponse)) {
                checkLoginResponseIsSuccess(loginResponse);
            } else {
                Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Sorry, we couldn't find an account with that email or you typed the password wrong. Try again or create a new account."));
            }
        });
        loginThread.start();
        return "Login button has been pressed and loginThread has been started";
    }

    public boolean checkLoginResponseNotNull(Message loginResponse) {
        return loginResponse != null;
    }

    public String checkLoginResponseIsSuccess(Message loginResponse) {
        if (loginResponse.isSuccess()) {
            LoggedInUser.getInstance().setUser(loginResponse.getUser());
            Platform.runLater(() -> PopupBox.display("Now logged in as\n" + LoggedInUser.getInstance().getUser().getUsername()));
            try {
                switchToMainPane();
                return "User is now logged in";
            }
            catch (IOException e) {
                e.printStackTrace();
                return "Error switching to mainPane";
            }
        } else {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Sorry, we couldn't find an account with that email or you typed the password wrong. Try again or create a new account."));
            return "User has not been logged in";
        }
    }

    /**
     * Method to switch to the mainPane FXML
     *
     * @throws IOException
     */
    @FXML
    private String switchToMainPane() throws IOException {
        StartClient.setRoot(String.valueOf(RootName.mainPane));
        return "Switched to mainPane";
    }

    /**
     * Method to switch to the registerPane
     *
     * @param actionEvent
     */
    public String swapToRegister(ActionEvent actionEvent) {
        try {
            StartClient.setRoot(RootName.registerPane.toString());
            return "Switched to registerPane";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Error switching to registerPane";
        }
    }
}
