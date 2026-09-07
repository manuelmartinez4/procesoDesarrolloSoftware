package logica;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MenuPrincipal {

    private Scanner scanner = new Scanner(System.in);
    private CentroDistribucion centro = new CentroDistribucion();
    private Camion camion = new Camion();
    private ABB arbolDepositos = new ABB();
    private RedDepositos redDepositos = new RedDepositos(100);
    private CargadorJson cargadorJson = new CargadorJson();

    private Set<String> idsUsados = new HashSet<>();

    public void iniciarMenu() {
        int opcion = 0;
        while (opcion != 13) {
            System.out.println("\n--- LOGI-UADE 2026: GESTIÓN LOGISTICA ---");
            System.out.println("1. Cargar inventario desde JSON");
            System.out.println("2. Cargar paquete manualmente");
            System.out.println("3. Enviar paquete del Centro al Camión");
            System.out.println("4. Ver estado del Camión");
            System.out.println("5. Deshacer última carga del Camión");
            System.out.println("6. Descargar Camión");
            System.out.println("--- Depósitos ---");
            System.out.println("7. Cargar depósitos desde JSON");
            System.out.println("8. Insertar depósito en el ABB");
            System.out.println("9. Auditar depósitos");
            System.out.println("10. Imprimir depósitos por nivel");
            System.out.println("11. Buscar depósito");
            System.out.println("12. Calcular ruta entre depósitos");
            System.out.println("13. Salir");
            System.out.print("Seleccione: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1: cargarDesdeJson(); break;
                case 2: cargarManual(); break;
                case 3: despacharHaciaCamion(); break;
                case 4: camion.mostrarPaquetesDelCamion(); break;
                case 5: deshacerCarga(); break;
                case 6: descargar(); break;
                case 7: cargarDepositosDesdeJson(); break;
                case 8: insertarDeposito(); break;
                case 9: arbolDepositos.auditarDepositos(); break;
                case 10: imprimirNivel(); break;
                case 11: buscarDeposito(); break;
                case 12: calcularRuta(); break;
                case 13: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
        
    // Operación: Cargar desde JSON
    private void cargarDesdeJson() {
        try (FileReader reader = new FileReader("src/logica/inventario.json")) {
            Gson gson = new Gson();
            Paquete[] lista = gson.fromJson(reader, Paquete[].class);
            if (lista != null) {                                        // 1
                for (Paquete p : lista) {                               // 1 + 3n
                    String idString = String.valueOf(p.getId());        // 2n
                    if (!idsUsados.contains(idString)) {                // 2n
                        idsUsados.add(idString);                        // n
                        Paquete<String> nuevo = new Paquete<>(idString, p.getPeso(), p.getDestino(), p.isUrgente(), (String)p.getContenido()); // 6n
                        centro.recibirPaquete(nuevo);                   // n
                    }
                }
                System.out.println("Inventario cargado exitosamente."); // 1
            }
        } catch (Exception e) {
            System.out.println("Error al cargar JSON: " + e.getMessage());
        }
    }

// Conteo de instrucciones (Peor caso del bloque if principal, 'n' paquetes nuevos):
// f(n) = 1 + (1 + 3n) + 2n + 2n + n + 6n + n + 1
// f(n) = 3 + 15n

// Complejidad asintótica:
// f(n) = O(n)
// f(n) <= c * n
// 3 + 15n <= c * n
// Dividimos por n:
// 3/n + 15 <= c
// Acotamos: 15 + 1 = 16
// Elegimos c = 16

// Verificación:
// n = 1: 3/1 + 15 = 18 <= 16  ✗ NO CUMPLE
// n = 2: 3/2 + 15 = 16.5 <= 16  ✗ NO CUMPLE
// n = 3: 3/3 + 15 = 16 <= 16  ✓ CUMPLE
// Por lo tanto: f(n) pertenece a O(n) con c = 16 y n0 = 3


    private void cargarManual() {
        String id;                                                      // 1
        // 'k' representa la cantidad de intentos fallidos por ID duplicado
        while (true) {                                                  // k + 1
            System.out.print("ID: ");                                   // k + 1
            id = scanner.nextLine();                                    // 2k + 2
            if (idsUsados.contains(id)) {                               // 2k + 2
                System.out.println("Ese ID ya existe...");              // k
            } else {
                idsUsados.add(id);                                      // 1
                break;                                                  // 1
            }
        }

        System.out.print("Peso: ");                                     // 1
        double peso = scanner.nextDouble();                             // 2
        scanner.nextLine();                                             // 1

        System.out.print("Destino: ");                                  // 1
        String destino = scanner.nextLine();                            // 2

        System.out.print("¿Es Urgente? (Si/No): ");                     // 1
        boolean urgente = scanner.nextLine().equalsIgnoreCase("si");    // 3

        System.out.print("Contenido: ");                                // 1
        String cont = scanner.nextLine();                               // 2

        centro.recibirPaquete(new Paquete<>(id, peso, destino, urgente, cont)); // 3
        System.out.println("Paquete ingresado correctamente al Centro."); // 1
    }

    /*
     * * Conteo:
     * f(k) = (k + 1) + (k + 1) + (2k + 2) + (2k + 2) + k + (1 + 1 + 1 + 2 + 1 + 1 + 2 + 1 + 3 + 1 + 2 + 3 + 1)
     * f(k) = 7k + 27
     * * Demostración:
     * f(k) = O(k)
     * 7k + 27 <= c * k
     * Dividimos por k: 7 + 27/k <= c
     * Acotamos: término dominante + 1 = 7 + 1 = 8
     * Elegimos c = 8
     * * Verificación de n0 (k0):
     * n = 1: 34 <= 8 (Falso)
     * n = 27: 8 <= 8 (Verdadero)
     * * Resultado: f(k) pertenece a O(k) con c = 8 y k0 = 27
     */



    private void despacharHaciaCamion() {
        Paquete<String> p = centro.despacharSiguiente();
        if (p != null) {
            camion.cargarPaqueteAlCamion(p);
            System.out.println("Paquete enviado al camión: " + p.getId());
        } else {
            System.out.println("No hay paquetes en el centro.");
        }
    }

    private void deshacerCarga() {
        Paquete<String> p = camion.deshacerUltimaCargaDelCamion();
        if (p != null) {
            System.out.println("Carga deshecha: " + p.getId());
        } else {
            System.out.println("Camión vacío.");
        }
    }

    private void descargar() {
        Paquete<String> p = camion.descargarPaqueteDelCamion();
        if (p != null) {
            System.out.println("Descargando: " + p);
        } else {
            System.out.println("Camión vacío.");
        }
    }


    private void insertarDeposito() {
        System.out.print("ID del depósito: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        // Por defecto, un depósito dado de alta de forma manual se asume no auditado
        arbolDepositos.insertar(id, false);
        System.out.println("Depósito " + id + " insertado.");
    }

    private void imprimirNivel() {
        System.out.print("Nivel a imprimir: ");
        int nivel = scanner.nextInt();
        scanner.nextLine();
        arbolDepositos.imprimirNivel(nivel);
    }

    private void buscarDeposito() {
        System.out.print("ID del depósito a buscar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        arbolDepositos.buscar(id);
    }

    private void calcularRuta() {
        System.out.print("ID origen: ");
        int origen = scanner.nextInt();
        System.out.print("ID destino: ");
        int destino = scanner.nextInt();
        scanner.nextLine();
        int saltos = redDepositos.cantidadSaltos(origen, destino);
        if (saltos == -1) {
            System.out.println("No hay ruta entre los depósitos.");
        } else {
            System.out.println("Cantidad de saltos: " + saltos);
        }
    }

    private void cargarDepositosDesdeJson() {
        try (FileReader reader = new FileReader("src/logica/depositos.json")) {
            Gson gson = new Gson();
            DepositosWrapper wrapper = gson.fromJson(reader, DepositosWrapper.class);
            if (wrapper != null && wrapper.depositos != null) {
                for (DepositoJson d : wrapper.depositos) {
                    // Se envía el ID y el booleano 'auditado' real leído desde la persistencia
                    arbolDepositos.insertar(d.id, d.auditado);
                    if (d.conexiones != null) {
                        for (int conexion : d.conexiones) {
                            redDepositos.agregarRuta(d.id, conexion);
                        }
                    }
                }
                System.out.println("Depósitos cargados exitosamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar depósitos: " + e.getMessage());
        }
    }


}
