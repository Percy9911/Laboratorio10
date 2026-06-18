package graph.tad;

public class Exercise3App {
    public static void main(String[] args) {
        GraphLink<String, Integer> graph = new GraphLink<>();

        graph.insertVertex("Arequipa");
        graph.insertVertex("Cusco");
        graph.insertVertex("Puno");
        graph.insertVertex("Tacna");
        graph.insertVertex("Moquegua");

        graph.insertEdge("Arequipa", "Cusco", 510);
        graph.insertEdge("Arequipa", "Moquegua", 230);
        graph.insertEdge("Moquegua", "Tacna", 160);
        graph.insertEdge("Cusco", "Puno", 390);
        graph.insertEdge("Puno", "Tacna", 420);

        System.out.println("Grafo con listas de adyacencia:");
        System.out.println(graph);

        System.out.println("Existe Arequipa? " + graph.searchVertex("Arequipa"));
        System.out.println("Existe carretera Arequipa - Tacna? " + graph.searchEdge("Arequipa", "Tacna"));
        System.out.println("Adyacentes de Moquegua: " + graph.adjacentVertices("Moquegua"));
        System.out.println("Distancia Arequipa - Moquegua: "
                + graph.getEdgeData("Arequipa", "Moquegua") + " km");

        graph.removeEdge("Puno", "Tacna");
        graph.removeVertex("Cusco");

        System.out.println("\nGrafo despues de eliminar carretera Puno-Tacna y ciudad Cusco:");
        System.out.println(graph);
    }
}
