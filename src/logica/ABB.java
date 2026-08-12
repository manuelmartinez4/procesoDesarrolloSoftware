package logica;

import java.time.LocalDateTime;

public class ABB {
    Deposito raiz;

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
            Deposito nuevo = new Deposito(id);
            nuevo.setVisitado(auditado); // Sincroniza el atributo 'visitado'

            // Si el JSON indica que no está auditado, se simula una fecha vieja (hace 45 días)
            // para posibilitar el correcto funcionamiento del recorrido de auditoría del examen.
            if (!auditado) {
                nuevo.fechaUltimaAuditoria = LocalDateTime.now().minusDays(45);
            }
            return nuevo;
        }
        if (id < nodo.id) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, id, auditado);
        } else if (id > nodo.id) {
            nodo.derecho = insertarRecursivo(nodo.derecho, id, auditado);
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

        auditarDepositosRecursivo(nodo.izquierdo, limite);
        auditarDepositosRecursivo(nodo.derecho, limite);

        if (nodo.fechaUltimaAuditoria.isBefore(limite)) {
            nodo.visitado = true;
            nodo.fechaUltimaAuditoria = LocalDateTime.now();
        }
    }

    // --- REPORTE POR NIVEL (OPTIMIZADO) ---
    // Complejidad temporal: O(n) -> En el peor caso recorre los nodos hasta el nivel objetivo
    // Complejidad espacial: O(n) por la pila de recursión
    public void imprimirNivel(int nivel) {
        imprimirNivelRecursivo(this.raiz, nivel, 0);
    }

    private void imprimirNivelRecursivo(Deposito nodo, int nivel, int nivelActual) {
        if (nodo == null) return;

        if (nivelActual == nivel) {
            System.out.println("Depósito ID: " + nodo.id + " | Visitado: " + nodo.visitado +
                    " | Última Auditoría: " + nodo.fechaUltimaAuditoria);
            return; // Corte temprano: evita seguir explorando los hijos innecesariamente
        }

        imprimirNivelRecursivo(nodo.izquierdo, nivel, nivelActual + 1);
        imprimirNivelRecursivo(nodo.derecho, nivel, nivelActual + 1);
    }

    // --- BÚSQUEDA ---
    // Complejidad temporal: O(log n) caso promedio, O(n) peor caso
    // Caso promedio: árbol balanceado, descarta la mitad en cada comparación
    // Peor caso: árbol desbalanceado, recorre todos los nodos
    // Complejidad espacial: O(log n) por la pila de recursión
    public void buscar(int id) {
        Deposito resultado = buscarRecursivo(this.raiz, id);
        if (resultado != null) {
            System.out.println("Depósito encontrado: ID " + resultado.id + " | Visitado: " + resultado.visitado +
                    " | Última Auditoría: " + resultado.fechaUltimaAuditoria);
        } else {
            System.out.println("Depósito con ID " + id + " no encontrado.");
        }
    }

    private Deposito buscarRecursivo(Deposito nodo, int id) {
        if (nodo == null || nodo.id == id) {
            return nodo;
        }
        if (id < nodo.id) {
            return buscarRecursivo(nodo.izquierdo, id);
        } else {
            return buscarRecursivo(nodo.derecho, id);
        }
    }
}