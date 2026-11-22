package excepciones;

/** Indica que no existe un estudiante con el ID solicitado en el sistema. */
public class EstudianteNoEncontradoException extends Exception {
    public EstudianteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
