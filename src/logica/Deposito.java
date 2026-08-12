package logica;

import java.time.LocalDateTime;

public class Deposito {
    int id;
    boolean visitado;
    LocalDateTime fechaUltimaAuditoria;
    Deposito izquierdo;
    Deposito derecho;

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

    @Override
    public String toString() {
        return "Depósito [" + id + "] - " + " (Auditado: " + visitado + ")";
    }
}
