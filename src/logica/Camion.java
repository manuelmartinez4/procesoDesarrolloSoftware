package logica;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class Camion {

    // Pila que representa los paquetes cargados en el camión.
    // LIFO: el último paquete cargado es el primero en descargarse.
    private Stack<Paquete> pilaDePaquetes = new Stack<>();

    // Operación: Cargar Paquete
    public void cargarPaquete(Paquete paquete) {
        pilaDePaquetes.push(paquete);                   // 1
    }

// Conteo de instrucciones:
// f(n) = 1

// Complejidad asintótica:
// f(n) = O(1)
// f(n) <= c * 1
// 1 <= c
// Elegimos c = 1

// Verificación:
// n = 1: 1 <= 1 ✓ CUMPLE
// Por lo tanto: f(n) pertenece a O(1) con c = 1 y n0 = 1


    // Operación: Descargar Paquete / Deshacer Carga
    public Paquete descargarPaquete() {
        if (pilaDePaquetes.isEmpty()) {                 // 1
            return null;                                // 1 (Mejor caso)
        }
        return pilaDePaquetes.pop();                    // 1 (Peor caso)
    }
    public Paquete deshacerUltimaCarga() {
        return descargarPaquete();
    }

    public List<Paquete> obtenerPaquetes() {
        return new ArrayList<>(pilaDePaquetes);
    }
}
