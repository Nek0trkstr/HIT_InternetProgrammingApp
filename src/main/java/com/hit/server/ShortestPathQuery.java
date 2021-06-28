package com.hit.server;

import com.hit.dm.Place;
import com.hit.graph.Vertex;

public class ShortestPathQuery {
    private String locationName;
    private Vertex source;
    private Vertex destination;

    public ShortestPathQuery(String locationName, Vertex source, Vertex destination) {
        this.locationName = locationName;
        this.source = source;
        this.destination = destination;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getDestination() {
        return this.destination;
    }
}
