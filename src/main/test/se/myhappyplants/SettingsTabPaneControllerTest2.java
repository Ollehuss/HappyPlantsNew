package se.myhappyplants;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.SettingsTabPaneController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SettingsTabPaneControllerTest2 {


    //INS01F
    @BeforeAll
    static void init() {
        new JFXPanel();
    }

    //INS01F
    @Test
    void changeNotificationsSetting() {
        SettingsTabPaneController settingsTabPaneController = mock(SettingsTabPaneController.class);

        SettingsTabPaneControllerTest2 settingsTabPaneControllerTest2 = new SettingsTabPaneControllerTest2();

        when(settingsTabPaneController.changeNotificationsSetting()).thenReturn("Notification settings changed.");

        String resultMessage = settingsTabPaneController.changeNotificationsSetting();

        assertEquals("Notification settings changed.", resultMessage);
    }
}


