package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import excepciones.CupoLlenoException;
import estructuras.ListaEnlazada;
import modelo.Estudiante;
import modelo.Materia;

/** Operación reversible para eliminar y restaurar un estudiante. */
public class EliminarEstudianteOperacion extends Operacion {
    private SistemaAcademico sistema;
    private String idEstudiante;
    private Estudiante estudiante;
    private ListaEnlazada<Materia> materiasInscritas;
    private ListaEnlazada<Materia> materiasEnEspera;
    private ListaEnlazada<Integer> posicionesEnEspera;
    private boolean ejecutada;

    public EliminarEstudianteOperacion(
            SistemaAcademico sistema,
            String idEstudiante
    ) {
        this.sistema = sistema;
        this.idEstudiante = idEstudiante;
        ejecutada = false;
    }

    @Override
    public void ejecutar()
            throws EstudianteNoEncontradoException {
        estudiante = sistema.buscarEstudiante(idEstudiante);
        materiasInscritas = sistema.materiasInscritas(idEstudiante);
        materiasEnEspera = sistema.materiasEnEspera(idEstudiante);
        posicionesEnEspera = new ListaEnlazada<>();

        int indice = 0;
        while (indice < materiasEnEspera.tamano()) {
            Materia materia = materiasEnEspera.obtener(indice);
            int posicion = materia.posicionEnEspera(estudiante);
            posicionesEnEspera.agregarAlFinal(posicion);
            indice++;
        }

        sistema.eliminarEstudiante(idEstudiante);
        ejecutada = true;
    }

    @Override
    public void deshacer() {
        if (!ejecutada) {
            throw new IllegalStateException(
                    "La eliminación todavía no se ha ejecutado."
            );
        }

        sistema.restaurarEstudiante(estudiante);

        int indice = 0;
        while (indice < materiasInscritas.tamano()) {
            Materia materia = materiasInscritas.obtener(indice);

            try {
                materia.agregarInscrito(estudiante);
            } catch (CupoLlenoException excepcion) {
                throw new IllegalStateException(
                        "No se pudo restaurar una inscripción del estudiante."
                );
            }
            indice++;
        }

        indice = 0;
        while (indice < materiasEnEspera.tamano()) {
            Materia materia = materiasEnEspera.obtener(indice);
            int posicion = posicionesEnEspera.obtener(indice);
            materia.insertarEnEspera(estudiante, posicion);
            indice++;
        }

        ejecutada = false;
    }

    @Override
    public String getDescripcion() {
        return "Eliminar estudiante " + idEstudiante;
    }
}
