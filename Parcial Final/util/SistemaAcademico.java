package util;

import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import estructuras.ListaEnlazada;
import estructuras.Pila;
import modelo.Aula;
import modelo.Estudiante;
import modelo.Facultad;
import modelo.Materia;

import java.util.HashMap;
import java.util.TreeMap;

/** Coordina estudiantes, materias, aulas y facultades. */
public class SistemaAcademico {
    private HashMap<String, Estudiante> estudiantes;
    private HashMap<String, Materia> materias;
    private TreeMap<String, Aula> aulas;
    private Facultad[] facultades;
    private int cantidadFacultades;

    public SistemaAcademico() {
        estudiantes = new HashMap<>();
        materias = new HashMap<>();
        aulas = new TreeMap<>();
        facultades = new Facultad[5];
        cantidadFacultades = 0;
    }

    public Estudiante registrarEstudiante(
            String nombre,
            String id,
            String email,
            int semestre
    ) {
        if (estudiantes.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con ID: " + id
            );
        }

        Estudiante estudiante = new Estudiante(nombre, id, email, semestre);
        estudiantes.put(id, estudiante);
        return estudiante;
    }

    public Estudiante buscarEstudiante(String id)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = estudiantes.get(id);

        if (estudiante == null) {
            throw new EstudianteNoEncontradoException(
                    "No existe estudiante con ID: " + id
            );
        }

        return estudiante;
    }

    /** Imprime estudiantes; el orden depende del HashMap. */
    public void listarEstudiantes() {
        Object[] valores = estudiantes.values().toArray();
        int indice = 0;

        while (indice < valores.length) {
            Estudiante estudiante = (Estudiante) valores[indice];
            System.out.println(
                    estudiante.getId() + " - "
                            + estudiante.getNombre() + " - semestre "
                            + estudiante.getSemestre()
            );
            indice++;
        }
    }

    /** Elimina al estudiante de mapas, inscripciones y colas de espera. */
    public Estudiante eliminarEstudiante(String id)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(id);
        estudiantes.remove(id);

        Object[] valoresMaterias = materias.values().toArray();
        int indice = 0;

        while (indice < valoresMaterias.length) {
            Materia materia = (Materia) valoresMaterias[indice];
            materia.quitarInscrito(estudiante);
            materia.quitarDeEspera(estudiante);
            indice++;
        }

        return estudiante;
    }

    public ListaEnlazada<Materia> materiasInscritas(String id)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(id);
        ListaEnlazada<Materia> resultado = new ListaEnlazada<>();
        Object[] valoresMaterias = materias.values().toArray();

        int indice = 0;
        while (indice < valoresMaterias.length) {
            Materia materia = (Materia) valoresMaterias[indice];

            if (materia.estaInscrito(estudiante)) {
                resultado.agregarAlFinal(materia);
            }
            indice++;
        }

        return resultado;
    }

    /** Devuelve las materias en las que el estudiante está esperando. */
    public ListaEnlazada<Materia> materiasEnEspera(String id)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(id);
        ListaEnlazada<Materia> resultado = new ListaEnlazada<>();
        Object[] valoresMaterias = materias.values().toArray();

        int indice = 0;
        while (indice < valoresMaterias.length) {
            Materia materia = (Materia) valoresMaterias[indice];

            if (materia.estaEnEspera(estudiante)) {
                resultado.agregarAlFinal(materia);
            }
            indice++;
        }

        return resultado;
    }

    /** Vuelve a registrar el mismo objeto de estudiante en el mapa. */
    public void restaurarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException(
                    "El estudiante no puede ser null."
            );
        }
        if (estudiantes.containsKey(estudiante.getId())) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con ID: " + estudiante.getId()
            );
        }

        estudiantes.put(estudiante.getId(), estudiante);
    }

    public Materia crearMateria(
            String codigo,
            String nombre,
            int cuposMaximos,
            int creditos
    ) {
        if (materias.containsKey(codigo)) {
            throw new IllegalArgumentException(
                    "Ya existe una materia con código: " + codigo
            );
        }
        if (cuposMaximos <= 0) {
            throw new IllegalArgumentException(
                    "Los cupos máximos deben ser mayores que cero."
            );
        }
        if (creditos <= 0) {
            throw new IllegalArgumentException(
                    "Los créditos deben ser mayores que cero."
            );
        }

        Materia materia =
                new Materia(codigo, nombre, cuposMaximos, creditos);
        materias.put(codigo, materia);
        return materia;
    }

    public Materia buscarMateria(String codigo) {
        Materia materia = materias.get(codigo);

        if (materia == null) {
            throw new IllegalArgumentException(
                    "No existe materia con código: " + codigo
            );
        }

        return materia;
    }

    public void agregarPrerrequisito(
            String codigoMateria,
            String codigoPrerrequisito
    ) {
        Materia materia = buscarMateria(codigoMateria);
        Materia prerrequisito = buscarMateria(codigoPrerrequisito);

        if (dependeDe(prerrequisito, codigoMateria)) {
            throw new IllegalArgumentException(
                    "El prerrequisito crearía una dependencia circular entre "
                            + codigoMateria + " y " + codigoPrerrequisito + "."
            );
        }

        materia.agregarPrerrequisito(prerrequisito);
    }

    public void mostrarPrerrequisitos(String codigoMateria) {
        Materia materia = buscarMateria(codigoMateria);
        ListaEnlazada<Materia> prerrequisitos =
                materia.getPrerrequisitos();

        if (prerrequisitos.estaVacia()) {
            System.out.println(codigoMateria + " no tiene prerrequisitos.");
            return;
        }

        System.out.println("Prerrequisitos de " + codigoMateria + ":");
        int indice = 0;

        while (indice < prerrequisitos.tamano()) {
            Materia prerrequisito = prerrequisitos.obtener(indice);
            System.out.println(
                    "- " + prerrequisito.getCodigo()
                            + " (" + prerrequisito.getNombre() + ")"
            );
            indice++;
        }
    }

    public boolean inscribir(String idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        Materia materia = buscarMateria(codigoMateria);

        if (materia.estaInscrito(estudiante)
                || materia.estaEnEspera(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito o en espera para esta materia."
            );
        }

        String faltantes = "";
        ListaEnlazada<Materia> prerrequisitos =
                materia.getPrerrequisitos();

        int indice = 0;
        while (indice < prerrequisitos.tamano()) {
            Materia prerrequisito = prerrequisitos.obtener(indice);

            if (!estudiante.haAprobado(prerrequisito.getCodigo())) {
                if (!faltantes.isEmpty()) {
                    faltantes += ", ";
                }
                faltantes += prerrequisito.getCodigo();
            }
            indice++;
        }

        if (!faltantes.isEmpty()) {
            throw new PreRequisitoNoAprobadoException(
                    "Faltan prerrequisitos aprobados: " + faltantes
            );
        }

        try {
            materia.agregarInscrito(estudiante);
            return true;
        } catch (CupoLlenoException excepcion) {
            materia.encolarEnEspera(estudiante);
            return false;
        }
    }

    public Estudiante cancelarInscripcion(
            String idEstudiante,
            String codigoMateria
    ) throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        Materia materia = buscarMateria(codigoMateria);

        if (materia.estaInscrito(estudiante)) {
            materia.quitarInscrito(estudiante);

            if (!materia.getColaEspera().estaVacia()) {
                try {
                    Estudiante siguiente = materia.siguienteEnEspera();
                    materia.agregarInscrito(siguiente);
                    return siguiente;
                } catch (ColaDeEsperaVaciaException excepcion) {
                    throw new IllegalStateException(
                            "La cola quedó vacía antes de asignar el cupo."
                    );
                } catch (CupoLlenoException excepcion) {
                    throw new IllegalStateException(
                            "No se pudo asignar el cupo recién liberado."
                    );
                }
            }

            return null;
        }

        if (materia.quitarDeEspera(estudiante)) {
            return null;
        }

        throw new IllegalArgumentException(
                "El estudiante no está inscrito ni en espera para esta materia."
        );
    }

    public void mostrarColaEspera(String codigoMateria) {
        Materia materia = buscarMateria(codigoMateria);

        if (materia.cantidadEnEspera() == 0) {
            System.out.println("La cola de espera está vacía.");
            return;
        }

        int indice = 0;
        while (indice < materia.cantidadEnEspera()) {
            Estudiante estudiante =
                    materia.getColaEspera().obtener(indice);
            System.out.println(
                    "Posicion " + (indice + 1) + ": "
                            + estudiante.getNombre()
            );
            indice++;
        }

        System.out.println(
                "Total en espera: " + materia.cantidadEnEspera()
        );
    }

    public Aula crearAula(String nombre, int capacidad) {
        if (aulas.containsKey(nombre)) {
            throw new IllegalArgumentException(
                    "Ya existe un aula con nombre: " + nombre
            );
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero."
            );
        }

        Aula aula = new Aula(nombre, capacidad);
        aulas.put(nombre, aula);
        return aula;
    }

    public Aula buscarAula(String nombre) {
        Aula aula = aulas.get(nombre);

        if (aula == null) {
            throw new IllegalArgumentException(
                    "No existe aula con nombre: " + nombre
            );
        }

        return aula;
    }

    public void listarAulas() {
        Object[] valores = aulas.values().toArray();
        int indice = 0;

        while (indice < valores.length) {
            Aula aula = (Aula) valores[indice];
            System.out.println(aula);
            indice++;
        }
    }

    public void reservarHorario(
            String nombreAula,
            int dia,
            int hora,
            int duracion,
            String codigoMateria
    ) throws HorarioConflictivoException {
        buscarMateria(codigoMateria);
        Aula aula = buscarAula(nombreAula);
        aula.reservar(dia, hora, duracion, codigoMateria);
    }

    public void liberarHorario(
            String nombreAula,
            int dia,
            int hora,
            int duracion
    ) {
        Aula aula = buscarAula(nombreAula);
        aula.liberar(dia, hora, duracion);
    }

    public boolean consultarDisponibilidad(
            String nombreAula,
            int dia,
            int hora
    ) {
        Aula aula = buscarAula(nombreAula);
        return aula.consultarDisponibilidad(dia, hora);
    }

    public void agregarFacultad(Facultad facultad) {
        if (cantidadFacultades == facultades.length) {
            throw new IllegalStateException(
                    "No se pueden agregar más de cinco facultades."
            );
        }
        if (facultad == null) {
            throw new IllegalArgumentException(
                    "La facultad no puede ser null."
            );
        }

        facultades[cantidadFacultades] = facultad;
        cantidadFacultades++;
    }

    public void listarFacultades() {
        int indice = 0;
        while (indice < cantidadFacultades) {
            System.out.println(facultades[indice]);
            indice++;
        }
    }

    private boolean dependeDe(Materia inicio, String codigoBuscado) {
        Pila<Materia> pendientes = new Pila<>();
        ListaEnlazada<String> revisados = new ListaEnlazada<>();
        pendientes.push(inicio);

        while (!pendientes.estaVacia()) {
            Materia actual = pendientes.pop();

            if (actual.getCodigo().equals(codigoBuscado)) {
                return true;
            }

            if (!revisados.contiene(actual.getCodigo())) {
                revisados.agregarAlFinal(actual.getCodigo());
                ListaEnlazada<Materia> prerrequisitos =
                        actual.getPrerrequisitos();

                int indice = 0;
                while (indice < prerrequisitos.tamano()) {
                    pendientes.push(prerrequisitos.obtener(indice));
                    indice++;
                }
            }
        }

        return false;
    }
}
