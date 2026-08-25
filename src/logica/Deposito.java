package logica;

import java.time.LocalDateTime;

public class Deposito {
    private int id;
    private boolean visitado;
    private LocalDateTime fechaUltimaAuditoria;
    private Deposito izquierdo;
    private Deposito derecho;

    public Deposito(int id) {
        this.id = id;
        this.visitado = false;
        this.fechaUltimaAuditoria = LocalDateTime.now();
        this.izquierdo = null;
        this.derecho = null;
    }

    public int getId() { return id; }
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

    @Override
    public String toString() {
        return "Depósito [" + id + "] - " + " (Auditado: " + visitado + ")";
    }
}
