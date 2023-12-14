package com.andrelut.gimnasioMVC.main;

import com.andrelut.gimnasioMVC.gui.Controlador;
import com.andrelut.gimnasioMVC.gui.Modelo;
import com.andrelut.gimnasioMVC.gui.Vista;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            Vista vista = new Vista();
            Modelo modelo = new Modelo();
            Controlador controlador = new Controlador(modelo, vista);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
