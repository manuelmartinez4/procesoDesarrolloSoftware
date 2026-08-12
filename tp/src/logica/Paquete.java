package logica;

public class Paquete<T> {
    private String id;
    private double peso;
    private String destino;
    private boolean urgente;
    private T contenido;
    private String zonaDestino;

    public Paquete(String id, double peso, String destino, boolean urgente, T contenido) {
        this.id = id;
        this.peso = peso;
        this.destino = destino;
        this.urgente = urgente;
        this.contenido = contenido;
        this.zonaDestino = clasificarZona(destino);
    }

    private String clasificarZona(String destino) {
        if (destino == null) return "Otro";
        String d = destino.toLowerCase();
        if (d.contains("norte")) return "Norte";
        if (d.contains("sur")) return "Sur";
        if (d.contains("centro")) return "Centro";
        return "Otro";
    }

    public String getId() { return id; }
    public double getPeso() { return peso; }
    public String getDestino() { return destino; }
    public boolean isUrgente() { return urgente; }
    public T getContenido() { return contenido; }
    public String getZonaDestino() { return zonaDestino; }

    @Override
    public String toString() {
        return "ID: " + id + " | Peso: " + peso + "kg | Destino: " + destino +
                " | Urgente: " + (urgente ? "SI" : "NO") + " | Contenido: " + contenido +
                " | Zona: " + zonaDestino;
    }
}
