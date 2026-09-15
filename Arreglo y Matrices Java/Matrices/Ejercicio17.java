import java.util.Random;
import java.util.Scanner;

public class Ejercicio17 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el número de filas: ");
        int filas = sc.nextInt();
        System.out.print("Ingrese el número de columnas: ");
        int columnas = sc.nextInt();

        int[][] matriz = new int[filas][columnas];
        Random rand = new Random();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = rand.nextInt(20) + 1;
            }
        }

        System.out.println("Matriz generada:");
        imprimirMatriz(matriz);

        int[] sumaFilas = new int[filas];
        for (int i = 0; i < filas; i++) {
            int suma = 0;
            for (int j = 0; j < columnas; j++) {
                suma += matriz[i][j];
            }
            sumaFilas[i] = suma;
        }

        int[] sumaColumnas = new int[columnas];
        for (int j = 0; j < columnas; j++) {
            int suma = 0;
            for (int i = 0; i < filas; i++) {
                suma += matriz[i][j];
            }
            sumaColumnas[j] = suma;
        }

        System.out.println("\nSuma de cada fila:");
        for (int i = 0; i < filas; i++) {
            System.out.println("Fila " + (i + 1) + ": " + sumaFilas[i]);
        }

        System.out.println("\nSuma de cada columna:");
        for (int j = 0; j < columnas; j++) {
            System.out.println("Columna " + (j + 1) + ": " + sumaColumnas[j]);
        }

        sc.close();
    }

    static void imprimirMatriz(int[][] m) {
        for (int[] fila : m) {
            for (int val : fila) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }
}