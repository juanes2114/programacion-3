package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import modelo.Aula;

/** Operación reversible para cambiar una reserva de aula. */
public class CambiarHorarioOperacion extends Operacion {
    private SistemaAcademico sistema;
    private String nombreAula;
    private String codigoMateria;
    private int diaAnterior;
    private int horaAnterior;
    private int duracionAnterior;
    private int diaNuevo;
    private int horaNueva;
    private int duracionNueva;
    private boolean ejecutada;

    public CambiarHorarioOperacion(
            SistemaAcademico sistema,
            String nombreAula,
            String codigoMateria,
            int diaAnterior,
            int horaAnterior,
            int duracionAnterior,
            int diaNuevo,
            int horaNueva,
            int duracionNueva
    ) {
        this.sistema = sistema;
        this.nombreAula = nombreAula;
        this.codigoMateria = codigoMateria;
        this.diaAnterior = diaAnterior;
        this.horaAnterior = horaAnterior;
        this.duracionAnterior = duracionAnterior;
        this.diaNuevo = diaNuevo;
        this.horaNueva = horaNueva;
        this.duracionNueva = duracionNueva;
        ejecutada = false;
    }

    @Override
    public void ejecutar()
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException,
            HorarioConflictivoException {
        // Buscar primero evita liberar el horario si falta aula o materia.
        Aula aula = sistema.buscarAula(nombreAula);
        sistema.buscarMateria(codigoMateria);

        aula.liberar(diaAnterior, horaAnterior, duracionAnterior);

        try {
            sistema.reservarHorario(
                    nombreAula,
                    diaNuevo,
                    horaNueva,
                    duracionNueva,
                    codigoMateria
            );
            ejecutada = true;
        } catch (HorarioConflictivoException excepcion) {
            restaurarHorarioAnterior();
            throw excepcion;
        } catch (IllegalArgumentException excepcion) {
            restaurarHorarioAnterior();
            throw excepcion;
        }
    }

    @Override
    public void deshacer() {
        if (!ejecutada) {
            throw new IllegalStateException(
                    "El cambio de horario todavía no se ha ejecutado."
            );
        }

        Aula aula = sistema.buscarAula(nombreAula);
        aula.liberar(diaNuevo, horaNueva, duracionNueva);
        restaurarHorarioAnterior();
        ejecutada = false;
    }

    private void restaurarHorarioAnterior() {
        try {
            sistema.reservarHorario(
                    nombreAula,
                    diaAnterior,
                    horaAnterior,
                    duracionAnterior,
                    codigoMateria
            );
        } catch (HorarioConflictivoException excepcion) {
            throw new IllegalStateException(
                    "No se pudo restaurar el horario anterior: "
                            + excepcion.getMessage()
            );
        } catch (IllegalArgumentException excepcion) {
            throw new IllegalStateException(
                    "No se pudo restaurar el horario anterior: "
                            + excepcion.getMessage()
            );
        }
    }

    @Override
    public String getDescripcion() {
        return "Cambiar horario de " + codigoMateria
                + " en el aula " + nombreAula;
    }
}
