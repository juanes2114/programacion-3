package modelo;

/** Representa los datos comunes de una persona del sistema académico. */
public abstract class Persona {
    private String nombre;
    private String id;
    private String email;

    /** Crea una persona con sus datos básicos. */
    public Persona(String nombre, String id, String email) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    /** Muestra la información concreta que corresponde al tipo de persona. */
    public abstract void mostrarInformacion();
}
