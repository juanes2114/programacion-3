package estructuras;

/** Lista enlazada simple con referencias a la cabeza y a la cola. */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;

    /** Crea una lista vacía. */
    public ListaEnlazada() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    /** Agrega un elemento al final de la lista. */
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

    /** Agrega un elemento al inicio de la lista. */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;

        if (cola == null) {
            cola = nuevo;
        }

        tamano++;
    }

    /** Elimina la primera coincidencia y devuelve si la encontró. */
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

    /** Indica si la lista contiene el dato indicado. */
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
     * Devuelve el dato del índice indicado, empezando en cero.
     *
     * @throws IndexOutOfBoundsException si el índice no está dentro de la lista
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de los límites de la lista: " + indice
            );
        }

        Nodo<T> actual = cabeza;
        int posicion = 0;

        while (posicion < indice) {
            actual = actual.getSiguiente();
            posicion++;
        }

        return actual.getDato();
    }

    /** Devuelve el número de elementos. */
    public int tamano() {
        return tamano;
    }

    /** Indica si la lista está vacía. */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /** Desconecta todos los nodos y deja la lista vacía. */
    public void vaciar() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    /** Devuelve el contenido en orden, desde la cabeza. */
    @Override
    public String toString() {
        String resultado = "[";
        Nodo<T> actual = cabeza;

        while (actual != null) {
            resultado += actual.getDato();
            actual = actual.getSiguiente();

            if (actual != null) {
                resultado += ", ";
            }
        }

        return resultado + "]";
    }

    private boolean sonIguales(T primero, T segundo) {
        if (primero == null) {
            return segundo == null;
        }
        return primero.equals(segundo);
    }
}
