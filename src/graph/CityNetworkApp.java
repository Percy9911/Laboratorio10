package graph;

public class CityNetworkApp {
    public static void main(String[] args) {
        CityNetwork network = new CityNetwork();

        network.addCity("Arequipa");
        network.addCity("Cusco");
        network.addCity("Puno");
        network.addCity("Tacna");
        network.addCity("Moquegua");

        network.addRoad("Arequipa", "Cusco", 510);
        network.addRoad("Arequipa", "Moquegua", 230);
        network.addRoad("Moquegua", "Tacna", 160);
        network.addRoad("Cusco", "Puno", 390);
        network.addRoad("Puno", "Tacna", 420);

        network.printCities();
        network.printRoads();
        network.printConnections();
        network.printShortestPath("Arequipa", "Tacna");
    }
}
