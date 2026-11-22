package excepciones;

/** Indica que se intentó atender o retirar un estudiante cuando la cola de espera está vacía. */
public class ColaDeEsperaVaciaException extends Exception {
    public ColaDeEsperaVaciaException(String mensaje) {
        super(mensaje);
    }
}
