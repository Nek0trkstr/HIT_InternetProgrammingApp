package com.hit.service;
import com.hit.dao.*;
import com.hit.dm.Location;
import com.hit.dm.Path;
import com.hit.dm.Place;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TripMapTest {
    Place waterfall;
    Place entrance;
    Path path;
    Location testLocation;
    IDao dataAccess = new DaoFileImpl("testfile");

    @BeforeEach
    public void init() {
        waterfall = new Place("waterfall");
        entrance = new Place("entrance");
        path = new Path(entrance, waterfall, 1);
        testLocation = new Location("Great waterfall", "Seriously great waterfall");
        testLocation.addPlace(entrance);
        testLocation.addPlace(waterfall);
        testLocation.addPath(path);

    }

    @Test
    public void fileReadWriteSingleTest() {
        dataAccess.saveLocation(testLocation);
        Location savedLocation = dataAccess.getLocation(testLocation.getName());
    }

    @Test
    public void fileReadWriteMultipleTest() {
        Place gate = new Place("Gate");
        Place house = new Place("House");
        Path pathGateToHouse = new Path(gate, house, 2);
        Location newTestLocation = new Location("Simple Gate", "Nothing interesting");
        newTestLocation.addPlace(gate);
        newTestLocation.addPlace(house);
        newTestLocation.addPath(pathGateToHouse);
        List<Location> locationList = new ArrayList<>();
        locationList.add(testLocation);
        locationList.add(newTestLocation);
        dataAccess.saveLocations(locationList);
        List<Location> locationsFromFile = dataAccess.listLocations();
        Assertions.assertTrue(locationsFromFile.size() == locationList.size());
    }

    @Test
    public void fileDeleteTest() {
        // Init
        Place gate = new Place("Gate");
        Place house = new Place("House");
        Path pathGateToHouse = new Path(gate, house, 2);
        Location newTestLocation = new Location("Simple Gate", "Nothing interesting");
        newTestLocation.addPlace(gate);
        newTestLocation.addPlace(house);
        newTestLocation.addPath(pathGateToHouse);
        List<Location> locationList = new ArrayList<>();
        locationList.add(testLocation);
        locationList.add(newTestLocation);
        dataAccess.saveLocations(locationList);

        // Delete
        dataAccess.deleteLocation(newTestLocation.getName());

        // Assert
        List<Location> locationsFromFile = dataAccess.listLocations();
        Assertions.assertTrue(locationsFromFile.size() == locationList.size() - 1);
    }

    @Test
    public void fileReadTest() {
        Location location = dataAccess.getLocation(testLocation.getName());
        Assertions.assertTrue(location.getName().equals("Great waterfall"));
        Assertions.assertTrue(location.getVertices().size() == 2);
        Assertions.assertTrue(location.getEdges().size() == 1);
    }
}
