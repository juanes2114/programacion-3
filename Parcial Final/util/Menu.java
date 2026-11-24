package util;

import excepciones.ArchivoInvalidoException;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import modelo.Aula;
import modelo.Campus;
import modelo.Estudiante;
import modelo.Materia;

import java.util.Scanner;

/** Menú de consola para usar el sistema de gestión académica. */
public class Menu {
    private Scanner scanner;
    private SistemaAcademico sistema;
    private Campus campus;
    private GestorOperaciones gestor;
    private NavegadorReportes navegador;
    private ProcesadorLotes procesador;

    /** Crea el menú con los componentes del sistema ya preparados. */
    public Menu(
            SistemaAcademico sistema,
            Campus campus,
            GestorOperaciones gestor,
            NavegadorReportes navegador,
            ProcesadorLotes procesador
    ) {
        if (sistema == null || campus == null || gestor == null
                || navegador == null || procesador == null) {
            throw new IllegalArgumentException(
                    "Los componentes del menú no pueden ser null."
            );
        }

        scanner = new Scanner(System.in);
        this.sistema = sistema;
        this.campus = campus;
        this.gestor = gestor;
        this.navegador = navegador;
        this.procesador = procesador;
    }

    /** Muestra y atiende el menú hasta que el usuario elija salir. */
    public void iniciar() {
        int opcion = 0;

        do {
            imprimirMenu();
            String textoOpcion = leerTextoSinValidarVacio(
                    "Seleccione una opcion: "
            );

            try {
                opcion = Integer.parseInt(textoOpcion);
                ejecutarOpcion(opcion);
            } catch (NumberFormatException excepcion) {
                System.out.println(
                        "Debes escribir un numero entre 1 y 22."
                );
            }
        } while (opcion != 22);

        scanner.close();
    }

    private void imprimirMenu() {
        System.out.println("============================================================");
        System.out.println("PLANIFICACION ACADEMICA - SISTEMA UNIVERSITARIO");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("=== GESTION DE ESTUDIANTES ===");
        System.out.println("1. Registrar estudiante");
        System.out.println("2. Buscar estudiante por ID");
        System.out.println("3. Listar todos los estudiantes");
        System.out.println("4. Eliminar estudiante");
        System.out.println();
        System.out.println("=== GESTION DE MATERIAS ===");
        System.out.println("5. Crear materia");
        System.out.println("6. Agregar pre-requisito");
        System.out.println("7. Mostrar pre-requisitos");
        System.out.println("8. Inscribir estudiante");
        System.out.println("9. Cancelar inscripcion");
        System.out.println("10. Mostrar cola de espera");
        System.out.println();
        System.out.println("=== GESTION DE HORARIOS ===");
        System.out.println("11. Reservar horario en aula");
        System.out.println("12. Liberar horario");
        System.out.println("13. Consultar disponibilidad");
        System.out.println();
        System.out.println("=== RUTAS ENTRE EDIFICIOS ===");
        System.out.println("14. Agregar conexion entre edificios");
        System.out.println("15. Calcular ruta mas corta");
        System.out.println();
        System.out.println("=== REPORTES ACADEMICOS ===");
        System.out.println("16. Registrar nota");
        System.out.println("17. Ver reporte academico");
        System.out.println("18. Navegador de reportes (atras/adelante)");
        System.out.println();
        System.out.println("=== SISTEMA DESHACER/REHACER ===");
        System.out.println("19. Deshacer ultima operacion");
        System.out.println("20. Rehacer ultima operacion");
        System.out.println();
        System.out.println("=== PROCESAMIENTO POR LOTES ===");
        System.out.println("21. Procesar archivo CSV");
        System.out.println();
        System.out.println("=== SALIR ===");
        System.out.println("22. Salir");
        System.out.println();
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarEstudiante();
                break;
            case 2:
                buscarEstudiante();
                break;
            case 3:
                sistema.listarEstudiantes();
                break;
            case 4:
                eliminarEstudiante();
                break;
            case 5:
                crearMateria();
                break;
            case 6:
                agregarPrerrequisito();
                break;
            case 7:
                mostrarPrerrequisitos();
                break;
            case 8:
                inscribirEstudiante();
                break;
            case 9:
                cancelarInscripcion();
                break;
            case 10:
                mostrarColaEspera();
                break;
            case 11:
                reservarHorario();
                break;
            case 12:
                liberarHorario();
                break;
            case 13:
                consultarDisponibilidad();
                break;
            case 14:
                agregarConexion();
                break;
            case 15:
                calcularRuta();
                break;
            case 16:
                registrarNota();
                break;
            case 17:
                verReporte();
                break;
            case 18:
                navegarReportes();
                break;
            case 19:
                deshacer();
                break;
            case 20:
                rehacer();
                break;
            case 21:
                procesarArchivo();
                break;
            case 22:
                System.out.println(
                        "Gracias por usar el sistema. Hasta luego!"
                );
                break;
            default:
                System.out.println("Opcion no valida. Intenta de nuevo.");
                break;
        }
    }

    private String leerTexto(String mensaje) {
        String texto = "";

        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("El texto no puede quedar vacío.");
            }
        }

        return texto;
    }

    private String leerTextoSinValidarVacio(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);

            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException excepcion) {
                System.out.println("Debes escribir un numero entero.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            texto = texto.replace(',', '.');

            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException excepcion) {
                System.out.println("Debes escribir un numero decimal.");
            }
        }
    }

    private boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje);

            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Responde s o n.");
        }
    }

    private void registrarEstudiante() {
        String id = leerTexto("ID: ");
        String nombre = leerTexto("Nombre: ");
        String email = leerTexto("Email: ");
        int semestre = leerEntero("Semestre actual: ");

        try {
            sistema.registrarEstudiante(nombre, id, email, semestre);
            System.out.println("Estudiante registrado exitosamente.");
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void buscarEstudiante() {
        String id = leerTexto("ID del estudiante: ");

        try {
            Estudiante estudiante = sistema.buscarEstudiante(id);
            System.out.println("Resultado encontrado:");
            System.out.println("  ID: " + estudiante.getId());
            System.out.println("  Nombre: " + estudiante.getNombre());
            System.out.println("  Email: " + estudiante.getEmail());
            System.out.println("  Semestre: " + estudiante.getSemestre());

            if (estudiante.contarNotas() == 0) {
                System.out.println(
                        "  Promedio acumulado: 0.0 (sin notas aun)"
                );
            } else {
                System.out.println(
                        "  Promedio acumulado: "
                                + String.format(
                                        "%.2f",
                                        estudiante.calcularPromedioAcumulado()
                                )
                );
            }
        } catch (EstudianteNoEncontradoException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void eliminarEstudiante() {
        String id = leerTexto("ID del estudiante: ");

        if (!confirmar(
                "Esto es una baja definitiva. ¿Continuar? (s/n): "
        )) {
            System.out.println("Operacion cancelada.");
            return;
        }

        EliminarEstudianteOperacion operacion =
                new EliminarEstudianteOperacion(sistema, id);

        if (ejecutarConGestor(operacion)) {
            System.out.println(
                    "Estudiante eliminado. Puedes deshacerlo con la opcion 19."
            );
        }
    }

    private void crearMateria() {
        String codigo = leerTexto("Código de la materia: ");
        String nombre = leerTexto("Nombre: ");
        int cupos = leerEntero("Cupos máximos: ");
        int creditos = leerEntero("Créditos: ");

        try {
            sistema.crearMateria(codigo, nombre, cupos, creditos);
            System.out.println("Materia creada exitosamente.");
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void agregarPrerrequisito() {
        String codigoMateria = leerTexto("Código de la materia: ");
        String codigoPrerrequisito =
                leerTexto("Código del prerrequisito: ");

        try {
            sistema.agregarPrerrequisito(
                    codigoMateria,
                    codigoPrerrequisito
            );
            System.out.println("Pre-requisito agregado.");
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void mostrarPrerrequisitos() {
        String codigo = leerTexto("Código de la materia: ");

        try {
            sistema.mostrarPrerrequisitos(codigo);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void inscribirEstudiante() {
        String id = leerTexto("ID del estudiante: ");
        String codigo = leerTexto("Código de la materia: ");

        InscribirOperacion operacion =
                new InscribirOperacion(sistema, id, codigo);

        if (ejecutarConGestor(operacion)) {
            if (operacion.quedoInscrito()) {
                System.out.println("Inscripcion exitosa");
            } else {
                System.out.println(
                        "Materia llena. Agregada a COLA DE ESPERA"
                );
            }
        }
    }

    private void cancelarInscripcion() {
        String id = leerTexto("ID del estudiante: ");
        String codigo = leerTexto("Código de la materia: ");

        CancelarInscripcionOperacion operacion =
                new CancelarInscripcionOperacion(sistema, id, codigo);

        if (ejecutarConGestor(operacion)) {
            System.out.println("Cancelacion exitosa. Cupo liberado.");

            Estudiante asignado = operacion.getEstudianteAsignado();
            if (asignado != null) {
                System.out.println(
                        "Asignando cupo a " + asignado.getNombre()
                                + " (primer estudiante en cola)"
                );
            }
        }
    }

    private void mostrarColaEspera() {
        String codigo = leerTexto("Código de la materia: ");

        try {
            sistema.mostrarColaEspera(codigo);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }
      private void reservarHorario() {
        System.out.println("1. Reserva nueva");
        System.out.println("2. Cambiar el horario de una materia");
        int opcion = leerEntero("Opcion: ");

        if (opcion == 1) {
            reservarHorarioNuevo();
        } else if (opcion == 2) {
            cambiarHorarioExistente();
        } else {
            System.out.println("Opcion no valida. Intenta de nuevo.");
        }
    }

    private void reservarHorarioNuevo() {
        String aula = leerTexto("Aula: ");
        int dia = leerEntero(
                "Dia (0=Domingo, 1=Lunes ... 6=Sabado): "
        );
        int hora = leerEntero("Hora (0 a 23): ");
        int duracion = leerEntero("Duracion en horas: ");
        String codigo = leerTexto("Código de la materia: ");

        try {
            sistema.reservarHorario(
                    aula,
                    dia,
                    hora,
                    duracion,
                    codigo
            );
            System.out.println("Reserva exitosa.");
        } catch (HorarioConflictivoException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void cambiarHorarioExistente() {
        String aula = leerTexto("Aula: ");
        String codigo = leerTexto("Código de la materia: ");
        int diaAnterior = leerEntero("Día actual: ");
        int horaAnterior = leerEntero("Hora actual: ");
        int duracionAnterior = leerEntero("Duración actual: ");
        int diaNuevo = leerEntero("Día nuevo: ");
        int horaNueva = leerEntero("Hora nueva: ");
        int duracionNueva = leerEntero("Duración nueva: ");

        CambiarHorarioOperacion operacion =
                new CambiarHorarioOperacion(
                        sistema,
                        aula,
                        codigo,
                        diaAnterior,
                        horaAnterior,
                        duracionAnterior,
                        diaNuevo,
                        horaNueva,
                        duracionNueva
                );

        if (ejecutarConGestor(operacion)) {
            System.out.println(
                    "Horario cambiado. Puedes deshacerlo con la opcion 19."
            );
        }
    }

    private void liberarHorario() {
        String aula = leerTexto("Aula: ");
        int dia = leerEntero("Día: ");
        int hora = leerEntero("Hora: ");
        int duracion = leerEntero("Duración: ");

        try {
            sistema.liberarHorario(aula, dia, hora, duracion);
            System.out.println("Horario liberado.");
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void consultarDisponibilidad() {
        String nombreAula = leerTexto("Aula: ");
        int dia = leerEntero("Día: ");
        int hora = leerEntero("Hora: ");

        try {
            Aula aula = sistema.buscarAula(nombreAula);
            boolean libre =
                    sistema.consultarDisponibilidad(nombreAula, dia, hora);
            String nombreDia = Aula.nombreDia(dia);

            if (libre) {
                System.out.println(
                        nombreDia + " " + hora + ":00 -> LIBRE"
                );
            } else {
                String codigoMateria = aula.materiaEn(dia, hora);
                System.out.println(
                        nombreDia + " " + hora
                                + ":00 -> OCUPADO (" + codigoMateria + ")"
                );
            }
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void agregarConexion() {
        campus.mostrarEdificios();

        String origen = leerTexto("Origen: ");
        String destino = leerTexto("Destino: ");
        int metros = leerEntero("Distancia en metros: ");

        try {
            campus.agregarConexion(origen, destino, metros);
            System.out.println("Conexion guardada.");
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void calcularRuta() {
        campus.mostrarEdificios();

        String origen = leerTexto("Origen: ");
        String destino = leerTexto("Destino: ");

        try {
            campus.mostrarRuta(
                    campus.calcularRutaMasCorta(origen, destino)
            );
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalStateException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void registrarNota() {
        String id = leerTexto("ID del estudiante: ");
        int semestre = leerEntero("Semestre (1 a 10): ");
        String codigoMateria = leerTexto("Código de la materia: ");
        double nota = leerDecimal("Nota (0 a 5): ");

        RegistrarNotaOperacion operacion =
                new RegistrarNotaOperacion(
                        sistema,
                        id,
                        semestre,
                        codigoMateria,
                        nota
                );

        if (ejecutarConGestor(operacion)) {
            System.out.println(
                    "Nota registrada. Puedes deshacerla con la opcion 19."
            );
        }
    }

    private void verReporte() {
        String id = leerTexto("ID del estudiante: ");

        try {
            Estudiante estudiante = sistema.buscarEstudiante(id);

            System.out.println("1. Reporte completo");
            System.out.println("2. Reporte de un semestre");
            System.out.println("3. Materias reprobadas");

            int opcion = leerEntero("Opcion: ");
            String reporte;

            if (opcion == 1) {
                reporte = estudiante.generarReporteAcademico();
            } else if (opcion == 2) {
                int semestre = leerEntero("Semestre (1 a 10): ");
                reporte = estudiante.generarReporteSemestre(semestre);
            } else if (opcion == 3) {
                reporte = estudiante.generarReporteReprobadas();
            } else {
                System.out.println("Opcion no valida. Intenta de nuevo.");
                return;
            }

            navegador.mostrarReporte(reporte);
        } catch (EstudianteNoEncontradoException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void navegarReportes() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("1. Atras");
            System.out.println("2. Adelante");
            System.out.println("3. Ver estado");
            System.out.println("0. Volver");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    try {
                        navegador.irAtras();
                    } catch (IllegalStateException excepcion) {
                        mostrarError(excepcion);
                    }
                    break;
                case 2:
                    try {
                        navegador.irAdelante();
                    } catch (IllegalStateException excepcion) {
                        mostrarError(excepcion);
                    }
                    break;
                case 3:
                    mostrarEstadoNavegador();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida. Intenta de nuevo.");
                    break;
            }
        }
    }

    private void mostrarEstadoNavegador() {
        System.out.println("Reportes anteriores: "
                + navegador.cantidadAtras());
        System.out.println("Reportes siguientes: "
                + navegador.cantidadAdelante());

        if (navegador.getReporteActual() == null) {
            System.out.println("No hay reporte actual.");
        } else {
            System.out.println("Hay un reporte actual.");
        }
    }

    private void deshacer() {
        try {
            String descripcion = gestor.deshacer();
            System.out.println("Operacion deshecha: " + descripcion);
        } catch (PilaDeshacerVaciaException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalStateException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void rehacer() {
        try {
            String descripcion = gestor.rehacer();
            System.out.println("Operacion rehecha: " + descripcion);
        } catch (PilaDeshacerVaciaException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalStateException excepcion) {
            mostrarError(excepcion);
        }
    }

    private void procesarArchivo() {
        System.out.print(
                "Archivo (Enter para datos/inscripciones.csv): "
        );
        String ruta = scanner.nextLine().trim();

        if (ruta.isEmpty()) {
            ruta = "datos/inscripciones.csv";
        }

        try {
            int cantidad = procesador.cargarArchivo(ruta);
            System.out.println("Se encolaron " + cantidad + " solicitudes.");
            procesador.procesarCola();
        } catch (ArchivoInvalidoException excepcion) {
            mostrarError(excepcion);
        }
    }

    /**
     * Ejecuta una operación reversible y comunica los errores al usuario.
     *
     * @return true si la operación se ejecutó; false si falló
     */
    private boolean ejecutarConGestor(Operacion operacion) {
        try {
            gestor.ejecutar(operacion);
            return true;
        } catch (EstudianteNoEncontradoException excepcion) {
            mostrarError(excepcion);
        } catch (PreRequisitoNoAprobadoException excepcion) {
            mostrarError(excepcion);
        } catch (HorarioConflictivoException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion);
        } catch (IllegalStateException excepcion) {
            mostrarError(excepcion);
        }

        return false;
    }

    private void mostrarError(Exception excepcion) {
        System.out.println(
                "Error: " + excepcion.getClass().getSimpleName()
                        + " - " + excepcion.getMessage()
        );
    }

    private void mostrarError(RuntimeException excepcion) {
        System.out.println("Error: " + excepcion.getMessage());
    }
}
