package socialmedia.model;

public class DirectedGraph {

    // name de users
    private String[] users;
    private int userCount;

    // Adyacencia: vecinos por vértice y cantidad
    private int[][] adj;
    private int[] adjCount;

    public DirectedGraph() {
        users = new String[8];
        userCount = 0;
        adj = new int[8][];
        adjCount = new int[8];
    }

    // id del user
    private int indexOfUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].equals(name)) return i;
        }
        return -1;
    }

    // ++ la capacidad para mas users
    private void ensureUsersCapacity() {
        if (userCount < users.length) return;

        int newCap = users.length * 2;

        String[] newUsers = new String[newCap];
        for (int i = 0; i < userCount; i++) newUsers[i] = users[i];
        users = newUsers;

        int[][] newAdj = new int[newCap][];
        for (int i = 0; i < userCount; i++) newAdj[i] = adj[i];
        adj = newAdj;

        int[] newAdjCount = new int[newCap];
        for (int i = 0; i < userCount; i++) newAdjCount[i] = adjCount[i];
        adjCount = newAdjCount;
    }

    // Crea fila de adyacencia
    private void initAdjRow(int v) {
        adj[v] = new int[4];
        adjCount[v] = 0;
    }

    // Duplica capacidad de vecinos de v
    private void ensureAdjCapacity(int v) {
        if (adj[v] == null) { initAdjRow(v); return; }
        if (adjCount[v] < adj[v].length) return;

        int newCap = adj[v].length * 2;
        int[] newRow = new int[newCap];
        for (int i = 0; i < adjCount[v]; i++) newRow[i] = adj[v][i];
        adj[v] = newRow;
    }

    //Agg user si no existe
    public void addUser(String name) {
        if (indexOfUser(name) != -1) return;
        ensureUsersCapacity();
        users[userCount] = name;
        initAdjRow(userCount);
        userCount++;
    }

    // agg arista dirigida from to
    public void addRelation(String from, String to) {
        int iFrom = indexOfUser(from);
        int iTo = indexOfUser(to);
        if (iFrom == -1 || iTo == -1) {
            System.out.println("Cannot add relation: user not found.");
            return;
        }
        for (int i = 0; i < adjCount[iFrom]; i++) {
            if (adj[iFrom][i] == iTo) return;
        }
        ensureAdjCapacity(iFrom);
        adj[iFrom][adjCount[iFrom]] = iTo;
        adjCount[iFrom]++;
    }

    // getters 

    public int getVertexCount() { return userCount; }

    public String getUserByIndex(int index) { return users[index]; }

    // Copia de vecinos de v
    public int[] getNeighbors(int v) {
        int n = adjCount[v];
        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = adj[v][i];
        return out;
    }


    public int[] getNeighborsBuffer(int v) { return adj[v]; }

    public int getNeighborsCount(int v) { return adjCount[v]; }

    @Override
    public String toString() {
        String s = "";
        for (int v = 0; v < userCount; v++) {
            s += v + " [" + users[v] + "] -> [";
            for (int i = 0; i < adjCount[v]; i++) {
                s += adj[v][i];
                if (i + 1 < adjCount[v]) s += ", ";
            }
            s += "]\n";
        }
        return s;
    }
}
