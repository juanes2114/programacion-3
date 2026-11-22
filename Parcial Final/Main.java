import modelo.Estudiante;

/** Punto de entrada temporal para probar las clases del modelo. */
public class Main {
    public static void main(String[] args) {
        Estudiante estudiante = new Estudiante(
                "Ana Torres",
                "A001",
                "ana.torres@universidad.edu",
                3
        );

        estudiante.mostrarInformacion();
    }
}
