package util;

import excepciones.ArchivoInvalidoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.PreRequisitoNoAprobadoException;
import estructuras.Cola;
import estructuras.ListaEnlazada;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** Carga y procesa solicitudes de inscripción desde un archivo CSV. */
public class ProcesadorLotes {
    private SistemaAcademico sistema;
    private Cola<SolicitudInscripcion> colaProcesamiento;
    private int exitosas;
    private int enEspera;
    private int fallidas;
    private ListaEnlazada<String> detalleFallidas;

    /** Crea un procesador asociado al sistema indicado. */
    public ProcesadorLotes(SistemaAcademico sistema) {
        if (sistema == null) {
            throw new IllegalArgumentException(
                    "El sistema académico no puede ser null."
            );
        }

        this.sistema = sistema;
        colaProcesamiento = new Cola<>();
        exitosas = 0;
        enEspera = 0;
        fallidas = 0;
        detalleFallidas = new ListaEnlazada<>();
    }

    /**
     * Lee y valida todo el CSV antes de añadir solicitudes a la cola principal.
     *
     * @return cantidad de solicitudes que se cargaron
     * @throws ArchivoInvalidoException si la ruta, el archivo o alguna línea
     *                                  son inválidos, o si no hay solicitudes
     */
    public int cargarArchivo(String rutaArchivo)
            throws ArchivoInvalidoException {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new ArchivoInvalidoException(
                    "La ruta del archivo está vacía: " + rutaArchivo
            );
        }

        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            throw new ArchivoInvalidoException(
                    "No existe el archivo: " + rutaArchivo
            );
        }
        if (!archivo.isFile()) {
            throw new ArchivoInvalidoException(
                    "La ruta no corresponde a un archivo: " + rutaArchivo
            );
        }

        Cola<SolicitudInscripcion> solicitudesTemporales = new Cola<>();
        Scanner lector = null;
        int numeroLinea = 0;
        int solicitudesLeidas = 0;
        boolean primeraLineaConContenido = true;

        try {
            lector = new Scanner(archivo);

            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                numeroLinea++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String lineaLimpia = linea.trim();

                if (primeraLineaConContenido) {
                    primeraLineaConContenido = false;

                    if (lineaLimpia.equalsIgnoreCase(
                            "idEstudiante,codigoMateria"
                    )) {
                        continue;
                    }
                }

                String[] campos = lineaLimpia.split(",", -1);

                if (campos.length != 2
                        || campos[0].trim().isEmpty()
                        || campos[1].trim().isEmpty()) {
                    throw new ArchivoInvalidoException(
                            "Linea " + numeroLinea
                                    + " invalida: se esperaba "
                                    + "idEstudiante,codigoMateria"
                    );
                }

                String idEstudiante = campos[0].trim();
                String codigoMateria = campos[1].trim();

                SolicitudInscripcion solicitud =
                        new SolicitudInscripcion(
                                idEstudiante,
                                codigoMateria,
                                numeroLinea
                        );

                solicitudesTemporales.encolar(solicitud);
                solicitudesLeidas++;
            }

            if (lector.ioException() != null) {
                throw new ArchivoInvalidoException(
                        "No se pudo leer el archivo: " + rutaArchivo
                );
            }

            if (solicitudesLeidas == 0) {
                throw new ArchivoInvalidoException(
                        "El archivo no contiene solicitudes: " + rutaArchivo
                );
            }
        } catch (FileNotFoundException excepcion) {
            throw new ArchivoInvalidoException(
                    "No se pudo abrir el archivo: " + rutaArchivo
            );
        } finally {
            if (lector != null) {
                lector.close();
            }
        }

        while (!solicitudesTemporales.estaVacia()) {
            colaProcesamiento.encolar(solicitudesTemporales.desencolar());
        }

        return solicitudesLeidas;
    }

    /** Procesa todas las solicitudes pendientes en orden FIFO. */
    public void procesarCola() {
        if (colaProcesamiento.estaVacia()) {
            System.out.println("No hay solicitudes para procesar.");
            return;
        }

        exitosas = 0;
        enEspera = 0;
        fallidas = 0;
        detalleFallidas.vaciar();

        int total = colaProcesamiento.tamano();
        int numeroSolicitud = 0;

        while (!colaProcesamiento.estaVacia()) {
            SolicitudInscripcion solicitud =
                    colaProcesamiento.desencolar();
            numeroSolicitud++;

            try {
                boolean quedoInscrito = sistema.inscribir(
                        solicitud.getIdEstudiante(),
                        solicitud.getCodigoMateria()
                );

                if (quedoInscrito) {
                    exitosas++;
                    imprimirResultado(
                            numeroSolicitud,
                            total,
                            solicitud,
                            "Exitosa"
                    );
                } else {
                    enEspera++;
                    imprimirResultado(
                            numeroSolicitud,
                            total,
                            solicitud,
                            "En espera"
                    );
                }
            } catch (EstudianteNoEncontradoException excepcion) {
                registrarFalla(
                        numeroSolicitud,
                        total,
                        solicitud,
                        excepcion.getMessage()
                );
            } catch (PreRequisitoNoAprobadoException excepcion) {
                registrarFalla(
                        numeroSolicitud,
                        total,
                        solicitud,
                        excepcion.getMessage()
                );
            } catch (IllegalArgumentException excepcion) {
                registrarFalla(
                        numeroSolicitud,
                        total,
                        solicitud,
                        excepcion.getMessage()
                );
            }
        }

        System.out.println("=== RESUMEN ===");
        System.out.println("Exitosas: " + exitosas);
        System.out.println("En espera: " + enEspera);
        System.out.println("Fallidas: " + fallidas);

        if (fallidas > 0) {
            System.out.println("Detalle de fallidas:");
            int indice = 0;

            while (indice < detalleFallidas.tamano()) {
                System.out.println(detalleFallidas.obtener(indice));
                indice++;
            }
        }
    }

    private void imprimirResultado(
            int numeroSolicitud,
            int total,
            SolicitudInscripcion solicitud,
            String resultado
    ) {
        System.out.println(
                "[" + numeroSolicitud + "/" + total + "] "
                        + solicitud.getIdEstudiante() + " -> "
                        + solicitud.getCodigoMateria() + " -> "
                        + resultado
        );
    }

    private void registrarFalla(
            int numeroSolicitud,
            int total,
            SolicitudInscripcion solicitud,
            String mensaje
    ) {
        fallidas++;

        imprimirResultado(
                numeroSolicitud,
                total,
                solicitud,
                "Fallida"
        );

        String detalle = "Línea " + solicitud.getNumeroLinea()
                + ", ID " + solicitud.getIdEstudiante()
                + ", materia " + solicitud.getCodigoMateria()
                + ": " + mensaje;

        detalleFallidas.agregarAlFinal(detalle);
    }

    public int getExitosas() {
        return exitosas;
    }

    public int getEnEspera() {
        return enEspera;
    }

    public int getFallidas() {
        return fallidas;
    }

    public int cantidadPendientes() {
        return colaProcesamiento.tamano();
    }
}
