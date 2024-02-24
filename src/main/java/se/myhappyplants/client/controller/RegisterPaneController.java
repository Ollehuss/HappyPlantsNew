package se.myhappyplants.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import se.myhappyplants.client.model.BoxTitle;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.RootName;
import se.myhappyplants.client.model.Verifier;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.client.view.MessageBox;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;
import se.myhappyplants.shared.User;

import java.io.IOException;

/**
 * Created by Anton Holm
 */
public class RegisterPaneController {
    @FXML public TextField txtFldNewEmail;
    @FXML public TextField txtFldNewEmail1;
    @FXML public TextField txtFldNewUsername;
    @FXML public PasswordField passFldNewPassword;
    @FXML public PasswordField passFldNewPassword1;
    @FXML public Label goBackIcon;

    protected Verifier verifier;
    public void setVerifier(Verifier verifier) {
        this.verifier = verifier;
    }

    @FXML
    public void initialize() {
        verifier = new Verifier();
        goBackIcon.setFocusTraversable(true); //sets the goback button on focus to remove from first textfield
    }
    public interface UIMessageService {
        void showMessage(String message);
        int askYesNo(String question);
    }
    protected UIMessageService uiMessageService;

    public void setUIMessageService(UIMessageService uiMessageService) {
        this.uiMessageService = uiMessageService;
    }
    protected void displayMessage(BoxTitle title, String message) {
        Platform.runLater(() -> MessageBox.display(title, message));
    }


    @FXML
    protected void registerButtonPressed() {
        boolean verifiedRegistration = isVerifiedRegistration();
        if (!verifiedRegistration) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Invalid account details. Please check your inputs."));
            return;
        }

        int answer = MessageBox.askYesNo(BoxTitle.GDPR, "Your account details will be saved in accordance with GDPR requirements" + "\n" + "Do you still want to create the account?");
        if (answer == 1) {
            Thread registerThread = new Thread(() -> {
                try {
                    Message registerRequest = new Message(MessageType.register, new User(txtFldNewEmail.getText(), txtFldNewUsername.getText(), passFldNewPassword.getText(), true));
                    ServerConnection connection = ServerConnection.getClientConnection();
                    Message registerResponse = connection.makeRequest(registerRequest);

                    // Validate the response
                    if (registerResponseSuccess(registerResponse)) {
                        // Further validation can be added here if needed
                        LoggedInUser.getInstance().setUser(registerResponse.getUser());
                        Platform.runLater(() -> MessageBox.display(BoxTitle.Success, "Account created successfully! Now logged in as " + LoggedInUser.getInstance().getUser().getUsername()));
                        switchToMainPane();
                    } else {
                        // Handle failed registration
                        Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "Registration failed. Please check the details and try again."));
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> MessageBox.display(BoxTitle.Failed, "An error occurred: " + e.getMessage()));
                    e.printStackTrace();
                }
            });
            registerThread.start();
        } else {
            return;
        }
    }


    public boolean isVerifiedRegistration() {
        return verifier.validateRegistration(this);
    }

    protected static boolean registerResponseSuccess(Message registerResponse) {
        return registerResponse != null && registerResponse.isSuccess();
    }

    @FXML
    private void switchToMainPane() throws IOException {
        StartClient.setRoot(String.valueOf(RootName.mainPane));
    }

    public void swapToLogin(MouseEvent mouseEvent) {
        try {
            StartClient.setRoot(RootName.loginPane.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] getComponentsToVerify() {
        String[] loginInfoToCompare = new String[5];
        loginInfoToCompare[0] = txtFldNewEmail.getText();
        loginInfoToCompare[1] = txtFldNewEmail1.getText();
        loginInfoToCompare[2] = txtFldNewUsername.getText();
        loginInfoToCompare[3] = passFldNewPassword.getText();
        loginInfoToCompare[4] = passFldNewPassword1.getText();
        return loginInfoToCompare;
    }
}
