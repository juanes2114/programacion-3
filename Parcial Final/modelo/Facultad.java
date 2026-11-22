package modelo;

/** Facultad identificada por un código y un nombre. */
public class Facultad {
    private String codigo;
    private String nombre;

    /** Crea una facultad con su código y nombre. */
    public Facultad(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    /** Devuelve una representación textual de la facultad. */
    @Override
    public String toString() {
        return "Facultad{codigo='" + codigo + "', nombre='" + nombre + "'}";
    }
}
