import excepciones.ArchivoInvalidoException;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import estructuras.Cola;
import estructuras.ListaEnlazada;
import estructuras.Pila;
import modelo.Aula;
import modelo.Campus;
import modelo.Estudiante;
import modelo.Facultad;
import modelo.Materia;
import modelo.Persona;
import modelo.Profesor;
import modelo.Ruta;
import util.CancelarInscripcionOperacion;
import util.CambiarHorarioOperacion;
import util.EliminarEstudianteOperacion;
import util.GestorOperaciones;
import util.InscribirOperacion;
import util.NavegadorReportes;
import util.ProcesadorLotes;
import util.RegistrarNotaOperacion;
import util.SistemaAcademico;

/** Ejecuta pruebas de las estructuras y funciones desarrolladas hasta el paso 9. */
public class Main {
    public static void main(String[] args) {
        probarEstructuras();
        probarMateria();
        probarAula();
        probarSistemaAcademico();
        probarPolimorfismo();
        probarCampus();
        probarReportes();
        probarOperaciones();
        probarLotes();
    }

    private static void probarEstructuras() {
        System.out.println("=== Paso 2: estructuras propias ===");

        ListaEnlazada<String> lista = new ListaEnlazada<>();
        lista.agregarAlFinal("Algoritmos");
        lista.agregarAlFinal("Estructuras");
        lista.agregarAlInicio("Programación");

        System.out.println("Lista inicial: " + lista);
        System.out.println("Elemento en índice 1: " + lista.obtener(1));
        System.out.println("¿Contiene Estructuras?: "
                + lista.contiene("Estructuras"));
        System.out.println("¿Eliminó Algoritmos?: "
                + lista.eliminar("Algoritmos"));
        System.out.println("Lista final: " + lista);

        Pila<Integer> pila = new Pila<>();
        pila.push(10);
        pila.push(20);
        pila.push(30);

        System.out.println("Pila desde el tope: " + pila);
        System.out.println("Elemento retirado: " + pila.pop());
        System.out.println("Nuevo tope: " + pila.peek());

        pila.pop();
        pila.pop();

        try {
            pila.pop();
        } catch (IllegalStateException excepcion) {
            System.out.println("Error esperado: " + excepcion.getMessage());
        }

        Cola<String> cola = new Cola<>();
        cola.encolar("Ana");
        cola.encolar("Luis");
        cola.encolar("Marta");
        cola.encolar("Carlos");

        System.out.println("Cola inicial: " + cola);
        System.out.println("Atendido: " + cola.desencolar());
        System.out.println("¿Eliminó Marta?: " + cola.eliminar("Marta"));
        System.out.println("Cola final: " + cola);

        cola.desencolar();
        cola.desencolar();

        try {
            cola.desencolar();
        } catch (IllegalStateException excepcion) {
            System.out.println("Error esperado: " + excepcion.getMessage());
        }
    }

    private static void probarMateria() {
        System.out.println();
        System.out.println("=== Paso 3: materias y cola de espera ===");

        Materia calculo101 =
                new Materia("CALC101", "Cálculo I", 2, 4);
        Materia calculo102 =
                new Materia("CALC102", "Cálculo II", 2, 4);
        calculo102.agregarPrerrequisito(calculo101);

        System.out.println(calculo101);
        System.out.println(calculo102);
        System.out.println("Prerrequisito de CALC102: "
                + calculo102.getPrerrequisitos().obtener(0).getCodigo());

        Estudiante ana = crearEstudiante("Ana", "E001");
        Estudiante juan = crearEstudiante("Juan", "E002");
        Estudiante maria = crearEstudiante("Maria", "E003");

        try {
            calculo101.agregarInscrito(ana);
            calculo101.agregarInscrito(juan);

            try {
                calculo101.agregarInscrito(maria);
            } catch (CupoLlenoException excepcion) {
                System.out.println("Cupo lleno: " + excepcion.getMessage());
                calculo101.encolarEnEspera(maria);
            }

            imprimirColaDeMateria(calculo101);

            calculo101.quitarInscrito(ana);
            Estudiante siguiente = calculo101.siguienteEnEspera();
            calculo101.agregarInscrito(siguiente);
            System.out.println("Pasa de espera a inscrita: "
                    + siguiente.getNombre());

            try {
                calculo101.siguienteEnEspera();
            } catch (ColaDeEsperaVaciaException excepcion) {
                System.out.println("Espera vacía: " + excepcion.getMessage());
            }
        } catch (CupoLlenoException excepcion) {
            System.out.println("Error de cupo: " + excepcion.getMessage());
        } catch (ColaDeEsperaVaciaException excepcion) {
            System.out.println("Error de espera: " + excepcion.getMessage());
        }
    }

    private static void imprimirColaDeMateria(Materia materia) {
        System.out.println("Estudiantes en espera:");

        int indice = 0;
        while (indice < materia.cantidadEnEspera()) {
            Estudiante estudiante =
                    materia.getColaEspera().obtener(indice);
            System.out.println("- " + estudiante.getNombre());
            indice++;
        }
    }

    private static void probarAula() {
        System.out.println();
        System.out.println("=== Paso 4: horarios de aula ===");

        Aula aula = new Aula("101", 30);

        try {
            aula.reservar(1, 8, 2, "CALC101");

            System.out.println("Lunes 8:00 libre: "
                    + aula.consultarDisponibilidad(1, 8));
            System.out.println("Lunes 10:00 libre: "
                    + aula.consultarDisponibilidad(1, 10));
            System.out.println("Materia el lunes a las 9:00: "
                    + aula.materiaEn(1, 9));

            try {
                aula.reservar(1, 9, 2, "CALC102");
            } catch (HorarioConflictivoException excepcion) {
                System.out.println("Conflicto: " + excepcion.getMessage());
            }

            System.out.println("Lunes 10:00 sigue libre: "
                    + aula.consultarDisponibilidad(1, 10));

            aula.liberar(1, 8, 2);
            System.out.println("Lunes 8:00 libre tras liberar: "
                    + aula.consultarDisponibilidad(1, 8));

            try {
                aula.liberar(1, 8, 1);
            } catch (IllegalArgumentException excepcion) {
                System.out.println("Error al liberar: "
                        + excepcion.getMessage());
            }
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error de horario: "
                    + excepcion.getMessage());
        }

        probarReservaInvalida(aula, 7, 8, 1);
        probarReservaInvalida(aula, 1, 24, 1);
        probarReservaInvalida(aula, 6, 23, 2);

        try {
            aula.reservar(2, 10, 1, "FIS101");
            aula.reservar(6, 14, 2, "HIST101");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        aula.mostrarHorario();
    }

    private static void probarReservaInvalida(
            Aula aula,
            int dia,
            int hora,
            int duracion
    ) {
        try {
            aula.reservar(dia, hora, duracion, "MAT101");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Reserva inválida: "
                    + excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarSistemaAcademico() {
        System.out.println();
        System.out.println("=== Paso 5: SistemaAcademico ===");

        SistemaAcademico sistema = crearSistemaDePrueba();

        try {
            System.out.println("Estudiante encontrado: "
                    + sistema.buscarEstudiante("E001").getNombre());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        try {
            sistema.buscarEstudiante("E999");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println("Error: EstudianteNoEncontradoException - "
                    + excepcion.getMessage());
        }

        try {
            sistema.registrarEstudiante(
                    "Otro Juan", "E002", "otro@uni.edu", 1
            );
        } catch (IllegalArgumentException excepcion) {
            System.out.println("ID repetido: " + excepcion.getMessage());
        }

        sistema.crearMateria("CALC101", "Cálculo I", 3, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 2, 4);
        sistema.agregarPrerrequisito("CALC102", "CALC101");
        sistema.mostrarPrerrequisitos("CALC102");

        try {
            sistema.agregarPrerrequisito("CALC101", "CALC102");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Ciclo rechazado: "
                    + excepcion.getMessage());
        }

        inscribirYMostrar(sistema, "E002", "CALC101");
        inscribirYMostrar(sistema, "E003", "CALC101");
        inscribirYMostrar(sistema, "E004", "CALC101");
        inscribirYMostrar(sistema, "E005", "CALC101");
        inscribirYMostrar(sistema, "E006", "CALC101");
        sistema.mostrarColaEspera("CALC101");

        try {
            Estudiante queOcupoCupo =
                    sistema.cancelarInscripcion("E002", "CALC101");
            if (queOcupoCupo != null) {
                System.out.println("Ocupó el cupo: "
                        + queOcupoCupo.getNombre());
            }
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        sistema.mostrarColaEspera("CALC101");

        try {
            sistema.cancelarInscripcion("E006", "CALC101");
            System.out.println("Pablo salió de la cola.");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        try {
            sistema.inscribir("E001", "CALC102");
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println("Prerrequisito pendiente: "
                    + excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        try {
            Estudiante ana = sistema.buscarEstudiante("E001");
            ana.registrarNota(
                    1,
                    sistema.buscarMateria("CALC101"),
                    4.0
            );
            System.out.println("Inscripción tras aprobar prerrequisito: "
                    + sistema.inscribir("E001", "CALC102"));
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        sistema.crearAula("Lab-1", 25);
        sistema.crearAula("101", 30);
        sistema.crearAula("A-2", 20);
        System.out.println("Aulas en orden:");
        sistema.listarAulas();

        try {
            sistema.reservarHorario(
                    "Lab-1", 1, 8, 2, "CALC101"
            );
            sistema.reservarHorario(
                    "Lab-1", 1, 9, 1, "CALC101"
            );
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Conflicto: " + excepcion.getMessage());
        }

        try {
            sistema.eliminarEstudiante("E003");
            System.out.println("Maria eliminada.");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        System.out.println("Estudiantes restantes:");
        sistema.listarEstudiantes();

        sistema.agregarFacultad(new Facultad("F01", "Ingeniería"));
        sistema.agregarFacultad(new Facultad("F02", "Ciencias"));
        sistema.agregarFacultad(new Facultad("F03", "Artes"));
        sistema.agregarFacultad(new Facultad("F04", "Derecho"));
        sistema.agregarFacultad(new Facultad("F05", "Salud"));
        sistema.listarFacultades();

        try {
            sistema.agregarFacultad(new Facultad("F06", "Educación"));
        } catch (IllegalStateException excepcion) {
            System.out.println("Sexta facultad: "
                    + excepcion.getMessage());
        }
    }

    private static void inscribirYMostrar(
            SistemaAcademico sistema,
            String id,
            String codigoMateria
    ) {
        try {
            boolean inscrito = sistema.inscribir(id, codigoMateria);
            Estudiante estudiante = sistema.buscarEstudiante(id);

            if (inscrito) {
                System.out.println(estudiante.getNombre()
                        + " quedó inscrito.");
            } else {
                System.out.println(estudiante.getNombre()
                        + " quedó en espera.");
            }
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarPolimorfismo() {
        System.out.println();
        System.out.println("=== Polimorfismo ===");

        Persona[] personas = new Persona[2];
        personas[0] = crearEstudiante("Camila Rojas", "E007");
        personas[1] = new Profesor(
                "Carlos Gómez",
                "P001",
                "carlos@uni.edu",
                "Matemáticas",
                4200000
        );

        int indice = 0;
        while (indice < personas.length) {
            personas[indice].mostrarInformacion();
            indice++;
        }
    }

    private static void probarCampus() {
        System.out.println();
        System.out.println("=== Paso 6: Campus y Dijkstra ===");

        String[] edificios = {
                "Ingenieria", "Biblioteca", "Cafeteria",
                "Rectoria", "Laboratorios", "Gimnasio"
        };

        Campus campus = new Campus(edificios);
        campus.mostrarEdificios();

        campus.agregarConexion("Ingenieria", "Biblioteca", 100);
        campus.agregarConexion("Ingenieria", "Cafeteria", 150);
        campus.agregarConexion("Ingenieria", "Rectoria", 400);
        campus.agregarConexion("Biblioteca", "Cafeteria", 120);
        campus.agregarConexion("Biblioteca", "Laboratorios", 90);
        campus.agregarConexion("Cafeteria", "Rectoria", 180);
        campus.agregarConexion("Laboratorios", "Rectoria", 350);

        campus.mostrarMatriz();
        System.out.println("Matriz simétrica: "
                + campus.esMatrizSimetrica());

        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Ingenieria", "Rectoria"
        ));
        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Ingenieria", "Laboratorios"
        ));
        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Rectoria", "Ingenieria"
        ));
        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Biblioteca", "Biblioteca"
        ));

        try {
            campus.calcularRutaMasCorta("Ingenieria", "Gimnasio");
        } catch (IllegalStateException excepcion) {
            System.out.println("Sin ruta: " + excepcion.getMessage());
        }

        campus.agregarConexion("Gimnasio", "Cafeteria", 60);
        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Ingenieria", "Gimnasio"
        ));

        campus.agregarConexion("Ingenieria", "Rectoria", 200);
        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Ingenieria", "Rectoria"
        ));

        probarConexionInvalida(campus, "Ingenieria", "Museo", 50);
        probarConexionInvalida(campus, "Ingenieria", "Ingenieria", 50);
        probarConexionInvalida(campus, "Ingenieria", "Biblioteca", 0);
        probarConexionInvalida(campus, "Ingenieria", "Biblioteca", -10);
        probarCampusConPocosEdificios();
        probarCampusConNombresRepetidos();
    }

    private static void probarConexionInvalida(
            Campus campus,
            String origen,
            String destino,
            int metros
    ) {
        try {
            campus.agregarConexion(origen, destino, metros);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Conexión inválida: "
                    + excepcion.getMessage());
        }
    }

    private static void probarCampusConPocosEdificios() {
        try {
            String[] edificios = {"Uno", "Dos", "Tres", "Cuatro"};
            new Campus(edificios);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Campus inválido: "
                    + excepcion.getMessage());
        }
    }

    private static void probarCampusConNombresRepetidos() {
        try {
            String[] edificios = {
                    "Uno", "Dos", "dos", "Cuatro", "Cinco"
            };
            new Campus(edificios);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Nombres inválidos: "
                    + excepcion.getMessage());
        }
    }

    private static void probarReportes() {
        System.out.println();
        System.out.println("=== Paso 7: reportes ===");

        Estudiante ana = crearEstudiante("Ana Torres", "A001");
        ana.registrarNota(1, crearMateria("CALC101"), 4.5);
        ana.registrarNota(1, crearMateria("FIS101"), 2.8);
        ana.registrarNota(1, crearMateria("PROG101"), 5.0);
        ana.registrarNota(2, crearMateria("CALC102"), 3.5);
        ana.registrarNota(2, crearMateria("FIS102"), 3.0);
        ana.registrarNota(2, crearMateria("MATDIS"), 2.5);

        System.out.println(ana.generarReporteAcademico());
        System.out.println(ana.generarReporteSemestre(1));
        System.out.println(ana.generarReporteReprobadas());

        Estudiante sinNotas = crearEstudiante("Luis", "E008");
        System.out.println(sinNotas.generarReporteAcademico());
        System.out.println("Promedio semestre vacío: "
                + sinNotas.calcularPromedioSemestre(4));

        ana.registrarNota(1, crearMateria("CALC101"), 3.0);
        System.out.println("Promedio semestre 1 tras cambio: "
                + String.format("%.2f", ana.calcularPromedioSemestre(1)));
        System.out.println("Promedio acumulado tras cambio: "
                + String.format("%.2f", ana.calcularPromedioAcumulado()));

        Materia estadistica = crearMateria("EST101");
        ana.registrarNota(3, estadistica, 4.2);
        ana.deshacerRegistroNota(3, estadistica, null);
        System.out.println("EST101 después de deshacer: "
                + ana.consultarNota(3, "EST101"));
        System.out.println("EST101 en historial: "
                + ana.haCursado("EST101"));

        probarNotaInvalida(ana, 0, crearMateria("INV001"), 3.0);
        probarNotaInvalida(ana, 11, crearMateria("INV002"), 3.0);
        probarNotaInvalida(ana, 1, crearMateria("INV003"), -1.0);
        probarNotaInvalida(ana, 1, crearMateria("INV004"), 5.5);
        probarVigesimoPrimeraNota();

        NavegadorReportes navegador = new NavegadorReportes();
        navegador.mostrarReporte(ana.generarReporteAcademico());
        navegador.mostrarReporte(ana.generarReporteSemestre(1));
        navegador.mostrarReporte(ana.generarReporteReprobadas());
        navegador.irAtras();
        navegador.irAtras();
        navegador.irAdelante();
        navegador.mostrarReporte("Reporte nuevo");

        try {
            navegador.irAdelante();
        } catch (IllegalStateException excepcion) {
            System.out.println("Adelante: " + excepcion.getMessage());
        }

        while (navegador.hayAnterior()) {
            navegador.irAtras();
        }

        try {
            navegador.irAtras();
        } catch (IllegalStateException excepcion) {
            System.out.println("Atrás: " + excepcion.getMessage());
        }
    }

    private static void probarNotaInvalida(
            Estudiante estudiante,
            int semestre,
            Materia materia,
            double nota
    ) {
        try {
            estudiante.registrarNota(semestre, materia, nota);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Dato de nota inválido: "
                    + excepcion.getMessage());
        }
    }

    private static void probarVigesimoPrimeraNota() {
        Estudiante estudiante = crearEstudiante("Pedro", "E009");
        int numero = 1;

        while (numero <= 20) {
            String codigo = "MAT" + numero;
            estudiante.registrarNota(
                    1,
                    crearMateria(codigo),
                    4.0
            );
            numero++;
        }

        try {
            estudiante.registrarNota(
                    1,
                    crearMateria("MAT21"),
                    4.0
            );
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Materia 21: " + excepcion.getMessage());
        }
    }

    private static void probarOperaciones() {
        System.out.println();
        System.out.println("=== Paso 8: deshacer y rehacer ===");

        probarInscripcionDeshacerRehacer();
        probarCancelacionYEspera();
        probarCancelacionEnMedio();
        probarDeshacerNota();
        probarCambioHorario();
        probarEliminarEstudiante();
        probarOperacionFallida();
        probarOperacionNuevaLimpiaRehacer();
        probarDeshacerHastaPilaVacia();
    }

    private static void probarInscripcionDeshacerRehacer() {
        System.out.println("--- Inscribir, deshacer y rehacer ---");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new InscribirOperacion(
                    sistema, "A001", "CALC101"
            ));

            System.out.println("Inscrita: "
                    + sistema.buscarMateria("CALC101")
                    .estaInscrito(sistema.buscarEstudiante("A001")));
            System.out.println("Operacion deshecha: " + gestor.deshacer());
            System.out.println("Inscrita después de deshacer: "
                    + sistema.buscarMateria("CALC101")
                    .estaInscrito(sistema.buscarEstudiante("A001")));
            System.out.println("Operacion rehecha: " + gestor.rehacer());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        try {
            gestor.rehacer();
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println("Error: PilaDeshacerVaciaException - "
                    + excepcion.getMessage());
        }
    }

    private static void probarCancelacionYEspera() {
        System.out.println("--- Cancelar inscripción y restaurar espera ---");

        SistemaAcademico sistema = prepararSistemaConEspera();
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new CancelarInscripcionOperacion(
                    sistema, "E002", "CALC101"
            ));

            System.out.println("Cola tras cancelar a Juan:");
            sistema.mostrarColaEspera("CALC101");

            gestor.deshacer();
            System.out.println("Cola tras deshacer:");
            sistema.mostrarColaEspera("CALC101");
            System.out.println("Juan inscrito otra vez: "
                    + sistema.buscarMateria("CALC101")
                    .estaInscrito(sistema.buscarEstudiante("E002")));
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarCancelacionEnMedio() {
        System.out.println("--- Cancelar persona del medio de la espera ---");

        SistemaAcademico sistema = prepararSistemaConEspera();
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new CancelarInscripcionOperacion(
                    sistema, "E004", "CALC101"
            ));
            System.out.println("Cola sin Carlos:");
            sistema.mostrarColaEspera("CALC101");

            gestor.deshacer();
            int posicion = sistema.buscarMateria("CALC101")
                    .posicionEnEspera(
                            sistema.buscarEstudiante("E004")
                    );
            System.out.println("Posición de Carlos restaurada: " + posicion);
            sistema.mostrarColaEspera("CALC101");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarDeshacerNota() {
        System.out.println("--- Deshacer y rehacer notas ---");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new RegistrarNotaOperacion(
                    sistema, "A001", 1, "CALC101", 4.0
            ));
            gestor.deshacer();

            Estudiante ana = sistema.buscarEstudiante("A001");
            System.out.println("Nota tras deshacer: "
                    + ana.consultarNota(1, "CALC101"));

            gestor.ejecutar(new RegistrarNotaOperacion(
                    sistema, "A001", 1, "CALC101", 4.0
            ));
            gestor.ejecutar(new RegistrarNotaOperacion(
                    sistema, "A001", 1, "CALC101", 3.0
            ));
            gestor.deshacer();
            System.out.println("Nota restaurada: "
                    + ana.consultarNota(1, "CALC101"));
            gestor.rehacer();
            System.out.println("Nota tras rehacer: "
                    + ana.consultarNota(1, "CALC101"));
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarCambioHorario() {
        System.out.println("--- Cambiar, deshacer y rehacer horario ---");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 5, 4);
        Aula aula = sistema.crearAula("101", 30);
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            sistema.reservarHorario("101", 1, 8, 2, "CALC101");
            sistema.reservarHorario("101", 2, 10, 2, "CALC102");

            gestor.ejecutar(new CambiarHorarioOperacion(
                    sistema, "101", "CALC101",
                    1, 8, 2, 3, 8, 2
            ));

            System.out.println("Lunes libre: "
                    + aula.consultarDisponibilidad(1, 8));
            System.out.println("Miércoles ocupado: "
                    + !aula.consultarDisponibilidad(3, 8));

            gestor.deshacer();
            System.out.println("Lunes ocupado tras deshacer: "
                    + !aula.consultarDisponibilidad(1, 8));
            gestor.rehacer();

            int operacionesAntes = gestor.cantidadDeshacer();

            try {
                gestor.ejecutar(new CambiarHorarioOperacion(
                        sistema, "101", "CALC101",
                        3, 8, 2, 2, 11, 2
                ));
            } catch (HorarioConflictivoException excepcion) {
                System.out.println("Conflicto esperado: "
                        + excepcion.getMessage());
            }

            System.out.println("Miércoles sigue ocupado: "
                    + !aula.consultarDisponibilidad(3, 8));
            System.out.println("Historial sin cambio: "
                    + (operacionesAntes == gestor.cantidadDeshacer()));
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarEliminarEstudiante() {
        System.out.println("--- Eliminar y restaurar estudiante ---");

        SistemaAcademico sistema = new SistemaAcademico();
        Estudiante ana = sistema.registrarEstudiante(
                "Ana", "A001", "ana@uni.edu", 1
        );
        Estudiante juan = sistema.registrarEstudiante(
                "Juan", "E002", "juan@uni.edu", 1
        );
        sistema.crearMateria("CALC101", "Cálculo I", 2, 4);
        sistema.crearMateria("FIS101", "Física I", 1, 4);
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            sistema.buscarMateria("CALC101").agregarInscrito(ana);
            sistema.buscarMateria("FIS101").agregarInscrito(juan);
            sistema.buscarMateria("FIS101").encolarEnEspera(ana);
        } catch (CupoLlenoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        ana.registrarNota(
                1,
                sistema.buscarMateria("CALC101"),
                4.5
        );

        try {
            gestor.ejecutar(
                    new EliminarEstudianteOperacion(sistema, "A001")
            );

            try {
                sistema.buscarEstudiante("A001");
            } catch (EstudianteNoEncontradoException excepcion) {
                System.out.println("Eliminada: " + excepcion.getMessage());
            }

            gestor.deshacer();
            Estudiante restaurada = sistema.buscarEstudiante("A001");

            System.out.println("Ana restaurada: "
                    + restaurada.getNombre());
            System.out.println("Inscrita en CALC101: "
                    + sistema.buscarMateria("CALC101")
                    .estaInscrito(restaurada));
            System.out.println("Posición en espera de FIS101: "
                    + sistema.buscarMateria("FIS101")
                    .posicionEnEspera(restaurada));
            System.out.println("Nota conservada: "
                    + restaurada.consultarNota(1, "CALC101"));

            gestor.rehacer();
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarOperacionFallida() {
        System.out.println("--- Operación fallida no se apila ---");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 2, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 2, 4);
        sistema.agregarPrerrequisito("CALC102", "CALC101");

        GestorOperaciones gestor = new GestorOperaciones();
        int cantidadAntes = gestor.cantidadDeshacer();

        try {
            gestor.ejecutar(new InscribirOperacion(
                    sistema, "A001", "CALC102"
            ));
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println("Error: PreRequisitoNoAprobadoException - "
                    + excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        System.out.println("Pila sin cambio: "
                + (cantidadAntes == gestor.cantidadDeshacer()));
    }

    private static void probarOperacionNuevaLimpiaRehacer() {
        System.out.println("--- Operación nueva limpia rehacer ---");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        sistema.crearMateria("FIS101", "Física I", 5, 4);
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new InscribirOperacion(
                    sistema, "A001", "CALC101"
            ));
            gestor.deshacer();
            System.out.println("Rehacer disponible: "
                    + gestor.cantidadRehacer());

            gestor.ejecutar(new InscribirOperacion(
                    sistema, "A001", "FIS101"
            ));
            System.out.println("Rehacer tras operación nueva: "
                    + gestor.cantidadRehacer());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarDeshacerHastaPilaVacia() {
        System.out.println("--- Mostrar pilas y deshacer hasta vaciar ---");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        sistema.crearMateria("FIS101", "Física I", 5, 4);
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new InscribirOperacion(
                    sistema, "A001", "CALC101"
            ));
            gestor.ejecutar(new InscribirOperacion(
                    sistema, "A001", "FIS101"
            ));
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        gestor.mostrarPilas();

        try {
            while (gestor.hayParaDeshacer()) {
                System.out.println("Operacion deshecha: "
                        + gestor.deshacer());
            }

            gestor.deshacer();
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println("Fin de deshacer: "
                    + excepcion.getMessage());
        }
    }

    private static SistemaAcademico prepararSistemaConEspera() {
        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "E001", "ana@uni.edu", 1);
        sistema.registrarEstudiante("Juan", "E002", "juan@uni.edu", 1);
        sistema.registrarEstudiante("Maria", "E003", "maria@uni.edu", 1);
        sistema.registrarEstudiante("Carlos", "E004", "carlos@uni.edu", 1);
        sistema.registrarEstudiante("Laura", "E005", "laura@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 2, 4);

        try {
            sistema.buscarMateria("CALC101").agregarInscrito(
                    sistema.buscarEstudiante("E001")
            );
            sistema.buscarMateria("CALC101").agregarInscrito(
                    sistema.buscarEstudiante("E002")
            );
        } catch (CupoLlenoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        sistema.buscarMateria("CALC101").encolarEnEspera(
                buscarEstudianteSeguro(sistema, "E003")
        );
        sistema.buscarMateria("CALC101").encolarEnEspera(
                buscarEstudianteSeguro(sistema, "E004")
        );
        sistema.buscarMateria("CALC101").encolarEnEspera(
                buscarEstudianteSeguro(sistema, "E005")
        );

        return sistema;
    }

    private static void probarLotes() {
        System.out.println();
        System.out.println("=== Paso 9: procesamiento por lotes ===");

        SistemaAcademico sistema = crearSistemaParaLotes();
        ProcesadorLotes procesador = new ProcesadorLotes(sistema);

        try {
            int cantidadCargada = procesador.cargarArchivo(
                    "datos/inscripciones.csv"
            );
            System.out.println("Solicitudes cargadas: " + cantidadCargada);
            System.out.println("Pendientes antes de procesar: "
                    + procesador.cantidadPendientes());
            procesador.procesarCola();
        } catch (ArchivoInvalidoException excepcion) {
            System.out.println("Error: ArchivoInvalidoException - "
                    + excepcion.getMessage());
        }

        System.out.println("Cola de CALC101:");
        sistema.mostrarColaEspera("CALC101");
        System.out.println("Cola de FIS101:");
        sistema.mostrarColaEspera("FIS101");
        System.out.println("Pendientes después de procesar: "
                + procesador.cantidadPendientes());

        try {
            procesador.cargarArchivo("datos/no_existe.csv");
        } catch (ArchivoInvalidoException excepcion) {
            System.out.println("Error: ArchivoInvalidoException - "
                    + excepcion.getMessage());
        }

        try {
            procesador.cargarArchivo(
                    "datos/inscripciones_invalido.csv"
            );
        } catch (ArchivoInvalidoException excepcion) {
            System.out.println("Error: ArchivoInvalidoException - "
                    + excepcion.getMessage());
        }

        System.out.println("Pendientes tras archivo inválido: "
                + procesador.cantidadPendientes());
        procesador.procesarCola();
    }

    private static SistemaAcademico crearSistemaParaLotes() {
        SistemaAcademico sistema = new SistemaAcademico();

        sistema.registrarEstudiante("Ana", "E001", "ana@uni.edu", 1);
        sistema.registrarEstudiante("Juan", "E002", "juan@uni.edu", 1);
        sistema.registrarEstudiante("Maria", "E003", "maria@uni.edu", 1);
        sistema.registrarEstudiante("Carlos", "E004", "carlos@uni.edu", 1);
        sistema.registrarEstudiante("Laura", "E005", "laura@uni.edu", 1);
        sistema.registrarEstudiante("Pablo", "E006", "pablo@uni.edu", 1);
        sistema.registrarEstudiante("Sofia", "E007", "sofia@uni.edu", 1);
        sistema.registrarEstudiante("Diego", "E008", "diego@uni.edu", 1);

        sistema.crearMateria("CALC101", "Cálculo I", 3, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 2, 4);
        sistema.crearMateria("FIS101", "Física I", 2, 4);
        sistema.agregarPrerrequisito("CALC102", "CALC101");

        Estudiante ana = buscarEstudianteSeguro(sistema, "E001");
        ana.registrarNota(
                1,
                sistema.buscarMateria("CALC101"),
                4.0
        );

        return sistema;
    }

    private static Estudiante buscarEstudianteSeguro(
            SistemaAcademico sistema,
            String id
    ) {
        try {
            return sistema.buscarEstudiante(id);
        } catch (EstudianteNoEncontradoException excepcion) {
            throw new IllegalStateException(excepcion.getMessage());
        }
    }

    private static SistemaAcademico crearSistemaDePrueba() {
        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana", "E001", "ana@uni.edu", 1);
        sistema.registrarEstudiante("Juan", "E002", "juan@uni.edu", 1);
        sistema.registrarEstudiante("Maria", "E003", "maria@uni.edu", 2);
        sistema.registrarEstudiante("Carlos", "E004", "carlos@uni.edu", 2);
        sistema.registrarEstudiante("Laura", "E005", "laura@uni.edu", 1);
        sistema.registrarEstudiante("Pablo", "E006", "pablo@uni.edu", 1);
        return sistema;
    }

    private static Materia crearMateria(String codigo) {
        return new Materia(codigo, codigo, 20, 4);
    }

    private static Estudiante crearEstudiante(String nombre, String id) {
        return new Estudiante(
                nombre,
                id,
                id.toLowerCase() + "@uni.edu",
                1
        );
    }
}
