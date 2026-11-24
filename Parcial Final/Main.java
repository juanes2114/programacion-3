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
import util.RegistrarNotaOperacion;
import util.SistemaAcademico;

/** Ejecuta pruebas de las estructuras y funciones implementadas hasta el paso 8. */
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
    }

    private static void probarEstructuras() {
        System.out.println("=== Paso 2: estructuras propias ===");

        ListaEnlazada<String> lista = new ListaEnlazada<>();
        lista.agregarAlFinal("Uno");
        lista.agregarAlFinal("Dos");
        lista.agregarAlInicio("Cero");
        System.out.println("Lista: " + lista);
        System.out.println("Elemento índice 1: " + lista.obtener(1));

        Pila<Integer> pila = new Pila<>();
        pila.push(10);
        pila.push(20);
        System.out.println("Pila: " + pila);
        System.out.println("Sale de la pila: " + pila.pop());

        Cola<String> cola = new Cola<>();
        cola.encolar("Ana");
        cola.encolar("Luis");
        cola.encolar("Marta");
        cola.eliminar("Luis");
        System.out.println("Cola: " + cola);
        System.out.println("Sale de la cola: " + cola.desencolar());
    }

    private static void probarMateria() {
        System.out.println();
        System.out.println("=== Paso 3: Materia y cola ===");

        Materia materia = new Materia("MAT101", "Matemáticas", 2, 3);
        Estudiante ana = crearEstudiante("Ana", "E001");
        Estudiante juan = crearEstudiante("Juan", "E002");
        Estudiante maria = crearEstudiante("Maria", "E003");

        try {
            materia.agregarInscrito(ana);
            materia.agregarInscrito(juan);
            materia.agregarInscrito(maria);
        } catch (CupoLlenoException excepcion) {
            System.out.println("Cupo lleno: " + excepcion.getMessage());
            materia.encolarEnEspera(maria);
        }

        System.out.println("Espera: " + materia.cantidadEnEspera());
        System.out.println("Primero en espera: "
                + materia.getColaEspera().obtener(0).getNombre());

        materia.quitarInscrito(ana);
        try {
            Estudiante siguiente = materia.siguienteEnEspera();
            materia.agregarInscrito(siguiente);
            System.out.println("Pasa a inscrito: " + siguiente.getNombre());
        } catch (ColaDeEsperaVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (CupoLlenoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarAula() {
        System.out.println();
        System.out.println("=== Paso 4: Aula ===");

        Aula aula = new Aula("101", 30);

        try {
            aula.reservar(1, 8, 2, "CALC101");
            System.out.println("Lunes 8 libre: "
                    + aula.consultarDisponibilidad(1, 8));

            try {
                aula.reservar(1, 9, 1, "FIS101");
            } catch (HorarioConflictivoException excepcion) {
                System.out.println("Conflicto: " + excepcion.getMessage());
            }

            aula.liberar(1, 8, 2);
            System.out.println("Lunes 8 libre al liberar: "
                    + aula.consultarDisponibilidad(1, 8));
        } catch (HorarioConflictivoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarSistemaAcademico() {
        System.out.println();
        System.out.println("=== Paso 5: Sistema académico ===");

        SistemaAcademico sistema = new SistemaAcademico();
        registrarEstudiantesDePrueba(sistema);
        sistema.crearMateria("CALC101", "Cálculo I", 3, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 2, 4);
        sistema.agregarPrerrequisito("CALC102", "CALC101");

        try {
            sistema.agregarPrerrequisito("CALC101", "CALC102");
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Ciclo rechazado: " + excepcion.getMessage());
        }

        inscribirYMostrar(sistema, "E002", "CALC101");
        inscribirYMostrar(sistema, "E003", "CALC101");
        inscribirYMostrar(sistema, "E004", "CALC101");
        inscribirYMostrar(sistema, "E005", "CALC101");
        inscribirYMostrar(sistema, "E006", "CALC101");
        sistema.mostrarColaEspera("CALC101");

        try {
            Estudiante asignado =
                    sistema.cancelarInscripcion("E002", "CALC101");
            System.out.println("Nuevo inscrito: " + asignado.getNombre());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        sistema.crearAula("Lab-1", 25);
        sistema.crearAula("101", 30);
        sistema.crearAula("A-2", 20);
        sistema.listarAulas();

        try {
            sistema.reservarHorario("Lab-1", 1, 8, 2, "CALC101");
            sistema.reservarHorario("Lab-1", 1, 9, 1, "CALC101");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Conflicto: " + excepcion.getMessage());
        }

        sistema.agregarFacultad(new Facultad("F01", "Ingeniería"));
        sistema.agregarFacultad(new Facultad("F02", "Ciencias"));
        sistema.agregarFacultad(new Facultad("F03", "Artes"));
        sistema.agregarFacultad(new Facultad("F04", "Derecho"));
        sistema.agregarFacultad(new Facultad("F05", "Salud"));
        sistema.listarFacultades();

        try {
            sistema.agregarFacultad(new Facultad("F06", "Educación"));
        } catch (IllegalStateException excepcion) {
            System.out.println("Facultad adicional: " + excepcion.getMessage());
        }
    }

    private static void registrarEstudiantesDePrueba(
            SistemaAcademico sistema
    ) {
        sistema.registrarEstudiante("Ana", "E001", "ana@uni.edu", 1);
        sistema.registrarEstudiante("Juan", "E002", "juan@uni.edu", 1);
        sistema.registrarEstudiante("Maria", "E003", "maria@uni.edu", 2);
        sistema.registrarEstudiante("Carlos", "E004", "carlos@uni.edu", 2);
        sistema.registrarEstudiante("Laura", "E005", "laura@uni.edu", 1);
        sistema.registrarEstudiante("Pablo", "E006", "pablo@uni.edu", 1);
    }

    private static void inscribirYMostrar(
            SistemaAcademico sistema,
            String id,
            String codigo
    ) {
        try {
            boolean inscrito = sistema.inscribir(id, codigo);
            Estudiante estudiante = sistema.buscarEstudiante(id);

            if (inscrito) {
                System.out.println(estudiante.getNombre() + " inscrito.");
            } else {
                System.out.println(estudiante.getNombre() + " en espera.");
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
        personas[0] = crearEstudiante("Camila", "E007");
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
        System.out.println("=== Paso 6: Campus ===");

        String[] edificios = {
                "Ingenieria", "Biblioteca", "Cafeteria",
                "Rectoria", "Laboratorios", "Gimnasio"
        };
        Campus campus = new Campus(edificios);
        campus.agregarConexion("Ingenieria", "Biblioteca", 100);
        campus.agregarConexion("Ingenieria", "Cafeteria", 150);
        campus.agregarConexion("Ingenieria", "Rectoria", 400);
        campus.agregarConexion("Biblioteca", "Cafeteria", 120);
        campus.agregarConexion("Biblioteca", "Laboratorios", 90);
        campus.agregarConexion("Cafeteria", "Rectoria", 180);
        campus.agregarConexion("Laboratorios", "Rectoria", 350);

        System.out.println("Matriz simétrica: "
                + campus.esMatrizSimetrica());
        Ruta ruta = campus.calcularRutaMasCorta(
                "Ingenieria", "Rectoria"
        );
        campus.mostrarRuta(ruta);

        try {
            campus.calcularRutaMasCorta("Ingenieria", "Gimnasio");
        } catch (IllegalStateException excepcion) {
            System.out.println("Sin ruta: " + excepcion.getMessage());
        }

        campus.agregarConexion("Gimnasio", "Cafeteria", 60);
        campus.mostrarRuta(campus.calcularRutaMasCorta(
                "Ingenieria", "Gimnasio"
        ));
    }

    private static void probarReportes() {
        System.out.println();
        System.out.println("=== Paso 7: reportes ===");

        Estudiante ana = crearEstudiante("Ana Torres", "A001");
        Materia calculo = new Materia("CALC101", "Cálculo I", 20, 4);
        Materia fisica = new Materia("FIS101", "Física I", 20, 4);

        ana.registrarNota(1, calculo, 4.5);
        ana.registrarNota(1, fisica, 2.8);
        System.out.println(ana.generarReporteAcademico());

        NavegadorReportes navegador = new NavegadorReportes();
        navegador.mostrarReporte(ana.generarReporteAcademico());
        navegador.mostrarReporte(ana.generarReporteSemestre(1));
        navegador.irAtras();
    }

    private static void probarOperaciones() {
        System.out.println();
        System.out.println("=== Paso 8: deshacer y rehacer ===");

        probarInscripcionDeshacerRehacer();
        probarCancelacionYEspera();
        probarCancelacionEnMedio();
        probarRegistroNota();
        probarCambioHorario();
        probarEliminarEstudiante();
        probarOperacionFallida();
        probarCaminoNuevo();
        probarPilasHastaVacias();
    }

    private static void probarInscripcionDeshacerRehacer() {
        System.out.println("--- Inscribir, deshacer y rehacer ---");

        SistemaAcademico sistema = new SistemaAcademico();
        GestorOperaciones gestor = new GestorOperaciones();
        Estudiante ana = sistema.registrarEstudiante(
                "Ana", "A001", "ana@uni.edu", 1
        );
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);

        InscribirOperacion operacion =
                new InscribirOperacion(sistema, "A001", "CALC101");

        try {
            gestor.ejecutar(operacion);
            System.out.println("Inscrita: "
                    + sistema.buscarMateria("CALC101").estaInscrito(ana));
            System.out.println("Operacion deshecha: " + gestor.deshacer());
            System.out.println("Inscrita después de deshacer: "
                    + sistema.buscarMateria("CALC101").estaInscrito(ana));
            System.out.println("Operacion rehecha: " + gestor.rehacer());
            System.out.println("Inscrita después de rehacer: "
                    + sistema.buscarMateria("CALC101").estaInscrito(ana));
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
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
        System.out.println("--- Cancelar y restaurar cola ---");

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
        System.out.println("--- Cancelar estudiante del medio de la espera ---");

        SistemaAcademico sistema = prepararSistemaConEspera();
        GestorOperaciones gestor = new GestorOperaciones();

        try {
            gestor.ejecutar(new CancelarInscripcionOperacion(
                    sistema, "E004", "CALC101"
            ));
            System.out.println("Cola sin Carlos:");
            sistema.mostrarColaEspera("CALC101");

            gestor.deshacer();
            System.out.println("Carlos vuelve a la posición "
                    + sistema.buscarMateria("CALC101")
                    .posicionEnEspera(sistema.buscarEstudiante("E004")));
            sistema.mostrarColaEspera("CALC101");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarRegistroNota() {
        System.out.println("--- Deshacer y rehacer notas ---");

        SistemaAcademico sistema = new SistemaAcademico();
        GestorOperaciones gestor = new GestorOperaciones();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);

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
        GestorOperaciones gestor = new GestorOperaciones();
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 5, 4);
        Aula aula = sistema.crearAula("101", 30);

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

            int cantidadAntes = gestor.cantidadDeshacer();

            try {
                gestor.ejecutar(new CambiarHorarioOperacion(
                        sistema, "101", "CALC101",
                        3, 8, 2, 2, 11, 2
                ));
            } catch (HorarioConflictivoException excepcion) {
                System.out.println("Conflicto esperado: "
                        + excepcion.getMessage());
            }

            System.out.println("Lunes sigue libre: "
                    + aula.consultarDisponibilidad(1, 8));
            System.out.println("Miércoles sigue ocupado: "
                    + !aula.consultarDisponibilidad(3, 8));
            System.out.println("Pila sin cambio: "
                    + (cantidadAntes == gestor.cantidadDeshacer()));
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
        GestorOperaciones gestor = new GestorOperaciones();
        Estudiante ana = sistema.registrarEstudiante(
                "Ana", "A001", "ana@uni.edu", 1
        );
        Estudiante juan = sistema.registrarEstudiante(
                "Juan", "E002", "juan@uni.edu", 1
        );
        sistema.crearMateria("CALC101", "Cálculo I", 2, 4);
        sistema.crearMateria("FIS101", "Física I", 1, 4);

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
        GestorOperaciones gestor = new GestorOperaciones();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 2, 4);
        sistema.crearMateria("CALC102", "Cálculo II", 2, 4);
        sistema.agregarPrerrequisito("CALC102", "CALC101");

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

    private static void probarCaminoNuevo() {
        System.out.println("--- Operación nueva limpia rehacer ---");

        SistemaAcademico sistema = new SistemaAcademico();
        GestorOperaciones gestor = new GestorOperaciones();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        sistema.crearMateria("FIS101", "Física I", 5, 4);

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
            System.out.println("Rehacer tras nueva operación: "
                    + gestor.cantidadRehacer());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PilaDeshacerVaciaException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarPilasHastaVacias() {
        System.out.println("--- Contenido de las pilas ---");

        SistemaAcademico sistema = new SistemaAcademico();
        GestorOperaciones gestor = new GestorOperaciones();
        sistema.registrarEstudiante("Ana", "A001", "ana@uni.edu", 1);
        sistema.crearMateria("CALC101", "Cálculo I", 5, 4);
        sistema.crearMateria("FIS101", "Física I", 5, 4);

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
            System.out.println("Fin de deshacer: " + excepcion.getMessage());
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

    private static Estudiante crearEstudiante(String nombre, String id) {
        return new Estudiante(
                nombre,
                id,
                id.toLowerCase() + "@uni.edu",
                1
        );
    }
}
