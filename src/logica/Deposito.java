package logica;

import java.time.LocalDateTime;

public class Deposito {
    private int id;
    private String nombre;
    private boolean visitado;
    private LocalDateTime fechaUltimaAuditoria;
    private Deposito izquierdo;
    private Deposito derecho;

    public Deposito(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID del depósito debe ser mayor o igual que cero.");
        }
        this.id = id;
        this.nombre = "Depósito " + id;
        this.visitado = false;
        this.fechaUltimaAuditoria = LocalDateTime.now();
        this.izquierdo = null;
        this.derecho = null;
    }

    public Deposito(int id, boolean auditado) {
        this(id);
        this.visitado = auditado;
        if (!auditado) {
            this.fechaUltimaAuditoria = LocalDateTime.now().minusDays(45);
        }
    }

    public Deposito(int id, String nombre, boolean auditado) {
        this(id, auditado);
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre.trim();
        }
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isVisitado() { return visitado; }
    public void setVisitado(boolean visitado) { this.visitado = visitado; }
    public LocalDateTime getFechaUltimaAuditoria() { return fechaUltimaAuditoria; }
        public void setFechaUltimaAuditoria(LocalDateTime fechaUltimaAuditoria) {
    this.fechaUltimaAuditoria = fechaUltimaAuditoria;
    }
    
    public Deposito getIzquierdo() {
        return izquierdo;
    }
    
    public void setIzquierdo(Deposito izquierdo) {
        this.izquierdo = izquierdo;
    }
    
    public Deposito getDerecho() {
        return derecho;
    }
    
    public void setDerecho(Deposito derecho) {
        this.derecho = derecho;
    }
    public boolean necesitaAuditoria(LocalDateTime limite) {
        return fechaUltimaAuditoria.isBefore(limite);
    }
    
    public void auditar() {
        visitado = true;
        fechaUltimaAuditoria = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "Depósito [" + id + "] - " + " (Auditado: " + visitado + ")";
    }
}
