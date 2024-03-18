package se.myhappyplants.client.model;

import javafx.application.Platform;
import se.myhappyplants.client.controller.LoginPaneController;
import se.myhappyplants.client.controller.RegisterPaneController;
import se.myhappyplants.client.view.MessageBox;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to validate the registration
 */
public class Verifier {

    public enum errorType {
        WRONG_EMAIL_FORMAT,
        NO_USERNAME,
        NO_PASSWORD,
        WRONG_PASSWORD,
        WRONG_EMAIL
    }


    /**
     * Static method to validate the registration when a user register a new account
     * @return boolean if successful
     */
    public boolean validateRegistration(RegisterPaneController registerPaneController) {
        String[] loginInfoToCompare = registerPaneController.getComponentsToVerify();

        if (!validateEmail(loginInfoToCompare[0])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, getErrorMessage(errorType.WRONG_EMAIL_FORMAT)));
            return false;
        }
        if (loginInfoToCompare[2].isEmpty()) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, getErrorMessage(errorType.NO_USERNAME)));
            return false;
        }
        if (loginInfoToCompare[3].isEmpty()) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, getErrorMessage(errorType.NO_PASSWORD)));
            return false;
        }
        if (!loginInfoToCompare[1].equals(loginInfoToCompare[0])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, getErrorMessage(errorType.WRONG_EMAIL)));
            return false;
        }
        if (!loginInfoToCompare[4].equals(loginInfoToCompare[3])) {
            Platform.runLater(() -> MessageBox.display(BoxTitle.Error, getErrorMessage(errorType.WRONG_PASSWORD)));
            return false;
        }

        return true;

    }

    public String getErrorMessage(errorType errorType) {

        switch (errorType) {
            case WRONG_EMAIL_FORMAT:
                return "Please enter your email address in format: yourname@example.com";

            case NO_USERNAME:
                return "Please enter a username";

            case NO_PASSWORD:
                return "Please enter a password";

            case WRONG_EMAIL:
                return "Please enter the same email twice";

            case WRONG_PASSWORD:
                return "Please enter the same password twice";

            default:
                return "Invalid input";
        }
    }

    /**
     * Method for validating an email by checking that it contains @
     *
     * @param email input email from user in application
     * @return true if the email contains @, false if it is not valid
     */
    public boolean validateEmail(String email) {
        final String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
