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
