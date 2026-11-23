package modelo;

import excepciones.HorarioConflictivoException;

/** Aula con disponibilidad por día y hora, y códigos de materia reservada. */
public class Aula {
    private String nombre;
    private int capacidad;
    private boolean[][] disponibilidad;
    private String[][] codigosMaterias;

    private static final String[] DIAS = {
            "Domingo", "Lunes", "Martes", "Miércoles",
            "Jueves", "Viernes", "Sábado"
    };

    /** Crea un aula con todos sus horarios inicialmente libres. */
    public Aula(String nombre, int capacidad) {
        if (capacidad < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa.");
        }

        this.nombre = nombre;
        this.capacidad = capacidad;
        this.disponibilidad = new boolean[7][24];
        this.codigosMaterias = new String[7][24];
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Reserva un bloque de horas sin asociarle un código de materia.
     *
     * @throws IllegalArgumentException si el día, la hora o la duración son inválidos
     * @throws HorarioConflictivoException si alguna casilla del bloque ya está reservada
     */
    public void reservar(int dia, int hora, int duracion)
            throws HorarioConflictivoException {
        reservar(dia, hora, duracion, null);
    }

    /**
     * Reserva un bloque de horas y asocia el código de materia indicado.
     *
     * @throws IllegalArgumentException si el día, la hora o la duración son inválidos
     * @throws HorarioConflictivoException si alguna casilla del bloque ya está reservada
     */
    public void reservar(int dia, int hora, int duracion, String codigoMateria)
            throws HorarioConflictivoException {
        validarBloque(dia, hora, duracion);

        // Primero se revisa el bloque completo, sin modificar la matriz.
        for (int horaActual = hora; horaActual < hora + duracion; horaActual++) {
            if (disponibilidad[dia][horaActual]) {
                throw new HorarioConflictivoException(
                        nombreDia(dia) + " " + horaActual
                                + ":00 ya esta reservado"
                );
            }
        }

        // Solo si todo el bloque está libre se reserva.
        for (int horaActual = hora; horaActual < hora + duracion; horaActual++) {
            disponibilidad[dia][horaActual] = true;
            codigosMaterias[dia][horaActual] = codigoMateria;
        }
    }

    /**
     * Libera un bloque completo de horas.
     *
     * @throws IllegalArgumentException si los datos son inválidos o alguna hora
     *                                  del bloque no está reservada
     */
    public void liberar(int dia, int hora, int duracion) {
        validarBloque(dia, hora, duracion);

        // Comprobar todo primero evita liberar solo una parte del bloque.
        for (int horaActual = hora; horaActual < hora + duracion; horaActual++) {
            if (!disponibilidad[dia][horaActual]) {
                throw new IllegalArgumentException(
                        "No se puede liberar: " + nombreDia(dia) + " "
                                + horaActual + ":00 no está reservado."
                );
            }
        }

        for (int horaActual = hora; horaActual < hora + duracion; horaActual++) {
            disponibilidad[dia][horaActual] = false;
            codigosMaterias[dia][horaActual] = null;
        }
    }

    /**
     * Indica si una hora está libre.
     *
     * @return true si está libre; false si está reservada
     */
    public boolean consultarDisponibilidad(int dia, int hora) {
        validarDiaYHora(dia, hora);
        return !disponibilidad[dia][hora];
    }

    /** Devuelve el código de la materia que reservó la casilla, o null si está libre. */
    public String materiaEn(int dia, int hora) {
        validarDiaYHora(dia, hora);
        return codigosMaterias[dia][hora];
    }

    /** Devuelve el nombre del día correspondiente al índice de 0 a 6. */
    public static String nombreDia(int dia) {
        if (dia < 0 || dia >= DIAS.length) {
            throw new IllegalArgumentException("El día debe estar entre 0 y 6.");
        }
        return DIAS[dia];
    }

    /** Imprime las horas reservadas de cada día o indica que el día está libre. */
    public void mostrarHorario() {
        for (int dia = 0; dia < disponibilidad.length; dia++) {
            StringBuilder horasReservadas = new StringBuilder();

            for (int hora = 0; hora < disponibilidad[dia].length; hora++) {
                if (disponibilidad[dia][hora]) {
                    if (horasReservadas.length() > 0) {
                        horasReservadas.append(", ");
                    }

                    horasReservadas.append(hora)
                            .append(":00");

                    String codigo = codigosMaterias[dia][hora];
                    if (codigo != null) {
                        horasReservadas.append(" (").append(codigo).append(")");
                    }
                }
            }

            if (horasReservadas.length() == 0) {
                System.out.println(nombreDia(dia) + ": libre");
            } else {
                System.out.println(nombreDia(dia) + ": " + horasReservadas);
            }
        }
    }

    /** Devuelve el nombre y la capacidad del aula. */
    @Override
    public String toString() {
        return "Aula{nombre='" + nombre + "', capacidad=" + capacidad + "}";
    }

    private void validarBloque(int dia, int hora, int duracion) {
        validarDiaYHora(dia, hora);

        if (duracion < 1) {
            throw new IllegalArgumentException(
                    "La duración debe ser de al menos una hora."
            );
        }
        if (hora + duracion > 24) {
            throw new IllegalArgumentException(
                    "La reserva no puede pasar de la medianoche."
            );
        }
    }

    private void validarDiaYHora(int dia, int hora) {
        if (dia < 0 || dia > 6) {
            throw new IllegalArgumentException("El día debe estar entre 0 y 6.");
        }
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23.");
        }
    }
}
