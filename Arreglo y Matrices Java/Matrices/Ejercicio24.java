import java.util.Random;

public class Ejercicio24 {
    public static void main(String[] args) {
        String[] cereales = {"Arroz", "Avena", "Cebada", "Trigo"};
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                           "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        int[][] produccion = new int[12][4];
        Random rand = new Random();

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 4; j++) {
                produccion[i][j] = rand.nextInt(100) + 50;
            }
        }

        System.out.println("Producción mensual (toneladas) por tipo de cereal:\n");
        System.out.printf("%-12s", "Mes");
        for (String c : cereales) System.out.printf("%10s", c);
        System.out.printf("%10s%n", "Total");

        int[] totalMes = new int[12];
        int totalAnual = 0;

        for (int i = 0; i < 12; i++) {
            System.out.printf("%-12s", meses[i]);
            int suma = 0;
            for (int j = 0; j < 4; j++) {
                System.out.printf("%10d", produccion[i][j]);
                suma += produccion[i][j];
            }
            totalMes[i] = suma;
            totalAnual += suma;
            System.out.printf("%10d%n", suma);
        }

        double promedioAnual = (double) totalAnual / 12;
        System.out.println("\na. Promedio mensual de toneladas cosechadas: " + promedioAnual);

        int mesesSuperiores = 0, mesesInferiores = 0;
        for (int i = 0; i < 12; i++) {
            if (totalMes[i] > promedioAnual) mesesSuperiores++;
            else if (totalMes[i] < promedioAnual) mesesInferiores++;
        }

        System.out.println("b. Meses con cosecha superior al promedio: " + mesesSuperiores);
        System.out.println("c. Meses con cosecha inferior al promedio: " + mesesInferiores);

        int mesMayor = 0;
        for (int i = 1; i < 12; i++) {
            if (totalMes[i] > totalMes[mesMayor]) mesMayor = i;
        }
        System.out.println("d. Mes con mayor producción: " + meses[mesMayor] + " con " + totalMes[mesMayor] + " toneladas");
    }
}