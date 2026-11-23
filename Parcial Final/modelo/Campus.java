package modelo;

import estructuras.Pila;

/** Campus representado por un grafo no dirigido ponderado mediante una matriz. */
public class Campus {
    private String[] nombres;
    private int[][] distancias;

    /**
     * Crea un campus sin conexiones.
     *
     * @param nombresEdificios nombres de al menos cinco edificios
     * @throws IllegalArgumentException si hay menos de cinco, nombres inválidos
     *                                  o repetidos sin distinguir mayúsculas
     */
    public Campus(String[] nombresEdificios) {
        if (nombresEdificios == null || nombresEdificios.length < 5) {
            throw new IllegalArgumentException(
                    "El campus debe tener al menos cinco edificios."
            );
        }

        nombres = new String[nombresEdificios.length];

        for (int i = 0; i < nombresEdificios.length; i++) {
            String nombre = nombresEdificios[i];

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El nombre del edificio en el índice " + i + " está vacío."
                );
            }

            nombres[i] = nombre.trim();

            for (int j = 0; j < i; j++) {
                if (nombres[i].equalsIgnoreCase(nombres[j])) {
                    throw new IllegalArgumentException(
                            "Nombre de edificio repetido: " + nombres[i]
                    );
                }
            }
        }

        distancias = new int[nombres.length][nombres.length];
    }

    /** Devuelve la cantidad de edificios del campus. */
    public int getCantidadEdificios() {
        return nombres.length;
    }

    /** Devuelve el nombre del edificio ubicado en el índice indicado. */
    public String getNombre(int indice) {
        if (indice < 0 || indice >= nombres.length) {
            throw new IllegalArgumentException(
                    "Índice de edificio fuera de rango: " + indice
            );
        }
        return nombres[indice];
    }

    /**
     * Busca un edificio sin distinguir mayúsculas.
     *
     * @throws IllegalArgumentException si el edificio no existe
     */
    public int indiceDe(String nombre) {
        if (nombre != null) {
            for (int i = 0; i < nombres.length; i++) {
                if (nombres[i].equalsIgnoreCase(nombre.trim())) {
                    return i;
                }
            }
        }

        throw new IllegalArgumentException("No existe el edificio: " + nombre);
    }

    /**
     * Agrega o reemplaza una conexión bidireccional entre dos edificios.
     *
     * @throws IllegalArgumentException si algún edificio no existe, son iguales
     *                                  o la distancia no es positiva
     */
    public void agregarConexion(String origen, String destino, int metros) {
        int indiceOrigen = indiceDe(origen);
        int indiceDestino = indiceDe(destino);

        if (indiceOrigen == indiceDestino) {
            throw new IllegalArgumentException(
                    "No se puede conectar un edificio consigo mismo."
            );
        }
        if (metros <= 0) {
            throw new IllegalArgumentException(
                    "La distancia debe ser mayor que cero."
            );
        }

        distancias[indiceOrigen][indiceDestino] = metros;
        distancias[indiceDestino][indiceOrigen] = metros;
    }

    /** Imprime cada edificio con su índice. */
    public void mostrarEdificios() {
        for (int i = 0; i < nombres.length; i++) {
            System.out.println(i + ": " + nombres[i]);
        }
    }

    /** Imprime la matriz de distancias, usando cero cuando no hay conexión directa. */
    public void mostrarMatriz() {
        System.out.printf("%15s", "");
        for (String nombre : nombres) {
            System.out.printf("%15s", nombre);
        }
        System.out.println();

        for (int fila = 0; fila < nombres.length; fila++) {
            System.out.printf("%15s", nombres[fila]);
            for (int columna = 0; columna < nombres.length; columna++) {
                System.out.printf("%15d", distancias[fila][columna]);
            }
            System.out.println();
        }
    }

    /** Comprueba que cada distancia tenga el mismo valor en la posición simétrica. */
    public boolean esMatrizSimetrica() {
        for (int fila = 0; fila < distancias.length; fila++) {
            for (int columna = fila + 1; columna < distancias.length; columna++) {
                if (distancias[fila][columna] != distancias[columna][fila]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calcula la ruta más corta con Dijkstra.
     *
     * @throws IllegalArgumentException si el origen o destino no existen
     * @throws IllegalStateException si no hay un camino entre ellos
     */
    public Ruta calcularRutaMasCorta(String origen, String destino) {
        int indiceOrigen = indiceDe(origen);
        int indiceDestino = indiceDe(destino);
        int cantidad = nombres.length;

        if (indiceOrigen == indiceDestino) {
            return new Ruta(
                    new String[]{nombres[indiceOrigen]},
                    new int[0],
                    0
            );
        }

        int[] mejorDistancia = new int[cantidad];
        boolean[] visitados = new boolean[cantidad];
        int[] anterior = new int[cantidad];

        for (int i = 0; i < cantidad; i++) {
            mejorDistancia[i] = Integer.MAX_VALUE;
            anterior[i] = -1;
        }
        mejorDistancia[indiceOrigen] = 0;

        for (int vuelta = 0; vuelta < cantidad; vuelta++) {
            int actual = indiceMenorNoVisitado(mejorDistancia, visitados);

            if (actual == -1) {
                break;
            }
            if (actual == indiceDestino) {
                break;
            }

            visitados[actual] = true;

            for (int vecino = 0; vecino < cantidad; vecino++) {
                int distanciaDirecta = distancias[actual][vecino];

                if (distanciaDirecta > 0 && !visitados[vecino]) {
                    long distanciaPropuesta =
                            (long) mejorDistancia[actual] + distanciaDirecta;

                    if (distanciaPropuesta < mejorDistancia[vecino]) {
                        mejorDistancia[vecino] = (int) distanciaPropuesta;
                        anterior[vecino] = actual;
                    }
                }
            }
        }

        if (mejorDistancia[indiceDestino] == Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "No existe una ruta entre " + nombres[indiceOrigen]
                            + " y " + nombres[indiceDestino] + "."
            );
        }

        Pila<Integer> caminoIndices = new Pila<>();
        int actual = indiceDestino;

        while (actual != -1) {
            caminoIndices.push(actual);
            if (actual == indiceOrigen) {
                break;
            }
            actual = anterior[actual];
        }

        if (caminoIndices.peek() != indiceOrigen) {
            throw new IllegalStateException(
                    "No se pudo reconstruir la ruta encontrada."
            );
        }

        int cantidadNodos = caminoIndices.tamano();
        String[] camino = new String[cantidadNodos];
        int[] tramos = new int[cantidadNodos - 1];

        for (int i = 0; i < cantidadNodos; i++) {
            camino[i] = nombres[caminoIndices.pop()];
        }

        for (int i = 0; i < tramos.length; i++) {
            int desde = indiceDe(camino[i]);
            int hasta = indiceDe(camino[i + 1]);
            tramos[i] = distancias[desde][hasta];
        }

        return new Ruta(camino, tramos, mejorDistancia[indiceDestino]);
    }

    /** Imprime los edificios del camino, el largo de cada tramo y el total. */
    public void mostrarRuta(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser null.");
        }

        System.out.println("Ruta mas corta:");

        String[] edificios = ruta.getNombresEdificios();
        int[] metrosTramos = ruta.getMetrosTramos();

        if (edificios.length == 1) {
            System.out.println("  " + edificios[0]);
        } else {
            StringBuilder recorrido = new StringBuilder("  ");
            recorrido.append(edificios[0]);

            for (int i = 0; i < metrosTramos.length; i++) {
                recorrido.append(" -> ")
                        .append(edificios[i + 1])
                        .append(" (")
                        .append(metrosTramos[i])
                        .append("m)");
            }

            System.out.println(recorrido);
        }

        System.out.println("Distancia TOTAL: "
                + ruta.getDistanciaTotal() + " metros");
    }

    private int indiceMenorNoVisitado(int[] distanciasActuales, boolean[] visitados) {
        int indiceMenor = -1;
        int valorMenor = Integer.MAX_VALUE;

        for (int i = 0; i < distanciasActuales.length; i++) {
            if (!visitados[i] && distanciasActuales[i] < valorMenor) {
                indiceMenor = i;
                valorMenor = distanciasActuales[i];
            }
        }

        return indiceMenor;
    }

    /** Resultado de una ruta: edificios ordenados, tramos y distancia total. */
    public static class Ruta {
        private String[] nombresEdificios;
        private int[] metrosTramos;
        private int distanciaTotal;

        private Ruta(
                String[] nombresEdificios,
                int[] metrosTramos,
                int distanciaTotal
        ) {
            this.nombresEdificios = copiar(nombresEdificios);
            this.metrosTramos = copiar(metrosTramos);
            this.distanciaTotal = distanciaTotal;
        }

        /** Devuelve una copia de los nombres de edificios del camino. */
        public String[] getNombresEdificios() {
            return copiar(nombresEdificios);
        }

        /** Devuelve una copia de las distancias de cada tramo. */
        public int[] getMetrosTramos() {
            return copiar(metrosTramos);
        }

        public int getDistanciaTotal() {
            return distanciaTotal;
        }

        private static String[] copiar(String[] original) {
            String[] copia = new String[original.length];
            for (int i = 0; i < original.length; i++) {
                copia[i] = original[i];
            }
            return copia;
        }

        private static int[] copiar(int[] original) {
            int[] copia = new int[original.length];
            for (int i = 0; i < original.length; i++) {
                copia[i] = original[i];
            }
            return copia;
        }
    }
}
