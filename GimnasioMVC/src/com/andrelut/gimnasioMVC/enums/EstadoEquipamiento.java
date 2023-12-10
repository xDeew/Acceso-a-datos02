package com.andrelut.gimnasioMVC.enums;

public enum EstadoEquipamiento {
    NUEVO("Nuevo"),
    USADO("Usado"),
    DEFECTUOSO("Defectuoso"),
    REPARADO("Reparado");

    private String valor;

    EstadoEquipamiento(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
