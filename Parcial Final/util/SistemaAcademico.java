package util;

import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import estructuras.ListaEnlazada;
import modelo.Aula;
import modelo.Estudiante;
import modelo.Facultad;
import modelo.Materia;

import java.util.HashMap;
import java.util.TreeMap;

/** Coordina estudiantes, materias, aulas y facultades del sistema. */
public class SistemaAcademico {
    private HashMap<String, Estudiante> estudiantes;
    private HashMap<String, Materia> materias;
    private TreeMap<String, Aula> aulas;
    private Facultad[] facultades;
    private int cantidadFacultades;

    /** Crea el sistema con mapas vacíos y espacio para cinco facultades. */
    public SistemaAcademico() {
        estudiantes = new HashMap<>();
        materias = new HashMap<>();
        aulas = new TreeMap<>();
        facultades = new Facultad[5];
        cantidadFacultades = 0;
    }

    /** Crea y registra un estudiante si su ID no está registrado. */
    public Estudiante registrarEstudiante(
            String nombre,
            String id,
            String email,
            int semestre
    ) {
        if (estudiantes.containsKey(id)) {
            throw new IllegalArgumentException("Ya existe un estudiante con ID: " + id);
        }

        Estudiante estudiante = new Estudiante(nombre, id, email, semestre);
        estudiantes.put(id, estudiante);
        return estudiante;
    }

    /**
     * Busca un estudiante por ID.
     *
     * @throws EstudianteNoEncontradoException si el ID no existe
     */
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

    /** Imprime una línea por estudiante. El orden depende del HashMap. */
    public void listarEstudiantes() {
        for (Estudiante estudiante : estudiantes.values()) {
            System.out.println(
                    estudiante.getId() + " - "
                            + estudiante.getNombre() + " - semestre "
                            + estudiante.getSemestre()
            );
        }
    }

    /**
     * Elimina al estudiante y lo quita de las inscripciones y esperas.
     *
     * @throws EstudianteNoEncontradoException si el ID no existe
     */
    public Estudiante eliminarEstudiante(String id)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(id);
        estudiantes.remove(id);

        for (Materia materia : materias.values()) {
            materia.quitarInscrito(estudiante);
            materia.quitarDeEspera(estudiante);
        }

        return estudiante;
    }

    /**
     * Devuelve las materias en las que el estudiante está inscrito.
     *
     * @throws EstudianteNoEncontradoException si el ID no existe
     */
    public ListaEnlazada<Materia> materiasInscritas(String id)
            throws EstudianteNoEncontradoException {
        Estudiante estudiante = buscarEstudiante(id);
        ListaEnlazada<Materia> resultado = new ListaEnlazada<>();

        for (Materia materia : materias.values()) {
            if (materia.estaInscrito(estudiante)) {
                resultado.agregarAlFinal(materia);
            }
        }

        return resultado;
    }

    /** Crea una materia si el código no existe y cupos y créditos son positivos. */
    public Materia crearMateria(
            String codigo,
            String nombre,
            int cuposMaximos,
            int creditos
    ) {
        if (materias.containsKey(codigo)) {
            throw new IllegalArgumentException("Ya existe una materia con código: " + codigo);
        }
        if (cuposMaximos <= 0) {
            throw new IllegalArgumentException("Los cupos máximos deben ser mayores que cero.");
        }
        if (creditos <= 0) {
            throw new IllegalArgumentException("Los créditos deben ser mayores que cero.");
        }

        Materia materia = new Materia(codigo, nombre, cuposMaximos, creditos);
        materias.put(codigo, materia);
        return materia;
    }

    /** Busca una materia por código. */
    public Materia buscarMateria(String codigo) {
        Materia materia = materias.get(codigo);

        if (materia == null) {
            throw new IllegalArgumentException("No existe materia con código: " + codigo);
        }

        return materia;
    }

    /**
     * Agrega un prerrequisito y rechaza dependencias que formen un ciclo.
     *
     * Un ciclo aparecería si el prerrequisito ya depende, directa o
     * indirectamente, de la materia a la que se quiere agregar.
     */
    public void agregarPrerrequisito(
            String codigoMateria,
            String codigoPrerrequisito
    ) {
        Materia materia = buscarMateria(codigoMateria);
        Materia prerrequisito = buscarMateria(codigoPrerrequisito);

        if (dependeDe(prerrequisito, codigoMateria, new ListaEnlazada<String>())) {
            throw new IllegalArgumentException(
                    "El prerrequisito crearía una dependencia circular entre "
                            + codigoMateria + " y " + codigoPrerrequisito + "."
            );
        }

        materia.agregarPrerrequisito(prerrequisito);
    }

    /** Imprime los prerrequisitos de la materia o indica que no tiene. */
    public void mostrarPrerrequisitos(String codigoMateria) {
        Materia materia = buscarMateria(codigoMateria);

        if (materia.getPrerrequisitos().estaVacia()) {
            System.out.println(codigoMateria + " no tiene prerrequisitos.");
            return;
        }

        System.out.println("Prerrequisitos de " + codigoMateria + ":");
        for (Materia prerrequisito : materia.getPrerrequisitos()) {
            System.out.println("- " + prerrequisito.getCodigo()
                    + " (" + prerrequisito.getNombre() + ")");
        }
    }

    /**
     * Inscribe al estudiante o lo agrega al final de la espera si no hay cupo.
     *
     * @return true si queda inscrito; false si queda esperando
     */
    public boolean inscribir(String idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException {
        Estudiante estudiante = buscarEstudiante(idEstudiante);
        Materia materia = buscarMateria(codigoMateria);

        if (materia.estaInscrito(estudiante) || materia.estaEnEspera(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito o en espera para esta materia."
            );
        }

        StringBuilder faltantes = new StringBuilder();
        for (Materia prerrequisito : materia.getPrerrequisitos()) {
            if (!estudiante.haAprobado(prerrequisito.getCodigo())) {
                if (faltantes.length() > 0) {
                    faltantes.append(", ");
                }
                faltantes.append(prerrequisito.getCodigo());
            }
        }

        if (faltantes.length() > 0) {
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

    /**
     * Cancela una inscripción o una espera. Si libera un cupo, lo asigna al
     * primer estudiante en espera.
     *
     * @return el estudiante que pasó de la espera a estar inscrito, o null
     * @throws EstudianteNoEncontradoException si el ID no existe
     */
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
                    // La cola se comprobó antes de retirar; este caso no debería ocurrir.
                    throw new IllegalStateException(
                            "La cola de espera cambió inesperadamente.",
                            excepcion
                    );
                } catch (CupoLlenoException excepcion) {
                    // Se acaba de liberar un cupo, por lo que no debería estar llena.
                    throw new IllegalStateException(
                            "No se pudo asignar el cupo recién liberado.",
                            excepcion
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

    /** Imprime la cola en orden de atención. */
    public void mostrarColaEspera(String codigoMateria) {
        Materia materia = buscarMateria(codigoMateria);

        if (materia.cantidadEnEspera() == 0) {
            System.out.println("La cola de espera está vacía.");
            return;
        }

        int posicion = 1;
        for (Estudiante estudiante : materia.getColaEspera()) {
            System.out.println("Posicion " + posicion + ": " + estudiante.getNombre());
            posicion++;
        }
        System.out.println("Total en espera: " + materia.cantidadEnEspera());
    }

    /** Crea un aula si no existe otra con el mismo nombre. */
    public Aula crearAula(String nombre, int capacidad) {
        if (aulas.containsKey(nombre)) {
            throw new IllegalArgumentException("Ya existe un aula con nombre: " + nombre);
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }

        Aula aula = new Aula(nombre, capacidad);
        aulas.put(nombre, aula);
        return aula;
    }

    /** Busca un aula por nombre. */
    public Aula buscarAula(String nombre) {
        Aula aula = aulas.get(nombre);

        if (aula == null) {
            throw new IllegalArgumentException("No existe aula con nombre: " + nombre);
        }

        return aula;
    }

    /** Imprime las aulas en el orden de sus nombres en el TreeMap. */
    public void listarAulas() {
        for (Aula aula : aulas.values()) {
            System.out.println(aula);
        }
    }

    /** Verifica la materia y delega la reserva en el aula correspondiente. */
    public void reservarHorario(
            String nombreAula,
            int dia,
            int hora,
            int duracion,
            String codigoMateria
    ) throws HorarioConflictivoException {
        buscarMateria(codigoMateria);
        buscarAula(nombreAula).reservar(dia, hora, duracion, codigoMateria);
    }

    /** Delega la liberación del horario en el aula correspondiente. */
    public void liberarHorario(
            String nombreAula,
            int dia,
            int hora,
            int duracion
    ) {
        buscarAula(nombreAula).liberar(dia, hora, duracion);
    }

    /** Delega la consulta de disponibilidad en el aula correspondiente. */
    public boolean consultarDisponibilidad(String nombreAula, int dia, int hora) {
        return buscarAula(nombreAula).consultarDisponibilidad(dia, hora);
    }

    /** Agrega una facultad al arreglo fijo de cinco posiciones. */
    public void agregarFacultad(Facultad facultad) {
        if (cantidadFacultades == facultades.length) {
            throw new IllegalStateException(
                    "No se pueden agregar más de cinco facultades."
            );
        }
        if (facultad == null) {
            throw new IllegalArgumentException("La facultad no puede ser null.");
        }

        facultades[cantidadFacultades] = facultad;
        cantidadFacultades++;
    }

    /** Imprime las facultades almacenadas. */
    public void listarFacultades() {
        for (int indice = 0; indice < cantidadFacultades; indice++) {
            System.out.println(facultades[indice]);
        }
    }

    private boolean dependeDe(
            Materia actual,
            String codigoBuscado,
            ListaEnlazada<String> visitados
    ) {
        if (actual.getCodigo().equals(codigoBuscado)) {
            return true;
        }
        if (visitados.contiene(actual.getCodigo())) {
            return false;
        }

        visitados.agregarAlFinal(actual.getCodigo());

        for (Materia prerrequisito : actual.getPrerrequisitos()) {
            if (dependeDe(prerrequisito, codigoBuscado, visitados)) {
                return true;
            }
        }

        return false;
    }
}
