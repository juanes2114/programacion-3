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
import util.SistemaAcademico;

/** Ejecuta pruebas de las estructuras y clases desarrolladas en los pasos 2 a 6. */
public class Main {
    public static void main(String[] args) {
        probarEstructuras();
        probarMateria();
        probarAula();
        probarSistemaAcademico();
        probarCampus();
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

        Materia materia = new Materia("MAT101", "Matemáticas", 2, 3);
        Estudiante ana = new Estudiante("Ana", "E001", "ana@uni.edu", 1);
        Estudiante luis = new Estudiante("Luis", "E002", "luis@uni.edu", 1);
        Estudiante marta = new Estudiante("Marta", "E003", "marta@uni.edu", 1);

        try {
            materia.agregarInscrito(ana);
            materia.agregarInscrito(luis);

            try {
                materia.agregarInscrito(marta);
            } catch (CupoLlenoException excepcion) {
                System.out.println("Cupo lleno: " + excepcion.getMessage());
                materia.encolarEnEspera(marta);
            }

            System.out.println("Espera: " + materia.getColaEspera());
            materia.quitarInscrito(ana);
            Estudiante siguiente = materia.siguienteEnEspera();
            materia.agregarInscrito(siguiente);
            System.out.println("Pasa de espera a inscrita: " + siguiente.getNombre());

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
            System.out.println(excepcion.getMessage());
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
            sistema.inscribir("E001", "CALC102");
        } catch (PreRequisitoNoAprobadoException excepcion) {
            System.out.println("Prerrequisito pendiente: " + excepcion.getMessage());
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        try {
            Estudiante ana = sistema.buscarEstudiante("E001");
            ana.registrarNota(1, sistema.buscarMateria("CALC101"), 4.0);
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
        } catch (EstudianteNoEncontradoException excepcion) {
            System.out.println(excepcion.getMessage());
        }

        System.out.println("Aulas ordenadas:");
        sistema.crearAula("Lab-1", 25);
        sistema.crearAula("101", 30);
        sistema.crearAula("A-2", 20);
        sistema.listarAulas();
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

    private static void probarCampus() {
        System.out.println();
        System.out.println("=== Paso 6: Campus y Dijkstra ===");

        String[] nombres = {
                "Ingenieria",
                "Biblioteca",
                "Cafeteria",
                "Rectoria",
                "Laboratorios",
                "Gimnasio"
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
        System.out.println("Matriz simétrica: " + campus.esMatrizSimetrica());

        Ruta ruta = campus.calcularRutaMasCorta("Ingenieria", "Rectoria");
        campus.mostrarRuta(ruta);

        campus.mostrarRuta(
                campus.calcularRutaMasCorta("Ingenieria", "Laboratorios")
        );
        campus.mostrarRuta(
                campus.calcularRutaMasCorta("Rectoria", "Ingenieria")
        );
        campus.mostrarRuta(
                campus.calcularRutaMasCorta("Biblioteca", "Biblioteca")
        );

        try {
            campus.calcularRutaMasCorta("Ingenieria", "Gimnasio");
        } catch (IllegalStateException excepcion) {
            System.out.println("Sin ruta: " + excepcion.getMessage());
        }

        campus.agregarConexion("Gimnasio", "Cafeteria", 60);
        campus.mostrarRuta(
                campus.calcularRutaMasCorta("Ingenieria", "Gimnasio")
        );

        campus.agregarConexion("Ingenieria", "Rectoria", 200);
        campus.mostrarRuta(
                campus.calcularRutaMasCorta("Ingenieria", "Rectoria")
        );

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
            System.out.println("Edificio inválido: " + excepcion.getMessage());
        }
    }

    private static void probarConexionConsigoMismo(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Ingenieria", 50);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Autoconexión inválida: " + excepcion.getMessage());
        }
    }

    private static void probarDistanciaCero(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Biblioteca", 0);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Distancia inválida: " + excepcion.getMessage());
        }
    }

    private static void probarDistanciaNegativa(Campus campus) {
        try {
            campus.agregarConexion("Ingenieria", "Biblioteca", -10);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Distancia inválida: " + excepcion.getMessage());
        }
    }

    private static void probarCampusPequeno() {
        try {
            String[] pocosEdificios = {"Uno", "Dos", "Tres", "Cuatro"};
            new Campus(pocosEdificios);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Campus inválido: " + excepcion.getMessage());
        }
    }

    private static void probarNombreRepetido() {
        try {
            String[] nombresRepetidos = {
                    "Uno", "Dos", "dos", "Cuatro", "Cinco"
            };
            new Campus(nombresRepetidos);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("Nombres inválidos: " + excepcion.getMessage());
        }
    }
}
