package modelo;

import estructuras.ListaEnlazada;

/** Estudiante con notas por semestre e historial de materias cursadas. */
public class Estudiante extends Persona {
    private int semestre;
    private Double[][] notas;
    private String[][] codigosMateriasNotas;
    private ListaEnlazada<Materia> historialMaterias;

    /** Crea un estudiante con espacio para diez semestres y veinte materias por semestre. */
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

    /**
     * Registra una nota o reemplaza la anterior para esa materia y semestre.
     *
     * @throws IllegalArgumentException si el semestre, la materia, la nota
     *                                  o la cantidad de materias no son válidos
     */
    public void registrarNota(int semestre, Materia materia, double nota) {
        validarSemestre(semestre);

        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser null.");
        }
        if (nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException(
                    "La nota debe estar entre 0.0 y 5.0."
            );
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

    /** Indica si existe una nota de 3.0 o más para el código indicado. */
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

    /** Devuelve la nota registrada o null si no hay una nota para esa materia. */
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
     * Restaura la nota anterior. Si notaAnterior es null, elimina la nota.
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
                && (notaAnterior < 0.0 || notaAnterior > 5.0)) {
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

    /** Calcula el promedio simple de las notas de un semestre. */
    public double calcularPromedioSemestre(int semestre) {
        validarSemestre(semestre);

        int fila = semestre - 1;
        double suma = 0.0;
        int cantidad = 0;
        int columna = 0;

        while (columna < notas[fila].length) {
            if (notas[fila][columna] != null) {
                suma += notas[fila][columna];
                cantidad++;
            }
            columna++;
        }

        if (cantidad == 0) {
            return 0.0;
        }

        return suma / cantidad;
    }

    /** Calcula el promedio de todas las notas registradas. */
    public double calcularPromedioAcumulado() {
        double suma = 0.0;
        int cantidad = 0;
        int fila = 0;

        while (fila < notas.length) {
            int columna = 0;

            while (columna < notas[fila].length) {
                if (notas[fila][columna] != null) {
                    suma += notas[fila][columna];
                    cantidad++;
                }
                columna++;
            }
            fila++;
        }

        if (cantidad == 0) {
            return 0.0;
        }

        return suma / cantidad;
    }

    /** Cuenta todas las notas registradas. */
    public int contarNotas() {
        int cantidad = 0;
        int fila = 0;

        while (fila < notas.length) {
            int columna = 0;

            while (columna < notas[fila].length) {
                if (notas[fila][columna] != null) {
                    cantidad++;
                }
                columna++;
            }
            fila++;
        }

        return cantidad;
    }

    /** Cuenta las notas registradas en un semestre. */
    public int contarNotasSemestre(int semestre) {
        validarSemestre(semestre);

        int cantidad = 0;
        int fila = semestre - 1;
        int columna = 0;

        while (columna < notas[fila].length) {
            if (notas[fila][columna] != null) {
                cantidad++;
            }
            columna++;
        }

        return cantidad;
    }

    /** Cuenta las notas aprobadas, es decir, de 3.0 o más. */
    public int contarAprobadas() {
        int cantidad = 0;
        int fila = 0;

        while (fila < notas.length) {
            int columna = 0;

            while (columna < notas[fila].length) {
                if (notas[fila][columna] != null
                        && notas[fila][columna] >= 3.0) {
                    cantidad++;
                }
                columna++;
            }
            fila++;
        }

        return cantidad;
    }

    /** Cuenta las notas reprobadas, es decir, menores que 3.0. */
    public int contarReprobadas() {
        int cantidad = 0;
        int fila = 0;

        while (fila < notas.length) {
            int columna = 0;

            while (columna < notas[fila].length) {
                if (notas[fila][columna] != null
                        && notas[fila][columna] < 3.0) {
                    cantidad++;
                }
                columna++;
            }
            fila++;
        }

        return cantidad;
    }

    /** Genera el reporte académico completo sin imprimirlo. */
    public String generarReporteAcademico() {
        String reporte = "--- REPORTE ACADEMICO ---\n";
        reporte += "Estudiante: " + getNombre()
                + " (ID: " + getId() + ")\n";

        int semestreActual = 1;
        while (semestreActual <= 10) {
            int cantidad = contarNotasSemestre(semestreActual);

            if (cantidad > 0) {
                reporte += "\nSemestre " + semestreActual + ":\n";
                int fila = semestreActual - 1;
                int columna = 0;

                while (columna < notas[fila].length) {
                    if (notas[fila][columna] != null) {
                        reporte += "  "
                                + codigosMateriasNotas[fila][columna]
                                + ": "
                                + String.format("%.1f", notas[fila][columna])
                                + "\n";
                    }
                    columna++;
                }

                reporte += "  Promedio: "
                        + String.format(
                                "%.2f",
                                calcularPromedioSemestre(semestreActual)
                        )
                        + "\n";
            }

            semestreActual++;
        }

        double promedio = calcularPromedioAcumulado();

        if (contarNotas() == 0) {
            reporte += "\nPromedio acumulado: 0.0 (sin notas aun)";
        } else {
            reporte += "\n=== RESUMEN ===\n";
            reporte += "Promedio acumulado: "
                    + String.format("%.2f", promedio) + "\n";
            reporte += "Materias aprobadas: " + contarAprobadas() + "\n";
            reporte += "Materias reprobadas: " + contarReprobadas();
        }

        return reporte;
    }

    /** Genera el reporte de notas de un semestre sin imprimirlo. */
    public String generarReporteSemestre(int semestre) {
        validarSemestre(semestre);

        String reporte = "--- REPORTE SEMESTRE "
                + semestre + " ---\n";
        int cantidad = contarNotasSemestre(semestre);

        if (cantidad == 0) {
            reporte += "No hay notas registradas en este semestre.";
            return reporte;
        }

        int fila = semestre - 1;
        int columna = 0;

        while (columna < notas[fila].length) {
            if (notas[fila][columna] != null) {
                reporte += codigosMateriasNotas[fila][columna]
                        + ": "
                        + String.format("%.1f", notas[fila][columna])
                        + "\n";
            }
            columna++;
        }

        reporte += "Promedio: "
                + String.format("%.2f", calcularPromedioSemestre(semestre));

        return reporte;
    }

    /** Genera el reporte de todas las notas reprobadas sin imprimirlo. */
    public String generarReporteReprobadas() {
        String reporte = "--- MATERIAS REPROBADAS ---\n";
        int cantidad = contarReprobadas();

        if (cantidad == 0) {
            return reporte + "No hay materias reprobadas.";
        }

        int fila = 0;
        while (fila < notas.length) {
            int columna = 0;

            while (columna < notas[fila].length) {
                if (notas[fila][columna] != null
                        && notas[fila][columna] < 3.0) {
                    reporte += "Semestre " + (fila + 1)
                            + ": " + codigosMateriasNotas[fila][columna]
                            + " - "
                            + String.format("%.1f", notas[fila][columna])
                            + "\n";
                }
                columna++;
            }
            fila++;
        }

        return reporte;
    }

    private void validarSemestre(int semestre) {
        if (semestre < 1 || semestre > 10) {
            throw new IllegalArgumentException(
                    "El semestre debe estar entre 1 y 10."
            );
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

    /** Muestra los datos básicos y el tamaño del historial. */
    @Override
    public void mostrarInformacion() {
        System.out.println("Estudiante");
        System.out.println("Nombre: " + getNombre());
        System.out.println("ID: " + getId());
        System.out.println("Email: " + getEmail());
        System.out.println("Semestre: " + semestre);
        System.out.println("Matriz de notas: " + notas.length
                + " semestres x " + notas[0].length + " materias");
        System.out.println("Materias en el historial: "
                + historialMaterias.tamano());
    }
}
