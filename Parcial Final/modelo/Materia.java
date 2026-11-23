package modelo;

import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import estructuras.Cola;
import estructuras.ListaEnlazada;

/** Materia con cupos, prerrequisitos, inscritos y estudiantes en espera. */
public class Materia {
    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int creditos;
    private ListaEnlazada<Materia> prerrequisitos;
    private ListaEnlazada<Estudiante> inscritos;
    private Cola<Estudiante> colaEspera;

    /** Crea una materia sin prerrequisitos, inscritos ni estudiantes en espera. */
    public Materia(String codigo, String nombre, int cuposMaximos, int creditos) {
        if (cuposMaximos < 0) {
            throw new IllegalArgumentException("Los cupos máximos no pueden ser negativos.");
        }
        if (creditos < 0) {
            throw new IllegalArgumentException("Los créditos no pueden ser negativos.");
        }

        this.codigo = codigo;
        this.nombre = nombre;
        this.cuposMaximos = cuposMaximos;
        this.creditos = creditos;
        this.prerrequisitos = new ListaEnlazada<>();
        this.inscritos = new ListaEnlazada<>();
        this.colaEspera = new Cola<>();
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

    /** Agrega un prerrequisito sin repetir código ni aceptar la misma materia. */
    public void agregarPrerrequisito(Materia materia) {
        if (materia == null) {
            throw new IllegalArgumentException("El prerrequisito no puede ser null.");
        }
        if (codigo.equals(materia.getCodigo())) {
            throw new IllegalArgumentException(
                    "Una materia no puede ser prerrequisito de sí misma."
            );
        }

        for (Materia prerrequisito : prerrequisitos) {
            if (prerrequisito.getCodigo().equals(materia.getCodigo())) {
                return;
            }
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

    /** Indica si el estudiante está en la cola de espera. */
    public boolean estaEnEspera(Estudiante estudiante) {
        for (Estudiante enEspera : colaEspera) {
            if (enEspera == estudiante) {
                return true;
            }
        }
        return false;
    }

    /** Quita al estudiante de la cola de espera si se encuentra allí. */
    public boolean quitarDeEspera(Estudiante estudiante) {
        return colaEspera.eliminar(estudiante);
    }

    /**
     * Inscribe al estudiante si hay cupo y no estaba inscrito.
     *
     * @throws CupoLlenoException si la materia ya no tiene cupos disponibles
     */
    public void agregarInscrito(Estudiante estudiante) throws CupoLlenoException {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null.");
        }
        if (estaInscrito(estudiante)) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito en esta materia."
            );
        }
        if (!tieneCupo()) {
            throw new CupoLlenoException(
                    "La materia " + codigo + " no tiene cupos disponibles."
            );
        }

        inscritos.agregarAlFinal(estudiante);
    }

    /** Encola al estudiante si no está inscrito ni está esperando ya. */
    public void encolarEnEspera(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null.");
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

    /** Saca al primero de la cola o convierte el error de Cola en excepción del dominio. */
    public Estudiante siguienteEnEspera() throws ColaDeEsperaVaciaException {
        try {
            return colaEspera.desencolar();
        } catch (IllegalStateException excepcion) {
            throw new ColaDeEsperaVaciaException(
                    "No hay estudiantes en espera para la materia " + codigo + "."
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
