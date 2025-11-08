package socialmedia.algorithms;

/*
@author JuanFerreira
 * @version 1.0
*/
import socialmedia.model.DirectedGraph;

public class Kosaraju {

    public static class SCCResult {
        public int[][] comps; // = nodos del componente i
        public int count;    
    }

    public static SCCResult findSCC(DirectedGraph g) {
        int n = g.getVertexCount();
        boolean[] visited = new boolean[n];
        int[] stack = new int[n];
        int stackSize = 0;

        //DFS para llenar pila
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                stackSize = dfsOriginal(g, v, visited, stack, stackSize);
            }
        }

        //grafo transpuesto
        int[][] T = new int[n][n];
        int[] tCnt = new int[n];
        for (int v = 0; v < n; v++) {
            int cnt = g.getNeighborsCount(v);
            int[] nb = g.getNeighborsBuffer(v);
            for (int i = 0; i < cnt; i++) {
                int w = nb[i];
                T[w][tCnt[w]] = v;
                tCnt[w]++;
            }
        }

        // 3) DFS en transpuesto follow a la pila
        for (int i = 0; i < n; i++) visited[i] = false;

        int[][] comps = new int[n][n + 1]; // 
        int compCount = 0;
        int[] temp = new int[n];

        for (int i = stackSize - 1; i >= 0; i--) {
            int v = stack[i];
            if (!visited[v]) {
                int size = dfsTransposedCollect(T, tCnt, v, visited, temp, 0);
                for (int k = 0; k < size; k++) comps[compCount][k] = temp[k];
                comps[compCount][size] = -1; 
                compCount++;
            }
        }

        SCCResult r = new SCCResult();
        r.comps = comps;
        r.count = compCount;
        return r;
    }

    private static int dfsOriginal(DirectedGraph g, int v, boolean[] visited,
                                   int[] stack, int stackSize) {
        visited[v] = true;
        int cnt = g.getNeighborsCount(v);
        int[] nb = g.getNeighborsBuffer(v);
        for (int i = 0; i < cnt; i++) {
            int w = nb[i];
            if (!visited[w]) {
                stackSize = dfsOriginal(g, w, visited, stack, stackSize);
            }
        }
        stack[stackSize] = v;
        return stackSize + 1;
    }

    private static int dfsTransposedCollect(int[][] T, int[] tCnt, int v,
                                            boolean[] visited, int[] acc, int accSize) {
        visited[v] = true;
        acc[accSize] = v;
        accSize++;
        for (int i = 0; i < tCnt[v]; i++) {
            int w = T[v][i];
            if (!visited[w]) {
                accSize = dfsTransposedCollect(T, tCnt, w, visited, acc, accSize);
            }
        }
        return accSize;
    }
}
