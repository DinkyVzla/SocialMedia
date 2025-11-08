/*
   @author JuanFerreira 
   @author AlejandroSimanca
 * @version 1.0
*/

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

public class MainWindow extends JFrame {

    private JTextArea area;
    private DirectedGraph graph;
    private JButton btnLoad;
    private JButton btnSCC;
    private JButton btnSave;
    private JButton btnAddUser;
    private JButton btnAddRel;
    private JButton btnDelUser;
    private JButton btnShow;
    private boolean modified;

    public MainWindow() {
        setTitle("Social Media Graph");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnLoad = new JButton("Load file");
        btnSCC = new JButton("Find SCC");
        btnSave = new JButton("Save file");
        btnAddUser = new JButton("Add user");
        btnAddRel = new JButton("Add relation");
        btnDelUser = new JButton("Delete user");
        btnShow = new JButton("Show Graph");

        // bttons
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(2, 4));  // Usamos GridLayout para mejorar el orden
        top.add(btnLoad);      // Cargar archivo
        top.add(btnSCC);       // Encontrar CFC
        top.add(btnSave);      // Guardar archivo
        top.add(btnAddUser);   // Agregar usuario
        top.add(btnAddRel);    // Agregar relación
        top.add(btnDelUser);   // Eliminar usuario
        top.add(btnShow);      // Mostrar grafo

        area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Inicializamos el grafo
        graph = new DirectedGraph();
        modified = false; // Estado inicial del grafo

        // CARGAR ARCHIVO INICIAL AUTOMÁTICAMENTE
        loadInitialFile();

        // Acciones de los botones
        btnLoad.addActionListener(e -> loadFile());
        btnSCC.addActionListener(e -> runSCC());
        btnSave.addActionListener(e -> saveFile());
        btnAddUser.addActionListener(e -> addUser());
        btnAddRel.addActionListener(e -> addRelation());
        btnDelUser.addActionListener(e -> deleteUser());
        btnShow.addActionListener(e -> showGraph());
    }

    /**
     * script para abrir el archivo inicial
     */
    private void loadInitialFile() {
    try {
        
        java.net.URL resourceUrl = getClass().getClassLoader().getResource("red_social.txt");
        
        if (resourceUrl != null) {
            
            File defaultFile = new File(resourceUrl.toURI());
            graph = FileSocialMedia.loadFromFile(defaultFile);
            area.append("File uploaded automatically: red_social.txt\n\n");
            area.append("Loaded graph:\n");
            area.append(graph.toString());
            modified = false;
        } else {
            
            File[] possibleLocations = {
                new File("red_social.txt"), 
                new File("src/red_social.txt"), 
                new File("src/socialmedia/red_social.txt") 
            };
            
            boolean loaded = false;
            for (File file : possibleLocations) {
                if (file.exists()) {
                    graph = FileSocialMedia.loadFromFile(file);
                    area.append("File uploaded automatically: " + file.getPath() + "\n\n");
                    area.append("Loaded graph:\n");
                    area.append(graph.toString());
                    modified = false;
                    loaded = true;
                    break;
                }
            }
            
            if (!loaded) {
                area.setText("Error loading initial file: 'red_social.txt'\n");
                area.append("Use the 'Load file' button to upload a file manually.\n");
                area.append("Or place the file in the project folder.\n");
            }
        }
        
    } catch (Exception ex) {
        area.setText("Error loading initial file: " + ex.getMessage());
    }
}

    // script para cargar el archivo
    private void loadFile() {
        if (modified) {
            int option = JOptionPane.showConfirmDialog(this, 
                "There are unsaved changes. Do you want to save before uploading a new file?", 
                "Unsaved changes", 
                JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                saveFile(); // Guardar antes de cargar
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return; // Cancelar la operación
            }
            // If not continuar sin guardar
        }
        
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
                modified = false; // Después de cargar, no hay cambios sin guardar
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // exec korasaju y components
    private void runSCC() {
        if (graph == null) {
            JOptionPane.showMessageDialog(this, "Load a file first.", "Warning", JOptionPane.WARNING_MESSAGE);
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

    // Show the graph
private void showGraph() {
    if (graph == null) {
        JOptionPane.showMessageDialog(this, "Load a file first.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // config GS
    System.setProperty("org.graphstream.ui", "swing");
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

    Graph gg = new SingleGraph("G");

    // Create nodos
    for (int v = 0; v < graph.getVertexCount(); v++) {
        String userName = graph.getUserByIndex(v);
        Node node = gg.addNode(userName);
        node.setAttribute("ui.label", userName);
    }

    // Create aristas dirigidas
    for (int v = 0; v < graph.getVertexCount(); v++) {
        String fromUser = graph.getUserByIndex(v);
        int cnt = graph.getNeighborsCount(v);
        int[] nb = graph.getNeighborsBuffer(v);
        for (int i = 0; i < cnt; i++) {
            String toUser = graph.getUserByIndex(nb[i]);
            String edgeId = fromUser + "->" + toUser;
            if (gg.getEdge(edgeId) == null) {
                gg.addEdge(edgeId, fromUser, toUser, true);
            }
        }
    }

    // Colors for components usando kosaraju
    SCCResult r = Kosaraju.findSCC(graph);
    String[] colors = { "red", "blue", "green", "magenta", "orange", "cyan", "yellow" };

    for (int c = 0; c < r.count; c++) {
        int k = 0;
        String color = colors[c % colors.length];
        while (k < r.comps[c].length && r.comps[c][k] != -1) {
            int idx = r.comps[c][k];
            String userName = graph.getUserByIndex(idx);
            Node node = gg.getNode(userName);
            if (node != null) {
                node.setAttribute("ui.style", "fill-color: " + color + ";");
            }
            k++;
        }
    }

    // Para ver etiquetas
    gg.setAttribute("ui.stylesheet", "node { text-size: 14; text-alignment: above; }");

    try {
        gg.display();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error showing graph: " + e.getMessage(), 
            "Graph Display Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // Agg new user
    private void addUser() {
        String name = JOptionPane.showInputDialog(this, "Enter user name:");
        if (name != null && !name.trim().isEmpty()) {
            graph.addUser(name.trim());
            modified = true;
            refreshText();
        }
    }

    // Agg new relacion 
    private void addRelation() {
        String from = JOptionPane.showInputDialog(this, "Enter source user:");
        if (from == null || from.trim().isEmpty()) return;
        
        String to = JOptionPane.showInputDialog(this, "Enter target user:");
        if (to == null || to.trim().isEmpty()) return;

        graph.addRelation(from.trim(), to.trim());
        modified = true;
        refreshText();
    }

    // Delete el user (y sus relaciones) creando un grafo nuevo
    private void deleteUser() {
        if (graph == null) {
            JOptionPane.showMessageDialog(this, "No graph loaded.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = JOptionPane.showInputDialog(this, "User to delete:");
        if (name == null || name.trim().isEmpty()) return; // Si es cancelado o vacío

        name = name.trim(); // Delete espacios al inicio y fin

        // Crear un nuevo grafo sin el usuario
        DirectedGraph newGraph = new DirectedGraph();
        int n = graph.getVertexCount();

        // Copiar usuarios excepto el que se elimina
        for (int i = 0; i < n; i++) {
            String u = graph.getUserByIndex(i);
            if (!u.equals(name)) {
                newGraph.addUser(u);
            }
        }

        // Copiar relaciones que no involucren al usuario eliminado
        for (int i = 0; i < n; i++) {
            String from = graph.getUserByIndex(i);
            int cnt = graph.getNeighborsCount(i);
            int[] nb = graph.getNeighborsBuffer(i);
            for (int j = 0; j < cnt; j++) {
                String to = graph.getUserByIndex(nb[j]);
                if (!from.equals(name) && !to.equals(name)) {
                    newGraph.addRelation(from, to);
                }
            }
        }

        graph = newGraph; // update el grafo
        modified = true;  // Alert cambio
        refreshText();    // update el area de texto
    }

    // uopdate el text area para mostrar el grafo
    private void refreshText() {
        if (graph != null) {
            area.setText("Graph:\n" + graph.toString());
        } else {
            area.setText("No graph loaded.");
        }

        // show de cambios no guardados
        if (modified) {
            area.append("\n* There are unsaved changes.");
        }
    }

    // Método save el archivo
    private void saveFile() {
        if (graph == null) {
            JOptionPane.showMessageDialog(this, "Nothing to save (no graph loaded).", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser ch = new JFileChooser();
        int result = ch.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try {
                FileSocialMedia.saveToFile(graph, f); 
                modified = false; 
                JOptionPane.showMessageDialog(this, "File saved successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}