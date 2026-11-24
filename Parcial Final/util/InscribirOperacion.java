package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import modelo.Estudiante;
import modelo.Materia;

/** Operación reversible para inscribir a un estudiante o dejarlo en espera. */
public class InscribirOperacion extends Operacion {
    private SistemaAcademico sistema;
    private String idEstudiante;
    private String codigoMateria;
    private Estudiante estudiante;
    private Materia materia;
    private boolean quedoInscrito;
    private boolean ejecutada;

    public InscribirOperacion(
            SistemaAcademico sistema,
            String idEstudiante,
            String codigoMateria
    ) {
        this.sistema = sistema;
        this.idEstudiante = idEstudiante;
        this.codigoMateria = codigoMateria;
        ejecutada = false;
    }

    @Override
    public void ejecutar()
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException {
        estudiante = sistema.buscarEstudiante(idEstudiante);
        materia = sistema.buscarMateria(codigoMateria);
        quedoInscrito = sistema.inscribir(idEstudiante, codigoMateria);
        ejecutada = true;
    }

    @Override
    public void deshacer() {
        if (!ejecutada) {
            throw new IllegalStateException(
                    "La inscripción todavía no se ha ejecutado."
            );
        }

        boolean eliminado;

        if (quedoInscrito) {
            eliminado = materia.quitarInscrito(estudiante);
        } else {
            eliminado = materia.quitarDeEspera(estudiante);
        }

        if (!eliminado) {
            throw new IllegalStateException(
                    "No se encontró al estudiante para deshacer la inscripción."
            );
        }

        ejecutada = false;
    }

    @Override
    public String getDescripcion() {
        return "Inscribir al estudiante " + idEstudiante
                + " en " + codigoMateria;
    }
}
