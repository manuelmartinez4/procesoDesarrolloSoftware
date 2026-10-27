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
        // Un depósito agregado manualmente comienza sin auditar.
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
