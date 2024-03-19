package se.myhappyplants;

import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.SearchTabPaneController;
import se.myhappyplants.shared.Plant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchTabPaneControllerTest {

    // KRAV-ID: BIB01F
    // TEST-ID: T14
    @Test
    public void testAddPlantToCurrentUserLibrary() {
        Plant testPlant = new Plant("plantID", "commonName", "scientificName", "familyName", "imageURL");
        Plant insertPlant = mock(Plant.class);

        SearchTabPaneController controller = mock(SearchTabPaneController.class);
        when(controller.addPlantToCurrentUserLibrary(eq(insertPlant))).thenReturn(testPlant.getCommonName());

        String result = controller.addPlantToCurrentUserLibrary(insertPlant);
        String expected = testPlant.getCommonName();

        assertEquals(expected, result);
    }
}
