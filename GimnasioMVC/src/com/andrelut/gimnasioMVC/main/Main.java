package com.andrelut.gimnasioMVC.main;

import com.andrelut.gimnasioMVC.gui.Controlador;
import com.andrelut.gimnasioMVC.gui.Modelo;
import com.andrelut.gimnasioMVC.gui.Vista;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);

    }
}
