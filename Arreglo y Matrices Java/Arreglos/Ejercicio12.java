import java.util.Random;
import java.util.Scanner;

public class Ejercicio12 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el tamaño N del arreglo: ");
        int n = sc.nextInt();

        int[] A = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(10) + 1;
        }

        System.out.println("Arreglo A generado:");
        for (int val : A) System.out.print(val + " ");
        System.out.println();

        System.out.print("\nIngrese el valor X a buscar: ");
        int x = sc.nextInt();

        int cont = 0;
        for (int val : A) {
            if (val == x) cont++;
        }

        int[] B = new int[cont];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == x) {
                B[k] = i + 1;
                k++;
            }
        }

        System.out.println("\nArreglo B con las posiciones donde aparece " + x + ":");
        for (int val : B) System.out.print(val + " ");
        System.out.println();

        sc.close();
    }
}