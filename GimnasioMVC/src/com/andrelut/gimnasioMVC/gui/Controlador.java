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
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {


    private final Modelo modelo;
    private final Vista vista;
    private final LocalDate fechaHoy;
    boolean refrescar;

    /**
     * Constructor del controlador
     * <p>
     * - Conecta con la base de datos
     * <p>
     * - Inicializa la fecha de hoy, que se usa para rellenar los campos de fecha de inicio y fin de suscripción
     * <p>
     * - Fija las opciones de la ventana de opciones (IP, usuario, contraseña, contraseña de administrador)
     * <p>
     * - Añade listeners a los botones y campos de texto
     * <p>
     * - Refresca las tablas
     * <p>
     * - Inicia el controlador
     *
     * @param modelo
     * @param vista
     */
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
        iniciar();


    }


    private void actualizarFechaInicioFin() {
        vista.fechaInicio.setDate(fechaHoy);
        LocalDate fechaFin = fechaHoy.plusYears(1);
        vista.fechaFin.setDate(fechaFin);
    }

    /**
     * Inicia el controlador
     * <p>
     * - Añade un listener al campo de email para validar que sea un email válido
     * <p>
     * - Añade un listener a la tabla de clientes para que cuando se seleccione una fila, se rellenen los campos con los datos de ese cliente
     * <p>
     * - Añade un listener a los botones de añadir, modificar y eliminar cliente para que se realicen las operaciones correspondientes
     */
    private void iniciar() {
        // es útil para validar o procesar el texto mientras el usuario lo está escribiendo
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

                } else if (e.getValueIsAdjusting() && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.clientesTabla.getSelectionModel())) {
                        limpiarCamposClientes();
                    }

                }
            }

        });
    }

    /**
     * Valida que el email introducido sea válido
     * <p>
     * Si el email contiene un @ y un ., el texto se pone en verde, si no, en rojo
     */
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

        vista.txtAforoMaximo.setEditable(false);
        vista.txtDuracionClase.setEditable(false);
        vista.txtMaterialUtilizado.setEditable(false);
        vista.txtInstructorAsignado.setEditable(false);

        vista.comboTiposClases.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String tipoClaseSeleccionado = (String) e.getItem();

                    int capacidad = modelo.obtenerCapacidadPorTipoClase(tipoClaseSeleccionado);
                    int duracion = modelo.obtenerDuracionPorTipoClase(tipoClaseSeleccionado);
                    String nombreEntrenador = modelo.obtenerEntrenadorPorTipoClase(tipoClaseSeleccionado);
                    String material = modelo.obtenerMaterialPorTipoClase(tipoClaseSeleccionado);

                    vista.txtAforoMaximo.setText(String.valueOf(capacidad));
                    vista.txtDuracionClase.setText(String.valueOf(duracion));
                    vista.txtInstructorAsignado.setText(nombreEntrenador);
                    vista.txtMaterialUtilizado.setText(material);
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
        vista.btnAddEntrenador.addActionListener(listener);
        vista.btnModificarEntrenador.addActionListener(listener);
        vista.btnEliminarEntrenador.addActionListener(listener);
        vista.btnAddClase.addActionListener(listener);
        vista.btnAddEquipamiento.addActionListener(listener);

    }

    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        List<String> comandosNoRequierenConexion = Arrays.asList("conectar", "salir", "abrirOpciones", "guardarOpciones", "opciones");
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
                modelo.setPropValues(vista.optionDialog.txtIP.getText(), vista.optionDialog.txtUser.getText(), String.valueOf(vista.optionDialog.pfPass.getPassword()), String.valueOf(vista.optionDialog.pfAdmin.getPassword()));
                vista.optionDialog.dispose();
                vista.dispose();
                new Controlador(new Modelo(), new Vista());
                break;

            case "añadirCliente":
                if (hayCamposVaciosCliente()) Util.showErrorAlert("Rellene todos los campos");

                else if (!vista.getTxtEmail().getForeground().equals(new Color(0, 100, 0))) {
                    JOptionPane.showMessageDialog(vista.frame, "Email no válido", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (vista.getTxtTelefono().getText().replace(" ", "").length() != 9) {
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
                if (hayCamposVaciosCliente()) {
                    Util.showErrorAlert("Rellene todos los campos");
                    vista.clientesTabla.clearSelection();
                } else {
                    int selectedRow = vista.clientesTabla.getSelectedRow();
                    if (selectedRow != -1) {
                        int idCliente = Integer.parseInt(vista.clientesTabla.getValueAt(selectedRow, 0).toString());
                        modelo.modificarCliente(vista.txtNombre.getText(),
                                vista.txtApellido.getText(),
                                vista.fechaNacimiento.getDate(),
                                vista.txtEmail.getText(),
                                vista.txtTelefono.getText(),
                                vista.txtDireccion.getText(), idCliente);
                        limpiarCamposClientes();
                        refrescarClientes();
                    } else {
                        JOptionPane.showMessageDialog(vista.frame, "Por favor, seleccione un cliente de la tabla antes de modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "eliminarCliente":
                break;
            case "añadirSuscripcion":
                if (hayCamposVaciosSuscripciones()) {
                    Util.showErrorAlert("Rellene todos los campos");
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
                if (hayCamposVaciosClases()) {
                    Util.showErrorAlert("Rellene todos los campos");
                    return;
                } else if (vista.txtInstructorAsignado.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(vista.frame, "No hay entrenador asignado", "Error", JOptionPane.ERROR_MESSAGE);
                    limpiarCamposClases();
                } else if (vista.txtMaterialUtilizado.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(vista.frame, "No hay material asignado", "Error", JOptionPane.ERROR_MESSAGE);
                    limpiarCamposClases();
                } else {
                    String tipoClase = vista.comboTiposClases.getSelectedItem().toString();
                    int aforoMaximo = Integer.parseInt(vista.txtAforoMaximo.getText());
                    int minutos = Integer.parseInt(vista.txtDuracionClase.getText());
                    Time duracion = Time.valueOf(LocalTime.MIN.plusMinutes(minutos));
                    String nombreEntrenadorClase = vista.txtInstructorAsignado.getText();
                    String materialUtilizado = vista.txtMaterialUtilizado.getText();
                    int idEntrenador = modelo.obtenerIdEntrenadorPorNombre(nombreEntrenadorClase);
                    int idEquipamiento = modelo.obtenerIdEquipamientoPorTipoClase(tipoClase);


                    if (idEntrenador == -1) {
                        JOptionPane.showMessageDialog(vista.frame, "Entrenador no encontrado");
                        return;
                    } else if (modelo.existeClaseAsignada(tipoClase)) {
                        JOptionPane.showMessageDialog(vista.frame, "Este entrenador ya tiene una clase asignada");
                        return;
                    } else {
                        try {
                            modelo.insertarClase(tipoClase, aforoMaximo, duracion, nombreEntrenadorClase, materialUtilizado, idEntrenador, idEquipamiento);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(vista.frame, "Clase añadida correctamente");
                        limpiarCamposClases();
                        refrescarClases();
                    }

                }
                break;
            case "modificarClase":
                break;
            case "eliminarClase":
                break;
            case "añadirEntrenador":
                if (hayCamposVaciosEntrenador()) {
                    Util.showErrorAlert("Rellene todos los campos");
                    return;
                }
                String nombreEntrenador = vista.txtNombreEntrenador.getText();
                String especialidad = vista.comboEspecialidadEntrenador.getSelectedItem().toString();
                LocalDate fechaContratacionEntrenador = vista.fechaContratacion.getDate();
                double salario = Double.parseDouble(vista.txtSalario.getText().replace(",", "."));
                if (modelo.existeEntrenadorEnClase(especialidad)) {
                    JOptionPane.showMessageDialog(vista.frame, "Esta clase ya tiene entrenador asignado");
                    return;
                } else {
                    modelo.insertarEntrenador(nombreEntrenador, especialidad, fechaContratacionEntrenador, salario);
                    JOptionPane.showMessageDialog(vista.frame, "Entrenador añadido correctamente");
                    refrescarEntrenadores();
                }
                break;
            case "modificarEntrenador":
                break;
            case "eliminarEntrenador":
                break;
            case "añadirEquipamiento":
                if (hayCamposVaciosEquipamiento()) {
                    Util.showErrorAlert("Rellene todos los campos");
                    return;
                } else if (modelo.existeEquipamiento(vista.comboEquipamiento.getSelectedItem().toString())) {
                    JOptionPane.showMessageDialog(vista.frame, "Este equipamiento ya está registrado");
                    return;
                } else {
                    modelo.insertarEquipamiento(vista.comboEquipamiento.getSelectedItem().toString(), vista.txtMarcaEquipamiento.getText(), vista.fechaCompraEquipamiento.getDate(), Double.parseDouble(vista.txtCostoEquipamiento.getText().replace(",", ".")), vista.comboEstadoEquipamiento.getSelectedItem().toString());
                    limpiarCamposEquipamiento();
                    refrescarEquipamiento();
                }

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
                    JOptionPane.showMessageDialog(vista.frame, "<html><body><p style='width: 200px;'>" + "Las ganancias mensuales actuales ascienden a: <b>" + String.format("%.2f", gananciasTotales) + "</b> euros." + "<br><br>Adicionalmente, las ganancias correspondientes a las suscripciones pendientes de pago suman: <b>" + String.format("%.2f", gananciasEnEspera) + "</b> euros." + "</p></body></html>", "Informe de Ganancias", JOptionPane.INFORMATION_MESSAGE);
                }
                break;

        }
    }

    private boolean existeCliente() {
        return modelo.obtenerIdClientePorNombre(vista.txtNombre.getText() + " " + vista.txtApellido.getText()) == -1;
    }

    private void limpiarCamposEquipamiento() {
        vista.comboEquipamiento.setSelectedIndex(-1);
        vista.txtMarcaEquipamiento.setText("");
        vista.fechaCompraEquipamiento.setDate(null);
        vista.txtCostoEquipamiento.setText("");
        vista.comboEstadoEquipamiento.setSelectedIndex(-1);
    }

    private boolean hayCamposVaciosEquipamiento() {
        return vista.comboEquipamiento.getSelectedIndex() == -1 || vista.txtMarcaEquipamiento.getText().isEmpty() || vista.fechaCompraEquipamiento.getDate() == null || vista.txtCostoEquipamiento.getText().isEmpty() || vista.comboEstadoEquipamiento.getSelectedIndex() == -1;
    }

    private void limpiarCamposClases() {
        vista.comboTiposClases.setSelectedIndex(-1);
        vista.txtAforoMaximo.setText("");
        vista.txtDuracionClase.setText("");
        vista.txtInstructorAsignado.setText("");
        vista.txtMaterialUtilizado.setText("");
    }

    private boolean hayCamposVaciosClases() {
        return vista.comboTiposClases.getSelectedIndex() == -1 || vista.txtAforoMaximo.getText().isEmpty() || vista.txtDuracionClase.getText().isEmpty();
    }

    private boolean hayCamposVaciosEntrenador() {
        return vista.txtNombreEntrenador.getText().isEmpty() || vista.comboEspecialidadEntrenador.getSelectedIndex() == -1 || vista.fechaContratacion.getDate() == null || vista.txtSalario.getText().isEmpty();
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

    private boolean hayCamposVaciosCliente() {
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

                Map<String, Object> datosCliente = modelo.obtenerDatosCliente(clienteSeleccionado);
                vista.txtNombre.setText((String) datosCliente.get("nombre"));
                vista.txtApellido.setText((String) datosCliente.get("apellido"));
                vista.txtEmail.setText((String) datosCliente.get("email"));
                vista.txtTelefono.setText((String) datosCliente.get("telefono"));
                vista.fechaNacimiento.setDate((LocalDate) datosCliente.get("fecha_nacimiento"));
                vista.txtDireccion.setText((String) datosCliente.get("direccion"));

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

    public void refrescarEntrenadores() {
        try {
            ResultSet rs = modelo.consultarEntrenadores();
            vista.dtmEntrenadores = construirTableModelEntrenadores(rs);
            vista.entrenadoresTabla.setModel(vista.dtmEntrenadores);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refrescarClases() {
        try {
            ResultSet rs = modelo.consultarClases();
            vista.dtmClases = construirTableModelClases(rs);
            vista.clasesTabla.setModel(vista.dtmClases);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refrescarEquipamiento() {
        try {
            ResultSet rs = modelo.consultarEquipamiento();
            vista.dtmEquipamiento = construirTableModelEquipamiento(rs);
            vista.equipamientoTabla.setModel(vista.dtmEquipamiento);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelEquipamiento(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmEquipamiento.setDataVector(data, columnNames);

        return vista.dtmEquipamiento;

    }

    private DefaultTableModel construirTableModelSuscripciones(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = null;
        try {
            metaData = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmSuscripciones.setDataVector(data, columnNames);

        return vista.dtmSuscripciones;
    }

    private void refrescarTodo() {
        actualizarListaClientes();
        refrescarClientes();
        refrescarSuscripciones();
        refrescarEntrenadores();
        refrescarClases();
        refrescarEquipamiento();
        refrescar = false;

    }

    private DefaultTableModel construirTableModelEntrenadores(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new java.util.Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmEntrenadores.setDataVector(data, columnNames);

        return vista.dtmEntrenadores;

    }

    private DefaultTableModel construirTableModelClientes(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new java.util.Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmClientes.setDataVector(data, columnNames);

        return vista.dtmClientes;


    }

    private DefaultTableModel construirTableModelClases(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new java.util.Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        setDataVector(rs, columnCount, data);

        vista.dtmClases.setDataVector(data, columnNames);

        return vista.dtmClases;
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