package logica;

import java.util.Set;

public interface CargadorDatos {
    void cargarInventario(Set<String> idsUsados, CentroDistribucion centro);

    void cargarDepositos(ABB arbolDepositos, RedDepositos redDepositos);
}
