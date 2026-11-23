import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import modelo.Aula;
import modelo.Estudiante;
import modelo.Facultad;
import modelo.Materia;
import util.SistemaAcademico;

/** Pruebas de integración de SistemaAcademico. */
public class Main {
    public static void main(String[] args) {
        SistemaAcademico sistema = new SistemaAcademico();

        probarEstudiantes(sistema);
        probarMateriasYCiclo(sistema);
        probarInscripciones(sistema);
        probarPrerequisitoYNota(sistema);
        probarEliminacion(sistema);
        probarAulas(sistema);
        probarFacultades(sistema);
    }

    private static void probarEstudiantes(SistemaAcademico sistema) {
        System.out.println("=== Estudiantes ===");

        sistema.registrarEstudiante("Ana", "E001", "ana@uni.edu", 1);
        sistema.registrarEstudiante("Juan", "E002", "juan@uni.edu", 1);
        sistema.registrarEstudiante("Maria", "E003", "maria@uni.edu", 2);
        sistema.registrarEstudiante("Carlos", "E004", "carlos@uni.edu", 2);
        sistema.registrarEstudiante("Laura", "E005", "laura@uni.edu", 1);
        sistema.registrarEstudiante("Pablo", "E006", "pablo@uni.edu", 1);

        try {
            System.out.println("Encontrado: "
                    + sistema.buscarEstudiante("E001").getNombre());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println("Error: EstudianteNoEncontradoException - "
                    + excepcion.getMessage());
        }

        try {
            sistema.buscarEstudiante("E999");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println("Error: EstudianteNoEncontradoException - "
                    + excepcion.getMessage());
        }

        try {
            sistema.registrarEstudiante("Otro Juan", "E002", "otro@uni.edu", 1);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static void probarMateriasYCiclo(SistemaAcademico sistema) {
        System.out.println();
        System.out.println("=== Materias y prerrequisitos ===");

        sistema.crearMateria("CALC101", "Cálculo I", 3, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 2, 4);
        sistema.agregarPrerrequisito("CALC102", "CALC101");
        sistema.mostrarPrerrequisitos("CALC102");

        try {
            sistema.agregarPrerrequisito("CALC101", "CALC102");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Ciclo rechazado: " + excepcion.getMessage());
        }
    }

    private static void probarInscripciones(SistemaAcademico sistema) {
        System.out.println();
        System.out.println("=== Inscripciones y cola de espera ===");

        inscribir(sistema, "E002", "CALC101");
        inscribir(sistema, "E003", "CALC101");
        inscribir(sistema, "E004", "CALC101");
        inscribir(sistema, "E005", "CALC101");
        inscribir(sistema, "E006", "CALC101");
        sistema.mostrarColaEspera("CALC101");

        Estudiante queOcupoCupo = cancelar(sistema, "E002", "CALC101");
        System.out.println("Ocupó el cupo liberado: " + queOcupoCupo.getNombre());
        sistema.mostrarColaEspera("CALC101");

        cancelar(sistema, "E006", "CALC101");
        System.out.println("Pablo fue quitado de la cola.");
        sistema.mostrarColaEspera("CALC101");
    }

    private static void probarPrerequisitoYNota(SistemaAcademico sistema) {
        System.out.println();
        System.out.println("=== Prerrequisito y nota ===");

        try {
            sistema.inscribir("E001", "CALC102");
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println("Error: PreRequisitoNoAprobadoException - "
                    + excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println("Error: EstudianteNoEncontradoException - "
                    + excepcion.getMessage());
        }

        Estudiante ana;
        try {
            ana = sistema.buscarEstudiante("E001");
        } catch (EstudianteNoEncontradoException excepcion) {
            throw new IllegalStateException(excepcion);
        }

        ana.registrarNota(1, sistema.buscarMateria("CALC101"), 4.0);
        System.out.println("Nota registrada: "
                + ana.consultarNota(1, "CALC101"));

        try {
            System.out.println("Inscripción a CALC102 exitosa: "
                    + sistema.inscribir("E001", "CALC102"));
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static void probarEliminacion(SistemaAcademico sistema) {
        System.out.println();
        System.out.println("=== Eliminar estudiante ===");

        try {
            sistema.eliminarEstudiante("E003");
            System.out.println("Maria eliminada.");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }

        sistema.listarEstudiantes();
        System.out.println("Inscritos en CALC101: "
                + sistema.buscarMateria("CALC101").getInscritos().tamano());
    }

    private static void probarAulas(SistemaAcademico sistema) {
        System.out.println();
        System.out.println("=== Aulas y horarios ===");

        sistema.crearAula("Lab-1", 25);
        sistema.crearAula("101", 30);
        sistema.crearAula("A-2", 20);
        sistema.listarAulas();

        try {
            sistema.reservarHorario("Lab-1", 1, 8, 2, "CALC101");
            sistema.reservarHorario("Lab-1", 1, 9, 1, "CALC101");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error: HorarioConflictivoException - "
                    + excepcion.getMessage());
        }

        Aula laboratorio = sistema.buscarAula("Lab-1");
        System.out.println("Horario del laboratorio:");
        laboratorio.mostrarHorario();
    }

    private static void probarFacultades(SistemaAcademico sistema) {
        System.out.println();
        System.out.println("=== Facultades ===");

        sistema.agregarFacultad(new Facultad("F01", "Ingeniería"));
        sistema.agregarFacultad(new Facultad("F02", "Ciencias"));
        sistema.agregarFacultad(new Facultad("F03", "Artes"));
        sistema.agregarFacultad(new Facultad("F04", "Derecho"));
        sistema.agregarFacultad(new Facultad("F05", "Salud"));
        sistema.listarFacultades();

        try {
            sistema.agregarFacultad(new Facultad("F06", "Educación"));
        } catch (IllegalStateException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static void inscribir(
            SistemaAcademico sistema,
            String idEstudiante,
            String codigoMateria
    ) {
        try {
            boolean inscrito = sistema.inscribir(idEstudiante, codigoMateria);
            Estudiante estudiante = sistema.buscarEstudiante(idEstudiante);

            if (inscrito) {
                System.out.println(estudiante.getNombre() + " quedó inscrito.");
            } else {
                System.out.println(estudiante.getNombre() + " quedó en espera.");
            }
        } catch (EstudianteNoEncontradoException
                 | PreRequisitoNoAprobadoException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static Estudiante cancelar(
            SistemaAcademico sistema,
            String idEstudiante,
            String codigoMateria
    ) {
        try {
            return sistema.cancelarInscripcion(idEstudiante, codigoMateria);
        } catch (EstudianteNoEncontradoException excepcion) {
            throw new IllegalStateException(excepcion);
        }
    }
}
