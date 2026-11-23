package modelo;

import estructuras.ListaEnlazada;

/** Estudiante con notas asociadas a códigos de materia e historial académico. */
public class Estudiante extends Persona {
    private int semestre;
    private Double[][] notas;
    private String[][] codigosMateriasNotas;
    private ListaEnlazada<Materia> historialMaterias;

    /** Crea un estudiante con una matriz vacía para diez semestres. */
    public Estudiante(String nombre, String id, String email, int semestre) {
        super(nombre, id, email);
        this.semestre = semestre;
        notas = new Double[10][20];
        codigosMateriasNotas = new String[10][20];
        historialMaterias = new ListaEnlazada<>();
    }

    public int getSemestre() {
        return semestre;
    }

    public Double[][] getNotas() {
        return notas;
    }

    /** Agrega la materia al historial si aún no aparece por su código. */
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

    /** Indica si el historial contiene una materia con ese código. */
    public boolean haCursado(String codigoMateria) {
        if (codigoMateria == null) {
            return false;
        }

        int indice = 0;
        while (indice < historialMaterias.tamano()) {
            Materia materia = historialMaterias.obtener(indice);
            if (materia.getCodigo().equals(codigoMateria)) {
                return true;
            }
            indice++;
        }

        return false;
    }

    /** Registra o reemplaza la nota y agrega la materia al historial. */
    public void registrarNota(int semestre, Materia materia, double nota) {
        validarSemestre(semestre);

        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser null.");
        }
        if (Double.isNaN(nota) || Double.isInfinite(nota)
                || nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0.");
        }

        int fila = semestre - 1;
        String codigo = materia.getCodigo();
        int columna = buscarColumna(fila, codigo);

        if (columna == -1) {
            columna = buscarColumnaLibre(fila);
            if (columna == -1) {
                throw new IllegalArgumentException(
                        "El semestre " + semestre
                                + " ya tiene 20 materias registradas."
                );
            }
            codigosMateriasNotas[fila][columna] = codigo;
        }

        notas[fila][columna] = nota;
        agregarAlHistorial(materia);
    }

    /** Indica si existe alguna nota aprobada para el código indicado. */
    public boolean haAprobado(String codigoMateria) {
        if (codigoMateria == null) {
            return false;
        }

        int fila = 0;
        while (fila < notas.length) {
            int columna = 0;
            while (columna < notas[fila].length) {
                if (codigoMateria.equals(codigosMateriasNotas[fila][columna])
                        && notas[fila][columna] != null
                        && notas[fila][columna] >= 3.0) {
                    return true;
                }
                columna++;
            }
            fila++;
        }

        return false;
    }

    /** Devuelve la nota del semestre indicado o null si no hay nota. */
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
     * Deshace el registro: restaura notaAnterior o quita la nota si es null.
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
            throw new IllegalArgumentException(
                    "La nota anterior debe estar entre 0.0 y 5.0."
            );
        }

        int fila = semestre - 1;
        String codigo = materia.getCodigo();
        int columna = buscarColumna(fila, codigo);

        if (columna == -1) {
            return;
        }

        if (notaAnterior != null) {
            notas[fila][columna] = notaAnterior;
            agregarAlHistorial(materia);
        } else {
            notas[fila][columna] = null;
            codigosMateriasNotas[fila][columna] = null;

            if (!tieneNotaRegistrada(codigo)) {
                quitarDelHistorial(codigo);
            }
        }
    }

    private void validarSemestre(int semestre) {
        if (semestre < 1 || semestre > 10) {
            throw new IllegalArgumentException("El semestre debe estar entre 1 y 10.");
        }
    }

    private int buscarColumna(int fila, String codigo) {
        int columna = 0;
        while (columna < codigosMateriasNotas[fila].length) {
            if (codigo.equals(codigosMateriasNotas[fila][columna])) {
                return columna;
            }
            columna++;
        }
        return -1;
    }

    private int buscarColumnaLibre(int fila) {
        int columna = 0;
        while (columna < codigosMateriasNotas[fila].length) {
            if (codigosMateriasNotas[fila][columna] == null) {
                return columna;
            }
            columna++;
        }
        return -1;
    }

    private boolean tieneNotaRegistrada(String codigo) {
        int fila = 0;
        while (fila < codigosMateriasNotas.length) {
            int columna = 0;
            while (columna < codigosMateriasNotas[fila].length) {
                if (codigo.equals(codigosMateriasNotas[fila][columna])
                        && notas[fila][columna] != null) {
                    return true;
                }
                columna++;
            }
            fila++;
        }
        return false;
    }

    private void quitarDelHistorial(String codigo) {
        int indice = 0;
        while (indice < historialMaterias.tamano()) {
            Materia materia = historialMaterias.obtener(indice);
            if (materia.getCodigo().equals(codigo)) {
                historialMaterias.eliminar(materia);
                return;
            }
            indice++;
        }
    }

    /** Imprime información del estudiante y el tamaño de su historial. */
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
