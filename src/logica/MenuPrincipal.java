package logica;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);
    private final ControladorLogistica controlador;

    public MenuPrincipal(ControladorLogistica controlador) {
        this.controlador = controlador;
    }

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
        System.out.println("1. Cargar inventario");
        System.out.println("2. Cargar paquete manualmente");
        System.out.println("3. Enviar paquete del Centro al Camión");
        System.out.println("4. Ver estado del Camión");
        System.out.println("5. Deshacer última carga del Camión");
        System.out.println("6. Descargar Camión");
        System.out.println("--- Depósitos ---");
        System.out.println("7. Cargar depósitos");
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
                cargarInventario();
                break;
            case 2:
                cargarManual();
                break;
            case 3:
                despacharHaciaCamion();
                break;
            case 4:
                mostrarPaquetes();
                break;
            case 5:
                deshacerCarga();
                break;
            case 6:
                descargar();
                break;
            case 7:
                cargarDepositos();
                break;
            case 8:
                insertarDeposito();
                break;
            case 9:
                controlador.auditarDepositos();
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

    private void cargarInventario() {
        try {
            controlador.cargarInventario();
            System.out.println("Inventario cargado exitosamente.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cargarDepositos() {
        try {
            controlador.cargarDepositos();
            System.out.println("Depósitos cargados exitosamente.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cargarManual() {
        String id;

        while (true) {
            System.out.print("ID: ");
            id = scanner.nextLine();

            if (controlador.existeId(id)) {
                System.out.println("Ese ID ya existe...");
            } else {
                break;
            }
        }

        System.out.print("Peso: ");
        double peso = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Destino: ");
        String destino = scanner.nextLine();

        System.out.print("¿Es Urgente? (Si/No): ");
        boolean urgente = scanner.nextLine().equalsIgnoreCase("si");

        System.out.print("Contenido: ");
        String contenido = scanner.nextLine();

        boolean registrado = controlador.cargarPaquete(
                id, peso, destino, urgente, contenido);

        if (registrado) {
            System.out.println("Paquete ingresado correctamente al Centro.");
        } else {
            System.out.println("Ese ID ya existe...");
        }
    }

    private void despacharHaciaCamion() {
        Paquete paquete = controlador.despacharHaciaCamion();

        if (paquete != null) {
            System.out.println("Paquete enviado al camión: " + paquete.getId());
        } else {
            System.out.println("No hay paquetes en el centro.");
        }
    }

    private void mostrarPaquetes() {
        List<Paquete> paquetes = controlador.obtenerPaquetes();

        if (paquetes.isEmpty()) {
            System.out.println("El camión está vacío.");
        } else {
            for (Paquete paquete : paquetes) {
                System.out.println(paquete);
            }
        }
    }

    private void deshacerCarga() {
        Paquete paquete = controlador.deshacerCarga();

        if (paquete != null) {
            System.out.println("Carga deshecha: " + paquete.getId());
        } else {
            System.out.println("Camión vacío.");
        }
    }

    private void descargar() {
        Paquete paquete = controlador.descargarCamion();

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

        controlador.insertarDeposito(id);
        System.out.println("Depósito " + id + " insertado.");
    }

    private void imprimirNivel() {
        System.out.print("Nivel a imprimir: ");
        int nivel = scanner.nextInt();
        scanner.nextLine();

        List<Deposito> depositos =
                controlador.obtenerDepositosPorNivel(nivel);

        for (Deposito deposito : depositos) {
            System.out.println("Depósito ID: " + deposito.getId()
                    + " | Visitado: " + deposito.isVisitado()
                    + " | Última Auditoría: "
                    + deposito.getFechaUltimaAuditoria());
        }
    }

    private void buscarDeposito() {
        System.out.print("ID del depósito a buscar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Deposito resultado = controlador.buscarDeposito(id);

        if (resultado != null) {
            System.out.println("Depósito encontrado: ID " + resultado.getId()
                    + " | Visitado: " + resultado.isVisitado()
                    + " | Última Auditoría: "
                    + resultado.getFechaUltimaAuditoria());
        } else {
            System.out.println("Depósito con ID " + id + " no encontrado.");
        }
    }

    private void calcularRuta() {
        System.out.print("ID origen: ");
        int origen = scanner.nextInt();

        System.out.print("ID destino: ");
        int destino = scanner.nextInt();
        scanner.nextLine();

        int saltos = controlador.calcularRuta(origen, destino);

        if (saltos == -1) {
            System.out.println("No hay ruta entre los depósitos.");
        } else {
            System.out.println("Cantidad de saltos: " + saltos);
        }
    }
}
