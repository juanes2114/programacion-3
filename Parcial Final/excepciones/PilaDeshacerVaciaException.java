package excepciones;

/** Indica que se intentó deshacer una operación cuando no hay operaciones guardadas. */
public class PilaDeshacerVaciaException extends Exception {
    public PilaDeshacerVaciaException(String mensaje) {
        super(mensaje);
    }
}
