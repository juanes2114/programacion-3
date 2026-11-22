package estructuras;

/** Nodo genérico que guarda un dato y la referencia al siguiente nodo. */
public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;

    /** Crea un nodo con el dato indicado y sin nodo siguiente. */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
