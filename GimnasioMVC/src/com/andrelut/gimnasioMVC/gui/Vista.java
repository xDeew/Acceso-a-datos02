package com.andrelut.gimnasioMVC.gui;

import com.andrelut.gimnasioMVC.enums.EstadoPago;
import com.andrelut.gimnasioMVC.enums.TipoSuscripcion;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Vista extends JFrame {
    private static final String TITULOFRAME = "Gimnasio";

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
    public JComboBox comboPagado;
    public JFrame frame;
    public JComboBox comboTipoSuscripcion;
    public JTextField txtPrecio;
    public JButton btnAddCliente;
    public JButton btnModificarCliente;
    public JButton btnEliminarCliente;
    public JTextField txtTelefono;
    public JButton btnAddSuscripciones;
    public JButton btnAddModificarSuscripciones;
    public JButton btnDeleteSuscripciones;
    public JTable clientesTabla;
    public JButton btnGananciasActuales;
    DefaultTableModel dtmClientes;
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    public Vista() {
        super(TITULOFRAME);
        initFrame();
    }

    public void initFrame() {
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(new Dimension(this.getWidth() + 200, this.getHeight() + 50));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.add(tabbedPane1, BorderLayout.CENTER);


        setMenu();
        setComboBox();
        setTableModels();
        setAdminDialog();
    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones = new JMenuItem("Opciones");
        itemOpciones.setActionCommand("Opciones");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setTableModels() {
        this.dtmClientes = new DefaultTableModel();
        this.clientesTabla.setModel(dtmClientes);

    }

    public void adjustColumnWidth() {
        for (int column = 0; column < clientesTabla.getColumnCount(); column++) {
            TableColumn tableColumn = clientesTabla.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            // Aumentar el ancho para la columna 'idCliente' si es la primera columna
            if (column == 0) { // Cambiar a if (tableColumn.getIdentifier().equals("idCliente")) si se conoce el nombre de la columna
                preferredWidth = Math.max(preferredWidth, 100); // Ajustar 100 al ancho deseado
            } else if (column == 3) {
                preferredWidth = Math.max(preferredWidth, 100);
            }

            for (int row = 0; row < clientesTabla.getRowCount(); row++) {
                TableCellRenderer cellRenderer = clientesTabla.getCellRenderer(row, column);
                Component c = clientesTabla.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + clientesTabla.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private void setComboBox() {
        for (TipoSuscripcion tipo : TipoSuscripcion.values()) {
            comboTipoSuscripcion.addItem(tipo.getNombre());
        }
        comboTipoSuscripcion.setSelectedIndex(-1);

        for (EstadoPago estado : EstadoPago.values()) {
            comboPagado.addItem(estado.getValor());
        }
        comboPagado.setSelectedIndex(-1);
    }

    private void setAdminDialog() {
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }
}
