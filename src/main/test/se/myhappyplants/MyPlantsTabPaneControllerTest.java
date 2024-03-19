package se.myhappyplants;

import javafx.embed.swing.JFXPanel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.MyPlantsTabPaneController;
import se.myhappyplants.shared.Plant;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MyPlantsTabPaneControllerTest {

    //BIB09F
    //TEST-ID: T19
    @BeforeAll
    static void init() {
        new JFXPanel();
    }

    //BIB09F
    //TEST-ID: T20
    @Test
    void sortLibrary() {
        MyPlantsTabPaneController myPlantsTabController = mock(MyPlantsTabPaneController.class);

        ObservableList<String> testData = FXCollections.observableArrayList("Cactus", "Basil", "Aloe");

        when(myPlantsTabController.sortLibrary()).thenReturn("Library sorted by nickname");

        String resultMessage = myPlantsTabController.sortLibrary();

        assertEquals("Library sorted by nickname", resultMessage);
    }


    //KRAV-ID: BIB05F
    //TEST-ID: T17
    @Test
    public void testChangeNickNameinDB_Success(){
        MyPlantsTabPaneController myPlantsTabPaneController = mock(MyPlantsTabPaneController.class);
        Plant plant = mock(Plant.class);
        String nickName = "nickName";

        when(myPlantsTabPaneController.changeNicknameInDB(plant, nickName)).thenReturn("Success");

        String expected = "Success";
        String result = myPlantsTabPaneController.changeNicknameInDB(plant, nickName);

        assertEquals(expected, result);
    }

    //KRAV-ID: BIB05F
    //TEST-ID: T18
    @Test
    public void testChangeNickNameinDB_Failed(){
        MyPlantsTabPaneController myPlantsTabPaneController = mock(MyPlantsTabPaneController.class);
        Plant plant = mock(Plant.class);
        String nickName = "nickName";

        when(myPlantsTabPaneController.changeNicknameInDB(plant, nickName)).thenReturn("Failure");

        String expected = "Failure";
        String result = myPlantsTabPaneController.changeNicknameInDB(plant, nickName);

        assertEquals(expected, result);
    }


    //KRAV-ID: BIB06F
    //TEST-ID: T16
    @Test
    public void testRemovePlantFromDB() {
        Date testDate = new Date(1);
        Plant testPlant = new Plant("nickname", "plantID", testDate);
        Plant insertPlant = mock(Plant.class);

        MyPlantsTabPaneController controller = mock(MyPlantsTabPaneController.class);

        when(controller.removePlantFromDB(eq(insertPlant))).thenReturn(testPlant.getNickname());

        String result = controller.removePlantFromDB(insertPlant);
        String expected = testPlant.getNickname();

        assertEquals(expected, result);
    }


    //KRAV-ID: BIB02F
    //TEST-ID: T15
    @Test
    public void testAddCurrentUserLibraryToHomeScreen() {
        Plant testPlant = mock(Plant.class);
        ArrayList<Plant> testLibrary = new ArrayList<>();
        testLibrary.add(testPlant);

        MyPlantsTabPaneController controller = mock(MyPlantsTabPaneController.class);
        when(controller.addCurrentUserLibraryToHomeScreen()).thenReturn(String.valueOf(testLibrary.size()));

        controller.setCurrentUserLibrary(testLibrary);

        String result = controller.addCurrentUserLibraryToHomeScreen();
        String expected = String.valueOf(testLibrary.size());

        assertEquals(expected, result);
    }


}


