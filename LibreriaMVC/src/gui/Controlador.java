package gui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {
    private Modelo modelo;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo=modelo;
        this.vista=vista;
        //cargar opciones, listerner...
    }

    private void refrescarTodo() {

    }

    private void addActionListerners() {

    }

    private void addWindowsListerners() {

    }

    void iniciar() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command= e.getActionCommand();
        switch (command) {
            case "Opciones":
                break;
            case "Desconectar":
                break;
            case "Salir":
                break;
            case "abrirOpciones":
                break;
            case "guardarOpciones":
                break;
            case "anadirLibro":
                break;
            case "modificarLibro":
                break;
            case "eliminarLibro":
                break;
            case "anadirAutor":
                break;
            case "modificarAutor":
                break;
            case "eliminarAutor":
                break;
            case "anadirEditorial":
                break;
            case "modificarEditorial":
                break;
            case "eliminarEditorial":
                break;
        }
    }

    private void refrescarAutores() {

    }

    private void construirTableModelAutores() {

    }
    private void refrescarEditoriales() {

    }

    private void construirTableModelEditoriales() {

    }

    private void refrescarLibros() {

    }

    private void construirTableModelLibros() {

    }

    private void setDataVector() {
    }

    private void setOptions() {

    }

    private void borrarCamposLibros() {

    }

    private void borrarCamposAutores() {

    }

    private void borrarCamposEditoriales() {

    }

    private boolean comprobarLibroVacio() {
        return true;
    }

    private boolean comprobarAutorVacio() {
        return true;
    }

    private boolean comprobarEditorialVacio() {
        return true;
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
