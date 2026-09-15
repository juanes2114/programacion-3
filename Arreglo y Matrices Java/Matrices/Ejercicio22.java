import java.util.Scanner;

public class Ejercicio22 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese el número de filas: ");
        int filas = sc.nextInt();
        System.out.print("Ingrese el número de columnas: ");
        int columnas = sc.nextInt();

        int[][] matriz = new int[filas][columnas];

        System.out.println("Ingrese los " + (filas * columnas) + " elementos de la matriz:");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print("Elemento fila " + (i + 1) + ", columna " + (j + 1) + ": ");
                matriz[i][j] = sc.nextInt();
            }
        }

        System.out.println("\nMatriz ingresada:");
        for (int[] fila : matriz) {
            for (int val : fila) System.out.printf("%5d", val);
            System.out.println();
        }

        int mayor = matriz[0][0], menor = matriz[0][0];
        int filaMayor = 0, colMayor = 0, filaMenor = 0, colMenor = 0;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j] > mayor) {
                    mayor = matriz[i][j];
                    filaMayor = i;
                    colMayor = j;
                }
                if (matriz[i][j] < menor) {
                    menor = matriz[i][j];
                    filaMenor = i;
                    colMenor = j;
                }
            }
        }

        System.out.println("\nMayor elemento: " + mayor + " en la fila " + (filaMayor + 1) + ", columna " + (colMayor + 1));
        System.out.println("Menor elemento: " + menor + " en la fila " + (filaMenor + 1) + ", columna " + (colMenor + 1));

        sc.close();
    }
}