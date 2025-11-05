package socialmedia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectedGraph {

    // Save del inbdice del user por el nombre
    private Map<String, Integer> userIndex;
    
    private List<String> users;
    // List adyacencia a cada user que otros sigue
    private List<List<Integer>> adjacency;

    public DirectedGraph() {
        userIndex = new HashMap<String, Integer>();
        users = new ArrayList<String>();
        adjacency = new ArrayList<List<Integer>>();
    }

    // agg usuario
    public void addUser(String name) {
        if (userIndex.containsKey(name)) {
            return; 
        }

        int index = users.size(); 
        users.add(name);  
        userIndex.put(name, index); // save del nombre e indice
        adjacency.add(new ArrayList<Integer>());
    }

    // Agg una relacion (seguimiento) entre dos usuarios
    public void addRelation(String from, String to) {
        Integer fromIndex = userIndex.get(from);
        Integer toIndex = userIndex.get(to);

        if (fromIndex == null || toIndex == null) {
            return; 
        }

        List<Integer> neighbors = adjacency.get(fromIndex);
        if (!neighbors.contains(toIndex)) {
            neighbors.add(toIndex); // Agg user al destino como vecino
        }
    }

    // Otros métodos como obtener el número de vértices, vecinos, etc.
    public int getVertexCount() {
        return users.size();
    }

    public String getUserByIndex(int index) {
        return users.get(index);
    }

    public List<Integer> getNeighbors(int index) {
        return adjacency.get(index);
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < users.size(); i++) {
            result += i + " [" + users.get(i) + "] -> " + adjacency.get(i) + "\n";
        }
        return result;
    }
}

