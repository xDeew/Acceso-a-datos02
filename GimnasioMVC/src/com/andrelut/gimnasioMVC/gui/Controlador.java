package com.andrelut.gimnasioMVC.gui;

import com.andrelut.gimnasioMVC.enums.EstadoPago;
import com.andrelut.gimnasioMVC.util.Util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {


    private final Modelo modelo;
    private final Vista vista;
    private final LocalDate fechaHoy;
    boolean refrescar;


    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        fechaHoy = LocalDate.now();
        modelo.conectar();
        setOptions();
        addActionListeners(this);
        addItemListeners(this);
        addWindowListeners(this);
        refrescarTodo();
        iniciarControlador();


    }


    private void actualizarFechaInicioFin() {
        vista.fechaInicio.setDate(fechaHoy);
        LocalDate fechaFin = fechaHoy.plusYears(1);
        vista.fechaFin.setDate(fechaFin);
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


        vista.clientesTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = vista.clientesTabla.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    int row = vista.clientesTabla.getSelectedRow();
                    vista.txtNombre.setText(String.valueOf(vista.clientesTabla.getValueAt(row, 1)));
                    vista.txtApellido.setText(String.valueOf(vista.clientesTabla.getValueAt(row, 2)));
                    vista.fechaNacimiento.setDate((Date.valueOf(String.valueOf(vista.clientesTabla.getValueAt(row, 3)))).toLocalDate());
                    vista.txtEmail.setText(String.valueOf(vista.clientesTabla.getValueAt(row, 4)));
                    vista.txtTelefono.setText(String.valueOf(vista.clientesTabla.getValueAt(row, 5)));
                    vista.txtDireccion.setText(String.valueOf(vista.clientesTabla.getValueAt(row, 6)));

                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.clientesTabla.getSelectionModel())) {
                        limpiarCamposClientes();
                    }

                }
            }

        });
    }


    private void validarEmail() {
        String email = vista.getTxtEmail().getText();
        if (email.contains("@") && email.contains(".")) {
            vista.getTxtEmail().setForeground(new Color(0, 100, 0));
        } else {
            vista.getTxtEmail().setForeground(Color.RED);
        }
    }

    private void addItemListeners(ItemListener listener) {
        vista.comboClientesRegistrados.addItemListener(listener);
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

    private void addActionListeners(ActionListener listener) {
        vista.btnAddCliente.addActionListener(listener);
        vista.btnModificarCliente.addActionListener(listener);
        vista.btnEliminarCliente.addActionListener(listener);
        vista.btnAddSuscripciones.addActionListener(listener);
        vista.btnModificarSuscripciones.addActionListener(listener);
        vista.btnDeleteSuscripciones.addActionListener(listener);
        vista.btnGananciaMensual.addActionListener(listener);
        vista.itemOpciones.addActionListener(listener);
        vista.itemConexion.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.btnValidate.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);

    }

    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        List<String> comandosNoRequierenConexion = Arrays.asList("conectar", "salir", "abrirOpciones", "guardarOpciones");
        if (!comandosNoRequierenConexion.contains(command) && !modelo.estaConectado()) {
            JOptionPane.showMessageDialog(vista.frame, "Para poder realizar esta operación necesita estar conectado con la base de datos.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (command) {
            case "opciones":
                vista.adminPasswordDialog.setVisible(true);
                break;
            case "desconectar":
                modelo.desconectar();
                JOptionPane.showMessageDialog(vista.frame, "Desconectado de la base de datos.");
                vista.itemConexion.setText("Conectar");
                vista.itemConexion.setActionCommand("conectar");
                vista.comboClientesRegistrados.setEnabled(false);

                break;
            case "conectar":
                modelo.conectar();
                JOptionPane.showMessageDialog(vista.frame, "Conectado a la base de datos.");
                vista.itemConexion.setText("Desconectar");
                vista.itemConexion.setActionCommand("desconectar");
                vista.comboClientesRegistrados.setEnabled(true);

                break;
            case "salir":
                System.exit(0);
                break;
            case "abrirOpciones":
                if (String.valueOf(vista.adminPassword.getPassword()).equals(modelo.getAdminPassword())) {
                    vista.adminPassword.setText("");
                    vista.adminPasswordDialog.dispose();
                    vista.optionDialog.setVisible(true);
                } else {
                    Util.showErrorAlert("La contraseña introducida no es correcta.");
                }
                break;
            case "guardarOpciones":
                modelo.setPropValues(vista.optionDialog.txtIP.getText(), vista.optionDialog.txtUser.getText(),
                        String.valueOf(vista.optionDialog.pfPass.getPassword()), String.valueOf(vista.optionDialog.pfAdmin.getPassword()));
                vista.optionDialog.dispose();
                vista.dispose();
                new Controlador(new Modelo(), new Vista());
                break;

            case "añadirCliente":
                if (hayCamposVacios())
                    JOptionPane.showMessageDialog(vista.frame, "Hay campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);

                else if (!vista.getTxtEmail().getForeground().equals(new Color(0, 100, 0))) {
                    JOptionPane.showMessageDialog(vista.frame, "Email no válido", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (vista.getTxtTelefono().getText().length() != 9) {
                    JOptionPane.showMessageDialog(vista.frame, "Teléfono no válido, ha de tener un máximo de 9 dígitos", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (modelo.existeEmail(vista.txtEmail.getText())) {
                    JOptionPane.showMessageDialog(vista.frame, "Email ya registrado", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    modelo.insertarCliente(vista.txtNombre.getText(), vista.txtApellido.getText(), vista.fechaNacimiento.getDate(), vista.txtEmail.getText(), vista.txtTelefono.getText(), vista.txtDireccion.getText());
                    JOptionPane.showMessageDialog(vista.frame, "Cliente añadido correctamente");
                    actualizarListaClientes();
                    refrescarClientes();
                    limpiarCamposClientes();

                }
                break;
            case "modificarCliente":
                break;
            case "eliminarCliente":
                break;
            case "añadirSuscripcion":
                if (hayCamposVaciosSuscripciones()) {
                    JOptionPane.showMessageDialog(vista.frame, "Hay campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDate fechaInicio = vista.fechaInicio.getDate();
                LocalDate fechaFin = vista.fechaFin.getDate();
                boolean pagado = vista.comboPagado.getSelectedItem().toString().equals(EstadoPago.PAGADO.getValor());
                String tipoSuscripcion = vista.comboTipoSuscripcion.getSelectedItem().toString();
                double precio = Double.parseDouble(vista.txtPrecio.getText().replace(",", "."));
                String nombreCliente = (String) vista.comboClientesRegistrados.getSelectedItem();
                if (nombreCliente == null) {
                    JOptionPane.showMessageDialog(vista.frame, "Cliente no seleccionado");
                    return;
                }
                int idCliente = modelo.obtenerIdClientePorNombre(nombreCliente);
                if (idCliente == -1) {
                    JOptionPane.showMessageDialog(vista.frame, "Cliente no encontrado");
                } else if (modelo.tieneSuscripcionActiva(idCliente)) {
                    JOptionPane.showMessageDialog(vista.frame, "Este cliente ya tiene una suscripción activa");
                    limpiarCamposSuscripciones();
                } else {
                    modelo.insertarSuscripcion(fechaInicio, fechaFin, pagado, tipoSuscripcion, precio, idCliente);
                    JOptionPane.showMessageDialog(vista.frame, "Suscripción añadida correctamente");
                    refrescarSuscripciones();
                    limpiarCamposSuscripciones();
                }
                break;

            case "modificarSuscripcion":
                break;
            case "eliminarSuscripcion":
                break;
            case "añadirClase":
                break;
            case "modificarClase":
                break;
            case "eliminarClase":
                break;
            case "añadirEntrenador":
                break;
            case "modificarEntrenador":
                break;
            case "eliminarEntrenador":
                break;
            case "añadirEquipamiento":
                break;
            case "modificarEquipamiento":
                break;
            case "eliminarEquipamiento":
                break;
            case "registrarMantenimiento":
                break;
            case "modificarMantenimiento":
                break;
            case "eliminarMantenimiento":
                break;
            case "gananciaMensual":
                List<Map<String, Object>> datosClientes = modelo.obtenerTodosLosClientesYSusSuscripciones();
                double gananciasTotales = 0.0;
                double gananciasEnEspera = 0.0;
                for (Map<String, Object> datosCliente : datosClientes) {
                    tipoSuscripcion = (String) datosCliente.get("tipo");
                    precio = (Double) datosCliente.get("precio");
                    boolean estadoPago = (Boolean) datosCliente.get("estado_pago");
                    if (estadoPago) {
                        gananciasTotales += modelo.obtenerGananciasPorClienteAdd(1, tipoSuscripcion);
                    } else {
                        gananciasEnEspera += modelo.obtenerGananciasPorClienteAdd(1, tipoSuscripcion);
                    }
                }
                if (gananciasTotales == 0.0 && gananciasEnEspera == 0.0) {
                    JOptionPane.showMessageDialog(vista.frame, "No hay suscripciones");
                } else {
                    JOptionPane.showMessageDialog(
                            vista.frame,
                            "<html><body><p style='width: 200px;'>" +
                                    "Las ganancias mensuales actuales ascienden a: <b>" + String.format("%.2f", gananciasTotales) + "</b> euros." +
                                    "<br><br>Adicionalmente, las ganancias correspondientes a las suscripciones pendientes de pago suman: <b>" + String.format("%.2f", gananciasEnEspera) + "</b> euros." +
                                    "</p></body></html>",
                            "Informe de Ganancias",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                break;

        }
    }

    private boolean hayCamposVaciosSuscripciones() {
        return vista.fechaInicio.getDate() == null || vista.fechaFin.getDate() == null || vista.comboPagado.getSelectedIndex() == -1 || vista.comboTipoSuscripcion.getSelectedIndex() == -1 || vista.txtPrecio.getText().isEmpty();
    }

    private void setOptions() {
        vista.optionDialog.txtIP.setText(modelo.getIp());
        vista.optionDialog.txtUser.setText(modelo.getUser());
        vista.optionDialog.pfPass.setText(modelo.getPassword());
        vista.optionDialog.pfAdmin.setText(modelo.getAdminPassword());
    }

    private void limpiarCamposSuscripciones() {
        vista.comboClientesRegistrados.setSelectedIndex(-1);
        vista.fechaInicio.clear();
        vista.fechaFin.clear();
        vista.comboPagado.setSelectedIndex(-1);
        vista.comboTipoSuscripcion.setSelectedIndex(-1);
        vista.txtPrecio.setText("");
    }

    private void limpiarCamposClientes() {
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
                limpiarCamposClientes();
            } else {
                String clienteSeleccionado = (String) vista.comboClientesRegistrados.getSelectedItem();

                // Un cliente ha sido seleccionado, obtén y muestra sus datos
                Map<String, Object> datosCliente = modelo.obtenerDatosCliente(clienteSeleccionado);
                vista.txtNombre.setText((String) datosCliente.get("nombre"));
                vista.txtApellido.setText((String) datosCliente.get("apellido"));
                vista.txtEmail.setText((String) datosCliente.get("email"));
                vista.txtTelefono.setText((String) datosCliente.get("telefono"));
                vista.fechaNacimiento.setDate((LocalDate) datosCliente.get("fecha_nacimiento"));
                vista.txtDireccion.setText((String) datosCliente.get("direccion"));

                // configura las fechas de inicio y fin
                actualizarFechaInicioFin();

            }
        }
    }

    public void refrescarClientes() {
        try {
            ResultSet rs = modelo.consultarClientes();
            vista.dtmClientes = construirTableModelClientes(rs);
            vista.clientesTabla.setModel(vista.dtmClientes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refrescarSuscripciones() {
        try {
            ResultSet rs = modelo.consultarSuscripciones();
            vista.dtmSuscripciones = construirTableModelSuscripciones(rs);
            vista.suscripcionesTabla.setModel(vista.dtmSuscripciones);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private DefaultTableModel construirTableModelSuscripciones(ResultSet rs) throws SQLException {
        // Obtiene los metadatos de la consulta
        ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Nombres de las columnas
        Vector<String> columnNames = new java.util.Vector<String>();
        int columnCount = 0;
        try {
            columnCount = metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Datos de la tabla
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmSuscripciones.setDataVector(data, columnNames);

        return vista.dtmSuscripciones;
    }

    private void refrescarTodo() {
        actualizarListaClientes();
        refrescarClientes();
        refrescarSuscripciones();
        refrescar = false;

    }

    private DefaultTableModel construirTableModelClientes(ResultSet rs) throws SQLException {

        // Obtiene los metadatos de la consulta
        ResultSetMetaData metaData = rs.getMetaData();

        // Nombres de las columnas
        Vector<String> columnNames = new java.util.Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Datos de la tabla
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmClientes.setDataVector(data, columnNames);

        return vista.dtmClientes;


    }

    private void setDataVector(ResultSet rs, int columnCount, Vector<Vector<Object>> data) throws SQLException {
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
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