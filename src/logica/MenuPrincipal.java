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
            mostrarMenu();
    
            opcion = scanner.nextInt();
            scanner.nextLine();
    
            procesarOpcion(opcion);
        }
    }
    private void mostrarMenu() {
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
    }
    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                cargadorJson.cargarInventario(idsUsados, centro);
                break;
            case 2:
                cargarManual();
                break;
            case 3:
                despacharHaciaCamion();
                break;
            case 4:
                camion.mostrarPaquetes();
                break;
            case 5:
                deshacerCarga();
                break;
            case 6:
                descargar();
                break;
            case 7:
                cargadorJson.cargarDepositos(arbolDepositos, redDepositos);
                break;
            case 8:
                insertarDeposito();
                break;
            case 9:
                arbolDepositos.auditarDepositos();
                break;
            case 10:
                imprimirNivel();
                break;
            case 11:
                buscarDeposito();
                break;
            case 12:
                calcularRuta();
                break;
            case 13:
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

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
        String contenido = scanner.nextLine();                               // 2

        centro.recibirPaquete(new Paquete(id, peso, destino, urgente, contenido)); // 3
        System.out.println("Paquete ingresado correctamente al Centro."); // 1
    }

    private void despacharHaciaCamion() {
        Paquete paquete = centro.despacharSiguiente();
    
        if (paquete != null) {
            camion.cargarPaquete(paquete);
            System.out.println("Paquete enviado al camión: " + paquete.getId());
        } else {
            System.out.println("No hay paquetes en el centro.");
        }
    }

    private void deshacerCarga() {
        Paquete paquete = camion.deshacerUltimaCarga();
    
        if (paquete != null) {
            System.out.println("Carga deshecha: " + paquete.getId());
        } else {
            System.out.println("Camión vacío.");
        }
    }

    private void descargar() {
        Paquete paquete = camion.descargarPaquete();
    
        if (paquete != null) {
            System.out.println("Descargando: " + paquete);
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
}
