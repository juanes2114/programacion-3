public class Sala {
    public static final String[] HORARIOS = {"14:00 - 16:30", "16:30 - 19:00", "19:00 - 21:00"};

    private int numero;
    private boolean es3D;
    private boolean preferencial;
    private int filas;
    private Funcion[] funciones;

    public Sala(int numero, boolean es3D, boolean preferencial) {
        this.numero = numero;
        this.es3D = es3D;
        this.preferencial = preferencial;
        if (preferencial) {
            this.filas = 8;
        } else {
            this.filas = 6;
        }
        this.funciones = new Funcion[3];
    }

    public int getNumero() {
        return numero;
    }

    public boolean es3D() {
        return es3D;
    }

    public Funcion getFuncion(int franja) {
        return funciones[franja];
    }

    public int columnasEnFila(int fila) {
        if (fila >= 6) {
            return 9;
        }
        return 12;
    }

    public int totalSillas() {
        int total = 6 * 12;
        if (preferencial) {
            total += 2 * 9;
        }
        return total;
    }

    public boolean existeSilla(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnasEnFila(fila);
    }

    public boolean esPreferencial(int fila) {
        return fila >= 6;
    }

    public int precio(int fila) {
        if (es3D) {
            return 10000;
        }
        if (esPreferencial(fila)) {
            return 12000;
        }
        return 8000;
    }

    public String asignarPelicula(int franja, Pelicula p) {
        if (funciones[franja] != null) {
            return "La sala " + numero + " ya tiene una pelicula en esa franja.";
        }
        if (es3D && !p.es3D()) {
            return "La sala 3 solo proyecta peliculas 3D.";
        }
        if (!es3D && p.es3D()) {
            return "Las salas 1 y 2 no proyectan peliculas 3D.";
        }
        funciones[franja] = new Funcion(p, filas, 12, totalSillas());
        return null;
    }

    public void mostrarSillas(int franja) {
        Funcion f = funciones[franja];
        System.out.print("\n     ");
        for (int c = 1; c <= 12; c++) {
            System.out.printf("%-2d", c);
        }
        System.out.println();

        for (int i = filas - 1; i >= 0; i--) {
            if (preferencial && i == 5) {
                System.out.println("    ------------------------");
            }
            System.out.print("  " + (char) ('A' + i) + "  ");
            if (esPreferencial(i)) {
                System.out.print("      ");
            }
            for (int j = 0; j < columnasEnFila(i); j++) {
                if (f.estaOcupada(i, j)) {
                    System.out.print("X ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
        System.out.println("      ==== PANTALLA ====\n");
    }
}
