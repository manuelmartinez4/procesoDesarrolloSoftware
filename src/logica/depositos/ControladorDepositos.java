package logica.depositos;

import logica.ABB;
import logica.CargadorDepositos;
import logica.Deposito;
import logica.RedDepositos;

import java.util.List;
import java.util.Objects;

public class ControladorDepositos {

    private final ABB arbolDepositos = new ABB();
    private final RedDepositos redDepositos = new RedDepositos(100);

    private CargadorDepositos cargador;

    public ControladorDepositos(CargadorDepositos cargador) {
        cambiarCargador(cargador);
    }

    // Cambia la fuente de carga sin borrar los depósitos existentes.
    public void cambiarCargador(CargadorDepositos cargador) {
        this.cargador = Objects.requireNonNull(
                cargador, "El cargador de depósitos no puede ser nulo.");
    }

    public void cargarDepositos() {
        cargador.cargarDepositos(arbolDepositos, redDepositos);
    }

    public void insertarDeposito(int id) {
        insertarDeposito(id, "Depósito " + id);
    }

    public void insertarDeposito(int id, String nombre) {
        validarId(id);
        if (arbolDepositos.buscar(id) != null) {
            throw new IllegalArgumentException("Ya existe el depósito " + id + ".");
        }
        arbolDepositos.insertar(id, nombre, false);
    }

    public void auditarDepositos() {
        arbolDepositos.auditarDepositos();
    }

    public List<Deposito> obtenerDepositosPorNivel(int nivel) {
        return arbolDepositos.obtenerDepositosPorNivel(nivel);
    }

    public Deposito buscarDeposito(int id) {
        validarId(id);
        return arbolDepositos.buscar(id);
    }

    public int calcularRuta(int origen, int destino) {
        exigirDeposito(origen);
        exigirDeposito(destino);
        if (origen == destino) return 0;
        return redDepositos.cantidadSaltos(origen, destino);
    }

    public void conectarDepositos(int origen, int destino) {
        exigirDeposito(origen);
        exigirDeposito(destino);
        redDepositos.agregarRuta(origen, destino);
    }

    public List<Deposito> obtenerTodos() {
        return arbolDepositos.obtenerTodos();
    }

    public int contarDepositos() {
        return arbolDepositos.contarDepositos();
    }

    public List<Integer> obtenerConexiones(int id) {
        exigirDeposito(id);
        return redDepositos.obtenerConexiones(id);
    }

    private void exigirDeposito(int id) {
        if (buscarDeposito(id) == null) {
            throw new IllegalArgumentException("No existe el depósito " + id + ".");
        }
    }

    private void validarId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID debe ser mayor o igual que cero.");
        }
    }
}
