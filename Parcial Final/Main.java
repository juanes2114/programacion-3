import estructuras.Cola;
import estructuras.ListaEnlazada;
import estructuras.Pila;

/** Pruebas breves de las estructuras enlazadas propias. */
public class Main {
    public static void main(String[] args) {
        probarLista();
        probarPila();
        probarCola();
    }

    private static void probarLista() {
        System.out.println("=== Prueba de lista enlazada ===");

        ListaEnlazada<String> lista = new ListaEnlazada<>();
        lista.agregarAlFinal("Algoritmos");
        lista.agregarAlFinal("Estructuras");
        lista.agregarAlInicio("Programación");

        System.out.println("Contenido: " + lista);
        System.out.println("Elemento en índice 1: " + lista.obtener(1));
        System.out.println("¿Contiene Estructuras?: " + lista.contiene("Estructuras"));
        System.out.println("¿Eliminó Algoritmos?: " + lista.eliminar("Algoritmos"));
        System.out.println("Lista final: " + lista);
        System.out.println("Recorrido con for-each:");

        for (String materia : lista) {
            System.out.println("- " + materia);
        }
    }

    private static void probarPila() {
        System.out.println();
        System.out.println("=== Prueba de pila ===");

        Pila<Integer> pila = new Pila<>();
        pila.push(10);
        pila.push(20);
        pila.push(30);

        System.out.println("Contenido (tope a fondo): " + pila);
        System.out.println("Elemento retirado: " + pila.pop());
        System.out.println("Nuevo tope: " + pila.peek());
        System.out.println("Contenido actual: " + pila);

        pila.pop();
        pila.pop();

        try {
            pila.pop();
        } catch (IllegalStateException excepcion) {
            System.out.println("Error esperado: " + excepcion.getMessage());
        }
    }

    private static void probarCola() {
        System.out.println();
        System.out.println("=== Prueba de cola ===");

        Cola<String> cola = new Cola<>();
        cola.encolar("Ana");
        cola.encolar("Luis");
        cola.encolar("Marta");
        cola.encolar("Carlos");

        System.out.println("Contenido (frente a final): " + cola);
        System.out.println("Atendido: " + cola.desencolar());
        System.out.println("¿Eliminó Marta?: " + cola.eliminar("Marta"));
        System.out.println("Contenido actual: " + cola);

        cola.desencolar();
        cola.desencolar();

        try {
            cola.desencolar();
        } catch (IllegalStateException excepcion) {
            System.out.println("Error esperado: " + excepcion.getMessage());
        }
    }
}
