package com.andrelut.gimnasioMVC.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {
    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    private void addActionListerners(ActionListener e) {


    }

    private void addWindowsListerners() {

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "AñadirCliente":
                break;
            case "ModificarCliente":
                break;
            case "EliminarCliente":
                break;
            case "AñadirSuscripcion":
                break;
            case "ModificarSuscripcion":
                break;
            case "EliminarSuscripcion":
                break;
            case "AñadirClase":
                break;
            case "ModificarClase":
                break;
            case "EliminarClase":
                break;
            case "AñadirEntrenador":
                break;
            case "ModificarEntrenador":
                break;
            case "EliminarEntrenador":
                break;
            case "AñadirEquipamiento":
                break;
            case "ModificarEquipamiento":
                break;
            case "EliminarEquipamiento":
                break;
            case "RegistrarMantenimiento":
                break;
            case "ModificarMantenimiento":
                break;
            case "EliminarMantenimiento":
                break;

        }
    }


    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
