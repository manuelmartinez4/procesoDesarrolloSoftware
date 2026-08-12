package logica;

import java.util.Stack;

public class Camion {

    // Pila que representa los paquetes cargados en el camión.
    // LIFO: el último paquete cargado es el primero en descargarse.
    private Stack<Paquete<String>> pilaDePaquetes = new Stack<>();

    // Operación: Cargar Paquete
    public void cargarPaqueteAlCamion(Paquete<String> paquete) {
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
    public Paquete<String> descargarPaqueteDelCamion() {
        if (pilaDePaquetes.isEmpty()) {                 // 1
            return null;                                // 1 (Mejor caso)
        }
        return pilaDePaquetes.pop();                    // 1 (Peor caso)
    }

// Conteo de instrucciones (Peor caso):
// f(n) = 1 + 1
// f(n) = 2

// Complejidad asintótica:
// f(n) = O(1)
// f(n) <= c * 1
// 2 <= c
// Elegimos c = 2

// Verificación:
// n = 1: 2 <= 2 ✓ CUMPLE
// Por lo tanto: f(n) pertenece a O(1) con c = 2 y n0 = 1


    // Elimina la última carga realizada en caso de error
    public Paquete<String> deshacerUltimaCargaDelCamion() {
    if (pilaDePaquetes.isEmpty()) {
        return null;
    }
    return pilaDePaquetes.pop();
}

    // Operación: Mostrar Paquetes
    public void mostrarPaquetesDelCamion() {
        if (pilaDePaquetes.isEmpty()) {                         // 1
            System.out.println("El camión está vacío.");        // 1
        } else {
            for (Paquete<String> paquete : pilaDePaquetes) {    // 1 + 3n (Init + n comparaciones + 2n asignaciones)
                System.out.println(paquete);                    // n * 1 = n
            }
        }
    }

// Conteo de instrucciones (Peor caso, con 'n' paquetes):
// f(n) = 1 + (1 + 3n) + n
// f(n) = 2 + 4n

// Complejidad asintótica:
// f(n) = O(n)
// f(n) <= c * n
// 2 + 4n <= c * n
// Dividimos por n:
// 2/n + 4 <= c
// Acotamos: 4 + 1 = 5
// Elegimos c = 5

// Verificación:
// n = 1: 2/1 + 4 = 6 <= 5  ✗ NO CUMPLE
// n = 2: 2/2 + 4 = 5 <= 5  ✓ CUMPLE
// Por lo tanto: f(n) pertenece a O(n) con c = 5 y n0 = 2

}