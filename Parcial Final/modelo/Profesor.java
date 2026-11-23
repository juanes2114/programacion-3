package modelo;

/** Profesor que hereda los datos básicos de Persona. */
public class Profesor extends Persona {
    private String departamento;
    private double salario;

    /** Crea un profesor con sus datos personales, departamento y salario. */
    public Profesor(
            String nombre,
            String id,
            String email,
            String departamento,
            double salario
    ) {
        super(nombre, id, email);
        this.departamento = departamento;
        this.salario = salario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public double getSalario() {
        return salario;
    }

    /** Muestra los datos personales y laborales del profesor. */
    @Override
    public void mostrarInformacion() {
        System.out.println("Profesor");
        System.out.println("Nombre: " + getNombre());
        System.out.println("ID: " + getId());
        System.out.println("Email: " + getEmail());
        System.out.println("Departamento: " + departamento);
        System.out.println("Salario: " + salario);
    }
}
