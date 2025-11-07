package socialmedia.ui;

import socialmedia.model.DirectedGraph;
import socialmedia.algorithms.Kosaraju;
import socialmedia.algorithms.Kosaraju.SCCResult;
import socialmedia.io.FileSocialMedia;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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

    // muestra los componentes del kosaraju
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
}

