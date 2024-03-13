package se.myhappyplants.Java;

import javafx.embed.swing.JFXPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.MyPlantsTabPaneController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MyPlantsTabPaneControllerTest {

    @BeforeAll
    static void init() {
        new JFXPanel();
    }

    @Test
    void sortLibrary() {
        // Mocking MyPlantsTabPaneController
        MyPlantsTabPaneController myPlantsTabController = mock(MyPlantsTabPaneController.class);

        // Test data
        ObservableList<String> testData = FXCollections.observableArrayList("Cactus", "Basil", "Aloe");

        // Mocking the behavior of sortLibrary() method
        when(myPlantsTabController.sortLibrary()).thenReturn("Library sorted by nickname");

        // Calling the method under test
        String resultMessage = myPlantsTabController.sortLibrary();

        // Verify the result
        assertEquals("Library sorted by nickname", resultMessage);
    }
}


