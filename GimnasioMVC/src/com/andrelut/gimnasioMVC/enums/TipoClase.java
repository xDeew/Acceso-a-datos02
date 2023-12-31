package com.andrelut.gimnasioMVC.enums;

public enum TipoClase {
    AEROBICO("Aerobico", 30, 60, TipoEquipamiento.CINTA_CORRER),
    MUSCULACION("Musculacion", 15, 60, TipoEquipamiento.PESAS_LIBRES),
    CROSSFIT("Crossfit", 20, 50, TipoEquipamiento.EQUIPAMIENTO_CROSSFIT),
    YOGA("Yoga", 25, 60, TipoEquipamiento.ESTERILLA_YOGA),
    PILATES("Pilates", 20, 60, TipoEquipamiento.REFORMER_PILATES),
    ZUMBA("Zumba", 30, 60, TipoEquipamiento.SISTEMA_SONIDO),
    GIMNASIA("Gimnasia", 15, 60, TipoEquipamiento.EQUIPAMIENTO_GIMNASIA),
    SPINNING("Spinning", 25, 45, TipoEquipamiento.BICICLETA_SPINNING);

    private String nombre;
    private int capacidad;
    private int duracion;
    private final TipoEquipamiento equipamiento;

    TipoClase(String nombre, int capacidad, int duracion, TipoEquipamiento equipamiento) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.duracion = duracion;
        this.equipamiento = equipamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getDuracion() {
        return duracion;
    }

    public TipoEquipamiento getEquipamiento() {
        return equipamiento;
    }
}
