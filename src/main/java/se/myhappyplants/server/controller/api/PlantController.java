package se.myhappyplants.server.controller.api;

import se.myhappyplants.server.model.api.Plant;
import se.myhappyplants.server.model.api.PlantResponse;
import se.myhappyplants.server.services.PasswordsAndKeys;

import java.io.IOException;
import java.net.URI;
//import com.google.gson.Gson;
import com.google.gson.Gson;
import se.myhappyplants.server.services.PlantRepository;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PlantController {
    private PlantRepository plantRepository;
    private static String token = PasswordsAndKeys.APIToken;
    private static final String API_URL = "https://trefle.io/api/v1/plants/search?token=" + token + "&q=";

    public PlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public void fetchPlantInfo(String query) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + query))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Plant> parsedInfo = parsePlantInfo(response.body());
            for (int i = 0; i < parsedInfo.size(); i++) {
                System.out.println("parsedInfo LIST:");
                System.out.println(parsedInfo.get(i));
                this.plantRepository.savePlant(parsedInfo.get(i));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<se.myhappyplants.server.model.api.Plant> parsePlantInfo(String responseBody) {
        Gson gson = new Gson();
        PlantResponse plantResponse = gson.fromJson(responseBody, PlantResponse.class);
        List<se.myhappyplants.server.model.api.Plant> plants = plantResponse.getData();
        System.out.println(plants);
        for (se.myhappyplants.server.model.api.Plant plant : plants) {
            //System.out.println("Scientific Name: " + plant.getScientificName());
            System.out.println("Scientific Name: " + plant.getScientific_name());
            //System.out.println("Genus: " + plant.getGenus());
            //System.out.println("Family: " + plant.getFamilyName());
            System.out.println("Common Name: " + plant.getCommon_name());
            //System.out.println("Image URL: " + plant.getImageURL());
            System.out.println(); // Print a blank line for readability
        }
        return plants;
    }
}
