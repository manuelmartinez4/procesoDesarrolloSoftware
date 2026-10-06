package logica;

import java.util.Set;

final class RegistroDatos {
    private RegistroDatos() {
    }

    static void registrarInventario(
            Paquete[] paquetes,
            Set<String> idsUsados,
            CentroDistribucion centro) {
    
        for (Paquete datos : paquetes) {
            Paquete paquete = new Paquete(
                    String.valueOf(datos.getId()),
                    datos.getPeso(),
                    datos.getDestino(),
                    datos.isUrgente(),
                    datos.getContenido()
            );
    
            registrarPaquete(paquete, idsUsados, centro);
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
    static boolean registrarPaquete(
        Paquete paquete,
        Set<String> idsUsados,
        CentroDistribucion centro) {

        if (idsUsados.contains(paquete.getId())) {
            return false;
        }
    
        centro.recibirPaquete(paquete);
        idsUsados.add(paquete.getId());
        return true;
    }
}
