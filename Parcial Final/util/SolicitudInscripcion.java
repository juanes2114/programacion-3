package util;

/** Solicitud de inscripción leída de una línea de un archivo CSV. */
public class SolicitudInscripcion {
    private String idEstudiante;
    private String codigoMateria;
    private int numeroLinea;

    /** Crea una solicitud con los datos y número de línea del archivo. */
    public SolicitudInscripcion(
            String idEstudiante,
            String codigoMateria,
            int numeroLinea
    ) {
        this.idEstudiante = idEstudiante;
        this.codigoMateria = codigoMateria;
        this.numeroLinea = numeroLinea;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    @Override
    public String toString() {
        return idEstudiante + "," + codigoMateria
                + " (línea " + numeroLinea + ")";
    }
}
