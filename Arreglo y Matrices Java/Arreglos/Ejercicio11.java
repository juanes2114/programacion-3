import java.util.Random;
import java.util.Scanner;

public class Ejercicio11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el tamaño N del arreglo: ");
        int n = sc.nextInt();

        int[] A = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(41) - 20;
        }

        System.out.println("Arreglo A generado:");
        for (int val : A) System.out.print(val + " ");
        System.out.println();

        int contNeg = 0, contCero = 0, contPos = 0;
        for (int val : A) {
            if (val < 0) contNeg++;
            else if (val == 0) contCero++;
            else contPos++;
        }

        int[] negativos = new int[contNeg];
        int[] ceros = new int[contCero];
        int[] positivos = new int[contPos];

        int iN = 0, iC = 0, iP = 0;
        for (int val : A) {
            if (val < 0) {
                negativos[iN++] = val;
            } else if (val == 0) {
                ceros[iC++] = val;
            } else {
                positivos[iP++] = val;
            }
        }

        System.out.println("\nNegativos:");
        for (int val : negativos) System.out.print(val + " ");

        System.out.println("\nCeros:");
        for (int val : ceros) System.out.print(val + " ");

        System.out.println("\nPositivos:");
        for (int val : positivos) System.out.print(val + " ");
        System.out.println();

        sc.close();
    }
}