package com.hit.dm;

import java.io.Serializable;
import com.hit.graph.Edge;

public class Path extends Edge implements Serializable {
    public Path(Place source, Place destination, int weight)
    {
        super(source, destination, weight);
    }
}
