package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Pila genérica basada en nodos; el primero es el tope de la pila. */
public class Pila<T> implements Iterable<T> {
    private Nodo<T> tope;
    private int tamano;

    /** Crea una pila vacía. */
    public Pila() {
        tope = null;
        tamano = 0;
    }

    /** Inserta un dato en el tope de la pila. */
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
            throw new IllegalStateException("No se puede hacer pop: la pila está vacía");
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
            throw new IllegalStateException("No se puede hacer peek: la pila está vacía");
        }

        return tope.getDato();
    }

    /** Indica si la pila no tiene elementos. */
    public boolean estaVacia() {
        return tope == null;
    }

    /** Devuelve la cantidad de elementos almacenados. */
    public int tamano() {
        return tamano;
    }

    /** Desconecta todos los nodos y deja la pila vacía. */
    public void vaciar() {
        tope = null;
        tamano = 0;
    }

    /** Devuelve un iterador que recorre desde el tope hasta el fondo. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = tope;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(
                            "No hay más elementos en la pila"
                    );
                }

                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    /** Devuelve los elementos desde el tope hasta el fondo. */
    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("[");
        Nodo<T> actual = tope;

        while (actual != null) {
            resultado.append(actual.getDato());
            actual = actual.getSiguiente();

            if (actual != null) {
                resultado.append(", ");
            }
        }

        return resultado.append("]").toString();
    }
}
