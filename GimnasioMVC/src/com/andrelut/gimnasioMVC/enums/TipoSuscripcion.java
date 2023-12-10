package com.andrelut.gimnasioMVC.enums;

public enum TipoSuscripcion {
    BASICA("Básica"),
    PREMIUM("Premium"),
    FAMILIAR("Familiar"),
    ESTUDIANTE("Estudiante");

    private String valor;

    TipoSuscripcion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}