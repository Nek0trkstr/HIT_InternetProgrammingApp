package com.hit.server;

import com.hit.dm.Place;

public class ShortestPathQuery {
    private String locationName;
    private Place source;
    private Place destination;

    public ShortestPathQuery(String locationName, Place source, Place destination) {
        this.locationName = locationName;
        this.source = source;
        this.destination = destination;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public Place getSource() {
        return this.source;
    }

    public Place getDestination() {
        return this.destination;
    }
}
