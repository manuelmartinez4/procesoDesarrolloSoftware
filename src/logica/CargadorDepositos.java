package logica;

@FunctionalInterface
public interface CargadorDepositos {
    /**
     * Carga los depósitos y sus conexiones bidireccionales en la red.
     * Respeta el estado de auditoría inicial y conserva los depósitos
     * ya existentes. Una fuente vacía no modifica las estructuras.
     */
    void cargarDepositos(ABB arbolDepositos, RedDepositos redDepositos);
}
