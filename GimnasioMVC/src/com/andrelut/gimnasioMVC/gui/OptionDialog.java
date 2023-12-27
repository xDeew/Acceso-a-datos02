package com.andrelut.gimnasioMVC.gui;

import javax.swing.*;
import java.awt.*;

public class OptionDialog extends JDialog{
    private JPanel panel1;
    JTextField txtIP;
    JTextField txtUser;
    JPasswordField pfPass;
    JPasswordField pfAdmin;
    public JButton btnOpcionesGuardar;
    private Frame owner;

    public OptionDialog(Frame owner) {
        super(owner,"Opciones",true);
        this.owner=owner;
        initDialog();
    }

    private void initDialog() {
        this.setContentPane(panel1);
        this.panel1.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.pack();
        this.setSize(new Dimension(this.getWidth()+200,this.getHeight()));
        this.setLocationRelativeTo(owner);
    }
}
