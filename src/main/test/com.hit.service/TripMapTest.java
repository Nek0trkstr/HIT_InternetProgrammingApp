package com.hit.service;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.algorithm.*;
import com.hit.dao.*;
import com.hit.dm.Location;
import com.hit.dm.Path;
import com.hit.dm.Place;
import com.hit.graph.GraphPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

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
//        deleteFile(storageFileName);
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

    @Test
    public void findShortestPathTest() {
        // Arrange
        tripMapService.createLocation(testLocation);
        //Act
        GraphPath calculatedPath = tripMapService.findShortestPath(testLocation.getName(), entrance, waterfall);
        // Assert
        Assertions.assertTrue(calculatedPath.getWeight() == path.getWeight());
    }

    @Test
    public void toJsonAndBackPath() {
        // Arrange
        Gson gson = new Gson();
        // Act
        String jsonPath = new Gson().toJson(path);
        Path deserializedPath = gson.fromJson(jsonPath, Path.class);
        // Assert
        Assertions.assertTrue(deserializedPath.equals(path));
    }

    @Test
    public void toJsonAndBackPathList() {
        // Arrange
        Gson gson = new Gson();
        List<Path> pathList = new ArrayList<>();
        pathList.add(path);
        Type pathListType = new TypeToken<List<Path>>(){}.getType();
        // Act
        String pathListJson = gson.toJson(pathList);
        List<Path> deserializedPathList = gson.fromJson(pathListJson, pathListType);
        // Assert
        Assertions.assertTrue(deserializedPathList.equals(pathList));
    }

    @Test
    public void toJsonAndBackPlace() {
        // Arrange
        Gson gson = new Gson();
        // Act
        String jsonPlace = gson.toJson(waterfall);
        Place deserializedPlace = gson.fromJson(jsonPlace, Place.class);
        // Assert
        Assertions.assertTrue(deserializedPlace.equals(waterfall));
    }

    @Test
    public void toJsonAndBackPlaceList() {
        // Arrange
        Gson gson = new Gson();
        List<Place> placeList = new ArrayList<>();
        placeList.add(waterfall);
        placeList.add(entrance);
        Type placeListType = new TypeToken<List<Place>>(){}.getType();
        // Act
        String placeListJson = gson.toJson(placeList);
        List<Place> deserializedPlace = gson.fromJson(placeListJson, placeListType);
        // Assert
        Assertions.assertTrue(deserializedPlace.equals(placeList));
    }

    @Test
    public void toJsonAndBackLocation() {
        // Arrange
        Gson gson = new Gson();
        // Act
        String jsonLocation = gson.toJson(testLocation);
        Location deserializedLocation = gson.fromJson(jsonLocation, Location.class);
        // Assert
        Assertions.assertTrue(deserializedLocation.equals(testLocation));
    }

    @Test
    public void toJsonAndBackLocationList() {
        // Arrange
        Gson gson = new Gson();
        List<Location> locationList = new ArrayList<>();
        locationList.add(testLocation);
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        // Act
        String jsonLocationList = gson.toJson(locationList);
        List<Location> deserializedLocationList = gson.fromJson(jsonLocationList, type);
        // Assert
        Assertions.assertTrue(deserializedLocationList.equals(locationList));
    }
}
