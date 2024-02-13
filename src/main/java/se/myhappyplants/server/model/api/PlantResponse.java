package se.myhappyplants.server.model.api;

import java.util.List;
//import se.myhappyplants.shared.Plant;
import se.myhappyplants.server.model.api.Plant;
public class PlantResponse {
    private List<Plant> data;

    // Getters and setters
    public List<Plant> getData() {
        return data;
    }

    public void setData(List<Plant> data) {
        this.data = data;
    }
}