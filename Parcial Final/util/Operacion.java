package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;

/** Base abstracta para las operaciones que se pueden deshacer y rehacer. */
public abstract class Operacion {
    /** Ejecuta la operación y guarda el estado que se necesitará para deshacer. */
    public abstract void ejecutar()
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException,
            HorarioConflictivoException;

    /** Restaura el estado anterior de la operación. */
    public abstract void deshacer();

    /** Devuelve una descripción breve de la operación. */
    public abstract String getDescripcion();

    /**
     * Vuelve a ejecutar la operación.
     * Las excepciones verificadas se convierten en una excepción de estado.
     */
    public void rehacer() {
        try {
            ejecutar();
        } catch (EstudianteNoEncontradoException excepcion) {
            throw new IllegalStateException(
                    "No se pudo rehacer la operación: "
                            + excepcion.getMessage()
            );
        } catch (PreRequisitoNoAprobadoException excepcion) {
            throw new IllegalStateException(
                    "No se pudo rehacer la operación: "
                            + excepcion.getMessage()
            );
        } catch (HorarioConflictivoException excepcion) {
            throw new IllegalStateException(
                    "No se pudo rehacer la operación: "
                            + excepcion.getMessage()
            );
        }
    }
}
