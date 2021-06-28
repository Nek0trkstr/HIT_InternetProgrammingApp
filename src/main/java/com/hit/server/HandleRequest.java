package com.hit.server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.Location;
import com.hit.graph.GraphPath;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

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
            String reqJson = (String) input.readObject();
            Gson gson = new Gson();
            Request request = gson.fromJson(reqJson, Request.class);
            String controller = request.getHeaders().getController();
            switch (controller) {
                case "Location":
                    Type ref = new TypeToken<Request<Location>>(){}.getType();
                    Request<Location> fullRequest = gson.fromJson(reqJson, ref);
                    Location location = fullRequest.getBody();
                    String action = request.getHeaders().getAction();
                    String locationName = request.getHeaders().getObjectName();
                    switch (action) {
                        case "GET":
                            Location respLocation = tripMapController.getLocation(locationName);
                            Response<Location> locationResponse = new Response<>(200, respLocation);
                            String parsedLocationResponse = parser.toJson(locationResponse);
                            output.writeObject(parsedLocationResponse);
                            break;
                        case "LIST": {
                            List<Location> locationList = tripMapController.listLocations();
                            Response<List<Location>> response = new Response<>(200, locationList);
                            String jsonResponse = parser.toJson(response);
                            output.writeObject(jsonResponse);
                            break;
                        }
                        case "POST":
                            tripMapController.createLocation(location);
                            Response createdLocationResponse = new Response(201, null);
                            String createLocationResponseJson = gson.toJson(createdLocationResponse);
                            output.writeObject(createLocationResponseJson);
                            break;
                        case "PUT":
                            tripMapController.editLocation(location);
                            Response editLocationResponse = new Response(200, null);
                            String editLocationResponseJson = gson.toJson(editLocationResponse);
                            output.writeObject(editLocationResponseJson);
                            break;
                        case "DELETE":
                            tripMapController.deleteLocation(locationName);
                            Response deleteLocationResponse = new Response(200);
                            String deleteLocationResponseJson = gson.toJson(deleteLocationResponse);
                            output.writeObject(deleteLocationResponseJson);
                            break;
                    }
                    break;
                case "TripMap":
                    Type tripMapType = new TypeToken<Request<ShortestPathQuery>>(){}.getType();
                    Request<ShortestPathQuery> fullMapRequest = gson.fromJson(reqJson, tripMapType);
                    ShortestPathQuery query = fullMapRequest.getBody();
                    String mapAction = request.getHeaders().getAction();
                    switch (mapAction){
                        case "GET":
                            GraphPath path = tripMapController.findShortestPath(query.getLocationName(), query.getSource(), query.getDestination());
                            Response<GraphPath> shortestPathResp = new Response(200, path);
                            String shortesPathRespJson = gson.toJson(shortestPathResp);
                            output.writeObject(shortesPathRespJson);
                            break;
                    }
                    break;
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
