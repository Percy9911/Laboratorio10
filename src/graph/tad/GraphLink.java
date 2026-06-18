package graph.tad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GraphLink<V, E> implements Graph<V, E> {
    private Map<V, List<Adjacent<V, E>>> adjacencyList;

    public GraphLink() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void insertVertex(V vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<Adjacent<V, E>>());
        }
    }

    @Override
    public void insertEdge(V origin, V destination, E edgeData) {
        insertVertex(origin);
        insertVertex(destination);

        if (searchEdge(origin, destination)) {
            return;
        }

        adjacencyList.get(origin).add(new Adjacent<V, E>(destination, edgeData));
        adjacencyList.get(destination).add(new Adjacent<V, E>(origin, edgeData));
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!searchVertex(vertex)) {
            return false;
        }

        adjacencyList.remove(vertex);

        for (List<Adjacent<V, E>> adjacentList : adjacencyList.values()) {
            removeAdjacent(adjacentList, vertex);
        }

        return true;
    }

    @Override
    public boolean removeEdge(V origin, V destination) {
        if (!searchVertex(origin) || !searchVertex(destination)) {
            return false;
        }

        boolean removedFromOrigin = removeAdjacent(adjacencyList.get(origin), destination);
        boolean removedFromDestination = removeAdjacent(adjacencyList.get(destination), origin);

        return removedFromOrigin || removedFromDestination;
    }

    @Override
    public boolean searchVertex(V vertex) {
        return adjacencyList.containsKey(vertex);
    }

    @Override
    public boolean searchEdge(V origin, V destination) {
        if (!searchVertex(origin)) {
            return false;
        }

        for (Adjacent<V, E> adjacent : adjacencyList.get(origin)) {
            if (Objects.equals(adjacent.getVertex(), destination)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<V> adjacentVertices(V vertex) {
        List<V> vertices = new ArrayList<>();

        if (!searchVertex(vertex)) {
            return vertices;
        }

        for (Adjacent<V, E> adjacent : adjacencyList.get(vertex)) {
            vertices.add(adjacent.getVertex());
        }

        return vertices;
    }

    public E getEdgeData(V origin, V destination) {
        if (!searchVertex(origin)) {
            return null;
        }

        for (Adjacent<V, E> adjacent : adjacencyList.get(origin)) {
            if (Objects.equals(adjacent.getVertex(), destination)) {
                return adjacent.getEdgeData();
            }
        }

        return null;
    }

    private boolean removeAdjacent(List<Adjacent<V, E>> adjacentList, V vertex) {
        for (int i = 0; i < adjacentList.size(); i++) {
            if (Objects.equals(adjacentList.get(i).getVertex(), vertex)) {
                adjacentList.remove(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for (V vertex : adjacencyList.keySet()) {
            text.append(vertex).append(" -> ");

            for (Adjacent<V, E> adjacent : adjacencyList.get(vertex)) {
                text.append(adjacent.getVertex())
                        .append("(")
                        .append(adjacent.getEdgeData())
                        .append(") ");
            }

            text.append("\n");
        }

        return text.toString();
    }

    private static class Adjacent<V, E> {
        private V vertex;
        private E edgeData;

        public Adjacent(V vertex, E edgeData) {
            this.vertex = vertex;
            this.edgeData = edgeData;
        }

        public V getVertex() {
            return vertex;
        }

        public E getEdgeData() {
            return edgeData;
        }
    }
}
