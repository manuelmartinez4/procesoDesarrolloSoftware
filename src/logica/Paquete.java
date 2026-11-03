package logica;

import java.util.Locale;

public class Paquete {

    public enum ZonaDestino {
        NORTE("Norte"),
        SUR("Sur"),
        CENTRO("Centro"),
        OTRO("Otro");

        private final String nombre;

        ZonaDestino(String nombre) {
            this.nombre = nombre;
        }

        public static ZonaDestino desdeTexto(String texto) {
            try {
                return valueOf(texto.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IllegalArgumentException("Zona inválida: " + texto);
            }
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    private String id;
    private double peso;
    private String destino;
    private boolean urgente;
    private String contenido;
    private ZonaDestino zonaDestino;

    public Paquete(String id, double peso, String destino, boolean urgente, String contenido) {
        this(id, peso, destino, urgente, contenido, null);
    }

    public Paquete(String id, double peso, String destino, boolean urgente,
                   String contenido, ZonaDestino zonaDestino) {
        this.id = obligatorio(id, "ID");
        if (!Double.isFinite(peso) || peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        this.peso = peso;
        this.destino = obligatorio(destino, "Destino");
        this.urgente = urgente;
        this.contenido = obligatorio(contenido, "Contenido");
        this.zonaDestino = zonaDestino == null ? clasificarZona(destino) : zonaDestino;
    }

    private static String obligatorio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Completá el campo " + campo + ".");
        }
        return texto.trim();
    }

    private ZonaDestino clasificarZona(String destino) {
        if (destino == null) {
            return ZonaDestino.OTRO;
        }

        String destinoNormalizado = destino.toLowerCase(Locale.ROOT);

        if (destinoNormalizado.contains("norte")) {
            return ZonaDestino.NORTE;
        }

        if (destinoNormalizado.contains("sur")) {
            return ZonaDestino.SUR;
        }

        if (destinoNormalizado.contains("centro")) {
            return ZonaDestino.CENTRO;
        }

        return ZonaDestino.OTRO;
    }

    public String getId() {
        return id;
    }

    public double getPeso() {
        return peso;
    }

    public String getDestino() {
        return destino;
    }

    public boolean isUrgente() {
        return urgente;
    }

    public boolean esPrioritario() {
        return urgente || peso > 50.0;
    }

    public String getContenido() {
        return contenido;
    }

    public ZonaDestino getZonaDestino() {
        return zonaDestino;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Peso: " + peso + "kg"
                + " | Destino: " + destino
                + " | Urgente: " + (urgente ? "SI" : "NO")
                + " | Contenido: " + contenido
                + " | Zona: " + zonaDestino;
    }
}
