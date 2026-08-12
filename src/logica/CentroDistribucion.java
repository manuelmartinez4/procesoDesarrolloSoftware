package logica;



import java.util.LinkedList;
import java.util.Queue;

public class CentroDistribucion {
    // Usamos dos colas para separar la prioridad
    private Queue<Paquete<String>> colaPrioridad = new LinkedList<>();
    private Queue<Paquete<String>> colaEstandar = new LinkedList<>();

    public void recibirPaquete(Paquete<String> p) {
        if (p.isUrgente() || p.getPeso() > 50.0) {      // 3 (isUrgente + getPeso + OR)
            colaPrioridad.add(p);                       // 1
        } else {
            colaEstandar.add(p);                        // 1
        }
    }

// Conteo de instrucciones:
// f(n) = 3 + 1
// f(n) = 4

// Complejidad asintótica:
// f(n) = O(1)
// f(n) <= c * 1
// 4 <= c
// Elegimos c = 4

// Verificación desde qué n0 se cumple:
// n = 1: 4 <= 4 ✓ CUMPLE
// Por lo tanto: f(n) pertenece a O(1) con c = 4 y n0 = 1


    // Operación: Despachar Siguiente
    public Paquete<String> despacharSiguiente() {
        if (!colaPrioridad.isEmpty()) {                 // 2
            return colaPrioridad.poll();                // 1
        }
        return colaEstandar.poll();                     // 1
    }

// Conteo de instrucciones (Peor caso, entra al if):
// f(n) = 2 + 1
// f(n) = 3

// Complejidad asintótica:
// f(n) = O(1)
// f(n) <= c * 1
// 3 <= c
// Elegimos c = 3

// Verificación desde qué n0 se cumple:
// n = 1: 3 <= 3 ✓ CUMPLE
// Por lo tanto: f(n) pertenece a O(1) con c = 3 y n0 = 1

    public boolean estaVacio() {
        return colaPrioridad.isEmpty() && colaEstandar.isEmpty();
    }
}
