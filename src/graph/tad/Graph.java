package graph.tad;

import java.util.List;

public interface Graph<V, E> {
    void insertVertex(V vertex);

    void insertEdge(V origin, V destination, E edgeData);

    boolean removeVertex(V vertex);

    boolean removeEdge(V origin, V destination);

    boolean searchVertex(V vertex);

    boolean searchEdge(V origin, V destination);

    List<V> adjacentVertices(V vertex);
}
