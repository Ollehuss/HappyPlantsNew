package se.myhappyplants.server.services;

import se.myhappyplants.shared.WaterCalculator;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.PlantDetails;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about plants.
 * Created by: Frida Jacobsson 2021-03-30
 * Updated by: Christopher O'Driscoll
 */
public class PlantRepository {

    private IQueryExecutor database;

    public PlantRepository(IQueryExecutor database) {
        this.database = database;
    }

    public ArrayList<Plant> getResult(String plantSearch) {
        ArrayList<Plant> plantList = new ArrayList<>();
        String query = "SELECT plant_id, common_name, scientific_name, family_name, image_url FROM public.plant WHERE scientific_name LIKE ('%" + plantSearch + "%') OR common_name LIKE ('%" + plantSearch + "%');";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String plantId = resultSet.getString("plant_id");
                String commonName = resultSet.getString("common_name");
                String scientificName = resultSet.getString("scientific_name");
                String familyName = resultSet.getString("family_name");
                String imageURL = resultSet.getString("image_url");
                plantList.add(new Plant(plantId, commonName, scientificName, familyName, imageURL));
            }
        }
        /*catch (SQLException sqlException) {
            System.out.println(sqlException.fillInStackTrace());
            plantList = null;
        }*/
        catch (SQLException sqlException) {
            System.out.println(sqlException.fillInStackTrace());
            plantList = null;
            sqlException.printStackTrace();
            // Log additional details from the exception
            System.out.println("SQL State: " + sqlException.getSQLState());
            System.out.println("Error Code: " + sqlException.getErrorCode());
            System.out.println("Message: " + sqlException.getMessage());
            //retries++;
            // Consider adding a delay here if you plan to retry
        }

        return plantList;
    }

    public PlantDetails getPlantDetails(Plant plant) {
        PlantDetails plantDetails = null;
        String query = "SELECT genus, light, scientific_name, water_frequency, family_name, common_name, image_url FROM public.plant WHERE plant_id = '" + plant.getPlantId() + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String genus = resultSet.getString("genus");
                String scientificName = resultSet.getString("scientific_name");
                String lightText = resultSet.getString("light");
                String waterText = resultSet.getString("water_frequency");
                String family = resultSet.getString("family_name");

                int light = (isNumeric(lightText)) ? Integer.parseInt(lightText) : -1;
                int water = (isNumeric(waterText)) ? Integer.parseInt(waterText) : -1;

                plantDetails = new PlantDetails(genus, scientificName, light, water, family);
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return plantDetails;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public long getWaterFrequency(String plantId) throws IOException, InterruptedException {
        long waterFrequency = -1;
        String query = "SELECT water_frequency FROM public.plant WHERE plant_id = '" + plantId + "';";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                String waterText = resultSet.getString("water_frequency");
                int water = (isNumeric(waterText)) ? Integer.parseInt(waterText) : -1;
                waterFrequency = WaterCalculator.calculateWaterFrequencyForWatering(water);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return waterFrequency;
    }

}
