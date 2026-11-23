package estructuras;

/** Pila genérica enlazada. El nodo superior representa el tope. */
public class Pila<T> {
    private Nodo<T> tope;
    private int tamano;

    /** Crea una pila vacía. */
    public Pila() {
        tope = null;
        tamano = 0;
    }

    /** Inserta un dato en el tope. */
    public void push(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(tope);
        tope = nuevo;
        tamano++;
    }

    /**
     * Quita y devuelve el dato del tope.
     *
     * @throws IllegalStateException si la pila está vacía
     */
    public T pop() {
        if (estaVacia()) {
            throw new IllegalStateException(
                    "No se puede hacer pop: la pila está vacía"
            );
        }

        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamano--;
        return dato;
    }

    /**
     * Devuelve el dato del tope sin quitarlo.
     *
     * @throws IllegalStateException si la pila está vacía
     */
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException(
                    "No se puede hacer peek: la pila está vacía"
            );
        }

        return tope.getDato();
    }

    /** Devuelve el elemento en el índice indicado, contando desde el tope. */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de los límites de la pila: " + indice
            );
        }

        Nodo<T> actual = tope;
        int posicion = 0;

        while (posicion < indice) {
            actual = actual.getSiguiente();
            posicion++;
        }

        return actual.getDato();
    }

    /** Indica si la pila está vacía. */
    public boolean estaVacia() {
        return tope == null;
    }

    /** Devuelve el número de elementos. */
    public int tamano() {
        return tamano;
    }

    /** Desconecta todos los nodos y deja la pila vacía. */
    public void vaciar() {
        tope = null;
        tamano = 0;
    }

    /** Devuelve el contenido desde el tope hasta el fondo. */
    @Override
    public String toString() {
        String resultado = "[";
        Nodo<T> actual = tope;

        while (actual != null) {
            resultado += actual.getDato();
            actual = actual.getSiguiente();

            if (actual != null) {
                resultado += ", ";
            }
        }

        return resultado + "]";
    }
}
