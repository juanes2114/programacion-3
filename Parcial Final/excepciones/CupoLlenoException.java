package excepciones;

/** Indica que no quedan cupos para inscribir directamente a un estudiante en una materia. */
public class CupoLlenoException extends Exception {
    public CupoLlenoException(String mensaje) {
        super(mensaje);
    }
}
