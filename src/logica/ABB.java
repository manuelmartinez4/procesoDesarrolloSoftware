package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ABB {
    private Deposito raiz;

    public ABB() {
        this.raiz = null;
    }

    // --- INSERCIÓN ---
    // Complejidad temporal: O(log n) caso promedio, O(n) peor caso
    // Caso promedio: árbol balanceado, cada paso descarta la mitad de los nodos
    // Peor caso: árbol desbalanceado (inserción en orden), se convierte en lista
    // Complejidad espacial: O(log n) por la pila de recursión
    // Nota adicional: Adaptado para recibir el estado 'auditado' desde el JSON.
    public void insertar(int id, boolean auditado) {
        raiz = insertarRecursivo(raiz, id, auditado);
    }

    private Deposito insertarRecursivo(Deposito nodo, int id, boolean auditado) {
        if (nodo == null) {
            return new Deposito(id, auditado);
        }
        if (id < nodo.getId()) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), id, auditado));
        } else if (id > nodo.getId()) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), id, auditado));
        }
        return nodo;
    }

    // --- AUDITORIA (OPTIMIZADO) ---
    // Complejidad temporal: O(n) -> Visita todos los nodos del árbol exactamente una vez en post-orden
    // Complejidad espacial: O(n) por la pila de recursión en el peor caso
    public void auditarDepositos() {
        // Se calcula el umbral una sola vez acá para ganar eficiencia
        LocalDateTime limite = LocalDateTime.now().minusDays(30);
        auditarDepositosRecursivo(this.raiz, limite);
    }

    private void auditarDepositosRecursivo(Deposito nodo, LocalDateTime limite) {
        if (nodo == null) return;

        auditarDepositosRecursivo(nodo.getIzquierdo(), limite);
        auditarDepositosRecursivo(nodo.getDerecho(), limite);
        
        if (nodo.necesitaAuditoria(limite)) {
            nodo.auditar();
        }
    }

    // --- REPORTE POR NIVEL (OPTIMIZADO) ---
    // Complejidad temporal: O(n) -> En el peor caso recorre los nodos hasta el nivel objetivo
    // Complejidad espacial: O(n) por la pila de recursión
    public List<Deposito> obtenerDepositosPorNivel(int nivel) {
    List<Deposito> resultado = new ArrayList<>();
    obtenerPorNivelRecursivo(raiz, nivel, 0, resultado);
    return resultado;
    }

    private void obtenerPorNivelRecursivo(
            Deposito nodo,
            int nivelBuscado,
            int nivelActual,
            List<Deposito> resultado) {
    
        if (nodo == null) {
            return;
        }
    
        if (nivelActual == nivelBuscado) {
            resultado.add(nodo);
            return;
        }
    
        obtenerPorNivelRecursivo(
                nodo.getIzquierdo(), nivelBuscado, nivelActual + 1, resultado);
    
        obtenerPorNivelRecursivo(
                nodo.getDerecho(), nivelBuscado, nivelActual + 1, resultado);
    }

    public Deposito buscar(int id) {
        return buscarRecursivo(raiz, id);
    }

    private Deposito buscarRecursivo(Deposito nodo, int id) {
        if (nodo == null || nodo.getId() == id) {
            return nodo;
        }
    
        if (id < nodo.getId()) {
            return buscarRecursivo(nodo.getIzquierdo(), id);
        }
    
        return buscarRecursivo(nodo.getDerecho(), id);
    }
    public int contarDepositos() {
        return contarDepositosRecursivo(raiz);
    }
    
    private int contarDepositosRecursivo(Deposito nodo) {
        if (nodo == null) {
            return 0;
        }
    
        return 1
                + contarDepositosRecursivo(nodo.getIzquierdo())
                + contarDepositosRecursivo(nodo.getDerecho());
    }    
}
