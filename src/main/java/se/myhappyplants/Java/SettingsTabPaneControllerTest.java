package se.myhappyplants.Java;

import javafx.stage.FileChooser;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.SettingsTabPaneController;

import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.shared.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import static org.mockito.Mockito.*;

class SettingsTabPaneControllerTest {

    @Test
    void selectAvatarImage() throws IOException {
        // Mock LoggedInUser
        User userMock = mock(User.class);
        when(userMock.getEmail()).thenReturn("test@example.com");
        LoggedInUser loggedInUserMock = mock(LoggedInUser.class);
        when(loggedInUserMock.getUser()).thenReturn(userMock);

        // Mock FileChooser
        FileChooser fileChooserMock = mock(FileChooser.class);
        File selectedImageMock = mock(File.class);
        when(fileChooserMock.showOpenDialog(null)).thenReturn(selectedImageMock);

        // Mock selectedImage
        String imagePath = "test/path/to/image.jpg";
        when(selectedImageMock.toString()).thenReturn(imagePath);
        when(selectedImageMock.toPath()).thenReturn(new File(imagePath).toPath());

        // Call the method
        SettingsTabPaneController settingsTabPaneController = new SettingsTabPaneController();
        settingsTabPaneController.selectAvatarImage();

        // Verify interactions and behavior
        verify(selectedImageMock).toString();
        verify(selectedImageMock).toPath();
        verify(userMock).setAvatar(anyString()); // Assuming setAvatar method exists in User class
        // You may add more verifications based on the behavior you want to test
    }
}

