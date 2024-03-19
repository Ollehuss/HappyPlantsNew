package se.myhappyplants;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;
import se.myhappyplants.client.controller.MainPaneController;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.shared.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPaneControllerTest {

    //KRAV-ID: ANV02F
    //TEST-ID: T04
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
    //TEST-ID: T05
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
    //TEST-ID: T06
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
}
