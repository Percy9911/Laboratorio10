package graph;

import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class CityNetwork {
    private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;

    public CityNetwork() {
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    // Agrega una ciudad como vertice del grafo.
    public void addCity(String city) {
        graph.addVertex(city);
    }

    // Agrega una carretera no dirigida con distancia en kilometros.
    public void addRoad(String origin, String destination, double distance) {
        graph.addVertex(origin);
        graph.addVertex(destination);

        DefaultWeightedEdge edge = graph.addEdge(origin, destination);

        if (edge != null) {
            graph.setEdgeWeight(edge, distance);
        }
    }

    public Set<String> getCities() {
        return graph.vertexSet();
    }

    public Set<DefaultWeightedEdge> getRoads() {
        return graph.edgeSet();
    }

    public GraphPath<String, DefaultWeightedEdge> shortestPath(String origin, String destination) {
        return DijkstraShortestPath.findPathBetween(graph, origin, destination);
    }

    public void printCities() {
        System.out.println("Ciudades:");

        for (String city : getCities()) {
            System.out.println("- " + city);
        }
    }

    public void printRoads() {
        System.out.println("\nCarreteras registradas:");

        for (DefaultWeightedEdge edge : getRoads()) {
            String origin = graph.getEdgeSource(edge);
            String destination = graph.getEdgeTarget(edge);
            double distance = graph.getEdgeWeight(edge);

            System.out.println(origin + " - " + destination + ": " + distance + " km");
        }
    }

    public void printConnections() {
        System.out.println("\nConexiones del grafo:");

        for (String city : getCities()) {
            System.out.print(city + " -> ");

            for (DefaultWeightedEdge edge : graph.edgesOf(city)) {
                String neighbor = graph.getEdgeSource(edge).equals(city)
                        ? graph.getEdgeTarget(edge)
                        : graph.getEdgeSource(edge);

                System.out.print(neighbor + "(" + graph.getEdgeWeight(edge) + " km) ");
            }

            System.out.println();
        }
    }

    public void printShortestPath(String origin, String destination) {
        GraphPath<String, DefaultWeightedEdge> path = shortestPath(origin, destination);

        System.out.println("\nCamino mas corto de " + origin + " a " + destination + ":");

        if (path == null) {
            System.out.println("No existe una ruta entre las ciudades.");
            return;
        }

        System.out.println("Ruta: " + path.getVertexList());
        System.out.println("Costo total: " + path.getWeight() + " km");
    }
}
