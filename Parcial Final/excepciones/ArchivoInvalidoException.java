package excepciones;

/** Indica que el CSV de procesamiento por lotes no se puede leer o no tiene el formato esperado. */
public class ArchivoInvalidoException extends Exception {
    public ArchivoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
