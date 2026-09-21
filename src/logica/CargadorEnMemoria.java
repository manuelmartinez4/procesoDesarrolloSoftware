package logica;

import java.util.Set;

public class CargadorEnMemoria implements CargadorDatos {
    private final Paquete[] inventario;
    private final DepositoJson[] depositos;

    public CargadorEnMemoria(Paquete[] inventario, DepositoJson[] depositos) {
        this.inventario = inventario.clone();
        this.depositos = depositos.clone();
    }

    @Override
    public void cargarInventario(Set<String> idsUsados, CentroDistribucion centro) {
        RegistroDatos.registrarInventario(inventario, idsUsados, centro);
        System.out.println("Inventario cargado exitosamente.");
    }

    @Override
    public void cargarDepositos(ABB arbolDepositos, RedDepositos redDepositos) {
        RegistroDatos.registrarDepositos(depositos, arbolDepositos, redDepositos);
        System.out.println("Depósitos cargados exitosamente.");
    }
}
