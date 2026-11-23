package modelo;

import estructuras.Pila;

/** Campus representado por un grafo no dirigido y una matriz de distancias. */
public class Campus {
    private String[] nombres;
    private int[][] distancias;

    private static final String[] DIAS_NO_USADO = {};

    /** Crea un campus sin conexiones y valida los nombres de edificios. */
    public Campus(String[] nombresEdificios) {
        if (nombresEdificios == null || nombresEdificios.length < 5) {
            throw new IllegalArgumentException(
                    "El campus debe tener al menos cinco edificios."
            );
        }

        nombres = new String[nombresEdificios.length];

        int i = 0;
        while (i < nombresEdificios.length) {
            String nombre = nombresEdificios[i];

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El nombre del edificio en el índice " + i + " está vacío."
                );
            }

            nombres[i] = nombre.trim();

            int j = 0;
            while (j < i) {
                if (nombres[i].equalsIgnoreCase(nombres[j])) {
                    throw new IllegalArgumentException(
                            "Nombre de edificio repetido: " + nombres[i]
                    );
                }
                j++;
            }
            i++;
        }

        distancias = new int[nombres.length][nombres.length];
    }

    public int getCantidadEdificios() {
        return nombres.length;
    }

    public String getNombre(int indice) {
        if (indice < 0 || indice >= nombres.length) {
            throw new IllegalArgumentException(
                    "Índice de edificio fuera de rango: " + indice
            );
        }
        return nombres[indice];
    }

    /** Busca un edificio ignorando mayúsculas y minúsculas. */
    public int indiceDe(String nombre) {
        if (nombre != null) {
            int i = 0;
            while (i < nombres.length) {
                if (nombres[i].equalsIgnoreCase(nombre.trim())) {
                    return i;
                }
                i++;
            }
        }

        throw new IllegalArgumentException("No existe el edificio: " + nombre);
    }

    /** Agrega o reemplaza la conexión en ambas direcciones. */
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

    /** Imprime los edificios con sus índices. */
    public void mostrarEdificios() {
        int i = 0;
        while (i < nombres.length) {
            System.out.println(i + ": " + nombres[i]);
            i++;
        }
    }

    /** Imprime la matriz de distancias. */
    public void mostrarMatriz() {
        System.out.printf("%15s", "");
        int columna = 0;
        while (columna < nombres.length) {
            System.out.printf("%15s", nombres[columna]);
            columna++;
        }
        System.out.println();

        int fila = 0;
        while (fila < nombres.length) {
            System.out.printf("%15s", nombres[fila]);
            columna = 0;
            while (columna < nombres.length) {
                System.out.printf("%15d", distancias[fila][columna]);
                columna++;
            }
            System.out.println();
            fila++;
        }
    }

    /** Comprueba que la matriz tenga los mismos valores a ambos lados de la diagonal. */
    public boolean esMatrizSimetrica() {
        int fila = 0;
        while (fila < distancias.length) {
            int columna = fila + 1;
            while (columna < distancias.length) {
                if (distancias[fila][columna] != distancias[columna][fila]) {
                    return false;
                }
                columna++;
            }
            fila++;
        }
        return true;
    }

    /** Calcula una ruta más corta usando Dijkstra y arreglos. */
    public Ruta calcularRutaMasCorta(String origen, String destino) {
        int indiceOrigen = indiceDe(origen);
        int indiceDestino = indiceDe(destino);

        if (indiceOrigen == indiceDestino) {
            return new Ruta(new String[]{nombres[indiceOrigen]}, new int[0], 0);
        }

        int cantidad = nombres.length;
        int[] mejorDistancia = new int[cantidad];
        boolean[] visitados = new boolean[cantidad];
        int[] anterior = new int[cantidad];

        int i = 0;
        while (i < cantidad) {
            mejorDistancia[i] = Integer.MAX_VALUE;
            anterior[i] = -1;
            i++;
        }
        mejorDistancia[indiceOrigen] = 0;

        int vuelta = 0;
        while (vuelta < cantidad) {
            int actual = indiceMenorNoVisitado(mejorDistancia, visitados);

            if (actual == -1 || actual == indiceDestino) {
                break;
            }

            visitados[actual] = true;
            int vecino = 0;

            while (vecino < cantidad) {
                int distanciaDirecta = distancias[actual][vecino];

                if (distanciaDirecta > 0 && !visitados[vecino]) {
                    long propuesta =
                            (long) mejorDistancia[actual] + distanciaDirecta;

                    if (propuesta < mejorDistancia[vecino]) {
                        mejorDistancia[vecino] = (int) propuesta;
                        anterior[vecino] = actual;
                    }
                }
                vecino++;
            }
            vuelta++;
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

        int cantidadNodos = caminoIndices.tamano();
        String[] camino = new String[cantidadNodos];
        int[] tramos = new int[cantidadNodos - 1];

        i = 0;
        while (i < cantidadNodos) {
            camino[i] = nombres[caminoIndices.pop()];
            i++;
        }

        i = 0;
        while (i < tramos.length) {
            int desde = indiceDe(camino[i]);
            int hasta = indiceDe(camino[i + 1]);
            tramos[i] = distancias[desde][hasta];
            i++;
        }

        return new Ruta(camino, tramos, mejorDistancia[indiceDestino]);
    }

    /** Imprime cada tramo de la ruta y la distancia total. */
    public void mostrarRuta(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser null.");
        }

        System.out.println("Ruta mas corta:");
        String[] edificios = ruta.getNombresEdificios();
        int[] tramos = ruta.getMetrosTramos();

        if (edificios.length == 1) {
            System.out.println("  " + edificios[0]);
        } else {
            String recorrido = "  " + edificios[0];
            int i = 0;
            while (i < tramos.length) {
                recorrido += " -> " + edificios[i + 1]
                        + " (" + tramos[i] + "m)";
                i++;
            }
            System.out.println(recorrido);
        }

        System.out.println("Distancia TOTAL: "
                + ruta.getDistanciaTotal() + " metros");
    }

    private int indiceMenorNoVisitado(
            int[] distanciasActuales,
            boolean[] visitados
    ) {
        int indiceMenor = -1;
        int valorMenor = Integer.MAX_VALUE;
        int i = 0;

        while (i < distanciasActuales.length) {
            if (!visitados[i] && distanciasActuales[i] < valorMenor) {
                indiceMenor = i;
                valorMenor = distanciasActuales[i];
            }
            i++;
        }

        return indiceMenor;
    }
}
