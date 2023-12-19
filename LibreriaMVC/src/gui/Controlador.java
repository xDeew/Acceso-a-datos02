package gui;



import Util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {
    private Modelo modelo;
    private Vista vista;
    boolean refrescar;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo=modelo;
        this.vista=vista;
        modelo.conectar();
        addActionListeners(this);
        addWindowsListerners(this);
        refrescarTodo();
        iniciar();
    }

    private void refrescarTodo() {
        refrescarAutores();
        refrescarEditoriales();
        refrescarLibros();
        refrescar=false;
    }

    private void addActionListeners(ActionListener listener) {
        vista.btnLibrosAnadir.addActionListener(listener);
        vista.btnAutoresAnadir.addActionListener(listener);
        vista.btnEditorialesAnadir.addActionListener(listener);
        vista.btnLibrosEliminar.addActionListener(listener);
        vista.btnAutoresEliminar.addActionListener(listener);
        vista.btnEditorialesEliminar.addActionListener(listener);
        vista.btnLibrosModificar.addActionListener(listener);
        vista.btnAutoresModificar.addActionListener(listener);
        vista.btnEditorialesModificar.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);
        vista.itemOpciones.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
        vista.btnValidate.addActionListener(listener);
    }

    private void addWindowsListerners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    void iniciar(){
        vista.editorialesTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel =  vista.editorialesTabla.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.editorialesTabla.getSelectionModel())) {
                        int row = vista.editorialesTabla.getSelectedRow();
                        vista.txtNombreEditorial.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 1)));
                        vista.txtEmail.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 2)));
                        vista.txtTelefono.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 3)));
                        vista.comboTipoEditorial.setSelectedItem(String.valueOf(vista.editorialesTabla.getValueAt(row, 4)));
                        vista.txtWeb.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 5)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.editorialesTabla.getSelectionModel())) {
                            borrarCamposEditoriales();
                        } else if (e.getSource().equals(vista.autoresTabla.getSelectionModel())) {
                            borrarCamposAutores();
                        } else if (e.getSource().equals(vista.librosTabla.getSelectionModel())) {
                            borrarCamposLibros();
                        }
                    }
                }
            }
        });

        vista.autoresTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel2 =  vista.autoresTabla.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.autoresTabla.getSelectionModel())) {
                        int row = vista.autoresTabla.getSelectedRow();
                        vista.txtNombre.setText(String.valueOf(vista.autoresTabla.getValueAt(row, 1)));
                        vista.txtApellidos.setText(String.valueOf(vista.autoresTabla.getValueAt(row, 2)));
                        vista.fechaNacimiento.setDate((Date.valueOf(String.valueOf(vista.autoresTabla.getValueAt(row, 3)))).toLocalDate());
                        vista.txtPais.setText(String.valueOf(vista.autoresTabla.getValueAt(row, 4)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.editorialesTabla.getSelectionModel())) {
                            borrarCamposEditoriales();
                        } else if (e.getSource().equals(vista.autoresTabla.getSelectionModel())) {
                            borrarCamposAutores();
                        } else if (e.getSource().equals(vista.librosTabla.getSelectionModel())) {
                            borrarCamposLibros();
                        }
                    }
                }
            }
        });

        vista.librosTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel3 =  vista.librosTabla.getSelectionModel();
        cellSelectionModel3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel3.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.librosTabla.getSelectionModel())) {
                        int row = vista.librosTabla.getSelectedRow();
                        vista.txtTitulo.setText(String.valueOf(vista.librosTabla.getValueAt(row, 1)));
                        vista.comboAutor.setSelectedItem(String.valueOf(vista.librosTabla.getValueAt(row, 5)));
                        vista.comboEditorial.setSelectedItem(String.valueOf(vista.librosTabla.getValueAt(row, 3)));
                        vista.comboGenero.setSelectedItem(String.valueOf(vista.librosTabla.getValueAt(row, 4)));
                        vista.fecha.setDate((Date.valueOf(String.valueOf(vista.librosTabla.getValueAt(row, 7)))).toLocalDate());
                        vista.txtIsbn.setText(String.valueOf(vista.librosTabla.getValueAt(row, 2)));
                        vista.txtPrecioLibro.setText(String.valueOf(vista.librosTabla.getValueAt(row, 6)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.editorialesTabla.getSelectionModel())) {
                            borrarCamposEditoriales();
                        } else if (e.getSource().equals(vista.autoresTabla.getSelectionModel())) {
                            borrarCamposAutores();
                        } else if (e.getSource().equals(vista.librosTabla.getSelectionModel())) {
                            borrarCamposLibros();
                        }
                    }
                }
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command= e.getActionCommand();
        switch (command) {
            case "Opciones":
                vista.adminPassWordDialog.setVisible(true);
                break;
            case "Desconectar":
                modelo.desconectar();
                break;
            case "Salir":
                System.exit(0);
                break;
            case "abrirOpciones":
                if (String.valueOf(vista.adminPassword.getPassword()).equals(modelo.getAdminPassword())) {
                    vista.adminPassword.setText("");
                    vista.adminPassWordDialog.dispose();
                    setOptions(); // Ahora que sabemos que optionDialog ha sido creado, llama a setOptions().
                    vista.optionDialog.setVisible(true);
                } else {
                    Util.showErrorAlert("La contraseña introducida no es correcta");
                }
                break;
            case "guardarOpciones":
                modelo.setPropValues(vista.optionDialog.txtIP.getText(),
                        vista.optionDialog.txtUsuario.getText(),
                        String.valueOf(vista.optionDialog.pfPass.getPassword()),
                        String.valueOf(vista.optionDialog.pfAdmin.getPassword()));
                vista.optionDialog.dispose();
                new Controlador(new Modelo(),new Vista());
                break;
            case "anadirLibro":
                try {
                    if (comprobarLibroVacio()) {
                        Util.showErrorAlert("Rellena todos lo campos");
                        vista.librosTabla.clearSelection();
                    } else if (modelo.libroIsbnYaExiste(vista.txtIsbn.getText())) {
                        Util.showErrorAlert("Ese ISBN ya existe" +
                                "\nIntroduce un libro diferente");
                        vista.librosTabla.clearSelection();
                    } else {
                        modelo.insertarLibro(
                                vista.txtTitulo.getText(),
                                vista.txtIsbn.getText(),
                                String.valueOf(vista.comboEditorial.getSelectedItem()),
                                String.valueOf(vista.comboGenero.getSelectedItem()),
                                String.valueOf(vista.comboAutor.getSelectedItem()),
                                Float.parseFloat(vista.txtPrecioLibro.getText()),
                                vista.fecha.getDate());
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce numeros en los campos que lo " +
                            "requieren");
                    vista.librosTabla.clearSelection();
                }
                borrarCamposLibros();
                refrescarLibros();
                break;
            case "modificarLibro":
                try {
                    if (comprobarLibroVacio()) {
                        Util.showErrorAlert("Rellena todos lo campos");
                        vista.librosTabla.clearSelection();
                    } else {
                        modelo.modificarLibro(
                                vista.txtTitulo.getText(),
                                vista.txtIsbn.getText(),
                                String.valueOf(vista.comboEditorial.getSelectedItem()),
                                String.valueOf(vista.comboGenero.getSelectedItem()),
                                String.valueOf(vista.comboAutor.getSelectedItem()),
                                Float.parseFloat(vista.txtPrecioLibro.getText()),
                                vista.fecha.getDate(),
                                Integer.parseInt((String)vista.librosTabla.getValueAt(
                                        vista.librosTabla.getSelectedRow(),0)));
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce numeros en los campos que lo " +
                            "requieren");
                    vista.librosTabla.clearSelection();
                }
                borrarCamposLibros();
                refrescarLibros();
                break;
            case "eliminarLibro":
                modelo.borrarLibro(Integer.parseInt((String)vista.librosTabla.getValueAt(
                        vista.librosTabla.getSelectedRow(),0)));
                borrarCamposLibros();
                refrescarLibros();
                break;
            case "anadirAutor": {
                try {
                    if (comprobarAutorVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.autoresTabla.clearSelection();
                    } else if (modelo.autorNombreYaExiste(vista.txtNombre.getText(),
                            vista.txtApellidos.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce un autor diferente");
                        vista.autoresTabla.clearSelection();
                    } else {
                        modelo.insertarAutor(vista.txtNombre.getText(),
                                vista.txtApellidos.getText(),
                                vista.fechaNacimiento.getDate(),
                                vista.txtPais.getText());
                        refrescarAutores();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.autoresTabla.clearSelection();
                }
                borrarCamposAutores();
            }
            break;
            case "modificarAutor": {
                try {
                    if (comprobarAutorVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.autoresTabla.clearSelection();
                    } else {
                        modelo.modificarAutor(vista.txtNombre.getText(), vista.txtApellidos.getText(),
                                vista.fechaNacimiento.getDate(), vista.txtPais.getText(),
                                Integer.parseInt((String)vista.autoresTabla.getValueAt(vista.autoresTabla.getSelectedRow(), 0)));
                        refrescarAutores();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.autoresTabla.clearSelection();
                }
                borrarCamposAutores();
            }
            break;
            case "eliminarAutor":
                modelo.borrarAutor(Integer.parseInt((String)vista.autoresTabla.getValueAt(vista.autoresTabla.getSelectedRow(), 0)));
                borrarCamposAutores();
                refrescarAutores();
                break;
            case "anadirEditorial": {
                try {
                    if (comprobarEditorialVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.editorialesTabla.clearSelection();
                    } else if (modelo.editorialNombreYaExiste(vista.txtNombreEditorial.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce una editorial diferente.");
                        vista.editorialesTabla.clearSelection();
                    } else {
                        modelo.insertarEditorial(vista.txtNombreEditorial.getText(), vista.txtEmail.getText(),
                                vista.txtTelefono.getText(),
                                (String) vista.comboTipoEditorial.getSelectedItem(),
                                vista.txtWeb.getText());
                        refrescarEditoriales();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.editorialesTabla.clearSelection();
                }
                borrarCamposEditoriales();
            }
            break;
            case "modificarEditorial": {
                try {
                    if (comprobarEditorialVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.editorialesTabla.clearSelection();
                    } else {
                        modelo.modificarEditorial(vista.txtNombreEditorial.getText(), vista.txtEmail.getText(), vista.txtTelefono.getText(),
                                String.valueOf(vista.comboTipoEditorial.getSelectedItem()), vista.txtWeb.getText(),
                                Integer.parseInt((String)vista.editorialesTabla.getValueAt(vista.editorialesTabla.getSelectedRow(), 0)));
                        refrescarEditoriales();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.editorialesTabla.clearSelection();
                }
                borrarCamposEditoriales();
            }
            break;
            case "eliminarEditorial":
                modelo.borrarEditorial(Integer.parseInt((String)vista.editorialesTabla.getValueAt(vista.editorialesTabla.getSelectedRow(), 0)));
                borrarCamposEditoriales();
                refrescarEditoriales();
                break;
        }
    }

    private void refrescarEditoriales() {
        try {
            vista.editorialesTabla.setModel(construirTableModelEditoriales(modelo.consultarEditorial()));
            vista.comboEditorial.removeAllItems();
            for(int i = 0; i < vista.dtmEditoriales.getRowCount(); i++) {
                vista.comboEditorial.addItem(vista.dtmEditoriales.getValueAt(i, 0)+" - "+
                        vista.dtmEditoriales.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private  DefaultTableModel construirTableModelEditoriales(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmEditoriales.setDataVector(data, columnNames);

        return vista.dtmEditoriales;

    }

    private void refrescarAutores() {
        try {
            vista.autoresTabla.setModel(construirTableModeloAutores(modelo.consultarAutor()));
            vista.comboAutor.removeAllItems();
            for(int i = 0; i < vista.dtmAutores.getRowCount(); i++) {
                vista.comboAutor.addItem(vista.dtmAutores.getValueAt(i, 0)+" - "+
                        vista.dtmAutores.getValueAt(i, 2)+", "+vista.dtmAutores.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModeloAutores(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmAutores.setDataVector(data, columnNames);

        return vista.dtmAutores;

    }

    /**
     * Actualiza los libros que se ven en la lista y los comboboxes
     */
    private void refrescarLibros() {
        try {
            vista.librosTabla.setModel(construirTableModelLibros(modelo.consultarLibros()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelLibros(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        setDataVector(rs, columnCount, data);

        vista.dtmLibros.setDataVector(data, columnNames);

        return vista.dtmLibros;

    }

    private void setDataVector(ResultSet rs, int columnCount, Vector<Vector<Object>> data) throws SQLException {
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
    }

    private void setOptions() {
        vista.optionDialog.txtIP.setText(modelo.getIp());
        vista.optionDialog.txtUsuario.setText(modelo.getUser());
        vista.optionDialog.pfPass.setText(modelo.getPassword());
        vista.optionDialog.pfAdmin.setText(modelo.getAdminPassword());
    }

    private void borrarCamposLibros() {
        vista.txtTitulo.setText("");
        vista.txtIsbn.setText("");
        vista.comboAutor.setSelectedIndex(-1);
        vista.comboEditorial.setSelectedIndex(-1);
        vista.comboGenero.setSelectedIndex(-1);
        vista.txtPrecioLibro.setText("");
        vista.fecha.setText("");
    }

    private void borrarCamposAutores() {
        vista.txtNombre.setText("");
        vista.txtApellidos.setText("");
        vista.txtPais.setText("");
        vista.fechaNacimiento.setText("");
    }

    private void borrarCamposEditoriales() {
        vista.txtNombreEditorial.setText("");
        vista.txtEmail.setText("");
        vista.txtTelefono.setText("");
        vista.comboTipoEditorial.setSelectedIndex(-1);
        vista.txtWeb.setText("");
    }

    private boolean comprobarLibroVacio() {
        return vista.txtTitulo.getText().isEmpty() ||
                vista.txtPrecioLibro.getText().isEmpty() ||
                vista.txtIsbn.getText().isEmpty() ||
                vista.comboGenero.getSelectedIndex()==-1 ||
                vista.comboAutor.getSelectedIndex()==-1 ||
                vista.comboEditorial.getSelectedIndex()==-1 ||
                vista.fecha.getText().isEmpty();
    }

    private boolean comprobarAutorVacio() {
        return vista.txtApellidos.getText().isEmpty() ||
                vista.txtNombre.getText().isEmpty() ||
                vista.txtPais.getText().isEmpty() ||
                vista.fechaNacimiento.getText().isEmpty();
    }

    private boolean comprobarEditorialVacia() {
        return vista.txtNombreEditorial.getText().isEmpty() ||
                vista.txtEmail.getText().isEmpty() ||
                vista.txtTelefono.getText().isEmpty() ||
                vista.comboTipoEditorial.getSelectedIndex()==-1 ||
                vista.txtWeb.getText().isEmpty();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }





    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
            if (e.getSource().equals(vista.editorialesTabla.getSelectionModel())) {
                int row = vista.editorialesTabla.getSelectedRow();
                vista.txtNombreEditorial.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 1)));
                vista.txtEmail.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 2)));
                vista.txtTelefono.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 3)));
                vista.comboTipoEditorial.setSelectedItem(String.valueOf(vista.editorialesTabla.getValueAt(row, 4)));
                vista.txtWeb.setText(String.valueOf(vista.editorialesTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.autoresTabla.getSelectionModel())) {
                int row = vista.autoresTabla.getSelectedRow();
                vista.txtNombre.setText(String.valueOf(vista.autoresTabla.getValueAt(row, 1)));
                vista.txtApellidos.setText(String.valueOf(vista.autoresTabla.getValueAt(row, 2)));
                vista.fechaNacimiento.setDate((Date.valueOf(String.valueOf(vista.autoresTabla.getValueAt(row, 3)))).toLocalDate());
                vista.txtPais.setText(String.valueOf(vista.autoresTabla.getValueAt(row, 4)));
            } else if (e.getSource().equals(vista.librosTabla.getSelectionModel())) {
                int row = vista.librosTabla.getSelectedRow();
                vista.txtTitulo.setText(String.valueOf(vista.librosTabla.getValueAt(row, 1)));
                vista.comboAutor.setSelectedItem(String.valueOf(vista.librosTabla.getValueAt(row, 5)));
                vista.comboEditorial.setSelectedItem(String.valueOf(vista.librosTabla.getValueAt(row, 3)));
                vista.comboGenero.setSelectedItem(String.valueOf(vista.librosTabla.getValueAt(row, 4)));
                vista.fecha.setDate((Date.valueOf(String.valueOf(vista.librosTabla.getValueAt(row, 7)))).toLocalDate());
                vista.txtIsbn.setText(String.valueOf(vista.librosTabla.getValueAt(row, 2)));
                vista.txtPrecioLibro.setText(String.valueOf(vista.librosTabla.getValueAt(row, 6)));
            } else if (e.getValueIsAdjusting()
                    && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                if (e.getSource().equals(vista.editorialesTabla.getSelectionModel())) {
                    borrarCamposEditoriales();
                } else if (e.getSource().equals(vista.autoresTabla.getSelectionModel())) {
                    borrarCamposAutores();
                } else if (e.getSource().equals(vista.librosTabla.getSelectionModel())) {
                    borrarCamposLibros();
                }
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

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
    public void itemStateChanged(ItemEvent e) {

    }
}
