package com.hit.server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class HandleRequest implements Runnable {
    private Socket socket;
    private Controller tripMapController = new Controller();
    private Gson parser = new Gson();

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            ObjectInputStream input = new ObjectInputStream(is);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            String reqString = (String) input.readObject();
            Gson gson = new Gson();
            Type ref = new TypeToken<Request>(){}.getType();
            Request request= gson.fromJson(reqString, ref);
            String action = request.getHeaders().getAction();
            String controller = request.getHeaders().getController();
            String locationName = request.getHeaders().getObjectName();
            Location location = new Location("lol", "da"); //request.getBody();
            switch (controller) {
                case "Location":
                    switch (action) {
                        case "GetLocation":
                            Location respLocation = tripMapController.getLocation(locationName);
                            Response<Location> locationResponse = new Response<>(200, respLocation);
                            String parsedLocationResponse = parser.toJson(locationResponse);
                            output.writeObject(parsedLocationResponse);
                            break;
                        case "ListLocation": {
                            List<Location> locationList = tripMapController.listLocations();
                            Response<List<Location>> response = new Response<>(200, locationList);
                            String jsonResponse = parser.toJson(response);
                            output.writeObject(jsonResponse);
                            break;
                        }
                        case "CreateLocation":
                            tripMapController.createLocation(location);
                            output.writeObject(new Response<Location>(201, null));
                            break;
                        case "EditLocation":
                            tripMapController.editLocation(location);
                            break;
                        case "DELETE":
                            tripMapController.deleteLocation(location.getName());
                            break;
                        case "FindShortestPath":
//                            tripMapController.findShortestPath(locationName
                    break;
                case "TripMap":
                    }
            }
            output.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
