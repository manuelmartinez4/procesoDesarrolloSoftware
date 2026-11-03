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
                    controlador.getDepositos(),
                    cargador,
                    crearDatosDemo()));

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.pack();
            ventana.setMinimumSize(ventana.getSize());
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }

    private static CargadorEnMemoria crearDatosDemo() {
        Paquete[] paquetes = {
                new Paquete("DEMO-1", 12, "Zona Sur", false, "Libros"),
                new Paquete("DEMO-2", 60, "Córdoba", false, "Herramientas", Paquete.ZonaDestino.CENTRO),
                new Paquete("DEMO-3", 5, "Zona Norte", true, "Documentos")
        };
        DepositoJson[] depositos = {
                depositoDemo(50, "Hub Central Buenos Aires", true, 20, 80),
                depositoDemo(20, "Depósito Córdoba", false, 10),
                depositoDemo(80, "Depósito Rosario", false),
                depositoDemo(10, "Depósito Mendoza", true)
        };
        return new CargadorEnMemoria(paquetes, depositos);
    }

    private static DepositoJson depositoDemo(int id, String nombre,
                                             boolean auditado, int... conexiones) {
        DepositoJson datos = new DepositoJson();
        datos.id = id;
        datos.nombre = nombre;
        datos.auditado = auditado;
        datos.conexiones = conexiones;
        return datos;
    }
}
