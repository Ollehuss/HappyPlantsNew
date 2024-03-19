package se.myhappyplants.server.services;

import org.json.JSONArray;
import se.myhappyplants.server.PasswordsAndKeys;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Random;

import org.json.JSONObject;

public class APIScript extends Thread {

    private IQueryExecutor database;
    private IDatabaseConnection connectionMyHappyPlants;
    private String APIurl = "https://trefle.io/api/v1/plants?token=" + PasswordsAndKeys.APIToken;
    private int pages = 0;
    private int counter = 1;

    public APIScript() throws IOException {
        init();
        transferPlantData();
    }

    private void init() {
        this.connectionMyHappyPlants = new DatabaseConnection("myhappyplants");
        this.database = new QueryExecutor(connectionMyHappyPlants);
    }

    private void transferPlantData() throws IOException {
        this.pages = getNumberOfPages();
        this.start();
    }

    private int getNumberOfPages() throws IOException {
        JSONObject json = getPlantData(1);
        String pages = String.valueOf(json.getJSONObject("links").get("last"));
        String[] parts = pages.split("=");
        int numberOfPages = Integer.parseInt(parts[1]);
        return numberOfPages;
    }

    private JSONObject getPlantData(int page) throws IOException {
        InputStream is = new URL(APIurl + "&page=" +  page).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        }
        finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public void run() {
        // Är på 116 nu
        for (int i = 1; i < pages; i++) {
            try {
                System.out.println(i + "/" + pages);
                addPlantsToDatabase(getPlantData(i));
                sleep(600);
            } catch (IOException e) {
                System.out.println("At page: " + i);
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                System.out.println("At page: " + i);
                throw new RuntimeException(e);
            }
        }
        connectionMyHappyPlants.closeConnection();
    }

    private void addPlantsToDatabase(JSONObject json) {
        JSONArray page = json.getJSONArray("data");

        for (int i = 0; i < page.length(); i++) {
            JSONObject plant = page.getJSONObject(i);

            String commonName = String.valueOf(plant.get("common_name"));
            String scientificName = String.valueOf(plant.get("scientific_name"));
            String familyName = String.valueOf(plant.get("family"));
            String imageURL = String.valueOf(plant.get("image_url"));
            String genus = String.valueOf(plant.get("genus"));

            if (commonName.equals("null") || commonName == "null") {
                commonName = scientificName;
            }

            if (commonName.contains("'")) {
                String[] parts = commonName.split("'");
                commonName = (parts[0] + parts[1]);
            }

            sendToDataBase(commonName, scientificName, familyName, imageURL, genus);
        }

    }

    private void sendToDataBase(String commonName, String scientificName, String familyName, String imageURL, String genus) {

        Random rnd = new Random();
        int waterFrequency = rnd.nextInt(1000);
        int light = rnd.nextInt(10) + 1;


        String query = "INSERT INTO public.plant(plant_id, common_name, scientific_name, family_name, image_url, water_frequency, light, genus, url_wikipedia_en) " +
                "VALUES (" + counter + ", '" + commonName + "', '" + scientificName +"', '" + familyName + "', '" + imageURL + "', " + waterFrequency + ", '" + light + "', '"+ genus + "', 'wiki')";

        try {
            database.executeUpdate(query);
            counter++;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    /**
     * Kör inte detta!!!!!!!
     */
    private static class StartScript {
        public static void main(String[] args) throws IOException {
            APIScript apiScript = new APIScript();
        }
    }
}



