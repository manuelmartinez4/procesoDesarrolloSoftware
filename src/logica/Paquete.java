package logica;

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
        this.id = id;
        this.peso = peso;
        this.destino = destino;
        this.urgente = urgente;
        this.contenido = contenido;
        this.zonaDestino = clasificarZona(destino);
    }

    private ZonaDestino clasificarZona(String destino) {
        if (destino == null) {
            return ZonaDestino.OTRO;
        }

        String destinoNormalizado = destino.toLowerCase();

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
