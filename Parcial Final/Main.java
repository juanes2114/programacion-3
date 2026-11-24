import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
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
import util.NavegadorReportes;
import util.SistemaAcademico;

/** Ejecuta pruebas de las estructuras y funciones desarrolladas hasta el paso 7. */
public class Main {
    public static void main(String[] args) {
        probarEstructuras();
        probarMateria();
        probarAula();
        probarSistemaAcademico();
        probarPolimorfismo();
        probarCampus();
        probarReportes();
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
        System.out.println("=== Paso 3: Materia y cola de espera ===");

        Materia materia =
                new Materia("MAT101", "Matemáticas", 2, 3);
        Estudiante ana =
                new Estudiante("Ana", "E001", "ana@uni.edu", 1);
        Estudiante luis =
                new Estudiante("Luis", "E002", "luis@uni.edu", 1);
        Estudiante marta =
                new Estudiante("Marta", "E003", "marta@uni.edu", 1);

        try {
            materia.agregarInscrito(ana);
            materia.agregarInscrito(luis);

            try {
                materia.agregarInscrito(marta);
            } catch (CupoLlenoException excepcion) {
                System.out.println("Cupo lleno: " + excepcion.getMessage());
                materia.encolarEnEspera(marta);
            }

            imprimirColaMateria(materia);
            materia.quitarInscrito(ana);
            Estudiante siguiente = materia.siguienteEnEspera();
            materia.agregarInscrito(siguiente);
            System.out.println("Pasa de espera a inscrita: "
                    + siguiente.getNombre());

            try {
                materia.siguienteEnEspera();
            } catch (ColaDeEsperaVaciaException excepcion) {
                System.out.println("Espera vacía: " + excepcion.getMessage());
            }
        } catch (CupoLlenoException excepcion) {
            System.out.println("Error de cupo: " + excepcion.getMessage());
        } catch (ColaDeEsperaVaciaException excepcion) {
            System.out.println("Error de espera: " + excepcion.getMessage());
        }
    }

    private static void imprimirColaMateria(Materia materia) {
        System.out.println("Cola de espera:");

        int indice = 0;
        while (indice < materia.cantidadEnEspera()) {
            Estudiante estudiante = materia.getColaEspera().obtener(indice);
            System.out.println(estudiante.getNombre());
            indice++;
        }
    }

    private static void probarAula() {
        System.out.println();
        System.out.println("=== Paso 4: Aula y reservas ===");

        Aula aula = new Aula("101", 30);

        try {
            aula.reservar(1, 8, 2, "CALC101");
            System.out.println("Lunes 8:00 libre: "
                    + aula.consultarDisponibilidad(1, 8));
            System.out.println("Lunes 10:00 libre: "
                    + aula.consultarDisponibilidad(1, 10));

            try {
                aula.reservar(1, 9, 2, "CALC102");
            } catch (HorarioConflictivoException excepcion) {
                System.out.println("Conflicto: " + excepcion.getMessage());
            }

            aula.liberar(1, 8, 2);
            System.out.println("Lunes 8:00 libre tras liberar: "
                    + aula.consultarDisponibilidad(1, 8));
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error de horario: " + excepcion.getMessage());
        }
    }

    private static void probarSistemaAcademico() {
        System.out.println();
        System.out.println("=== Paso 5: SistemaAcademico ===");

        SistemaAcademico sistema = new SistemaAcademico();
        sistema.registrarEstudiante("Ana Torres", "E001", "ana@uni.edu", 1);
        sistema.registrarEstudiante("Juan", "E002", "juan@uni.edu", 1);
        sistema.registrarEstudiante("Maria", "E003", "maria@uni.edu", 2);
        sistema.registrarEstudiante("Carlos", "E004", "carlos@uni.edu", 2);
        sistema.registrarEstudiante("Laura", "E005", "laura@uni.edu", 1);
        sistema.registrarEstudiante("Pablo", "E006", "pablo@uni.edu", 1);

        try {
            System.out.println("Encontrado: "
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
        sistema.mostrarColaEspera("CALC101");

        try {
            sistema.cancelarInscripcion("E006", "CALC101");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }
        sistema.mostrarColaEspera("CALC101");

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
            System.out.println("Nota de Ana: "
                    + ana.consultarNota(1, "CALC101"));
            System.out.println("Inscripción a CALC102: "
                    + sistema.inscribir("E001", "CALC102"));
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        try {
            sistema.eliminarEstudiante("E003");
            System.out.println("Maria eliminada.");
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        System.out.println("Estudiantes actuales:");
        sistema.listarEstudiantes();
        System.out.println("Inscritos en CALC101:");

        ListaEnlazada<Materia> materiasJuan =
                sistema.materiasInscritas("E002");
        System.out.println("Juan: " + materiasJuan);

        sistema.crearAula("Lab-1", 25);
        sistema.crearAula("101", 30);
        sistema.crearAula("A-2", 20);
        System.out.println("Aulas ordenadas:");
        sistema.listarAulas();

        try {
            sistema.reservarHorario("Lab-1", 1, 8, 2, "CALC101");
            sistema.reservarHorario("Lab-1", 1, 9, 1, "CALC101");
        } catch (HorarioConflictivoException excepcion) {
            System.out.println("Error: HorarioConflictivoException - "
                    + excepcion.getMessage());
        }

        Facultad[] facultades = new Facultad[5];
        facultades[0] = new Facultad("F01", "Ingeniería");
        facultades[1] = new Facultad("F02", "Ciencias");
        facultades[2] = new Facultad("F03", "Artes");
        facultades[3] = new Facultad("F04", "Derecho");
        facultades[4] = new Facultad("F05", "Salud");

        int indice = 0;
        while (indice < facultades.length) {
            sistema.agregarFacultad(facultades[indice]);
            indice++;
        }

        sistema.listarFacultades();

        try {
            sistema.agregarFacultad(new Facultad("F06", "Educación"));
        } catch (IllegalStateException excepcion) {
            System.out.println("Sexta facultad: " + excepcion.getMessage());
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
                System.out.println(estudiante.getNombre() + " quedó inscrito.");
            } else {
                System.out.println(estudiante.getNombre() + " quedó en espera.");
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
        personas[0] = new Estudiante(
                "Camila", "E007", "camila@uni.edu", 2
        );
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

        String[] nombres = {
                "Ingenieria", "Biblioteca", "Cafeteria",
                "Rectoria", "Laboratorios", "Gimnasio"
        };

        Campus campus = new Campus(nombres);
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

        Ruta ruta = campus.calcularRutaMasCorta(
                "Ingenieria", "Rectoria"
        );
        campus.mostrarRuta(ruta);

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

        probarEdificioInexistente(campus);
        probarConexionConsigoMismo(campus);
        probarDistanciaCero(campus);
        probarDistanciaNegativa(campus);
        probarCampusPequeno();
        probarNombreRepetido();
    }

    private static void probarEdificioInexistente(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Museo", 50);
        } catch (IllegalArgumentException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarConexionConsigoMismo(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Ingenieria", 50);
        } catch (IllegalArgumentException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarDistanciaCero(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Biblioteca", 0);
        } catch (IllegalArgumentException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarDistanciaNegativa(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Biblioteca", -10);
        } catch (IllegalArgumentException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarCampusPequeno() {
        try {
            String[] edificios = {"Uno", "Dos", "Tres", "Cuatro"};
            new Campus(edificios);
        } catch (IllegalArgumentException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarNombreRepetido() {
        try {
            String[] edificios = {
                    "Uno", "Dos", "dos", "Cuatro", "Cinco"
            };
            new Campus(edificios);
        } catch (IllegalArgumentException excepcion) {
            System.out.println(excepcion.getMessage());
        }
    }

    private static void probarReportes() {
        System.out.println();
        System.out.println("=== Paso 7: reportes académicos ===");

        Estudiante ana = crearAnaConNotas();

        System.out.println(ana.generarReporteAcademico());
        System.out.println("Promedio semestre 1: "
                + String.format("%.2f", ana.calcularPromedioSemestre(1)));
        System.out.println("Promedio semestre 2: "
                + String.format("%.2f", ana.calcularPromedioSemestre(2)));
        System.out.println("Promedio acumulado: "
                + String.format("%.2f", ana.calcularPromedioAcumulado()));

        Estudiante estudianteSinNotas =
                new Estudiante("Luis", "E008", "luis@uni.edu", 1);
        System.out.println(estudianteSinNotas.generarReporteAcademico());
        System.out.println("Promedio semestre vacío: "
                + estudianteSinNotas.calcularPromedioSemestre(4));

        System.out.println(ana.generarReporteSemestre(1));
        System.out.println(ana.generarReporteReprobadas());

        ana.registrarNota(
                1,
                new Materia("CALC101", "Cálculo I", 20, 4),
                3.0
        );
        System.out.println("Promedios después del cambio: semestre 1 = "
                + String.format("%.2f", ana.calcularPromedioSemestre(1))
                + ", acumulado = "
                + String.format("%.2f", ana.calcularPromedioAcumulado()));

        Materia estadistica =
                new Materia("EST101", "Estadística", 20, 3);
        ana.registrarNota(3, estadistica, 4.2);
        ana.deshacerRegistroNota(3, estadistica, null);
        System.out.println("Nota EST101 después de deshacer: "
                + ana.consultarNota(3, "EST101"));
        System.out.println("¿EST101 sigue en el historial?: "
                + ana.haCursado("EST101"));

        probarSemestreCero(ana);
        probarSemestreOnce(ana);
        probarNotaNegativa(ana);
        probarNotaMayorQueCinco(ana);
        probarVigesimoPrimeraMateria();

        probarNavegador(ana);
    }

    private static Estudiante crearAnaConNotas() {
        Estudiante ana = new Estudiante(
                "Ana Torres", "A001", "ana@uni.edu", 2
        );

        ana.registrarNota(
                1, new Materia("CALC101", "Cálculo I", 20, 4), 4.5
        );
        ana.registrarNota(
                1, new Materia("FIS101", "Física I", 20, 4), 2.8
        );
        ana.registrarNota(
                1, new Materia("PROG101", "Programación I", 20, 4), 5.0
        );
        ana.registrarNota(
                2, new Materia("CALC102", "Cálculo II", 20, 4), 3.5
        );
        ana.registrarNota(
                2, new Materia("FIS102", "Física II", 20, 4), 3.0
        );
        ana.registrarNota(
                2, new Materia("MATDIS", "Matemática Discreta", 20, 4), 2.5
        );

        return ana;
    }

    private static void probarSemestreCero(Estudiante estudiante) {
        try {
            estudiante.calcularPromedioSemestre(0);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Semestre inválido: " + excepcion.getMessage());
        }
    }

    private static void probarSemestreOnce(Estudiante estudiante) {
        try {
            estudiante.generarReporteSemestre(11);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Semestre inválido: " + excepcion.getMessage());
        }
    }

    private static void probarNotaNegativa(Estudiante estudiante) {
        try {
            estudiante.registrarNota(
                    1,
                    new Materia("INV101", "Nota inválida", 20, 1),
                    -1.0
            );
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Nota inválida: " + excepcion.getMessage());
        }
    }

    private static void probarNotaMayorQueCinco(Estudiante estudiante) {
        try {
            estudiante.registrarNota(
                    1,
                    new Materia("INV102", "Nota inválida", 20, 1),
                    5.5
            );
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Nota inválida: " + excepcion.getMessage());
        }
    }

    private static void probarVigesimoPrimeraMateria() {
        Estudiante estudiante =
                new Estudiante("Pedro", "E009", "pedro@uni.edu", 1);

        int numero = 1;
        while (numero <= 20) {
            String codigo = "MAT" + numero;
            Materia materia =
                    new Materia(codigo, "Materia " + numero, 20, 1);
            estudiante.registrarNota(1, materia, 4.0);
            numero++;
        }

        try {
            Materia materia21 =
                    new Materia("MAT21", "Materia 21", 20, 1);
            estudiante.registrarNota(1, materia21, 4.0);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Materia 21: " + excepcion.getMessage());
        }
    }

    private static void probarNavegador(Estudiante ana) {
        System.out.println();
        System.out.println("--- Navegador de reportes ---");

        NavegadorReportes navegador = new NavegadorReportes();
        navegador.mostrarReporte(ana.generarReporteAcademico());
        navegador.mostrarReporte(ana.generarReporteSemestre(1));
        navegador.mostrarReporte(ana.generarReporteReprobadas());

        navegador.irAtras();
        navegador.irAtras();
        navegador.irAdelante();

        navegador.mostrarReporte("Reporte nuevo");
        System.out.println("Reportes adelante: "
                + navegador.cantidadAdelante());

        try {
            navegador.irAdelante();
        } catch (IllegalStateException excepcion) {
            System.out.println("Adelante: " + excepcion.getMessage());
        }

        navegador.irAtras();
        navegador.irAtras();
        navegador.irAtras();

        try {
            navegador.irAtras();
        } catch (IllegalStateException excepcion) {
            System.out.println("Atrás: " + excepcion.getMessage());
        }
    }
}
