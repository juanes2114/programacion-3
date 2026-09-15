import java.util.Random;
import java.util.Scanner;

public class Ejercicio10 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el tamaño N del arreglo: ");
        int n = sc.nextInt();

        int[] A = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(20) + 1;
        }

        System.out.println("Arreglo A generado:");
        for (int val : A) System.out.print(val + " ");
        System.out.println();

        int tamB = (n % 2 == 0) ? n / 2 : (n / 2 + 1);
        int[] B = new int[tamB];

        int izq = 0, der = n - 1, k = 0;
        while (izq < der) {
            B[k] = A[izq] + A[der];
            izq++;
            der--;
            k++;
        }
        if (izq == der) {
            B[k] = A[izq];
        }

        System.out.println("\nArreglo B (suma de opuestos):");
        for (int val : B) System.out.print(val + " ");
        System.out.println();

        sc.close();
    }
}