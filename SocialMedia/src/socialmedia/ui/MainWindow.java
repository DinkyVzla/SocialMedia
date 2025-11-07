package socialmedia.ui;

import socialmedia.model.DirectedGraph;
import socialmedia.algorithms.Kosaraju;
import socialmedia.algorithms.Kosaraju.SCCResult;
import socialmedia.io.FileSocialMedia;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * load al archivo y ver los SCC.
 */
public class MainWindow extends JFrame {

    private JTextArea area;
    private DirectedGraph graph;

    public MainWindow() {
        setTitle("Social Media Graph");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnLoad = new JButton("Load file");
        JButton btnSCC = new JButton("Find SCC");

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));
        top.add(btnLoad);
        top.add(btnSCC);

        area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // acciones de los botones
        btnLoad.addActionListener(e -> loadFile());
        btnSCC.addActionListener(e -> runSCC());
    }

    // load al archivo con JFC
    private void loadFile() {
        JFileChooser ch = new JFileChooser();
        int result = ch.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try {
                graph = FileSocialMedia.loadFromFile(f);
                area.setText("");
                area.append("File loaded: " + f.getName() + "\n\n");
                area.append("Graph:\n");
                area.append(graph.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error loading file: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // exec el Kosaraju.java y muestra los componentes
    private void runSCC() {
        if (graph == null) {
            JOptionPane.showMessageDialog(this,
                    "Load a file first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        SCCResult r = Kosaraju.findSCC(graph);
        area.append("\nStrongly Connected Components:\n");
        for (int i = 0; i < r.count; i++) {
            area.append("Component " + (i + 1) + ": ");
            int k = 0;
            while (k < r.comps[i].length && r.comps[i][k] != -1) {
                int idx = r.comps[i][k];
                area.append(graph.getUserByIndex(idx) + " ");
                k++;
            }
            area.append("\n");
        }
    }
    // show del grafo con GraphStream
    private void showGraph() {
        if (graph == null) {
            JOptionPane.showMessageDialog(this,
                    "Load a file first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Graph gg = new SingleGraph("G");

        // crear nodos
        for (int v = 0; v < graph.getVertexCount(); v++) {
            Node node = gg.addNode(String.valueOf(v));
            node.setAttribute("ui.label", graph.getUserByIndex(v));
        }

        // crear aristas dirigidas
        for (int v = 0; v < graph.getVertexCount(); v++) {
            int cnt = graph.getNeighborsCount(v);
            int[] nb = graph.getNeighborsBuffer(v);
            for (int i = 0; i < cnt; i++) {
                int w = nb[i];
                String id = v + "->" + w;
                if (gg.getEdge(id) == null) {
                    gg.addEdge(id, String.valueOf(v), String.valueOf(w), true);
                }
            }
        }

        // colores por componente usando Kosaraju
        SCCResult r = Kosaraju.findSCC(graph);
        String[] colors = { "red", "blue", "green", "magenta", "orange", "cyan", "yellow" };

        for (int c = 0; c < r.count; c++) {
            int k = 0;
            String color = colors[c % colors.length];
            while (k < r.comps[c].length && r.comps[c][k] != -1) {
                int idx = r.comps[c][k];
                Node node = gg.getNode(String.valueOf(idx));
                if (node != null) {
                    node.setAttribute("ui.style", "fill-color: " + color + ";");
                }
                k++;
            }
        }

        // para ver etiquetas
        gg.setAttribute("ui.stylesheet",
                "node { text-size: 14; text-alignment: above; }");

        gg.display();
    }
}

