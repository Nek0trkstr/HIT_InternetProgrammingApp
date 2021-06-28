package com.hit.service;

import com.hit.algorithm.BellmanFord;
import com.hit.algorithm.IAlgoSingleSourceShortestPath;
import com.hit.dao.IDao;
import com.hit.dm.Location;
import com.hit.dm.Place;
import com.hit.graph.GraphPath;
import com.hit.graph.Vertex;

import java.security.InvalidParameterException;
import java.util.List;

public class TripMapService {
    private IAlgoSingleSourceShortestPath pathFinder;
    private IDao dataAccess;

    public TripMapService(IAlgoSingleSourceShortestPath pathFinder, IDao dataAccess) {
        this.pathFinder = pathFinder;
        this.dataAccess = dataAccess;
    }

    public Location getLocation(String locationName) {
        Location location = dataAccess.getLocation(locationName);
        return location;
    }

    public void createLocation(Location newLocation) {
        dataAccess.saveLocation(newLocation);
    }

    public void createLocations(List<Location> locationsList) {
        dataAccess.saveLocations(locationsList);
    }

    public List<Location> listLocations() {
        return dataAccess.listLocations();
    }

    public void deleteLocation(String locationName) {
        dataAccess.deleteLocation(locationName);
    }

    public GraphPath findShortestPath(Location location, Place source, Place destination) {
        return pathFinder.FindShortestPath(location, source, destination);
    }

    public GraphPath findShortestPath(String locationName, Vertex source, Vertex destination) {
        Location location = dataAccess.getLocation(locationName);
        if (location == null) {
            throw new InvalidParameterException(String.format("Location with name: %s not found", locationName));
        }

        return pathFinder.FindShortestPath(location, source, destination);
    }

    public void editLocation(Location editedLocation) {
        String locationName = editedLocation.getName();
        Location oldLocation = getLocation(locationName);
        if (oldLocation != null) {
            deleteLocation(oldLocation.getName());
            createLocation(editedLocation);
        }
    }
}
