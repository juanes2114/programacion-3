package util;

import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import estructuras.Pila;

/** Ejecuta y administra el deshacer y rehacer de operaciones. */
public class GestorOperaciones {
    private Pila<Operacion> pilaDeshacer;
    private Pila<Operacion> pilaRehacer;

    public GestorOperaciones() {
        pilaDeshacer = new Pila<>();
        pilaRehacer = new Pila<>();
    }

    /**
     * Ejecuta una operación; solo si termina bien la guarda para deshacer.
     */
    public void ejecutar(Operacion operacion)
            throws EstudianteNoEncontradoException,
            PreRequisitoNoAprobadoException,
            HorarioConflictivoException {
        if (operacion == null) {
            throw new IllegalArgumentException(
                    "La operación no puede ser null."
            );
        }

        operacion.ejecutar();
        pilaDeshacer.push(operacion);
        pilaRehacer.vaciar();
    }

    /** Deshace la operación más reciente y devuelve su descripción. */
    public String deshacer() throws PilaDeshacerVaciaException {
        if (pilaDeshacer.estaVacia()) {
            throw new PilaDeshacerVaciaException(
                    "No hay operaciones para deshacer"
            );
        }

        Operacion operacion = pilaDeshacer.peek();
        operacion.deshacer();
        pilaDeshacer.pop();
        pilaRehacer.push(operacion);

        return operacion.getDescripcion();
    }

    /** Rehace la operación más reciente y devuelve su descripción. */
    public String rehacer() throws PilaDeshacerVaciaException {
        if (pilaRehacer.estaVacia()) {
            throw new PilaDeshacerVaciaException(
                    "No hay operaciones para rehacer"
            );
        }

        Operacion operacion = pilaRehacer.peek();
        operacion.rehacer();
        pilaRehacer.pop();
        pilaDeshacer.push(operacion);

        return operacion.getDescripcion();
    }

    public int cantidadDeshacer() {
        return pilaDeshacer.tamano();
    }

    public int cantidadRehacer() {
        return pilaRehacer.tamano();
    }

    public boolean hayParaDeshacer() {
        return !pilaDeshacer.estaVacia();
    }

    public boolean hayParaRehacer() {
        return !pilaRehacer.estaVacia();
    }

    /** Imprime las pilas desde la operación más reciente hacia atrás. */
    public void mostrarPilas() {
        System.out.println("Pila para deshacer:");

        int indice = 0;
        while (indice < pilaDeshacer.tamano()) {
            System.out.println(
                    "- " + pilaDeshacer.obtener(indice).getDescripcion()
            );
            indice++;
        }

        System.out.println("Pila para rehacer:");
        indice = 0;

        while (indice < pilaRehacer.tamano()) {
            System.out.println(
                    "- " + pilaRehacer.obtener(indice).getDescripcion()
            );
            indice++;
        }
    }
}
