package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Cola genérica basada en nodos, con referencias al frente y al final. */
public class Cola<T> implements Iterable<T> {
    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int tamano;

    /** Crea una cola vacía. */
    public Cola() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    /** Agrega un dato al final de la cola. */
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            frente = nuevo;
            finalCola = nuevo;
        } else {
            finalCola.setSiguiente(nuevo);
            finalCola = nuevo;
        }

        tamano++;
    }

    /**
     * Quita y devuelve el dato que lleva más tiempo en la cola.
     *
     * @throws IllegalStateException si la cola está vacía
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException(
                    "No se puede desencolar: la cola está vacía"
            );
        }

        T dato = frente.getDato();
        frente = frente.getSiguiente();
        tamano--;

        if (frente == null) {
            finalCola = null;
        }

        return dato;
    }

    /**
     * Devuelve el dato del frente sin quitarlo.
     *
     * @throws IllegalStateException si la cola está vacía
     */
    public T verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException(
                    "No se puede ver el frente: la cola está vacía"
            );
        }

        return frente.getDato();
    }

    /** Elimina la primera coincidencia del dato y devuelve si la encontró. */
    public boolean eliminar(T dato) {
        Nodo<T> anterior = null;
        Nodo<T> actual = frente;

        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                if (anterior == null) {
                    frente = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }

                if (actual == finalCola) {
                    finalCola = anterior;
                }

                tamano--;

                if (frente == null) {
                    finalCola = null;
                }

                return true;
            }

            anterior = actual;
            actual = actual.getSiguiente();
        }

        return false;
    }

    /** Indica si la cola no tiene elementos. */
    public boolean estaVacia() {
        return frente == null;
    }

    /** Devuelve la cantidad de elementos almacenados. */
    public int tamano() {
        return tamano;
    }

    /** Desconecta todos los nodos y deja la cola vacía. */
    public void vaciar() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    /** Devuelve un iterador que recorre desde el frente hasta el final. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = frente;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(
                            "No hay más elementos en la cola"
                    );
                }

                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    /** Devuelve los elementos en orden desde el frente hasta el final. */
    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("[");
        Nodo<T> actual = frente;

        while (actual != null) {
            resultado.append(actual.getDato());
            actual = actual.getSiguiente();

            if (actual != null) {
                resultado.append(", ");
            }
        }

        return resultado.append("]").toString();
    }

    private boolean sonIguales(T primero, T segundo) {
        if (primero == null) {
            return segundo == null;
        }
        return primero.equals(segundo);
    }
}
