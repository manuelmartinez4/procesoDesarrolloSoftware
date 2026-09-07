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
        agregarRutaPonderada(origen, destino, 1);
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
}

