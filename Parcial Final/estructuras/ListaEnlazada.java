package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Lista enlazada simple que conserva referencias a la cabeza y a la cola. */
public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;

    /** Crea una lista vacía. */
    public ListaEnlazada() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    /** Agrega un dato después del último nodo. */
    public void agregarAlFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }

        tamano++;
    }

    /** Agrega un dato antes de la cabeza actual. */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;

        if (cola == null) {
            cola = nuevo;
        }

        tamano++;
    }

    /**
     * Elimina la primera coincidencia del dato.
     *
     * @return true si encontró y eliminó el dato; false si no estaba en la lista
     */
    public boolean eliminar(T dato) {
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;

        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                if (anterior == null) {
                    cabeza = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }

                if (actual == cola) {
                    cola = anterior;
                }

                tamano--;
                return true;
            }

            anterior = actual;
            actual = actual.getSiguiente();
        }

        return false;
    }

    /** Indica si la lista contiene un dato igual al recibido. */
    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;

        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    /**
     * Devuelve el dato ubicado en el índice indicado, empezando desde cero.
     *
     * @throws IndexOutOfBoundsException si el índice no pertenece a la lista
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de los límites de la lista: " + indice
            );
        }

        Nodo<T> actual = cabeza;
        for (int posicion = 0; posicion < indice; posicion++) {
            actual = actual.getSiguiente();
        }

        return actual.getDato();
    }

    /** Devuelve la cantidad de elementos almacenados. */
    public int tamano() {
        return tamano;
    }

    /** Indica si la lista no tiene elementos. */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /** Desconecta todos los nodos y deja la lista vacía. */
    public void vaciar() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    /** Devuelve un iterador que recorre la lista desde la cabeza hasta la cola. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException(
                            "No hay más elementos en la lista"
                    );
                }

                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    /** Devuelve los elementos en orden, entre corchetes y separados por comas. */
    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder("[");
        Nodo<T> actual = cabeza;

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
