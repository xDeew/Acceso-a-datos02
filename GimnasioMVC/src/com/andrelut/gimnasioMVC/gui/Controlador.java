package com.andrelut.gimnasioMVC.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {
    private final Modelo modelo;
    private final Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        iniciarControlador();
        addActionListeners();
        addItemListeners();
        addWindowsListerners();
        actualizarListaClientes();

    }


    private void iniciarControlador() {
        DocumentListener validadorEmail = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                validarEmail();
            }

            public void removeUpdate(DocumentEvent e) {
                validarEmail();
            }

            public void insertUpdate(DocumentEvent e) {
                validarEmail();
            }
        };
        vista.getTxtEmail().getDocument().addDocumentListener(validadorEmail);

        DocumentListener validadorTelefono = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                validarTelefono();
            }

            public void removeUpdate(DocumentEvent e) {
                validarTelefono();
            }

            public void insertUpdate(DocumentEvent e) {
                validarTelefono();
            }
        };
        vista.getTxtTelefono().getDocument().addDocumentListener(validadorTelefono);


    }

    private void validarTelefono() {
        String telefono = vista.getTxtTelefono().getText();
        if ((telefono.startsWith("8") || telefono.startsWith("9")) && telefono.length() == 9) {
            vista.getTxtTelefono().setForeground(new Color(0, 100, 0));
        } else if (telefono.startsWith("6") && telefono.length() == 9) {
            vista.getTxtTelefono().setForeground(new Color(0, 100, 0));
        } else {
            vista.getTxtTelefono().setForeground(Color.RED);
        }
    }

    private void validarEmail() {
        String email = vista.getTxtEmail().getText();
        if (email.contains("@") && email.contains(".")) {
            vista.getTxtEmail().setForeground(new Color(0, 100, 0));
        } else {
            vista.getTxtEmail().setForeground(Color.RED);
        }
    }

    private void addItemListeners() {
        vista.comboClientesRegistrados.addItemListener(this);
        vista.txtPrecio.setEditable(false);
        vista.comboTipoSuscripcion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String tipoSeleccionado = (String) e.getItem();
                    double precio = modelo.obtenerPrecioPorTipoSuscripcion(tipoSeleccionado);
                    vista.txtPrecio.setText(String.format("%.2f", precio));

                }
            }
        });

    }

    private void addActionListeners() {
        vista.btnAddCliente.addActionListener(this);
        vista.btnModificarCliente.addActionListener(this);
        vista.btnEliminarCliente.addActionListener(this);


        // Configurando los ActionCommands para cada botón
        vista.btnAddCliente.setActionCommand("AñadirCliente");
        vista.btnModificarCliente.setActionCommand("ModificarCliente");
        vista.btnEliminarCliente.setActionCommand("EliminarCliente");
    }

    private void addWindowsListerners() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "AñadirCliente":
                if (hayCamposVacios()) JOptionPane.showMessageDialog(vista.frame, "Hay campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);

                else if (!vista.getTxtEmail().getForeground().equals(new Color(0, 100, 0)) || !vista.getTxtTelefono().getForeground().equals(new Color(0, 100, 0))) {
                    JOptionPane.showMessageDialog(vista.frame, "Email o teléfono incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    modelo.insertarCliente(vista.txtNombre.getText(), vista.txtApellido.getText(), vista.fechaNacimiento.getDate(), vista.txtEmail.getText(), vista.txtTelefono.getText(), vista.txtDireccion.getText());
                    JOptionPane.showMessageDialog(vista.frame, "Cliente añadido correctamente");
                    actualizarListaClientes();
                    limpiarCampos();

                }
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

    private void limpiarCampos() {
        vista.txtNombre.setText("");
        vista.txtApellido.setText("");
        vista.txtEmail.setText("");
        vista.txtTelefono.setText("");
        vista.fechaNacimiento.setDate(null);
        vista.txtDireccion.setText("");

        vista.fechaInicio.clear();
        vista.fechaFin.clear();
    }

    private boolean hayCamposVacios() {
        return vista.txtNombre.getText().isEmpty() || vista.txtApellido.getText().isEmpty() || vista.txtEmail.getText().isEmpty() || vista.txtTelefono.getText().isEmpty() || vista.txtDireccion.getText().isEmpty();
    }

    private void actualizarListaClientes() {
        List<String> nombresClientes = modelo.obtenerNombresClientes();
        vista.comboClientesRegistrados.removeAllItems();

        vista.comboClientesRegistrados.addItem(null);

        for (String nombreCompleto : nombresClientes) {
            vista.comboClientesRegistrados.addItem(nombreCompleto);
        }

        vista.comboClientesRegistrados.setSelectedIndex(-1);
        vista.comboClientesRegistrados.addItemListener(this);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vista.comboClientesRegistrados) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                // siempre limpia los campos cuando algo se deselecciona
                limpiarCampos();
            } else {
                String clienteSeleccionado = (String) vista.comboClientesRegistrados.getSelectedItem();

                // Un cliente ha sido seleccionado, obtén y muestra sus datos
                Map<String, Object> datosCliente = modelo.obtenerDatosCliente(clienteSeleccionado);
                vista.txtNombre.setText((String) datosCliente.get("nombre"));
                vista.txtApellido.setText((String) datosCliente.get("apellido"));
                vista.txtEmail.setText((String) datosCliente.get("email"));
                vista.txtTelefono.setText((String) datosCliente.get("telefono"));
                // Asumiendo que el componente de fecha es compatible con LocalDate
                vista.fechaNacimiento.setDate((LocalDate) datosCliente.get("fecha_nacimiento"));
                vista.txtDireccion.setText((String) datosCliente.get("direccion"));

                // configura las fechas de inicio y fin
                vista.fechaInicio.setDateToToday();
                LocalDate fechaInicio = LocalDate.now();
                LocalDate fechaFin = fechaInicio.plusYears(1);
                vista.fechaFin.setDate(fechaFin);

            }
        }
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