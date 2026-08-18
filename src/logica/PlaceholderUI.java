package logica;

import javax.swing.*;

public class PlaceholderUI {

    public static void mostrar() {
        JFrame ventana = new JFrame();
        JLabel texto = new JLabel("texto de ejemplo", SwingConstants.CENTER);

        ventana.add(texto);

        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
}