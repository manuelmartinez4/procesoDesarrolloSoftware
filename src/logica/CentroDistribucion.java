package logica;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CentroDistribucion {
    private final Deque<Paquete> prioridad = new ArrayDeque<>();
    private final Deque<Paquete> estandar = new ArrayDeque<>();

    private Deque<Paquete> cola(Paquete paquete) {
        return paquete.esPrioritario() ? prioridad : estandar;
    }

    public void recibirPaquete(Paquete paquete) {
        cola(paquete).addLast(paquete);
    }

    public void devolverPaquete(Paquete paquete) {
        cola(paquete).addFirst(paquete);
    }

    public Paquete despacharSiguiente() {
        return !prioridad.isEmpty()
                ? prioridad.pollFirst()
                : estandar.pollFirst();
    }

    public List<Paquete> obtenerPaquetes() {
        List<Paquete> resultado = new ArrayList<>(prioridad);
        resultado.addAll(estandar);
        return resultado;
    }
}
