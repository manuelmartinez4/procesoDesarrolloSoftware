package logica;

import java.util.Set;

final class RegistroDatos {
    private RegistroDatos() {
    }

    static void registrarInventario(Paquete[] paquetes, Set<String> idsUsados,
                                    CentroDistribucion centro) {
        for (Paquete paquete : paquetes) {
            String id = String.valueOf(paquete.getId());
            if (!idsUsados.contains(id)) {
                idsUsados.add(id);
                centro.recibirPaquete(new Paquete(id, paquete.getPeso(),
                        paquete.getDestino(), paquete.isUrgente(), paquete.getContenido()));
            }
        }
    }

    static void registrarDepositos(DepositoJson[] depositos, ABB arbolDepositos,
                                   RedDepositos redDepositos) {
        for (DepositoJson deposito : depositos) {
            arbolDepositos.insertar(deposito.id, deposito.auditado);
            if (deposito.conexiones != null) {
                for (int conexion : deposito.conexiones) {
                    redDepositos.agregarRuta(deposito.id, conexion);
                }
            }
        }
    }
}
