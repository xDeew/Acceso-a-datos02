package com.andrelut.gimnasioMVC.gui;

import com.andrelut.gimnasioMVC.enums.EstadoPago;
import com.andrelut.gimnasioMVC.enums.TipoSuscripcion;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
    public JButton btnModificarSuscripciones;
    public JButton btnDeleteSuscripciones;
    public JTable clientesTabla;
    public JButton btnGananciaMensual;
    public OptionDialog optionDialog;
    public DefaultTableModel dtmClientes;
    public DefaultTableModel dtmSuscripciones;
    public JMenuItem itemOpciones;
    public JMenuItem itemConexion;
    public JMenuItem itemSalir;
    public JButton btnValidate;
    public JPasswordField adminPassword;
    public JDialog adminPasswordDialog;
    public JTable suscripcionesTabla;


    public Vista() {
        super(TITULOFRAME);
        initFrame();
    }

    public void initFrame() {
        panel1.setLayout(new BorderLayout());
        panel1.add(tabbedPane1, BorderLayout.CENTER);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(new Dimension(this.getWidth() + 320, this.getHeight() + 100));
        this.setLocationRelativeTo(null);
        optionDialog = new OptionDialog(this);

        setMenu();
        setAdminDialog();
        setComboBox();
        setTableModels();


        this.setVisible(true);


    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones = new JMenuItem("Opciones");
        itemOpciones.setActionCommand("opciones");
        itemConexion = new JMenuItem("Desconectar");
        itemConexion.setActionCommand("desconectar");
        itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("salir");
        menu.add(itemOpciones);
        menu.add(itemConexion);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setAdminDialog() {
        btnValidate = new JButton("Validar");
        btnValidate.setActionCommand("abrirOpciones");
        adminPassword = new JPasswordField();
        adminPassword.setPreferredSize(new Dimension(100, 26));
        Object[] options = new Object[]{adminPassword, btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña",
                JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION, null, options);
        adminPasswordDialog = new JDialog(this, "Opciones", true);
        adminPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        adminPasswordDialog.setContentPane(jop);
        adminPasswordDialog.pack();
        adminPasswordDialog.setLocationRelativeTo(this);
    }


    private void setTableModels() {
        this.dtmClientes = new DefaultTableModel();
        this.clientesTabla.setModel(dtmClientes);
        this.dtmSuscripciones = new DefaultTableModel();
        this.suscripcionesTabla.setModel(dtmSuscripciones);


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


    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }
}
