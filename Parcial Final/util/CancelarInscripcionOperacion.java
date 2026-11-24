package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import excepciones.CupoLlenoException;
import estructuras.ListaEnlazada;
import modelo.Estudiante;
import modelo.Materia;

/** Operación reversible para cancelar una inscripción o una espera. */
public class CancelarInscripcionOperacion extends Operacion {
    private SistemaAcademico sistema;
    private String idEstudiante;
    private String codigoMateria;
    private Estudiante estudiante;
    private Materia materia;
    private boolean estabaInscrito;
    private int posicionEspera;
    private Estudiante estudiantePromovido;
    private boolean ejecutada;

    public CancelarInscripcionOperacion(
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
            throws EstudianteNoEncontradoException {
        estudiante = sistema.buscarEstudiante(idEstudiante);
        materia = sistema.buscarMateria(codigoMateria);

        estabaInscrito = materia.estaInscrito(estudiante);
        posicionEspera = materia.posicionEnEspera(estudiante);

        estudiantePromovido =
                sistema.cancelarInscripcion(idEstudiante, codigoMateria);
        ejecutada = true;
    }

    @Override
    public void deshacer() {
        if (!ejecutada) {
            throw new IllegalStateException(
                    "La cancelación todavía no se ha ejecutado."
            );
        }

        if (estabaInscrito) {
            if (estudiantePromovido != null) {
                boolean seQuito =
                        materia.quitarInscrito(estudiantePromovido);

                if (!seQuito) {
                    throw new IllegalStateException(
                            "No se encontró al estudiante promovido."
                    );
                }

                materia.insertarEnEspera(estudiantePromovido, 0);
            }

            try {
                materia.agregarInscrito(estudiante);
            } catch (CupoLlenoException excepcion) {
                throw new IllegalStateException(
                        "No se pudo restaurar la inscripción cancelada."
                );
            }
        } else {
            materia.insertarEnEspera(estudiante, posicionEspera);
        }

        ejecutada = false;
    }

    @Override
    public String getDescripcion() {
        return "Cancelar inscripción de " + idEstudiante
                + " en " + codigoMateria;
    }
}
