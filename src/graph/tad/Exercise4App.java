package graph.tad;

public class Exercise4App {
    public static void main(String[] args) {
        GraphListEdge<String, Integer> graph = new GraphListEdge<String, Integer>();
        GraphListEdge<String, Integer> other = new GraphListEdge<String, Integer>();

        graph.insertEdge("A", "B", 1);
        graph.insertEdge("B", "C", 1);
        graph.insertEdge("C", "D", 1);
        graph.insertEdge("D", "A", 1);

        other.insertEdge("W", "X", 1);
        other.insertEdge("X", "Y", 1);
        other.insertEdge("Y", "Z", 1);
        other.insertEdge("Z", "W", 1);

        System.out.println("Grafo dirigido:");
        System.out.println(graph);

        System.out.println("Es isomorfo con el segundo grafo? " + graph.isIsomorfo(other));
        System.out.println("Es plano? " + graph.isPlano());
        System.out.println("Es conexo? " + graph.isConexo());
        System.out.println("Es auto complementario? " + graph.isAutoComplementario());

        System.out.println("\nComplemento del grafo:");
        System.out.println(graph.complement());
    }
}
