package com.andrelut.gimnasioMVC.enums;

public enum TipoEquipamiento {
    CINTA_CORRER("Cinta de Correr"),
    PESAS_LIBRES("Pesas Libres"),
    EQUIPAMIENTO_CROSSFIT("Equipamiento CrossFit"),
    ESTERILLA_YOGA("Esterilla de Yoga"),
    REFORMER_PILATES("Reformer de Pilates"),
    SISTEMA_SONIDO("Sistema de Sonido"), //zumba
    EQUIPAMIENTO_GIMNASIA("Equipamiento de Gimnasia"),
    BICICLETA_SPINNING("Bicicleta de Spinning");

    private final String descripcion;

    TipoEquipamiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
