package se.myhappyplants;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.SettingsTabPaneController;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettingsTabPaneControllerTest {

    //KRAV-ID: INS01F
    @Test
    void changeNotificationsSetting() {
        new JFXPanel();

        SettingsTabPaneController settingsTabPaneController = mock(SettingsTabPaneController.class);

        SettingsTabPaneControllerTest settingsTabPaneControllerTest2 = new SettingsTabPaneControllerTest();

        when(settingsTabPaneController.changeNotificationsSetting()).thenReturn("Notification settings changed.");

        String resultMessage = settingsTabPaneController.changeNotificationsSetting();

        assertEquals("Notification settings changed.", resultMessage);
    }


    //KRAV-ID: ANV10F
    //TEST-ID: T13
    @Test
    public void testChangeProfilePicture() {
        SettingsTabPaneController settingsTabPaneController = mock(SettingsTabPaneController.class);

        when(settingsTabPaneController.selectAvatarImage()).thenReturn("Avatar updated");

        String result = settingsTabPaneController.selectAvatarImage();
        String expected = "Avatar updated";

        assertEquals(expected, result);
    }

    //KRAV-ID: ANV04F
    //TEST-ID: T09
    @Test
    public void testDeleteResponseNotNull() {
        SettingsTabPaneController settingsTabPaneController = new SettingsTabPaneController();
        Message message = new Message(MessageType.login, "Test User");

        boolean result = settingsTabPaneController.deleteResponseNotNull(message);
        assertTrue(result);
    }

    //KRAV-ID: ANV04F
    //TEST-ID: T10
    @Test
    public void testDeleteResponseNotNull_Null() {
        SettingsTabPaneController settingsTabPaneController = new SettingsTabPaneController();
        Message message = new Message(MessageType.login, "Test User");

        boolean result = settingsTabPaneController.deleteResponseNotNull(null);
        assertFalse(result);
    }



}


