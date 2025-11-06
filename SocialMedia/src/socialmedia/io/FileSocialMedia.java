package socialmedia.io;

import socialmedia.model.DirectedGraph;
import java.io.*;

/**
 * load y save a grafos desde txt
 */
public class FileSocialMedia {

    public static DirectedGraph loadFromFile(File f) throws IOException {
        DirectedGraph g = new DirectedGraph();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            boolean readUsers = false;
            boolean readRelations = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) continue;

                if (line.equalsIgnoreCase("usuarios") || line.equalsIgnoreCase("users")) {
                    readUsers = true; readRelations = false; continue;
                }
                if (line.equalsIgnoreCase("relaciones") || line.equalsIgnoreCase("relations")) {
                    readUsers = false; readRelations = true; continue;
                }

                if (readUsers) g.addUser(line);
                else if (readRelations) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        g.addRelation(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } finally {
            if (br != null) br.close();
        }
        return g;
    }

    public static void saveToFile(DirectedGraph g, File f) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
            bw.write("usuarios"); bw.newLine();
            int n = g.getVertexCount();
            for (int i = 0; i < n; i++) {
                bw.write(g.getUserByIndex(i));
                bw.newLine();
            }
            bw.newLine(); bw.write("relaciones"); bw.newLine();
            for (int v = 0; v < n; v++) {
                String from = g.getUserByIndex(v);
                int cnt = g.getNeighborsCount(v);
                int[] nb = g.getNeighborsBuffer(v);
                for (int i = 0; i < cnt; i++) {
                    bw.write(from + ", " + g.getUserByIndex(nb[i]));
                    bw.newLine();
                }
            }
        } finally {
            if (bw != null) bw.close();
        }
    }
}

