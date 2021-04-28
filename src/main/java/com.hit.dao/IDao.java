package com.hit.dao;
import com.hit.dm.Location;

import java.util.List;

public interface IDao {
    public void saveLocation(Location location);
    public void saveLocations(List<Location> locationList);
    public Location getLocation(String locationName);
    public List<Location> listLocations();
    public void deleteLocation(String locationName);
    public void editLocation(String locationToEditName, Location newLocation);
}
