package com.andrelut.gimnasioMVC.gui;

import com.andrelut.gimnasioMVC.enums.TipoSuscripcion;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;

public class Vista {
    public JTabbedPane tabbedPane1;
    public JPanel panel1;
    public JPanel JPanelClientes;
    public JPanel JPanelSuscripciones;
    public JPanel JPanelClases;
    public JPanel JPanelEntrenadores;
    public JPanel JPanelEquipamiento;
    public JPanel JPanelMantenimientos;
    public JTextField txtNombre;
    public JTextField txtApellido;
    public JTextField txtEmail;
    public JTextField txtDireccion;
    public DatePicker fechaNacimiento;
    public JComboBox comboClientesRegistrados;
    public DatePicker fechaInicio;
    public DatePicker fechaFin;
    public JComboBox comboEstadoPago;
    public JComboBox comboPagado;
    public JFrame frame;
    private JComboBox<TipoSuscripcion> comboTipoSuscripcion;
    private JTextField txtPrecio;

    public Vista() {
        frame = new JFrame("Vista");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void inicializarComponentesSuscripcion() {
        // Inicialización del JComboBox para los tipos de suscripción
        comboTipoSuscripcion = new JComboBox<>();
        for (TipoSuscripcion tipo : TipoSuscripcion.values()) {
            comboTipoSuscripcion.addItem(tipo);
        }

        // Inicialización del JTextField para el precio
        txtPrecio = new JTextField();
        txtPrecio.setEditable(false); // El precio no se edita manualmente

        // Añadir un ActionListener al JComboBox
        comboTipoSuscripcion.addActionListener(e -> {
            TipoSuscripcion tipoSeleccionado = (TipoSuscripcion) comboTipoSuscripcion.getSelectedItem();
            if (tipoSeleccionado != null) {
                txtPrecio.setText(String.format("%.2f", tipoSeleccionado.getPrecio()));
            }
        });

        // Establece el precio inicial basado en la selección predeterminada del JComboBox
        comboTipoSuscripcion.setSelectedIndex(0);

        // ... (resto del método de inicialización)
    }

    // ... (resto de la clase Vista)
}

