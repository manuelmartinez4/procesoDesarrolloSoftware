package logica.envios;

import logica.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ControladorEnvios {
    private final CentroDistribucion centro = new CentroDistribucion();
    private final Camion camion = new Camion();
    private final Set<String> ids = new HashSet<>();
    private CargadorInventario cargador;

    public ControladorEnvios(CargadorInventario cargador) {
        cambiarCargador(cargador);
    }

    public void cambiarCargador(CargadorInventario cargador) {
        this.cargador = Objects.requireNonNull(cargador);
    }

    public void cargarInventario() {
        cargador.cargarInventario(ids, centro);
    }

    public boolean existeId(String id) {
        return id != null && ids.contains(id.trim());
    }

    public boolean cargarPaquete(
            String id,
            double peso,
            String destino,
            boolean urgente,
            String contenido) {
        return cargarPaquete(id, peso, destino, urgente, contenido, null);
    }

    public boolean cargarPaquete(String id, double peso, String destino,
                                 boolean urgente, String contenido,
                                 Paquete.ZonaDestino zona) {
        Paquete paquete = new Paquete(id, peso, destino, urgente, contenido, zona);

        if (ids.contains(paquete.getId())) {
            return false;
        }

        centro.recibirPaquete(paquete);
        ids.add(paquete.getId());
        return true;
    }

    public Paquete despacharHaciaCamion() {
        Paquete paquete = centro.despacharSiguiente();

        if (paquete != null) {
            camion.cargarPaquete(paquete);
        }

        return paquete;
    }

    public Paquete deshacerCarga() {
        Paquete paquete = camion.deshacerUltimaCarga();

        if (paquete != null) {
            centro.devolverPaquete(paquete);
        }

        return paquete;
    }

    public Paquete descargarCamion() {
        return camion.descargarPaquete();
    }

    public List<Paquete> obtenerPendientes() {
        return centro.obtenerPaquetes();
    }

    public List<Paquete> obtenerPaquetes() {
        return camion.obtenerPaquetes();
    }
}
