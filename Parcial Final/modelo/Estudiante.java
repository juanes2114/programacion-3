package modelo;

/** Estudiante con semestre actual y notas organizadas por semestre y materia. */
public class Estudiante extends Persona {
    private int semestre;
    private Double[][] notas;

    /** Crea la matriz de 10 semestres por 20 materias; sus casillas empiezan en null. */
    public Estudiante(String nombre, String id, String email, int semestre) {
        super(nombre, id, email);
        this.semestre = semestre;
        this.notas = new Double[10][20];
    }

    public int getSemestre() {
        return semestre;
    }

    public Double[][] getNotas() {
        return notas;
    }

    /** Imprime los datos básicos del estudiante y el tamaño de su matriz de notas. */
    @Override
    public void mostrarInformacion() {
        System.out.println("Estudiante");
        System.out.println("Nombre: " + getNombre());
        System.out.println("ID: " + getId());
        System.out.println("Email: " + getEmail());
        System.out.println("Semestre: " + semestre);
        System.out.println("Matriz de notas: " + notas.length
                + " semestres x " + notas[0].length + " materias");
    }
}
