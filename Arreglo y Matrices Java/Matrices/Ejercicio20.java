import java.util.Random;

public class Ejercicio20 {
    public static void main(String[] args) {
        int n = 10;
        int[][] matriz = new int[n][n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = rand.nextInt(9) + 1;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j > i) {
                    matriz[i][j] = 0;
                }
            }
        }

        System.out.println("Matriz 10x10 con ceros por encima de la diagonal principal:\n");
        for (int[] fila : matriz) {
            for (int val : fila) {
                System.out.printf("%3d", val);
            }
            System.out.println();
        }
    }
}