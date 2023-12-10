package com.andrelut.gimnasioMVC.main;

import com.andrelut.gimnasioMVC.gui.Modelo;
import com.andrelut.gimnasioMVC.gui.Vista;

public class Main {
    public static void main(String[] args) {
        // Instanciar el modelo que maneja la base de datos
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        modelo.desconectar();
    }
}
