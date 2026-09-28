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
