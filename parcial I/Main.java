import java.util.Scanner;

public class Main {
  static Scanner sc = new Scanner (System.in);
 static Teatro teatro = new Teatro();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n===== CINEMASTAR =====");
            System.out.println("1. Creacion de peliculas");
            System.out.println("2. Asignacion de funciones");
            System.out.println("3. Ventas");
            System.out.println("4. Salir");
            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    menuPeliculas();
                    break;
                case 2:
                    menuAsignacion();
                    break;
                case 3:
                    menuVentas();
                    break;
                case 4:
                    System.out.println("Hasta pronto.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 4);

        sc.close();
}

    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    static String leerTexto(String mensaje) {
        String s;
        do {
            System.out.print(mensaje);
            s = sc.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("No puede estar vacio.");
            }
        } while (s.isEmpty());
        return s;
    }
    static String dinero(int valor) {
        return "$" + String.format("%,d", valor).replace(',', '.');
    }

    static int leerSala(String mensaje) {
        int n;
        do {
            n = leerEntero(mensaje);
            if (n < 0 || n > 3) {
                System.out.println("Sala inexistente.");
            }
        } while (n < 0 || n > 3);
        return n;
    }

    static int leerFranja() {
        int f = leerEntero("Franja (1-3): ");
        if (f < 1 || f > 3) {
            System.out.println("Franja invalida.");
            return -1;
        }
        return f - 1;
    }
  
    static void menuPeliculas() {
        System.out.println("\n--- PELICULAS REGISTRADAS ---");
        teatro.listarPeliculas();

        System.out.print("Desea anadir una pelicula? (s/n): ");
        String resp = sc.nextLine().trim();
        if (!resp.equalsIgnoreCase("s")) {
            return;
        }

        String nombre = leerTexto("Nombre: ");
        String idioma = leerTexto("Idioma: ");

        String tipo;
        while (true) {
            tipo = leerTexto("Tipo (35mm o 3D): ");
            if (tipo.equalsIgnoreCase("35mm")) {
                tipo = "35mm";
                break;
            }
            if (tipo.equalsIgnoreCase("3D")) {
                tipo = "3D";
                break;
            }
            System.out.println("Tipo invalido.");
        }

        int duracion;
        do {
            duracion = leerEntero("Duracion (minutos): ");
            if (duracion <= 0) {
                System.out.println("La duracion debe ser positiva.");
            }
        } while (duracion <= 0);

        if (teatro.agregarPelicula(new Pelicula(nombre, idioma, tipo, duracion))) {
            System.out.println("Pelicula registrada.");
        } else {
            System.out.println("No hay espacio para mas peliculas.");
        }
    }

    static void menuAsignacion() {
        if (teatro.getCantidadPeliculas() == 0) {
            System.out.println("Primero debe registrar peliculas.");
            return;
        }

        int numSala = leerSala("Sala a programar (1-3, 0 para volver): ");
        if (numSala == 0) {
            return;
        }
        Sala sala = teatro.getSala(numSala);

        System.out.println("\nFunciones de la sala " + numSala + ":");
        for (int i = 0; i < 3; i++) {
            Funcion f = sala.getFuncion(i);
            if (f == null) {
                System.out.println((i + 1) + ". " + Sala.HORARIOS[i] + " -> (libre)");
            } else {
                System.out.println((i + 1) + ". " + Sala.HORARIOS[i] + " -> " + f.getPelicula().getNombre());
            }
        }

        int franja = leerFranja();
        if (franja == -1) {
            return;
        }

        System.out.println("\nPeliculas:");
        teatro.listarPeliculas();
        int idx = leerEntero("Numero de pelicula: ") - 1;
        if (idx < 0 || idx >= teatro.getCantidadPeliculas()) {
            System.out.println("Pelicula inexistente.");
            return;
        }

        String error = sala.asignarPelicula(franja, teatro.getPelicula(idx));
        if (error == null) {
            System.out.println("Funcion asignada.");
        } else {
            System.out.println("Error: " + error);
        }
    }

    static void menuVentas() {
        while (true) {
            int numSala = leerSala("\nSala (1-3, 0 para salir de ventas): ");
            if (numSala == 0) {
                return;
            }
            Sala sala = teatro.getSala(numSala);

            System.out.println("\nFunciones de la sala " + numSala + ":");
            for (int i = 0; i < 3; i++) {
                Funcion f = sala.getFuncion(i);
                if (f == null) {
                    System.out.println((i + 1) + ". " + Sala.HORARIOS[i] + " -> sin pelicula asignada");
                } else {
                    System.out.println((i + 1) + ". " + Sala.HORARIOS[i] + " -> "
                            + f.getPelicula().getNombre() + " (" + f.getDisponibles()
                            + " sillas disponibles)");
                }
            }

            int franja = leerFranja();
            if (franja == -1) {
                continue;
            }
            if (sala.getFuncion(franja) == null) {
                System.out.println("Esa franja no tiene pelicula asignada.");
                continue;
            }
            venderSillas(sala, franja);
        }
    }
  
    static void venderSillas(Sala sala, int franja) {
        Funcion funcion = sala.getFuncion(franja);

        sala.mostrarSillas(franja);
        System.out.println("Sillas disponibles: " + funcion.getDisponibles());
        if (funcion.getDisponibles() == 0) {
            System.out.println("Funcion agotada.");
            return;
        }

        System.out.print("Sillas a comprar separadas por coma (ej: A3, B8, D9): ");
        String[] partes = sc.nextLine().split(",");

        int total = 0;
        int contGeneral = 0, contPreferencial = 0, cont3D = 0;

        for (int i = 0; i < partes.length; i++) {
            String s = partes[i].trim().toUpperCase();
            if (s.length() < 2) {
                System.out.println("\"" + s + "\": formato invalido.");
                continue;
            }

            int fila = s.charAt(0) - 'A';
            int col;
            try {
                col = Integer.parseInt(s.substring(1)) - 1;
            } catch (NumberFormatException e) {
                System.out.println("\"" + s + "\": formato invalido.");
                continue;
            }

            if (!sala.existeSilla(fila, col)) {
                System.out.println(s + ": la silla no existe en esta sala.");
            } else if (!funcion.comprar(fila, col)) {
                System.out.println(s + ": la silla NO esta disponible.");
            } else {
                total += sala.precio(fila);
                if (sala.es3D()) {
                    cont3D++;
                } else if (sala.esPreferencial(fila)) {
                    contPreferencial++;
                } else {
                    contGeneral++;
                }
                System.out.println(s + ": comprada.");
            }
        }

        System.out.println("\n--- RESUMEN ---");
        if (contGeneral > 0) {
            System.out.println("General: " + contGeneral + " x " + dinero(8000));
        }
        if (contPreferencial > 0) {
            System.out.println("Preferencial: " + contPreferencial + " x " + dinero(12000));
        }
        if (cont3D > 0) {
            System.out.println("Sala 3D: " + cont3D + " x " + dinero(10000));
        }
        System.out.println("TOTAL A PAGAR: " + dinero(total));
        System.out.println("Sillas disponibles ahora: " + funcion.getDisponibles());
    }
}
