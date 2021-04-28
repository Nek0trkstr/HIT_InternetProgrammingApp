package com.hit.service;

import com.hit.algorithm.IAlgoSingleSourceShortestPath;
import com.hit.dao.IDao;
import com.hit.dm.Location;

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
}
