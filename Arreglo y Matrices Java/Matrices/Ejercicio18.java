public class Ejercicio18 {
    public static void main(String[] args) {
        int[][] tabla = new int[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tabla[i][j] = (i + 1) * (j + 1);
            }
        }

        System.out.println("Tabla de multiplicar del 1 al 10:\n");
        System.out.print("    ");
        for (int j = 1; j <= 10; j++) {
            System.out.printf("%5d", j);
        }
        System.out.println();
        for (int i = 0; i < 55; i++) System.out.print("-");
        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.printf("%3d|", i + 1);
            for (int j = 0; j < 10; j++) {
                System.out.printf("%5d", tabla[i][j]);
            }
            System.out.println();
        }
    }
}