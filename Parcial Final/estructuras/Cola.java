package estructuras;

/** Cola genérica enlazada con referencias al frente y al final. */
public class Cola<T> {
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
     * Quita y devuelve el dato del frente.
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

    /** Devuelve el elemento en el índice indicado, contando desde el frente. */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de los límites de la cola: " + indice
            );
        }

        Nodo<T> actual = frente;
        int posicion = 0;

        while (posicion < indice) {
            actual = actual.getSiguiente();
            posicion++;
        }

        return actual.getDato();
    }

    /** Indica si la cola está vacía. */
    public boolean estaVacia() {
        return frente == null;
    }

    /** Devuelve el número de elementos. */
    public int tamano() {
        return tamano;
    }

    /** Desconecta todos los nodos y deja la cola vacía. */
    public void vaciar() {
        frente = null;
        finalCola = null;
        tamano = 0;
    }

    /** Devuelve el contenido desde el frente hasta el final. */
    @Override
    public String toString() {
        String resultado = "[";
        Nodo<T> actual = frente;

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
