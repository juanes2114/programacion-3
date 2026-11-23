import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import modelo.Estudiante;
import modelo.Materia;
import modelo.Persona;
import modelo.Profesor;

/** Pruebas de materias, cupos, notas e herencia. */
public class Main {
    public static void main(String[] args) {
        probarMateriasYPrerrequisitos();
        probarInscripcionYCola();
        probarRegistroDeNota();
        probarPolimorfismo();
    }

    private static void probarMateriasYPrerrequisitos() {
        System.out.println("=== Materias y prerrequisitos ===");

        Materia calculo101 = new Materia("CALC101", "Cálculo I", 2, 4);
        Materia calculo102 = new Materia("CALC102", "Cálculo II", 2, 4);
        calculo102.agregarPrerrequisito(calculo101);

        System.out.println(calculo101);
        System.out.println(calculo102);
        System.out.println("Prerrequisito de CALC102: "
                + calculo102.getPrerrequisitos().obtener(0).getCodigo());
    }

    private static void probarInscripcionYCola() {
        System.out.println();
        System.out.println("=== Inscripción y cola de espera ===");

        Materia materia = new Materia("CALC101", "Cálculo I", 2, 4);
        Estudiante ana = new Estudiante("Ana Torres", "A001", "ana@uni.edu", 1);
        Estudiante luis = new Estudiante("Luis Pérez", "A002", "luis@uni.edu", 1);
        Estudiante marta = new Estudiante("Marta Díaz", "A003", "marta@uni.edu", 1);

        try {
            materia.agregarInscrito(ana);
            materia.agregarInscrito(luis);
            System.out.println("Inscritos: " + materia.getInscritos().tamano());

            try {
                materia.agregarInscrito(marta);
            } catch (CupoLlenoException excepcion) {
                System.out.println("Excepción capturada: " + excepcion.getMessage());
                materia.encolarEnEspera(marta);
            }

            System.out.println("Cola de espera: " + materia.getColaEspera());

            materia.quitarInscrito(ana);
            Estudiante siguiente = materia.siguienteEnEspera();
            materia.agregarInscrito(siguiente);
            System.out.println("Entra desde la espera: " + siguiente.getNombre());
            System.out.println("Inscritos ahora: " + materia.getInscritos().tamano());

            try {
                materia.siguienteEnEspera();
            } catch (ColaDeEsperaVaciaException excepcion) {
                System.out.println("Excepción capturada: " + excepcion.getMessage());
            }
        } catch (CupoLlenoException excepcion) {
            System.out.println("No se pudo completar la inscripción: " + excepcion.getMessage());
        } catch (ColaDeEsperaVaciaException excepcion) {
            System.out.println("No se pudo atender la espera: " + excepcion.getMessage());
        }
    }

    private static void probarRegistroDeNota() {
        System.out.println();
        System.out.println("=== Registro de nota e historial ===");

        Estudiante estudiante = new Estudiante(
                "Ana Torres",
                "A001",
                "ana@uni.edu",
                1
        );
        Materia materia = new Materia("CALC101", "Cálculo I", 2, 4);

        estudiante.registrarNota(1, materia, 4.2);

        System.out.println("Nota en CALC101: "
                + estudiante.consultarNota(1, "CALC101"));
        System.out.println("¿Aprobó CALC101?: "
                + estudiante.haAprobado("CALC101"));
        System.out.println("¿Ha cursado CALC101?: "
                + estudiante.haCursado("CALC101"));
        System.out.println("Historial: " + estudiante.getHistorialMaterias());
    }

    private static void probarPolimorfismo() {
        System.out.println();
        System.out.println("=== Polimorfismo ===");

        Persona[] personas = new Persona[2];
        personas[0] = new Estudiante(
                "Camila Rojas",
                "A004",
                "camila@uni.edu",
                2
        );
        personas[1] = new Profesor(
                "Carlos Gómez",
                "P001",
                "carlos@uni.edu",
                "Matemáticas",
                4200000
        );

        for (Persona persona : personas) {
            persona.mostrarInformacion();
            System.out.println();
        }
    }
}
