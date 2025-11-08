package socialmedia.model;

/**
 * Grafo dirigido pa representar una red social
 * Usa listas de adyacencia pa guardar los users y sus conexiones
 * Cada user es un nodo y las relaciones son las flechas entre ellos
 * 
 * @author Diego 
 * @version 1.0
 */
public class DirectedGraph {

    // array de usuarios
    private String[] users;
    private int userCount;

    // pa la adyacencia - guarda los vecinos de cada user
    private int[][] adj;
    private int[] adjCount;

    /**
     * Constructor del grafo - inicializa todo con capacidad pa 8 users
     * Si se llena, se hace mas grande automaticamente
     */
    public DirectedGraph() {
        users = new String[8];
        userCount = 0;
        adj = new int[8][];
        adjCount = new int[8];
    }

    /**
     * Busca un user por su nombre y devuelve su indice
     * 
     * @param name el nombre del user a buscar
     * @return el indice del user o -1 si no existe
     */
    private int indexOfUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].equals(name)) return i;
        }
        return -1;
    }

    /**
     * Hace el array de users mas grande si esta lleno
     * Duplica el tamaño cuando es necesario
     */
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

    /**
     * Inicializa la fila de adyacencia pa un user
     * 
     * @param v el indice del user
     */
    private void initAdjRow(int v) {
        adj[v] = new int[4];
        adjCount[v] = 0;
    }

    /**
     * Hace mas grande la lista de vecinos de un user si esta llena
     * 
     * @param v el indice del user
     */
    private void ensureAdjCapacity(int v) {
        if (adj[v] == null) { initAdjRow(v); return; }
        if (adjCount[v] < adj[v].length) return;

        int newCap = adj[v].length * 2;
        int[] newRow = new int[newCap];
        for (int i = 0; i < adjCount[v]; i++) newRow[i] = adj[v][i];
        adj[v] = newRow;
    }

    /**
     * Agrega un nuevo user al grafo si no existe
     * 
     * @param name el nombre del user a agregar
     */
    public void addUser(String name) {
        if (indexOfUser(name) != -1) return;
        ensureUsersCapacity();
        users[userCount] = name;
        initAdjRow(userCount);
        userCount++;
    }

    /**
     * Agrega una relacion de un user a otro
     * Si ya existe la relacion, no hace nada
     * 
     * @param from el user que sigue
     * @param to el user que es seguido
     */
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

    /**
     * Cuantos users hay en el grafo
     * 
     * @return el numero de users
     */
    public int getVertexCount() { return userCount; }

    /**
     * Devuelve el nombre de un user por su indice
     * 
     * @param index el indice del user
     * @return el nombre del user
     */
    public String getUserByIndex(int index) { return users[index]; }

    /**
     * Devuelve los vecinos de un user (a quien sigue)
     * 
     * @param v el indice del user
     * @return array con los indices de los users que sigue
     */
    public int[] getNeighbors(int v) {
        int n = adjCount[v];
        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = adj[v][i];
        return out;
    }

    /**
     * Devuelve el array interno de vecinos de un user
     * Mas eficiente pa recorrer pero puede tener espacios vacios
     * 
     * @param v el indice del user
     * @return el array interno de vecinos
     */
    public int[] getNeighborsBuffer(int v) { return adj[v]; }

    /**
     * Cuantos vecinos tiene un user
     * 
     * @param v el indice del user
     * @return cuantos users sigue
     */
    public int getNeighborsCount(int v) { return adjCount[v]; }

    /**
     * Convierte el grafo a string pa mostrarlo
     * Formato: "indice [user] -> [vecinos]"
     * 
     * @return string con todo el grafo
     */
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