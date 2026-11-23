import excepciones.HorarioConflictivoException;
import modelo.Aula;

/** Pruebas de reservas y disponibilidad de un aula. */
public class Main {
    public static void main(String[] args) {
        Aula aula = new Aula("101", 30);

        probarReservaInicial(aula);
        consultarHorario(aula);
        probarConflictoSinReservaParcial(aula);
        probarLiberacion(aula);
        probarDatosInvalidos(aula);
        mostrarHorarioSemanal(aula);
    }

    private static void probarReservaInicial(Aula aula) {
        System.out.println("=== Reserva inicial ===");

        try {
            aula.reservar(1, 8, 2, "CALC101");
            System.out.println(aula);
            System.out.println("Reserva de CALC101 realizada el lunes de 8:00 a 10:00.");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error: HorarioConflictivoException - "
                    + excepcion.getMessage());
        }
    }

    private static void consultarHorario(Aula aula) {
        System.out.println();
        System.out.println("=== Consulta de disponibilidad ===");

        System.out.println("Lunes 8:00 libre: "
                + aula.consultarDisponibilidad(1, 8));
        System.out.println("Lunes 10:00 libre: "
                + aula.consultarDisponibilidad(1, 10));
        System.out.println("Materia del lunes a las 9:00: "
                + aula.materiaEn(1, 9));
    }

    private static void probarConflictoSinReservaParcial(Aula aula) {
        System.out.println();
        System.out.println("=== Conflicto y reserva atómica ===");

        try {
            aula.reservar(1, 9, 2, "CALC102");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error: HorarioConflictivoException - "
                    + excepcion.getMessage());
        }

        System.out.println("Lunes 10:00 sigue libre: "
                + aula.consultarDisponibilidad(1, 10));
    }

    private static void probarLiberacion(Aula aula) {
        System.out.println();
        System.out.println("=== Liberar horario ===");

        aula.liberar(1, 8, 2);
        System.out.println("Lunes 8:00 libre después de liberar: "
                + aula.consultarDisponibilidad(1, 8));

        try {
            aula.liberar(1, 8, 1);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }
    }

    private static void probarDatosInvalidos(Aula aula) {
        System.out.println();
        System.out.println("=== Validación de datos ===");

        try {
            aula.reservar(7, 8, 1, "MAT101");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Día inválido: " + excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error de horario: " + excepcion.getMessage());
        }

        try {
            aula.reservar(1, 24, 1, "MAT101");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Hora inválida: " + excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error de horario: " + excepcion.getMessage());
        }

        try {
            aula.reservar(6, 23, 2, "MAT101");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Duración inválida: " + excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error de horario: " + excepcion.getMessage());
        }
    }

    private static void mostrarHorarioSemanal(Aula aula) {
        System.out.println();
        System.out.println("=== Horario semanal ===");

        try {
            aula.reservar(2, 10, 1, "FIS101");
            aula.reservar(6, 14, 2, "HIST101");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error: HorarioConflictivoException - "
                    + excepcion.getMessage());
        }

        aula.mostrarHorario();
    }
}
