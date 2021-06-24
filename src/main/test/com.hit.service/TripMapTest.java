package com.hit.service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.algorithm.*;
import com.hit.dao.*;
import com.hit.dm.Location;
import com.hit.dm.Path;
import com.hit.dm.Place;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;


public class TripMapTest {
    final static String storageFileName = "datasource.txt";
    Place waterfall;
    Place entrance;
    Path path;
    Location testLocation;
    IDao fileStorage = new DaoFileImpl(storageFileName);
    IAlgoSingleSourceShortestPath dijkstra = new Dijkstra();
    TripMapService tripMapService = new TripMapService(dijkstra, fileStorage);

    @BeforeEach
    public void init() {
        deleteFile(storageFileName);
        waterfall = new Place("waterfall");
        entrance = new Place("entrance");
        path = new Path(entrance, waterfall, 1);
        testLocation = new Location("Great waterfall", "Seriously great waterfall");
        testLocation.addPlace(entrance);
        testLocation.addPlace(waterfall);
        testLocation.addPath(path);
    }

    @AfterAll
    public static void clean() {
        deleteFile(storageFileName);
    }

    @Test
    public void createAndGetSingle() {
        // Arrange
        tripMapService.createLocation(testLocation);
        // Act
        Location savedLocation = tripMapService.getLocation(testLocation.getName());
        // Assert
        Assertions.assertTrue(testLocation.getName().equals(savedLocation.getName()));
    }

    @Test
    public void testParsingSingleLocation() {
        Gson gson = new Gson();
        String generatedJson = gson.toJson(testLocation);
        try {
            gson.fromJson(generatedJson, Location.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testParsingListOfLocations() {
        Gson gson = new Gson();
        List<Location> locationList = new ArrayList<>();
        locationList.add(testLocation);
        String serializedList = gson.toJson(locationList);
        try {
            gson.fromJson(serializedList, new TypeToken<List<Location>>(){}.getType());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createAndListMultiple() {
        // Arrange
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
        // Act
        tripMapService.createLocations(locationList);
        List<Location> locationsFromFile = tripMapService.listLocations();
        // Assert
        Assertions.assertTrue(locationsFromFile.size() == locationList.size());
    }

    @Test
    public void fileDeleteTest() {
        // Arrange
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
        tripMapService.createLocations(locationList);
        // Act
        tripMapService.deleteLocation(newTestLocation.getName());
        // Assert
        List<Location> locationsFromFile = tripMapService.listLocations();
        Assertions.assertTrue(locationsFromFile.size() == locationList.size() - 1);
    }

    @Test
    public void findShortestPathTest() {
        // Arrange
        tripMapService.createLocation(testLocation);
        //Act
        int distance = tripMapService.findShortestPath(testLocation.getName(), entrance, waterfall);
        // Assert
        Assertions.assertTrue(distance == path.getWeight());
    }

    private static void deleteFile(String filePath) {
        try {
            Files.delete(java.nio.file.Path.of(filePath));
        }
        catch (NoSuchFileException ex) { }
        catch (IOException ex) {
            // File permission problems are caught here.
           ex.printStackTrace();
        }
    }
}
