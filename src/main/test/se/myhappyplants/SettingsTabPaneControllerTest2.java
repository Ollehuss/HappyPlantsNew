package se.myhappyplants.Java;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.scene.control.ToggleButton;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import se.myhappyplants.client.controller.SettingsTabPaneController;
import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.User;

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


