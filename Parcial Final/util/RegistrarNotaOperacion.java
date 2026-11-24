package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import modelo.Estudiante;
import modelo.Materia;

/** Operación reversible para registrar o reemplazar una nota. */
public class RegistrarNotaOperacion extends Operacion {
    private SistemaAcademico sistema;
    private String idEstudiante;
    private int semestre;
    private String codigoMateria;
    private double notaNueva;
    private Double notaAnterior;
    private Estudiante estudiante;
    private Materia materia;
    private boolean ejecutada;

    public RegistrarNotaOperacion(
            SistemaAcademico sistema,
            String idEstudiante,
            int semestre,
            String codigoMateria,
            double nota
    ) {
        this.sistema = sistema;
        this.idEstudiante = idEstudiante;
        this.semestre = semestre;
        this.codigoMateria = codigoMateria;
        notaNueva = nota;
        ejecutada = false;
    }

    @Override
    public void ejecutar()
            throws EstudianteNoEncontradoException {
        estudiante = sistema.buscarEstudiante(idEstudiante);
        materia = sistema.buscarMateria(codigoMateria);
        notaAnterior =
                estudiante.consultarNota(semestre, codigoMateria);

        estudiante.registrarNota(semestre, materia, notaNueva);
        ejecutada = true;
    }

    @Override
    public void deshacer() {
        if (!ejecutada) {
            throw new IllegalStateException(
                    "El registro de nota todavía no se ha ejecutado."
            );
        }

        estudiante.deshacerRegistroNota(
                semestre,
                materia,
                notaAnterior
        );
        ejecutada = false;
    }

    @Override
    public String getDescripcion() {
        return "Registrar nota de " + idEstudiante
                + " en " + codigoMateria
                + " para el semestre " + semestre;
    }
}
