package logica;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CargadoresContratoTest {
    private static int comprobaciones;

    public static void main(String[] args) throws Exception {
        Paquete[] paquetes = {
                new Paquete("normal", 50.0, "Sur", false, "Libros"),
                new Paquete("pesado", 60.0, "Centro", false, "Herramientas"),
                new Paquete("urgente", 10.0, "Norte", true, "Documentos"),
                new Paquete("pesado", 1.0, "Otro", false, "Duplicado")
        };
        DepositoJson[] depositos = {
                deposito(50, true, new int[]{20, 80}),
                deposito(20, false, new int[]{10}),
                deposito(80, false, null),
                deposito(10, true, new int[0]),
                deposito(50, false, null)
        };
        Path carpeta = Files.createTempDirectory("contrato-cargadores-");
        Path inventarioJson = carpeta.resolve("inventario.json");
        Path depositosJson = carpeta.resolve("depositos.json");
        try {
            escribirDatos(inventarioJson, depositosJson, paquetes, depositos);
            CargadorJson json = new CargadorJson(inventarioJson.toString(), depositosJson.toString());
            CargadorEnMemoria memoria = new CargadorEnMemoria(paquetes, depositos);
            verificarContrato("JSON", json, json);
            verificarContrato("memoria", memoria, memoria);
            verificarMenu(json, json);
            verificarMenu(memoria, memoria);

            // Cada lambda implementa una sola operación: no necesita la otra interfaz.
            CargadorInventario soloInventario = (ids, centro) -> json.cargarInventario(ids, centro);
            CargadorDepositos soloDepositos = (arbol, red) -> memoria.cargarDepositos(arbol, red);
            verificarMenu(soloInventario, soloDepositos);

            escribirDatos(inventarioJson, depositosJson, new Paquete[0], new DepositoJson[0]);
            verificarFuenteVacia(json, json);
            CargadorEnMemoria vacio = new CargadorEnMemoria(new Paquete[0], new DepositoJson[0]);
            verificarFuenteVacia(vacio, vacio);
            System.out.println("OK: " + comprobaciones + " comprobaciones de contrato y sustitucion.");
        } finally {
            Files.deleteIfExists(inventarioJson);
            Files.deleteIfExists(depositosJson);
            Files.deleteIfExists(carpeta);
        }
    }

    private static void verificarContrato(String nombre, CargadorInventario inventario,
                                          CargadorDepositos depositos) {
        Set<String> ids = new HashSet<>();
        ids.add("previo");
        CentroDistribucion centro = new CentroDistribucion();
        Paquete previo = new Paquete("previo", 1.0, "Otro", false, "Existente");
        centro.recibirPaquete(previo);
        inventario.cargarInventario(ids, centro);
        inventario.cargarInventario(ids, centro);
        comprobar(ids.size() == 4 && ids.contains("previo") && ids.contains("normal")
                && ids.contains("pesado") && ids.contains("urgente"), nombre + ": IDs sin duplicados");
        Paquete pesado = centro.despacharSiguiente();
        comprobar(pesado != null && pesado.getId().equals("pesado") && pesado.getPeso() == 60.0,
                nombre + ": conserva el primer paquete con ID repetido");
        comprobar(pesado.getDestino().equals("Centro") && !pesado.isUrgente()
                && pesado.getContenido().equals("Herramientas")
                && pesado.getZonaDestino() == Paquete.ZonaDestino.CENTRO, nombre + ": datos del paquete");
        Paquete urgente = centro.despacharSiguiente();
        comprobar(urgente != null && urgente.getId().equals("urgente"), nombre + ": orden de prioridad");
        comprobar(centro.despacharSiguiente() == previo, nombre + ": paquete previo intacto");
        Paquete normal = centro.despacharSiguiente();
        comprobar(normal != null && normal.getId().equals("normal") && !normal.esPrioritario(),
                nombre + ": 50 kg no es prioritario por peso");
        comprobar(centro.despacharSiguiente() == null, nombre + ": repetir no encola duplicados");

        ABB arbol = new ABB();
        RedDepositos red = new RedDepositos(2);
        arbol.insertar(999, true);
        Deposito previoDeposito = arbol.buscar(999);
        red.agregarRuta(999, 1000);
        depositos.cargarDepositos(arbol, red);
        comprobar(arbol.contarDepositos() == 5, nombre + ": todos los depositos, sin duplicar IDs");
        comprobar(arbol.buscar(999) == previoDeposito && previoDeposito.isVisitado(),
                nombre + ": conserva depositos previos");
        comprobar(arbol.buscar(50).isVisitado() && !arbol.buscar(20).isVisitado()
                && !arbol.buscar(80).isVisitado() && arbol.buscar(10).isVisitado(),
                nombre + ": respeta estados iniciales");
        comprobar(!arbol.buscar(50).necesitaAuditoria(LocalDateTime.now().minusDays(30))
                && arbol.buscar(20).necesitaAuditoria(LocalDateTime.now().minusDays(30)),
                nombre + ": fechas de auditoria");
        comprobar(red.cantidadSaltos(10, 80) == 3 && red.cantidadSaltos(80, 10) == 3,
                nombre + ": rutas bidireccionales");
        comprobar(red.cantidadSaltos(999, 1000) == 1, nombre + ": conserva rutas previas");
        Deposito auditado = arbol.buscar(20);
        auditado.auditar();
        LocalDateTime fecha = auditado.getFechaUltimaAuditoria();
        depositos.cargarDepositos(arbol, red);
        comprobar(arbol.contarDepositos() == 5 && arbol.buscar(20) == auditado
                && auditado.isVisitado() && auditado.getFechaUltimaAuditoria().equals(fecha),
                nombre + ": repetir carga no revierte una auditoria");
        comprobar(arbol.buscar(12345) == null, nombre + ": no inventa depositos");
    }

    private static void verificarFuenteVacia(CargadorInventario inventario,
                                             CargadorDepositos depositos) {
        Set<String> ids = new HashSet<>();
        ids.add("previo");
        CentroDistribucion centro = new CentroDistribucion();
        Paquete previo = new Paquete("previo", 1.0, "Otro", false, "Existente");
        centro.recibirPaquete(previo);
        ABB arbol = new ABB();
        arbol.insertar(1, true);
        RedDepositos red = new RedDepositos(3);
        red.agregarRuta(1, 2);
        inventario.cargarInventario(ids, centro);
        depositos.cargarDepositos(arbol, red);
        comprobar(ids.size() == 1 && centro.despacharSiguiente() == previo
                && centro.despacharSiguiente() == null, "Inventario vacio conserva estado");
        comprobar(arbol.contarDepositos() == 1 && arbol.buscar(1).isVisitado()
                && red.cantidadSaltos(1, 2) == 1, "Depositos vacios conservan estado");
    }

    private static void verificarMenu(CargadorInventario inventario,
                                      CargadorDepositos depositos) throws Exception {
        InputStream entradaOriginal = System.in;
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try (PrintStream captura = new PrintStream(salida, true, "UTF-8")) {
            System.setIn(new ByteArrayInputStream(
                    "1\n7\n11\n20\n12\n10\n80\n13\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(captura);
            new MenuPrincipal(inventario, depositos).iniciarMenu();
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(salidaOriginal);
        }
        String texto = salida.toString("UTF-8");
        comprobar(texto.contains("Depósito encontrado: ID 20"), "Menu busca con el cargador recibido");
        comprobar(texto.contains("Cantidad de saltos: 3"), "Menu usa las conexiones cargadas");
        comprobar(texto.contains("Saliendo..."), "Menu completa el recorrido");
    }

    private static DepositoJson deposito(int id, boolean auditado, int[] conexiones) {
        DepositoJson deposito = new DepositoJson();
        deposito.id = id;
        deposito.auditado = auditado;
        deposito.conexiones = conexiones;
        return deposito;
    }

    private static void escribirDatos(Path inventario, Path depositos, Paquete[] paquetes,
                                      DepositoJson[] datos) throws Exception {
        Gson gson = new Gson();
        DepositosWrapper wrapper = new DepositosWrapper();
        wrapper.depositos = datos;
        Files.writeString(inventario, gson.toJson(paquetes), StandardCharsets.UTF_8);
        Files.writeString(depositos, gson.toJson(wrapper), StandardCharsets.UTF_8);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
        comprobaciones++;
    }
}
