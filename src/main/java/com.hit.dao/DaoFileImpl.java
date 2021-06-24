package com.hit.dao;

import com.hit.dm.Location;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DaoFileImpl implements IDao {
    private final String pathToResources = "src/main/resources/";
    private final String fileName;
    private BufferedWriter writer;
    private Gson gson = new Gson();
    private BufferedReader reader;

    public DaoFileImpl(String fileName) {
        this.fileName = pathToResources + fileName;
    }

    @Override
    public void saveLocations(List<Location> locationList) {
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
//            for (Location location : locationList) {
//                String locationJson = gson.toJson(location);
//                writer.write(locationJson);
//            }
            gson.toJson(locationList, writer);
//            writer.write(locationsJson);
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(String.format("File: %s was not found", fileName));
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveLocation(Location location) {
        List<Location> locationsList = listLocations();
        locationsList.add(location);
        saveLocations(locationsList);
    }

    @Override
    public Location getLocation(String locationName) {
        Location locationFromFile = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String text = reader.lines().collect(Collectors.joining());
            gson.fromJson(text, new TypeToken<List<Location>>(){}.getType() );
            List<Location> locations = gson.fromJson(text, new TypeToken<List<Location>>(){}.getType() );
            for (Location candidateLocation: locations) {
                if (candidateLocation.getName().equals(locationName)) {
                    locationFromFile = candidateLocation;
                }
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return locationFromFile;
    }

    @Override
    public List<Location> listLocations() {
        List<Location> locationList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String text = reader.lines().collect(Collectors.joining());
            locationList = gson.fromJson(text, new TypeToken<List<Location>>(){}.getType() );
        }
        catch (EOFException e) { }
        catch (FileNotFoundException e) { }
        catch (Exception e) {
            e.printStackTrace();
        }

        return locationList;
    }

    @Override
    public void deleteLocation(String locationToDeleteName) {
        // Get all locations
        List<Location> locationList = listLocations();

        // Find location to delete
        for (Location location : locationList) {
            if (location.getName().equals(locationToDeleteName)) {
                locationList.remove(location);
                break;
            }
        }

        // Overwrite the file with locations
        saveLocations(locationList);
    }

    @Override
    public void editLocation(String locationToEditName, Location newLocation) {
        // Get all locations
        List<Location> locationList = listLocations();

        // Edit location
        for (Location location : locationList) {
            if (location.getName().equals(locationToEditName)) {
                location = newLocation;
                break;
            }
        }

        // Overwrite the file with locations
        saveLocations(locationList);
    }
}
