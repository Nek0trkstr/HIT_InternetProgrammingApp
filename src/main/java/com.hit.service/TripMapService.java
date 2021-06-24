package com.hit.service;

import com.hit.algorithm.IAlgoSingleSourceShortestPath;
import com.hit.dao.IDao;
import com.hit.dm.Location;
import com.hit.dm.Place;

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

    public void editLocation(Location editedLocation) {
        String locationName = editedLocation.getName();
        Location oldLocation = dataAccess.getLocation(locationName);
        if (oldLocation != null) {
            dataAccess.deleteLocation(locationName);
            dataAccess.saveLocation(editedLocation);
        }
    }

    public List<Location> listLocations() {
        return dataAccess.listLocations();
    }

    public void deleteLocation(String locationName) {
        dataAccess.deleteLocation(locationName);
    }

    public int findShortestPath(Location location, Place source, Place destination) {
        return pathFinder.FindShortestDistance(location, source, destination);
    }

    public int findShortestPath(String locationName, Place source, Place destination) {
        Location location = dataAccess.getLocation(locationName);
        if (location == null) {
            return Integer.MAX_VALUE;
        }

        return pathFinder.FindShortestDistance(location, source, destination);
    }
}
