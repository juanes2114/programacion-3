public class Pelicula {
    private String nombre;
    private String idioma;
    private String tipo;
    private int duracion;

    public Pelicula(String nombre, String idioma, String tipo, int duracion) {
        this.nombre = nombre;
        this.idioma = idioma;
        this.tipo = tipo;
        this.duracion = duracion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getTipo() {
        return tipo;
    }

    public int getDuracion() {
        return duracion;
    }

    public boolean es3D() {
        return tipo.equals("3D");
    }

    public String toString() {
        return nombre + " | " + idioma + " | " + tipo + " | " + duracion + " min";
    }
}
