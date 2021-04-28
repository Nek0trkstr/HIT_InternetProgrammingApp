package com.hit.dm;

import java.io.Serializable;
import java.util.HashSet;
import com.hit.graph.Graph;

public class Location extends Graph implements Serializable {
    private String name;
    private String description;

    public Location(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public void addPlace(Place place) {
        addVertex(place);
        System.out.println();
    }

    public void addPath(Path path) {
        addEdge(path);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
