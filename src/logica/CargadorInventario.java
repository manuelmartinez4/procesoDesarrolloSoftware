package logica;

import java.util.Set;

@FunctionalInterface
public interface CargadorInventario {
    /**
     * Carga los paquetes de la fuente en el centro y registra sus IDs.
     * Omite los IDs ya registrados; repetir una carga no duplica paquetes.
     * Conserva los datos y el orden de ingreso de los paquetes nuevos.
     */
    void cargarInventario(Set<String> idsUsados, CentroDistribucion centro);
}
