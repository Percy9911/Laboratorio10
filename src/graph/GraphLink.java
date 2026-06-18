package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import listlinked.ListLinked;

public class GraphLink<E> {
    private ListLinked<AdjList<E>> graph;

    public GraphLink() {
        graph = new ListLinked<>();
    }

    public void insertVertex(E data) {
        if (findVertex(data) != null) {
            return;
        }

        Vertex<E> vertex = new Vertex<>(data);
        graph.addLast(new AdjList<>(vertex));
    }

    private AdjList<E> findVertex(E data) {
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            if (Objects.equals(adj.getVertex().getData(), data)) {
                return adj;
            }
        }

        return null;
    }

    public void insertEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);

        if (v1 == null || v2 == null || hasEdge(v1, destination)) {
            return;
        }

        v1.getEdges().addLast(new Edge<>(v2.getVertex()));
        v2.getEdges().addLast(new Edge<>(v1.getVertex()));
    }

    public void insertEdgeWeight(E origin, E destination, int weight) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);

        if (v1 == null || v2 == null || hasEdge(v1, destination)) {
            return;
        }

        v1.getEdges().addLast(new Edge<>(v2.getVertex(), weight));
        v2.getEdges().addLast(new Edge<>(v1.getVertex(), weight));
    }

    public boolean removeVertex(E data) {
        AdjList<E> vertexToRemove = findVertex(data);

        if (vertexToRemove == null) {
            return false;
        }

        for (int i = 0; i < graph.size(); i++) {
            removeEdgeTo(graph.get(i), data);
        }

        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);

            if (Objects.equals(adj.getVertex().getData(), data)) {
                graph.removeAt(i);
                return true;
            }
        }

        return false;
    }

    public boolean removeEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);

        if (v1 == null || v2 == null) {
            return false;
        }

        boolean removedFromOrigin = removeEdgeTo(v1, destination);
        boolean removedFromDestination = removeEdgeTo(v2, origin);

        return removedFromOrigin || removedFromDestination;
    }

    public ListLinked<E> BFS(E start) {
        ListLinked<E> result = new ListLinked<>();
        AdjList<E> startVertex = findVertex(start);

        if (startVertex == null) {
            return result;
        }

        Set<E> visited = new HashSet<>();
        Queue<AdjList<E>> queue = new LinkedList<>();

        visited.add(startVertex.getVertex().getData());
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            AdjList<E> current = queue.poll();
            result.addLast(current.getVertex().getData());

            for (int i = 0; i < current.getEdges().size(); i++) {
                E neighborData = current.getEdges().get(i).getDestination().getData();

                if (!visited.contains(neighborData)) {
                    visited.add(neighborData);
                    queue.add(findVertex(neighborData));
                }
            }
        }

        return result;
    }

    public ListLinked<E> DFS(E start) {
        ListLinked<E> result = new ListLinked<>();
        AdjList<E> startVertex = findVertex(start);

        if (startVertex == null) {
            return result;
        }

        DFS(startVertex, new HashSet<E>(), result);
        return result;
    }

    public boolean isConnected() {
        if (graph.isEmpty()) {
            return true;
        }

        E firstVertex = graph.get(0).getVertex().getData();
        return BFS(firstVertex).size() == graph.size();
    }

    public ListLinked<E> shortPath(E origin, E destination) {
        ListLinked<E> result = new ListLinked<>();

        if (findVertex(origin) == null || findVertex(destination) == null) {
            return result;
        }

        Map<E, Integer> distances = new HashMap<>();
        Map<E, E> previous = new HashMap<>();
        Set<E> visited = new HashSet<>();
        PriorityQueue<PathNode<E>> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.getDistance(), b.getDistance())
        );

        for (int i = 0; i < graph.size(); i++) {
            distances.put(graph.get(i).getVertex().getData(), Integer.MAX_VALUE);
        }

        distances.put(origin, 0);
        queue.add(new PathNode<>(origin, 0));

        while (!queue.isEmpty()) {
            PathNode<E> pathNode = queue.poll();
            E currentData = pathNode.getData();

            if (visited.contains(currentData)) {
                continue;
            }

            visited.add(currentData);

            if (Objects.equals(currentData, destination)) {
                break;
            }

            AdjList<E> current = findVertex(currentData);

            for (int i = 0; i < current.getEdges().size(); i++) {
                Edge<E> edge = current.getEdges().get(i);
                E neighborData = edge.getDestination().getData();
                int newDistance = distances.get(currentData) + edge.getWeight();

                if (newDistance < distances.get(neighborData)) {
                    distances.put(neighborData, newDistance);
                    previous.put(neighborData, currentData);
                    queue.add(new PathNode<>(neighborData, newDistance));
                }
            }
        }

        if (distances.get(destination) == Integer.MAX_VALUE) {
            return result;
        }

        LinkedList<E> path = new LinkedList<>();
        E current = destination;

        while (current != null) {
            path.addFirst(current);

            if (Objects.equals(current, origin)) {
                break;
            }

            current = previous.get(current);
        }

        if (path.isEmpty() || !Objects.equals(path.getFirst(), origin)) {
            return result;
        }

        for (E data : path) {
            result.addLast(data);
        }

        return result;
    }

    private void DFS(AdjList<E> current, Set<E> visited, ListLinked<E> result) {
        E currentData = current.getVertex().getData();

        visited.add(currentData);
        result.addLast(currentData);

        for (int i = 0; i < current.getEdges().size(); i++) {
            E neighborData = current.getEdges().get(i).getDestination().getData();

            if (!visited.contains(neighborData)) {
                DFS(findVertex(neighborData), visited, result);
            }
        }
    }

    private boolean hasEdge(AdjList<E> origin, E destination) {
        for (int i = 0; i < origin.getEdges().size(); i++) {
            E currentDestination = origin.getEdges().get(i).getDestination().getData();

            if (Objects.equals(currentDestination, destination)) {
                return true;
            }
        }

        return false;
    }

    private boolean removeEdgeTo(AdjList<E> origin, E destination) {
        for (int i = 0; i < origin.getEdges().size(); i++) {
            E currentDestination = origin.getEdges().get(i).getDestination().getData();

            if (Objects.equals(currentDestination, destination)) {
                origin.getEdges().removeAt(i);
                return true;
            }
        }

        return false;
    }

    private static class PathNode<T> {
        private T data;
        private int distance;

        public PathNode(T data, int distance) {
            this.data = data;
            this.distance = distance;
        }

        public T getData() {
            return data;
        }

        public int getDistance() {
            return distance;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            sb.append(adj.getVertex()).append(" -> ");

            for (int j = 0; j < adj.getEdges().size(); j++) {
                Edge<E> edge = adj.getEdges().get(j);
                sb.append(edge);

                if (edge.getWeight() != 1) {
                    sb.append("(").append(edge.getWeight()).append(")");
                }

                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}
