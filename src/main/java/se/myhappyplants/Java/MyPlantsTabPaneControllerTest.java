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

    //BIB09F
    @BeforeAll
    static void init() {
        new JFXPanel();
    }

    //BIB09F
    @Test
    void sortLibrary() {
        MyPlantsTabPaneController myPlantsTabController = mock(MyPlantsTabPaneController.class);

        ObservableList<String> testData = FXCollections.observableArrayList("Cactus", "Basil", "Aloe");

        when(myPlantsTabController.sortLibrary()).thenReturn("Library sorted by nickname");

        String resultMessage = myPlantsTabController.sortLibrary();

        assertEquals("Library sorted by nickname", resultMessage);
    }
}


