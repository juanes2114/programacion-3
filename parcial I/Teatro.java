public class Teatro {
    private Pelicula[] peliculas;
    private int cantidadPeliculas;
    private Sala[] salas;

    public Teatro() {
        peliculas = new Pelicula[100];
        cantidadPeliculas = 0;
        salas = new Sala[3];
        salas[0] = new Sala(1, false, true);
        salas[1] = new Sala(2, false, true);
        salas[2] = new Sala(3, true, false);
    }

    public int getCantidadPeliculas() {
        return cantidadPeliculas;
    }

    public Pelicula getPelicula(int i) {
        return peliculas[i];
    }

    public Sala getSala(int numero) {
        return salas[numero - 1];
    }

    public boolean agregarPelicula(Pelicula p) {
        if (cantidadPeliculas >= peliculas.length) {
            return false;
        }
        peliculas[cantidadPeliculas] = p;
        cantidadPeliculas++;
        return true;
    }

    public void listarPeliculas() {
        if (cantidadPeliculas == 0) {
            System.out.println("No hay peliculas registradas.");
            return;
        }
        for (int i = 0; i < cantidadPeliculas; i++) {
            System.out.println((i + 1) + ". " + peliculas[i]);
        }
    }
}
