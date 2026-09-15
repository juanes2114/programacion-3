import java.util.Random;
import java.util.Scanner;

public class Ejercicio21 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el orden M de la matriz cuadrada: ");
        int m = sc.nextInt();

        int[][] matriz = new int[m][m];
        Random rand = new Random();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                matriz[i][j] = rand.nextInt(20) + 1;
            }
        }

        System.out.println("Matriz generada:");
        for (int[] fila : matriz) {
            for (int val : fila) System.out.printf("%4d", val);
            System.out.println();
        }

        int[] B = new int[m];
        for (int i = 0; i < m; i++) {
            int sumaPares = 0;
            for (int j = 0; j < m; j++) {
                if (matriz[i][j] % 2 == 0) {
                    sumaPares += matriz[i][j];
                }
            }
            B[i] = sumaPares;
        }

        System.out.println("\nVector B (suma de valores pares por fila):");
        for (int i = 0; i < m; i++) {
            System.out.println("Fila " + (i + 1) + ": " + B[i]);
        }

        sc.close();
    }
}