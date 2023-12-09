package com.andrelut.gimnasioMVC.main;

import com.andrelut.gimnasioMVC.gui.Modelo;

public class Main {
    public static void main(String[] args) {
        // Instanciar el modelo que maneja la base de datos
        Modelo modelo = new Modelo();

        // Esta llamada es implícita en el constructor de Modelo,
        // pero puedes llamarla explícitamente si moviste la llamada fuera del constructor.
        // modelo.conectar();

        modelo.desconectar();
    }
}
