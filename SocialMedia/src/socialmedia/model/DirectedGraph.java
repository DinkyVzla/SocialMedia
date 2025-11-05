package socialmedia.model;

public class DirectedGraph {

    // Arreglo de nombres de usuario
    private String[] users;
    private int userCount;

    // Lista de adyacencia con arreglos dinámicos
    private int[][] adj;      // adj[v] = arreglo de vecinos de v
    private int[] adjCount;   // cuántos vecinos tiene cada v

    public DirectedGraph() {
        users = new String[8];     // capacidad inicial
        userCount = 0;
        adj = new int[8][];        // filas se crean al agregar usuario
        adjCount = new int[8];
    }

    // Busca el índice de un usuario por nombre (O(n))
    private int indexOfUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // Asegura capacidad para agregar un usuario más (dobla tamaño)
    private void ensureUsersCapacity() {
        if (userCount < users.length) return;

        int newCap = users.length * 2;
        // users
        String[] newUsers = new String[newCap];
        for (int i = 0; i < userCount; i++) newUsers[i] = users[i];
        users = newUsers;

        // adj (copiar referencias de filas existentes)
        int[][] newAdj = new int[newCap][];
        for (int i = 0; i < userCount; i++) newAdj[i] = adj[i];
        adj = newAdj;

        // adjCount
        int[] newAdjCount = new int[newCap];
        for (int i = 0; i < userCount; i++) newAdjCount[i] = adjCount[i];
        adjCount = newAdjCount;
    }

    // Crea una fila de adyacencia con capacidad inicial
    private void initAdjRow(int v) {
        adj[v] = new int[4];  // capacidad inicial de 4 vecinos
        adjCount[v] = 0;
    }

    // Asegura capacidad para agregar un vecino más a v (dobla tamaño)
    private void ensureAdjCapacity(int v) {
        if (adj[v] == null) {
            initAdjRow(v);
            return;
        }
        if (adjCount[v] < adj[v].length) return;

        int newCap = adj[v].length * 2;
        int[] newRow = new int[newCap];
        for (int i = 0; i < adjCount[v]; i++) newRow[i] = adj[v][i];
        adj[v] = newRow;
    }

    // Agrega un usuario si no existe (crece dinámicamente)
    public void addUser(String name) {
        if (indexOfUser(name) != -1) return; // ya existe

        ensureUsersCapacity();
        users[userCount] = name;
        initAdjRow(userCount);
        userCount = userCount + 1;
    }

    // Agrega una arista dirigida from -> to (evita duplicados)
    public void addRelation(String from, String to) {
        int iFrom = indexOfUser(from);
        int iTo   = indexOfUser(to);

        if (iFrom == -1 || iTo == -1) {
            System.out.println("Cannot add relation: user not found.");
            return;
        }

        // evitar duplicado
        for (int i = 0; i < adjCount[iFrom]; i++) {
            if (adj[iFrom][i] == iTo) return;
        }

        ensureAdjCapacity(iFrom);
        adj[iFrom][adjCount[iFrom]] = iTo;
        adjCount[iFrom] = adjCount[iFrom] + 1;
    }

    // -------- getters básicos para usar en otras clases --------

    public int getVertexCount() {
        return userCount;
    }

    public String getUserByIndex(int index) {
        return users[index];
    }

    // Devuelve una COPIA recortada de los vecinos de v
    public int[] getNeighbors(int v) {
        int n = adjCount[v];
        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = adj[v][i];
        return out;
    }

    // Devuelve el buffer interno (ÚSALO CON CUIDADO)
    public int[] getNeighborsBuffer(int v) {
        return adj[v];
    }

    public int getNeighborsCount(int v) {
        return adjCount[v];
    }

    @Override
    public String toString() {
        String s = "";
        for (int v = 0; v < userCount; v++) {
            s = s + v + " [" + users[v] + "] -> [";
            for (int i = 0; i < adjCount[v]; i++) {
                s = s + adj[v][i];
                if (i + 1 < adjCount[v]) s = s + ", ";
            }
            s = s + "]\n";
        }
        return s;
    }
}
