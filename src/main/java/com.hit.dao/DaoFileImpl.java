package com.hit.dao;

import com.hit.dm.Location;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DaoFileImpl implements IDao {
    private final String pathToResources = "src/main/resources/";
    private final String fileName;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public DaoFileImpl(String fileName) {
        this.fileName = pathToResources + fileName;
    }

    @Override
    public void saveLocations(List<Location> locationList) {
        try {
            writer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            for (Location location : locationList) {
                writer.writeObject(location);
            }
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
            reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            while ((locationFromFile = (Location) reader.readObject()) != null) {
                if (locationFromFile.getName().equals(locationName)) {
                    return locationFromFile;
                }
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Location> listLocations() {
        List<Location> locationList = new ArrayList<>();
        try (FileInputStream fi = new FileInputStream(fileName)) {
            Location location = null;
            reader = new ObjectInputStream(new BufferedInputStream(fi));
            while ((location = (Location) reader.readObject()) != null) {
                locationList.add(location);
            }
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
