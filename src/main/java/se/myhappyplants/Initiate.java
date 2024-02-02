package se.myhappyplants;

import se.myhappyplants.client.controller.StartClient;
import se.myhappyplants.server.StartServer;

import java.net.UnknownHostException;
import java.sql.SQLException;

public class Initiate {

    public static void main(String[] args) throws UnknownHostException, SQLException {

        StartServer.initiate();
        StartClient.initiate();

    }


}
