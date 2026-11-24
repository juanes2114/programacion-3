package modelo;

import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import estructuras.Cola;
import estructuras.ListaEnlazada;

/** Materia con prerrequisitos, inscritos y cola de espera. */
public class Materia {
    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int creditos;
    private ListaEnlazada<Materia> prerrequisitos;
    private ListaEnlazada<Estudiante> inscritos;
    private Cola<Estudiante> colaEspera;

    public Materia(String codigo, String nombre, int cuposMaximos, int creditos) {
        if (cuposMaximos < 0) {
            throw new IllegalArgumentException(
                    "Los cupos máximos no pueden ser negativos."
            );
        }
        if (creditos < 0) {
            throw new IllegalArgumentException(
                    "Los créditos no pueden ser negativos."
            );
        }

        this.codigo = codigo;
        this.nombre = nombre;
        this.cuposMaximos = cuposMaximos;
        this.creditos = creditos;
        prerrequisitos = new ListaEnlazada<>();
        inscritos = new ListaEnlazada<>();
        colaEspera = new Cola<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    public int getCreditos() {
        return creditos;
    }

    /** Agrega un prerrequisito sin duplicarlo ni aceptar la misma materia. */
    public void agregarPrerrequisito(Materia materia) {
        if (materia == null) {
            throw new IllegalArgumentException(
                    "El prerrequisito no puede ser null."
            );
        }
        if (codigo.equals(materia.getCodigo())) {
            throw new IllegalArgumentException(
                    "Una materia no puede ser prerrequisito de sí misma."
            );
        }

        int indice = 0;
        while (indice < prerrequisitos.tamano()) {
            if (prerrequisitos.obtener(indice).getCodigo()
                    .equals(materia.getCodigo())) {
                return;
            }
            indice++;
        }

        prerrequisitos.agregarAlFinal(materia);
    }

    public ListaEnlazada<Materia> getPrerrequisitos() {
        return prerrequisitos;
    }

    public boolean tieneCupo() {
        return inscritos.tamano() < cuposMaximos;
    }

    public int cuposDisponibles() {
        return cuposMaximos - inscritos.tamano();
    }

    public boolean estaInscrito(Estudiante estudiante) {
        return inscritos.contiene(estudiante);
    }

    public ListaEnlazada<Estudiante> getInscritos() {
        return inscritos;
    }

    public boolean quitarInscrito(Estudiante estudiante) {
        return inscritos.eliminar(estudiante);
    }

    /** Indica si el estudiante aparece en la cola de espera. */
    public boolean estaEnEspera(Estudiante estudiante) {
        return posicionEnEspera(estudiante) != -1;
    }

    /**
     * Devuelve la posición del estudiante en la espera, comenzando en cero.
     * Devuelve -1 si no está esperando.
     */
    public int posicionEnEspera(Estudiante estudiante) {
        int indice = 0;

        while (indice < colaEspera.tamano()) {
            if (colaEspera.obtener(indice) == estudiante) {
                return indice;
            }
            indice++;
        }

        return -1;
    }

    /** Quita al estudiante de la espera si se encuentra allí. */
    public boolean quitarDeEspera(Estudiante estudiante) {
        return colaEspera.eliminar(estudiante);
    }

    /**
     * Inserta un estudiante en la posición indicada de la espera.
     *
     * @throws IllegalArgumentException si la posición no es válida, el
     * estudiante es null, está inscrito o ya está esperando
     */
    public void insertarEnEspera(Estudiante estudiante, int posicion) {
        if (estudiante == null) {
            throw new IllegalArgumentException(
                    "El estudiante no puede ser null."
            );
        }
        if (estaInscrito(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito en esta materia."
            );
        }
        if (estaEnEspera(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está en la cola de espera."
            );
        }

        int cantidad = colaEspera.tamano();
        if (posicion < 0 || posicion > cantidad) {
            throw new IllegalArgumentException(
                    "La posición debe estar entre 0 y " + cantidad + "."
            );
        }

        Estudiante[] estudiantes = new Estudiante[cantidad + 1];
        int indiceViejo = 0;
        int indiceNuevo = 0;

        while (indiceNuevo < estudiantes.length) {
            if (indiceNuevo == posicion) {
                estudiantes[indiceNuevo] = estudiante;
            } else {
                estudiantes[indiceNuevo] =
                        colaEspera.obtener(indiceViejo);
                indiceViejo++;
            }
            indiceNuevo++;
        }

        colaEspera.vaciar();

        indiceNuevo = 0;
        while (indiceNuevo < estudiantes.length) {
            colaEspera.encolar(estudiantes[indiceNuevo]);
            indiceNuevo++;
        }
    }

    /** Inscribe al estudiante si hay cupo. */
    public void agregarInscrito(Estudiante estudiante)
            throws CupoLlenoException {
        if (estudiante == null) {
            throw new IllegalArgumentException(
                    "El estudiante no puede ser null."
            );
        }
        if (estaInscrito(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito en esta materia."
            );
        }
        if (!tieneCupo()) {
            throw new CupoLlenoException(
                    "La materia " + codigo
                            + " no tiene cupos disponibles."
            );
        }

        inscritos.agregarAlFinal(estudiante);
    }

    /** Encola al estudiante si no está inscrito ni está esperando. */
    public void encolarEnEspera(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException(
                    "El estudiante no puede ser null."
            );
        }
        if (estaInscrito(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito en esta materia."
            );
        }
        if (estaEnEspera(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está en la cola de espera."
            );
        }

        colaEspera.encolar(estudiante);
    }

    /** Saca al primer estudiante y traduce el error de la cola. */
    public Estudiante siguienteEnEspera()
            throws ColaDeEsperaVaciaException {
        try {
            return colaEspera.desencolar();
        } catch (IllegalStateException excepcion) {
            throw new ColaDeEsperaVaciaException(
                    "No hay estudiantes en espera para la materia "
                            + codigo + "."
            );
        }
    }

    public Cola<Estudiante> getColaEspera() {
        return colaEspera;
    }

    public int cantidadEnEspera() {
        return colaEspera.tamano();
    }

    @Override
    public String toString() {
        return "Materia{codigo='" + codigo
                + "', nombre='" + nombre
                + "', creditos=" + creditos
                + ", cupos=" + cuposDisponibles() + "/" + cuposMaximos
                + "}";
    }
}
