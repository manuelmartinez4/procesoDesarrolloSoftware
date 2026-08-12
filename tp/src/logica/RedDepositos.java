package logica;

public class RedDepositos {
    private int[][] matrizAdyacencia;
    private int numDepositos;

    public RedDepositos(int numDepositos) {
        this.numDepositos = numDepositos;
        this.matrizAdyacencia = new int[numDepositos][numDepositos];
    }

    // Metodo auxiliar para evitar ArrayIndexOutOfBoundsException si se lee un ID alto del JSON
    private void garantizarCapacidad(int idMaximo) {
        if (idMaximo >= numDepositos) {
            int nuevaCapacidad = idMaximo + 10; // Se define un margen extra seguro
            int[][] nuevaMatriz = new int[nuevaCapacidad][nuevaCapacidad];
            for (int i = 0; i < numDepositos; i++) {
                System.arraycopy(matrizAdyacencia[i], 0, nuevaMatriz[i], 0, numDepositos);
            }
            matrizAdyacencia = nuevaMatriz;
            numDepositos = nuevaCapacidad;
        }
    }

    public void agregarRuta(int origen, int destino) {
        // Asegura que la matriz pueda albergar los índices antes de asignar la conexión
        garantizarCapacidad(Math.max(origen, destino));
        matrizAdyacencia[origen][destino] = 1;
        matrizAdyacencia[destino][origen] = 1; // Conexión bidireccional
    }
    // Complejidad temporal: O(1) amortizado
    // Complejidad espacial: O(1)

    public int cantidadSaltos(int origen, int destino) {
        // Validación de rango para prevenir lecturas fuera de los límites actuales de la matriz
        if (origen < 0 || origen >= numDepositos || destino < 0 || destino >= numDepositos) {
            return -1;
        }
        if (origen == destino) return 0;
        boolean[] visitado = new boolean[numDepositos];
        return bfs(origen, destino, visitado);
    }
    // Complejidad temporal: O(V + E)
    // Complejidad espacial: O(V) por las estructuras auxiliares de la búsqueda

    private int bfs(int origen, int destino, boolean[] visitado) {
        java.util.Queue<Integer> cola = new java.util.LinkedList<>();
        int[] saltos = new int[numDepositos];
        cola.add(origen);
        visitado[origen] = true;

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            for (int i = 0; i < numDepositos; i++) {
                if (matrizAdyacencia[actual][i] == 1 && !visitado[i]) {
                    visitado[i] = true;
                    saltos[i] = saltos[actual] + 1;
                    if (i == destino) return saltos[i];
                    cola.add(i);
                }
            }
        }
        return -1;
    }

    public void agregarRutaPonderada(int origen, int destino, int peso) {
        garantizarCapacidad(Math.max(origen, destino));
        matrizAdyacencia[origen][destino] = peso;
        matrizAdyacencia[destino][origen] = peso;
    }

    public int dijkstra(int origen, int destino) {
        if (origen < 0 || origen >= numDepositos || destino < 0 || destino >= numDepositos) {
            return -1;
        }
        int[] distancias = new int[numDepositos];
        boolean[] visitado = new boolean[numDepositos];
        java.util.Arrays.fill(distancias, Integer.MAX_VALUE);
        distancias[origen] = 0;

        for (int i = 0; i < numDepositos; i++) {
            int nodoMinimo = -1;
            for (int j = 0; j < numDepositos; j++) {
                if (!visitado[j] && (nodoMinimo == -1 || distancias[j] < distancias[nodoMinimo])) {
                    nodoMinimo = j;
                }
            }
            if (nodoMinimo == -1 || distancias[nodoMinimo] == Integer.MAX_VALUE) break;
            visitado[nodoMinimo] = true;

            for (int j = 0; j < numDepositos; j++) {
                if (matrizAdyacencia[nodoMinimo][j] > 0 && !visitado[j]) {
                    int nuevaDistancia = distancias[nodoMinimo] + matrizAdyacencia[nodoMinimo][j];
                    if (nuevaDistancia < distancias[j]) {
                        distancias[j] = nuevaDistancia;
                    }
                }
            }
        }
        return distancias[destino] == Integer.MAX_VALUE ? -1 : distancias[destino];
    }
    public void bfs(int idOrigen) {
        if (idOrigen < 0 || idOrigen >= numDepositos) {
            System.out.println("ID de depósito inválido.");
            return;
        }
        boolean[] visitado = new boolean[numDepositos];
        java.util.Queue<Integer> cola = new java.util.LinkedList<>();
        cola.add(idOrigen);
        visitado[idOrigen] = true;

        System.out.println("Orden de visita BFS desde el depósito " + idOrigen + ":");
        while (!cola.isEmpty()) {
            int actual = cola.poll();
            System.out.print(actual + " ");
            for (int i = 0; i < numDepositos; i++) {
                if (matrizAdyacencia[actual][i] == 1 && !visitado[i]) {
                    visitado[i] = true;
                    cola.add(i);
                }
            }
        }
        System.out.println();
    }

    /**
     * Cuenta la cantidad de nodos en un Árbol Binario de Búsqueda (ABB) de depósitos.
     * Utiliza recursividad para recorrer el árbol completo.
     * 
     * @param raiz el nodo raíz del árbol (o subárbol) a contar
     * @return cantidad de nodos en el árbol, 0 si la raíz es null
     * Complejidad temporal: O(n) donde n es la cantidad de nodos
     * Complejidad espacial: O(h) donde h es la altura del árbol (por la pila de recursión)
     */
    public int contarDeposito(Deposito raiz) {
        // Caso base: si el nodo es null, retornar 0
        if (raiz == null) {
            return 0;
        }
        
        // Caso recursivo: contar el nodo actual + nodos del subárbol izquierdo + nodos del subárbol derecho
        return 1 + contarDeposito(raiz.izquierdo) + contarDeposito(raiz.derecho);
    }
}

