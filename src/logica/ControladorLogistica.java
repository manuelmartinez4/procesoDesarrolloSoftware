package logica;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ControladorLogistica {

    private final CentroDistribucion centro = new CentroDistribucion();
    private final Camion camion = new Camion();
    private final ABB arbolDepositos = new ABB();
    private final RedDepositos redDepositos = new RedDepositos(100);
    private final Set<String> idsUsados = new HashSet<>();

    private CargadorInventario cargadorInventario;
    private CargadorDepositos cargadorDepositos;

    public ControladorLogistica(
            CargadorInventario cargadorInventario,
            CargadorDepositos cargadorDepositos) {

        this.cargadorInventario = cargadorInventario;
        this.cargadorDepositos = cargadorDepositos;
    }

    public void cambiarCargadores(
            CargadorInventario inventario,
            CargadorDepositos depositos) {

        this.cargadorInventario = inventario;
        this.cargadorDepositos = depositos;
    }

    public void cargarInventario() {
        cargadorInventario.cargarInventario(idsUsados, centro);
    }

    public void cargarDepositos() {
        cargadorDepositos.cargarDepositos(arbolDepositos, redDepositos);
    }

    public boolean existeId(String id) {
        return idsUsados.contains(id);
    }

    public boolean cargarPaquete(
            String id,
            double peso,
            String destino,
            boolean urgente,
            String contenido) {

        Paquete paquete = new Paquete(
                id, peso, destino, urgente, contenido);

        return RegistroDatos.registrarPaquete(
                paquete, idsUsados, centro);
    }

    public Paquete despacharHaciaCamion() {
        Paquete paquete = centro.despacharSiguiente();

        if (paquete != null) {
            camion.cargarPaquete(paquete);
        }

        return paquete;
    }

    public Paquete deshacerCarga() {
        return camion.deshacerUltimaCarga();
    }

    public Paquete descargarCamion() {
        return camion.descargarPaquete();
    }

    public List<Paquete> obtenerPaquetes() {
        return camion.obtenerPaquetes();
    }

    public void insertarDeposito(int id) {
        arbolDepositos.insertar(id, false);
    }

    public void auditarDepositos() {
        arbolDepositos.auditarDepositos();
    }

    public List<Deposito> obtenerDepositosPorNivel(int nivel) {
        return arbolDepositos.obtenerDepositosPorNivel(nivel);
    }

    public Deposito buscarDeposito(int id) {
        return arbolDepositos.buscar(id);
    }

    public int calcularRuta(int origen, int destino) {
        return redDepositos.cantidadSaltos(origen, destino);
    }
}
