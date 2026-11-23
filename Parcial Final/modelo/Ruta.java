package modelo;

/** Resultado de una ruta con edificios ordenados y distancia por tramo. */
public class Ruta {
    private String[] nombresEdificios;
    private int[] metrosTramos;
    private int distanciaTotal;

    /** Crea una ruta y guarda copias de los arreglos recibidos. */
    public Ruta(
            String[] nombresEdificios,
            int[] metrosTramos,
            int distanciaTotal
    ) {
        if (nombresEdificios == null || metrosTramos == null) {
            throw new IllegalArgumentException("Los arreglos de la ruta no pueden ser null.");
        }
        if (nombresEdificios.length != metrosTramos.length + 1) {
            throw new IllegalArgumentException(
                    "La cantidad de tramos debe ser una menos que la cantidad de edificios."
            );
        }

        this.nombresEdificios = copiar(nombresEdificios);
        this.metrosTramos = copiar(metrosTramos);
        this.distanciaTotal = distanciaTotal;
    }

    /** Devuelve una copia de los nombres de edificios en orden. */
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

    private String[] copiar(String[] original) {
        String[] copia = new String[original.length];
        int indice = 0;
        while (indice < original.length) {
            copia[indice] = original[indice];
            indice++;
        }
        return copia;
    }

    private int[] copiar(int[] original) {
        int[] copia = new int[original.length];
        int indice = 0;
        while (indice < original.length) {
            copia[indice] = original[indice];
            indice++;
        }
        return copia;
    }
}
