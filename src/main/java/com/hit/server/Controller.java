package com.hit.server;

import com.hit.algorithm.Dijkstra;
import com.hit.algorithm.IAlgoSingleSourceShortestPath;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.Location;
import com.hit.dm.Place;
import com.hit.graph.GraphPath;
import com.hit.service.TripMapService;

import java.util.List;

public class Controller {
    TripMapService tripMapService;

    public Controller(IAlgoSingleSourceShortestPath algorithm, IDao dataAccess) {
        tripMapService = new TripMapService(algorithm, dataAccess);
    }

    public Controller() {
        tripMapService = new TripMapService(new Dijkstra(), new DaoFileImpl("datasource.txt"));
    }

    public Location getLocation(String name) {
        return tripMapService.getLocation(name);
    }

    public List<Location> listLocations() {
        return tripMapService.listLocations();
    }

    public void createLocation(Location newLocation) {
        tripMapService.createLocation(newLocation);
    }

    public void deleteLocation(String name) {
        tripMapService.deleteLocation(name);
    }

    public void editLocation(Location editedLocation) {
        tripMapService.editLocation(editedLocation);
    }

    public GraphPath findShortestPath(String locationName, Place source, Place destination) {
        return tripMapService.findShortestPath(locationName, source, destination);
    }
}
