package logica;



import java.util.LinkedList;
import java.util.Queue;

public class CentroDistribucion {
    // Usamos dos colas para separar la prioridad
    private Queue<Paquete> colaPrioridad = new LinkedList<>();
    private Queue<Paquete> colaEstandar = new LinkedList<>();

    public void recibirPaquete(Paquete paquete) {
        if (paquete.isUrgente() || paquete.getPeso() > 50.0) {
            colaPrioridad.add(paquete);
        } else {
            colaEstandar.add(paquete);
        }
    }



    // Operación: Despachar Siguiente
    public Paquete despacharSiguiente() {
        if (!colaPrioridad.isEmpty()) {                 // 2
            return colaPrioridad.poll();                // 1
        }
        return colaEstandar.poll();                     // 1
    }

}
