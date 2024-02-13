package se.myhappyplants.server.model.ResponseHandlers;

import se.myhappyplants.server.controller.api.PlantController;
import se.myhappyplants.server.model.IResponseHandler;
import se.myhappyplants.server.services.PlantRepository;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.Plant;

import java.util.ArrayList;
/**
 * Class that handles the request of a search
 */
public class Search implements IResponseHandler {
    private PlantRepository plantRepository;
    private PlantController plantController;

    public Search(PlantRepository plantRepository, PlantController plantController) {
        this.plantRepository = plantRepository;
        this.plantController = plantController;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        String searchText = request.getMessageText();
        try {
            System.out.println("Server:getResponse:searchText=" + searchText);
            ArrayList<Plant> plantList = plantRepository.getResult(searchText);
            //System.out.println("Server:getResponse:plantlist=" + plantList);
            if (plantList == null) {
                System.out.println("plantList is NULL");
            } else if (plantList.size() == 0) {
                System.out.println("plantList.size is 0");
                plantController.fetchPlantInfo(searchText);
            }
            response = new Message(plantList, true);
        } catch (Exception e) {
            response = new Message(false);
            e.printStackTrace();
        }
        return response;
    }
}
