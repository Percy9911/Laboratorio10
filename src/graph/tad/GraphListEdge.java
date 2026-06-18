package graph.tad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;

public class GraphListEdge<V, E> implements Graph<V, E> {
    private List<V> vertices;
    private List<EdgeData<V, E>> edges;

    public GraphListEdge() {
        vertices = new ArrayList<V>();
        edges = new ArrayList<EdgeData<V, E>>();
    }

    @Override
    public void insertVertex(V vertex) {
        if (!searchVertex(vertex)) {
            vertices.add(vertex);
        }
    }

    @Override
    public void insertEdge(V origin, V destination, E edgeData) {
        insertVertex(origin);
        insertVertex(destination);

        if (!searchEdge(origin, destination)) {
            edges.add(new EdgeData<V, E>(origin, destination, edgeData));
        }
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!searchVertex(vertex)) {
            return false;
        }

        vertices.remove(vertex);

        for (int i = edges.size() - 1; i >= 0; i--) {
            EdgeData<V, E> edge = edges.get(i);

            if (Objects.equals(edge.getOrigin(), vertex) || Objects.equals(edge.getDestination(), vertex)) {
                edges.remove(i);
            }
        }

        return true;
    }

    @Override
    public boolean removeEdge(V origin, V destination) {
        for (int i = 0; i < edges.size(); i++) {
            EdgeData<V, E> edge = edges.get(i);

            if (Objects.equals(edge.getOrigin(), origin) && Objects.equals(edge.getDestination(), destination)) {
                edges.remove(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean searchVertex(V vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public boolean searchEdge(V origin, V destination) {
        for (EdgeData<V, E> edge : edges) {
            if (Objects.equals(edge.getOrigin(), origin) && Objects.equals(edge.getDestination(), destination)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<V> adjacentVertices(V vertex) {
        List<V> adjacent = new ArrayList<V>();

        for (EdgeData<V, E> edge : edges) {
            if (Objects.equals(edge.getOrigin(), vertex)) {
                adjacent.add(edge.getDestination());
            }
        }

        return adjacent;
    }

    public List<V> getVertices() {
        return new ArrayList<V>(vertices);
    }

    public List<EdgeData<V, E>> getEdges() {
        return new ArrayList<EdgeData<V, E>>(edges);
    }

    public boolean isConexo() {
        if (vertices.isEmpty()) {
            return true;
        }

        Set<V> visited = new HashSet<V>();
        Queue<V> queue = new LinkedList<V>();

        queue.add(vertices.get(0));
        visited.add(vertices.get(0));

        while (!queue.isEmpty()) {
            V current = queue.poll();

            for (V neighbor : undirectedAdjacentVertices(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited.size() == vertices.size();
    }

    public boolean isPlano() {
        int vertexCount = vertices.size();
        int edgeCount = undirectedEdgeCount();

        if (vertexCount <= 4) {
            return true;
        }

        if (edgeCount > 3 * vertexCount - 6) {
            return false;
        }

        if (containsK5()) {
            return false;
        }

        return !containsK33();
    }

    public boolean isIsomorfo(GraphListEdge<?, ?> other) {
        if (vertices.size() != other.vertices.size() || edges.size() != other.edges.size()) {
            return false;
        }

        boolean[][] first = directedMatrix();
        boolean[][] second = other.directedMatrix();
        int[] permutation = new int[vertices.size()];
        boolean[] used = new boolean[vertices.size()];

        return isomorphicBacktracking(0, permutation, used, first, second);
    }

    public boolean isAutoComplementario() {
        return isIsomorfo(complement());
    }

    public GraphListEdge<V, E> complement() {
        GraphListEdge<V, E> complement = new GraphListEdge<V, E>();

        for (V vertex : vertices) {
            complement.insertVertex(vertex);
        }

        for (V origin : vertices) {
            for (V destination : vertices) {
                if (!Objects.equals(origin, destination) && !searchEdge(origin, destination)) {
                    complement.insertEdge(origin, destination, null);
                }
            }
        }

        return complement;
    }

    private boolean isomorphicBacktracking(int position, int[] permutation, boolean[] used,
            boolean[][] first, boolean[][] second) {
        if (position == permutation.length) {
            return sameShape(permutation, first, second);
        }

        for (int i = 0; i < permutation.length; i++) {
            if (!used[i]) {
                used[i] = true;
                permutation[position] = i;

                if (isomorphicBacktracking(position + 1, permutation, used, first, second)) {
                    return true;
                }

                used[i] = false;
            }
        }

        return false;
    }

    private boolean sameShape(int[] permutation, boolean[][] first, boolean[][] second) {
        for (int i = 0; i < permutation.length; i++) {
            for (int j = 0; j < permutation.length; j++) {
                if (first[i][j] != second[permutation[i]][permutation[j]]) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean[][] directedMatrix() {
        boolean[][] matrix = new boolean[vertices.size()][vertices.size()];

        for (EdgeData<V, E> edge : edges) {
            int origin = vertices.indexOf(edge.getOrigin());
            int destination = vertices.indexOf(edge.getDestination());

            matrix[origin][destination] = true;
        }

        return matrix;
    }

    private List<V> undirectedAdjacentVertices(V vertex) {
        List<V> adjacent = new ArrayList<V>();

        for (EdgeData<V, E> edge : edges) {
            if (Objects.equals(edge.getOrigin(), vertex)) {
                adjacent.add(edge.getDestination());
            }

            if (Objects.equals(edge.getDestination(), vertex)) {
                adjacent.add(edge.getOrigin());
            }
        }

        return adjacent;
    }

    private int undirectedEdgeCount() {
        Set<String> uniqueEdges = new HashSet<String>();

        for (EdgeData<V, E> edge : edges) {
            int origin = vertices.indexOf(edge.getOrigin());
            int destination = vertices.indexOf(edge.getDestination());
            int min = Math.min(origin, destination);
            int max = Math.max(origin, destination);

            uniqueEdges.add(min + "-" + max);
        }

        return uniqueEdges.size();
    }

    private boolean containsUndirectedEdge(int first, int second) {
        V origin = vertices.get(first);
        V destination = vertices.get(second);

        return searchEdge(origin, destination) || searchEdge(destination, origin);
    }

    private boolean containsK5() {
        if (vertices.size() < 5) {
            return false;
        }

        int n = vertices.size();

        for (int a = 0; a < n - 4; a++) {
            for (int b = a + 1; b < n - 3; b++) {
                for (int c = b + 1; c < n - 2; c++) {
                    for (int d = c + 1; d < n - 1; d++) {
                        for (int e = d + 1; e < n; e++) {
                            int[] group = {a, b, c, d, e};

                            if (isCompleteGroup(group)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isCompleteGroup(int[] group) {
        for (int i = 0; i < group.length; i++) {
            for (int j = i + 1; j < group.length; j++) {
                if (!containsUndirectedEdge(group[i], group[j])) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean containsK33() {
        if (vertices.size() < 6) {
            return false;
        }

        int n = vertices.size();

        for (int a = 0; a < n - 5; a++) {
            for (int b = a + 1; b < n - 4; b++) {
                for (int c = b + 1; c < n - 3; c++) {
                    for (int d = c + 1; d < n - 2; d++) {
                        for (int e = d + 1; e < n - 1; e++) {
                            for (int f = e + 1; f < n; f++) {
                                int[] group = {a, b, c, d, e, f};

                                if (hasK33Partition(group)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean hasK33Partition(int[] group) {
        for (int a = 0; a < group.length - 2; a++) {
            for (int b = a + 1; b < group.length - 1; b++) {
                for (int c = b + 1; c < group.length; c++) {
                    int[] left = {group[a], group[b], group[c]};
                    int[] right = remaining(group, left);

                    if (isCompleteBipartite(left, right)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int[] remaining(int[] group, int[] selected) {
        int[] result = new int[3];
        int index = 0;

        for (int value : group) {
            if (value != selected[0] && value != selected[1] && value != selected[2]) {
                result[index] = value;
                index++;
            }
        }

        return result;
    }

    private boolean isCompleteBipartite(int[] left, int[] right) {
        for (int i = 0; i < left.length; i++) {
            for (int j = 0; j < right.length; j++) {
                if (!containsUndirectedEdge(left[i], right[j])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for (V vertex : vertices) {
            text.append(vertex).append(" -> ").append(adjacentVertices(vertex)).append("\n");
        }

        return text.toString();
    }

    public static class EdgeData<V, E> {
        private V origin;
        private V destination;
        private E edgeData;

        public EdgeData(V origin, V destination, E edgeData) {
            this.origin = origin;
            this.destination = destination;
            this.edgeData = edgeData;
        }

        public V getOrigin() {
            return origin;
        }

        public V getDestination() {
            return destination;
        }

        public E getEdgeData() {
            return edgeData;
        }

        @Override
        public String toString() {
            return origin + " -> " + destination + " (" + edgeData + ")";
        }
    }
}
