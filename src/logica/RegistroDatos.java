package logica;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class RegistroDatos {
    private RegistroDatos() {
    }

    static void registrarInventario(Paquete[] paquetes, Set<String> idsUsados,
                                    CentroDistribucion centro) {
        if (paquetes == null) {
            throw new IllegalArgumentException("Falta la lista de paquetes.");
        }

        // Validar toda la carga antes de modificar las estructuras.
        List<Paquete> nuevos = new ArrayList<>();
        Set<String> idsValidados = new HashSet<>(idsUsados);
        for (Paquete datos : paquetes) {
            if (datos == null) {
                throw new IllegalArgumentException("El inventario contiene un paquete nulo.");
            }
            Paquete paquete = new Paquete(datos.getId(), datos.getPeso(),
                    datos.getDestino(), datos.isUrgente(), datos.getContenido(),
                    datos.getZonaDestino());
            if (idsValidados.add(paquete.getId())) nuevos.add(paquete);
        }
        for (Paquete paquete : nuevos) {
            registrarPaquete(paquete, idsUsados, centro);
        }
    }

    static void registrarDepositos(DepositoJson[] depositos, ABB arbolDepositos,
                                   RedDepositos redDepositos) {
        if (depositos == null) {
            throw new IllegalArgumentException("Falta la lista de depósitos.");
        }

        Set<Integer> idsDisponibles = new HashSet<>();
        for (Deposito existente : arbolDepositos.obtenerTodos()) {
            idsDisponibles.add(existente.getId());
        }
        for (DepositoJson deposito : depositos) {
            if (deposito == null || deposito.id < 0) {
                throw new IllegalArgumentException("Cada depósito necesita un ID no negativo.");
            }
            idsDisponibles.add(deposito.id);
        }
        for (DepositoJson deposito : depositos) {
            if (deposito.conexiones == null) continue;
            for (int conexion : deposito.conexiones) {
                if (conexion == deposito.id || !idsDisponibles.contains(conexion)) {
                    throw new IllegalArgumentException("Conexión inválida: "
                            + deposito.id + " → " + conexion + ".");
                }
            }
        }

        // El ABB conserva los depósitos ya registrados y sus auditorías.
        for (DepositoJson deposito : depositos) {
            arbolDepositos.insertar(deposito.id, deposito.nombre, deposito.auditado);
        }
        for (DepositoJson deposito : depositos) {
            if (deposito.conexiones == null) continue;
            for (int conexion : deposito.conexiones) {
                redDepositos.agregarRuta(deposito.id, conexion);
            }
        }
    }

    static boolean registrarPaquete(Paquete paquete, Set<String> idsUsados,
                                    CentroDistribucion centro) {
        if (idsUsados.contains(paquete.getId())) return false;
        centro.recibirPaquete(paquete);
        idsUsados.add(paquete.getId());
        return true;
    }
}
