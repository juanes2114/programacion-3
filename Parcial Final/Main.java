import modelo.Campus;
import modelo.Estudiante;
import modelo.Facultad;
import modelo.Materia;
import util.GestorOperaciones;
import util.Menu;
import util.NavegadorReportes;
import util.ProcesadorLotes;
import util.SistemaAcademico;

/** Crea los componentes del sistema, carga datos de ejemplo e inicia el menú. */
public class Main {
    public static void main(String[] args) {
        SistemaAcademico sistema = new SistemaAcademico();
        Campus campus = crearCampusDeEjemplo();

        GestorOperaciones gestor = new GestorOperaciones();
        NavegadorReportes navegador = new NavegadorReportes();

        cargarDatosDeEjemplo(sistema);

        ProcesadorLotes procesador = new ProcesadorLotes(sistema);
        Menu menu = new Menu(
                sistema,
                campus,
                gestor,
                navegador,
                procesador
        );

        menu.iniciar();
    }

    /** Carga facultades, estudiantes, materias y aulas para probar el sistema. */
    private static void cargarDatosDeEjemplo(SistemaAcademico sistema) {
        sistema.agregarFacultad(
                new Facultad("F01", "Ingenieria")
        );
        sistema.agregarFacultad(
                new Facultad("F02", "Ciencias")
        );
        sistema.agregarFacultad(
                new Facultad("F03", "Artes")
        );
        sistema.agregarFacultad(
                new Facultad("F04", "Derecho")
        );
        sistema.agregarFacultad(
                new Facultad("F05", "Salud")
        );

        sistema.registrarEstudiante(
                "Ana Gomez", "E001",
                "ana.gomez@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Juan Perez", "E002",
                "juan.perez@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Maria Lopez", "E003",
                "maria.lopez@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Carlos Ruiz", "E004",
                "carlos.ruiz@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Laura Diaz", "E005",
                "laura.diaz@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Pablo Ortiz", "E006",
                "pablo.ortiz@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Sofia Mora", "E007",
                "sofia.mora@universidad.edu", 1
        );
        sistema.registrarEstudiante(
                "Diego Rios", "E008",
                "diego.rios@universidad.edu", 1
        );

        Materia calculo101 = sistema.crearMateria(
                "CALC101", "Calculo I", 3, 4
        );
        sistema.crearMateria(
                "CALC102", "Calculo II", 2, 4
        );
        sistema.crearMateria(
                "FIS101", "Fisica I", 2, 3
        );
        sistema.agregarPrerrequisito("CALC102", "CALC101");

        Estudiante ana = buscarEstudiante(sistema, "E001");
        ana.registrarNota(1, calculo101, 4.0);

        sistema.crearAula("101", 30);
        sistema.crearAula("102", 25);
        sistema.crearAula("Lab-1", 20);

        sistema.listarFacultades();
        System.out.println("Datos de ejemplo cargados.");
    }

    /** Crea el campus inicial con sus conexiones de ejemplo. */
    private static Campus crearCampusDeEjemplo() {
        String[] edificios = {
                "Ingenieria",
                "Biblioteca",
                "Cafeteria",
                "Rectoria",
                "Laboratorios"
        };

        Campus campus = new Campus(edificios);
        campus.agregarConexion("Ingenieria", "Biblioteca", 100);
        campus.agregarConexion("Ingenieria", "Cafeteria", 150);
        campus.agregarConexion("Ingenieria", "Rectoria", 400);
        campus.agregarConexion("Biblioteca", "Cafeteria", 120);
        campus.agregarConexion("Biblioteca", "Laboratorios", 90);
        campus.agregarConexion("Cafeteria", "Rectoria", 180);
        campus.agregarConexion("Laboratorios", "Rectoria", 350);

        return campus;
    }

    private static Estudiante buscarEstudiante(
            SistemaAcademico sistema,
            String id
    ) {
        try {
            return sistema.buscarEstudiante(id);
        } catch (excepciones.EstudianteNoEncontradoException excepcion) {
            throw new IllegalStateException(excepcion.getMessage());
        }
    }
}
