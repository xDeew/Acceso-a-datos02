package com.andrelut.gimnasioMVC.enums;

public enum EstadoPago {
    PAGADO("Pagado"),
    PENDIENTE("Pendiente");

    private final String valor;

    EstadoPago(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}