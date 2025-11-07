package socialmedia;

import javax.swing.SwingUtilities;
import socialmedia.ui.MainWindow;

/**
 * Punto de entrada: abre la ventana principal.
 */
public class SocialMedia {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow w = new MainWindow();
                w.setVisible(true);
            }
        });
    }
}
