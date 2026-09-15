import java.util.Random;
import java.util.Scanner;

public class Ejercicio13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el tamaño N del arreglo: ");
        int n = sc.nextInt();

        int[] A = new int[n];
        Random rand = new Random();
        double suma = 0;
        for (int i = 0; i < n; i++) {
            A[i] = rand.nextInt(50) + 1;
            suma += A[i];
        }

        double media = suma / n;

        System.out.println("Arreglo A generado:");
        for (int val : A) System.out.print(val + " ");
        System.out.println("\nMedia: " + media);

        int contMayores = 0, contMenores = 0;
        for (int val : A) {
            if (val > media) contMayores++;
            else if (val < media) contMenores++;
        }

        double[] mayores = new double[contMayores];
        double[] menores = new double[contMenores];

        int iM = 0, im = 0;
        for (int val : A) {
            if (val > media) {
                mayores[iM++] = val;
            } else if (val < media) {
                menores[im++] = val;
            }
        }

        System.out.println("\nElementos MAYORES que la media:");
        for (double val : mayores) System.out.print(val + " ");

        System.out.println("\nElementos MENORES que la media:");
        for (double val : menores) System.out.print(val + " ");
        System.out.println();

        sc.close();
    }
}