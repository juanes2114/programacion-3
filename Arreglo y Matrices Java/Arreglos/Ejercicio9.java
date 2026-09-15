import java.util.Random;
import java.util.Scanner;

public class Ejercicio9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = 15;
        int[] A = new int[n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(10) + 1;
        }

        System.out.println("Arreglo generado:");
        for (int val : A) System.out.print(val + " ");
        System.out.println();

        System.out.print("\nIngrese el número que desea buscar: ");
        int x = sc.nextInt();

        int contador = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == x) {
                contador++;
            }
        }

        System.out.println("El número " + x + " se encuentra " + contador + " veces en el arreglo.");
        sc.close();
    }
}