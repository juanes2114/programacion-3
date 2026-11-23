package util;

import estructuras.Pila;

/** Permite navegar hacia atrás y adelante entre reportes de texto. */
public class NavegadorReportes {
    private Pila<String> pilaAtras;
    private Pila<String> pilaAdelante;
    private String reporteActual;

    /** Crea un navegador sin reportes. */
    public NavegadorReportes() {
        pilaAtras = new Pila<>();
        pilaAdelante = new Pila<>();
        reporteActual = null;
    }

    /**
     * Muestra un reporte y lo establece como actual.
     * Un reporte nuevo inicia un recorrido nuevo, por eso limpia la pila adelante.
     */
    public void mostrarReporte(String contenido) {
        if (contenido == null) {
            throw new IllegalArgumentException(
                    "El contenido del reporte no puede ser null."
            );
        }

        if (reporteActual != null) {
            pilaAtras.push(reporteActual);
        }

        pilaAdelante.vaciar();
        reporteActual = contenido;
        System.out.println(reporteActual);
    }

    /** Vuelve al reporte anterior y lo imprime. */
    public void irAtras() {
        if (!hayAnterior()) {
            throw new IllegalStateException("No hay reportes anteriores");
        }

        pilaAdelante.push(reporteActual);
        reporteActual = pilaAtras.pop();
        System.out.println(reporteActual);
    }

    /** Avanza al siguiente reporte y lo imprime. */
    public void irAdelante() {
        if (!hayAdelante()) {
            throw new IllegalStateException("No hay reportes siguientes");
        }

        pilaAtras.push(reporteActual);
        reporteActual = pilaAdelante.pop();
        System.out.println(reporteActual);
    }

    public boolean hayAnterior() {
        return !pilaAtras.estaVacia();
    }

    public boolean hayAdelante() {
        return !pilaAdelante.estaVacia();
    }

    public int cantidadAtras() {
        return pilaAtras.tamano();
    }

    public int cantidadAdelante() {
        return pilaAdelante.tamano();
    }

    public String getReporteActual() {
        return reporteActual;
    }
}
