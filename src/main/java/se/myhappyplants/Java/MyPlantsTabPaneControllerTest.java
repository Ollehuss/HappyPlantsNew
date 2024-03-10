package se.myhappyplants.Java;

import javafx.embed.swing.JFXPanel; // Import JFXPanel
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.MyPlantsTabPaneController;
import se.myhappyplants.client.model.SortingOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MyPlantsTabPaneControllerTest {

    @BeforeAll
    static void init() {
        // Initialize JavaFX toolkit
        new JFXPanel();
    }

    @Test
    void sortLibrary() {

        ListView<String> lstViewUserPlantLibrary = mock(ListView.class);

        ObservableList<String> testData = FXCollections.observableArrayList("Cactus", "Basil", "Aloe");

        MyPlantsTabPaneController myPlantsTabController = new MyPlantsTabPaneController();
        myPlantsTabController.setLstViewUserPlantLibrary(lstViewUserPlantLibrary);

        when(lstViewUserPlantLibrary.getItems()).thenReturn(testData);

        String resultMessage = myPlantsTabController.sortLibrary();

        assertEquals("Library sorted by nickname", resultMessage);
    }

}

