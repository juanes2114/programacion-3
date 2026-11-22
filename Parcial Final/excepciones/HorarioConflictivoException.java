package excepciones;

/** Indica que una reserva de aula se cruza con otra materia en el mismo horario. */
public class HorarioConflictivoException extends Exception {
    public HorarioConflictivoException(String mensaje) {
        super(mensaje);
    }
}
