package logica;

import logica.envios.ControladorEnvios;
import logica.depositos.ControladorDepositos;
import java.util.List;

// Conserva la API que utiliza el menú de consola.
public class ControladorLogistica {
    private final ControladorEnvios envios;
    private final ControladorDepositos depositos;

    public ControladorLogistica(
            CargadorInventario inventario,
            CargadorDepositos depositos) {

        this.envios = new ControladorEnvios(inventario);
        this.depositos = new ControladorDepositos(depositos);
    }

    public ControladorEnvios getEnvios() {
        return envios;
    }

    public ControladorDepositos getDepositos() {
        return depositos;
    }

    public void cambiarCargadores(
            CargadorInventario inventario,
            CargadorDepositos depositos) {

        envios.cambiarCargador(inventario);
        this.depositos.cambiarCargador(depositos);
    }

    public void cargarInventario() {
        envios.cargarInventario();
    }

    public boolean existeId(String id) {
        return envios.existeId(id);
    }

    public boolean cargarPaquete(
            String id,
            double peso,
            String destino,
            boolean urgente,
            String contenido) {

        return envios.cargarPaquete(
                id, peso, destino, urgente, contenido);
    }

    public Paquete despacharHaciaCamion() {
        return envios.despacharHaciaCamion();
    }

    public Paquete deshacerCarga() {
        return envios.deshacerCarga();
    }

    public Paquete descargarCamion() {
        return envios.descargarCamion();
    }

    public List<Paquete> obtenerPaquetes() {
        return envios.obtenerPaquetes();
    }

    public void cargarDepositos() {
        depositos.cargarDepositos();
    }

    public void insertarDeposito(int id) {
        depositos.insertarDeposito(id);
    }

    public void auditarDepositos() {
        depositos.auditarDepositos();
    }

    public List<Deposito> obtenerDepositosPorNivel(int nivel) {
        return depositos.obtenerDepositosPorNivel(nivel);
    }

    public Deposito buscarDeposito(int id) {
        return depositos.buscarDeposito(id);
    }

    public int calcularRuta(int origen, int destino) {
        return depositos.calcularRuta(origen, destino);
    }
}
