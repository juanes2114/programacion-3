package modelo;

import estructuras.ListaEnlazada;

/** Estudiante con notas asociadas a códigos de materia e historial académico. */
public class Estudiante extends Persona {
    private int semestre;
    private Double[][] notas;
    private String[][] codigosMateriasNotas;
    private ListaEnlazada<Materia> historialMaterias;

    /** Crea un estudiante con matriz de notas vacía para 10 semestres y 20 materias. */
    public Estudiante(String nombre, String id, String email, int semestre) {
        super(nombre, id, email);
        this.semestre = semestre;
        this.notas = new Double[10][20];
        this.codigosMateriasNotas = new String[10][20];
        this.historialMaterias = new ListaEnlazada<>();
    }

    public int getSemestre() {
        return semestre;
    }

    public Double[][] getNotas() {
        return notas;
    }

    /** Agrega la materia al historial si su código todavía no aparece allí. */
    public void agregarAlHistorial(Materia materia) {
        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser null.");
        }

        if (!haCursado(materia.getCodigo())) {
            historialMaterias.agregarAlFinal(materia);
        }
    }

    public ListaEnlazada<Materia> getHistorialMaterias() {
        return historialMaterias;
    }

    /** Indica si el código de materia está registrado en el historial. */
    public boolean haCursado(String codigoMateria) {
        if (codigoMateria == null) {
            return false;
        }

        for (Materia materia : historialMaterias) {
            if (materia.getCodigo().equals(codigoMateria)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Registra o reemplaza una nota y agrega la materia al historial.
     *
     * @param semestre semestre de 1 a 10
     * @param materia materia a la que corresponde la nota
     * @param nota calificación de 0 a 5
     * @throws IllegalArgumentException si los datos no son válidos o el semestre
     *                                  ya tiene 20 materias distintas registradas
     */
    public void registrarNota(int semestre, Materia materia, double nota) {
        validarSemestre(semestre);

        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser null.");
        }
        if (Double.isNaN(nota) || Double.isInfinite(nota) || nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0.");
        }

        int fila = semestre - 1;
        String codigoMateria = materia.getCodigo();
        int columnaExistente = buscarColumna(fila, codigoMateria);

        if (columnaExistente == -1) {
            int columnaLibre = buscarColumnaLibre(fila);
            if (columnaLibre == -1) {
                throw new IllegalArgumentException(
                        "El semestre " + semestre + " ya tiene 20 materias registradas."
                );
            }

            columnaExistente = columnaLibre;
            codigosMateriasNotas[fila][columnaExistente] = codigoMateria;
        }

        notas[fila][columnaExistente] = nota;
        agregarAlHistorial(materia);
    }

    /** Indica si hay una nota registrada de 3.0 o más para el código indicado. */
    public boolean haAprobado(String codigoMateria) {
        if (codigoMateria == null) {
            return false;
        }

        for (int fila = 0; fila < notas.length; fila++) {
            for (int columna = 0; columna < notas[fila].length; columna++) {
                if (codigoMateria.equals(codigosMateriasNotas[fila][columna])
                        && notas[fila][columna] != null
                        && notas[fila][columna] >= 3.0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Consulta la nota de una materia en un semestre.
     *
     * @return la nota, o null si todavía no hay nota registrada
     */
    public Double consultarNota(int semestre, String codigoMateria) {
        validarSemestre(semestre);

        if (codigoMateria == null) {
            return null;
        }

        int columna = buscarColumna(semestre - 1, codigoMateria);
        if (columna == -1) {
            return null;
        }

        return notas[semestre - 1][columna];
    }

    /**
     * Deshace el registro más reciente para esa materia y semestre.
     * Si notaAnterior es null, quita la nota; de lo contrario, restaura ese valor.
     * Si al quitarla no queda ninguna nota para la materia, también la quita del historial.
     */
    public void deshacerRegistroNota(
            int semestre,
            Materia materia,
            Double notaAnterior
    ) {
        validarSemestre(semestre);

        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser null.");
        }
        if (notaAnterior != null
                && (Double.isNaN(notaAnterior)
                || Double.isInfinite(notaAnterior)
                || notaAnterior < 0.0
                || notaAnterior > 5.0)) {
            throw new IllegalArgumentException("La nota anterior debe estar entre 0.0 y 5.0.");
        }

        int fila = semestre - 1;
        String codigoMateria = materia.getCodigo();
        int columna = buscarColumna(fila, codigoMateria);

        if (columna == -1) {
            return;
        }

        if (notaAnterior != null) {
            notas[fila][columna] = notaAnterior;
            agregarAlHistorial(materia);
            return;
        }

        notas[fila][columna] = null;
        codigosMateriasNotas[fila][columna] = null;

        if (!tieneNotaRegistrada(codigoMateria)) {
            quitarDelHistorial(codigoMateria);
        }
    }

    private void validarSemestre(int semestre) {
        if (semestre < 1 || semestre > 10) {
            throw new IllegalArgumentException("El semestre debe estar entre 1 y 10.");
        }
    }

    private int buscarColumna(int fila, String codigoMateria) {
        for (int columna = 0; columna < codigosMateriasNotas[fila].length; columna++) {
            if (codigoMateria.equals(codigosMateriasNotas[fila][columna])) {
                return columna;
            }
        }
        return -1;
    }

    private int buscarColumnaLibre(int fila) {
        for (int columna = 0; columna < codigosMateriasNotas[fila].length; columna++) {
            if (codigosMateriasNotas[fila][columna] == null) {
                return columna;
            }
        }
        return -1;
    }

    private boolean tieneNotaRegistrada(String codigoMateria) {
        for (int fila = 0; fila < codigosMateriasNotas.length; fila++) {
            for (int columna = 0; columna < codigosMateriasNotas[fila].length; columna++) {
                if (codigoMateria.equals(codigosMateriasNotas[fila][columna])
                        && notas[fila][columna] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void quitarDelHistorial(String codigoMateria) {
        for (Materia materia : historialMaterias) {
            if (materia.getCodigo().equals(codigoMateria)) {
                historialMaterias.eliminar(materia);
                return;
            }
        }
    }

    /** Muestra los datos del estudiante y el número de materias del historial. */
    @Override
    public void mostrarInformacion() {
        System.out.println("Estudiante");
        System.out.println("Nombre: " + getNombre());
        System.out.println("ID: " + getId());
        System.out.println("Email: " + getEmail());
        System.out.println("Semestre: " + semestre);
        System.out.println("Matriz de notas: " + notas.length
                + " semestres x " + notas[0].length + " materias");
        System.out.println("Materias en el historial: " + historialMaterias.tamano());
    }
}
