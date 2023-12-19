package gui;

import base.enums.GenerosLibros;
import base.enums.TiposEditoriales;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private final static String TITULOFRAME="Aplicacion libreria";

    //libros
    JPanel JPanelLibro;
    JTextField txtTitulo;
    JComboBox comboAutor;
    JComboBox comboEditorial;
    JComboBox comboGenero;
    DatePicker fecha;
    JTextField txtIsbn;
    JTextField txtPrecioLibro;
    JButton btnLibrosAnadir;
    JButton btnLibrosModificar;
    JButton btnLibrosEliminar;
    JTable librosTabla;

    //autores
    JPanel JPanelAutor;
    JTextField txtNombre;
    JTextField txtApellidos;
    DatePicker fechaNacimiento;
    JTextField txtPais;
    JButton btnAutoresAnadir;
    JButton btnAutoresModificar;
    JButton btnAutoresEliminar;
    JTable autoresTabla;

    //editoriales
    JPanel JPanelEditorial;
    JTextField txtNombreEditorial;
    JTextField txtEmail;
    JTextField txtTelefono;
    JComboBox comboTipoEditorial;
    JTextField txtWeb;
    JButton btnEditorialesAnadir;
    JButton btnEditorialesModificar;
    JButton btnEditorialesEliminar;
    JTable editorialesTabla;

    //busqueda
    JLabel etiquetaEstado;

    //default table model
    DefaultTableModel dtmEditoriales;
    DefaultTableModel dtmAutores;
    DefaultTableModel dtmLibros;

    //menubar
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;
    OptionDialog optionDialog;


    JDialog adminPassWordDialog;
    JButton btnValidate;
    JPasswordField adminPassword;

    public Vista() {
        super(TITULOFRAME);
        initFrame();
        optionDialog = new OptionDialog(this);
        optionDialog.setVisible(false); // Asegúrate de que no se muestre automáticamente.
    }

    public void initFrame() {
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(new Dimension(this.getWidth()+200,this.getHeight()));
        this.setLocationRelativeTo(null);

        setMenu();
        setComboBox();
        setTableModels();
        setAdminDialog();
    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones=new JMenuItem("Opciones");
        itemOpciones.setActionCommand("Opciones");
        itemDesconectar= new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir=new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setTableModels() {
        this.dtmLibros=new DefaultTableModel();
        this.librosTabla.setModel(dtmLibros);

        this.dtmEditoriales=new DefaultTableModel();
        this.editorialesTabla.setModel(dtmEditoriales);

        this.dtmAutores=new DefaultTableModel();
        this.autoresTabla.setModel(dtmAutores);
    }

    private void setComboBox() {
        for (TiposEditoriales constant: TiposEditoriales.values()) {
            comboTipoEditorial.addItem(constant.getValor());
        }
        comboTipoEditorial.setSelectedIndex(-1);
        for (GenerosLibros constant: GenerosLibros.values()) {
            comboGenero.addItem(constant.getValor());
        }
        comboGenero.setSelectedIndex(-1);
    }
    private void setAdminDialog() {
        btnValidate = new JButton("Validar");
        btnValidate.setActionCommand("abrirOpciones");
        adminPassword= new JPasswordField();
        adminPassword.setPreferredSize(new Dimension(100,26));
        Object[] options = new Object[] {adminPassword,btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña",
                JOptionPane.WARNING_MESSAGE,JOptionPane.YES_NO_OPTION,null,options);
        adminPassWordDialog= new JDialog(this,"Opciones",true);
        adminPassWordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        adminPassWordDialog.setContentPane(jop);
        adminPassWordDialog.pack();
        adminPassWordDialog.setLocationRelativeTo(this);
    }
}
