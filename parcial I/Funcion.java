public class Funcion {
    private Pelicula pelicula;
    private boolean[][] ocupadas;
    private int disponibles;

    public Funcion(Pelicula pelicula, int filas, int columnas, int totalSillas) {
        this.pelicula = pelicula;
        this.ocupadas = new boolean[filas][columnas];
        this.disponibles = totalSillas;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public boolean estaOcupada(int fila, int columna) {
        return ocupadas[fila][columna];
    }

    public boolean comprar(int fila, int columna) {
        if (ocupadas[fila][columna]) {
            return false;
        }
        ocupadas[fila][columna] = true;
        disponibles--;
        return true;
    }
}
