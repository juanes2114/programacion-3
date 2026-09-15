import java.util.Random;

public class Ejercicio7 {
    public static void main(String[] args) {
        int n = 10;
        int[] A = new int[n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(100) + 1;
        }

        System.out.println("Arreglo A generado:");
        imprimirArreglo(A);

        int contPares = 0;
        int contImpares = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] % 2 == 0) {
                contPares++;
            } else {
                contImpares++;
            }
        }

        int[] pares = new int[contPares];
        int[] impares = new int[contImpares];

        
        int p = 0, im = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] % 2 == 0) {
                pares[p] = A[i];
                p++;
            } else {
                impares[im] = A[i];
                im++;
            }
        }

        System.out.println("\nArreglo de PARES:");
        imprimirArreglo(pares);

        System.out.println("\nArreglo de IMPARES:");
        imprimirArreglo(impares);
    }

    static void imprimirArreglo(int[] arr) {
        for (int val : arr) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
}