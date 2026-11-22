package excepciones;

/** Indica que un estudiante intenta inscribir una materia sin aprobar sus prerrequisitos. */
public class PreRequisitoNoAprobadoException extends Exception {
    public PreRequisitoNoAprobadoException(String mensaje) {
        super(mensaje);
    }
}
