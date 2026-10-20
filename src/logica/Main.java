package logica;

import vista.VistaLogistica;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        CargadorJson cargador = new CargadorJson();

        ControladorLogistica controlador =
                new ControladorLogistica(cargador, cargador);

        if (args.length > 0 && "--consola".equals(args[0])) {
            new MenuPrincipal(controlador).iniciarMenu();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("LOGI-UADE | Gestión logística");

            ventana.setContentPane(new VistaLogistica(
                    controlador.getEnvios(),
                    controlador.getDepositos()));

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.pack();
            ventana.setMinimumSize(ventana.getSize());
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
