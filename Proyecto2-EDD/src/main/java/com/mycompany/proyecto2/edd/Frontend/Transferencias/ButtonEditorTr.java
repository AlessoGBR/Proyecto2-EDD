/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Frontend.Transferencias;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos.ResultadoDijkstra;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import com.mycompany.proyecto2.edd.Frontend.GestionLibros.*;
import com.mycompany.proyecto2.edd.Backend.Util.SistemaBiblioteca;
import com.mycompany.proyecto2.edd.Backend.Util.Transferencia;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alesso
 */
public class ButtonEditorTr extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private int currentRow;
    private ListarLibros manejo;
    private DefaultTableModel modeloTabla;
    private SistemaBiblioteca sistema;
    private JCheckBox checkTiempo;
    private JComboBox comboOrigen;
    private JComboBox comboDestino;
    private PanelColas panelColas;

    public ButtonEditorTr(JCheckBox checkBox, DefaultTableModel modeloRedes, SistemaBiblioteca sistema,
            JComboBox comboOrigen, JComboBox comboDestino, JCheckBox checkTiempo, PanelColas panelColas) {
        super(checkBox);
        button = new JButton();
        this.modeloTabla = modeloRedes;
        this.sistema = sistema;
        this.checkTiempo = checkTiempo;
        this.comboOrigen = comboOrigen;
        this.comboDestino = comboDestino;
        this.panelColas = panelColas;
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        currentRow = row;

        label = "ENVIAR";
        button.setText(label);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            String tituloLibro = (String) modeloTabla.getValueAt(currentRow, 1);
            String isbn = (String) modeloTabla.getValueAt(currentRow, 3);

            Libro libro = sistema.buscarLibroPorISBN(isbn);
            if (libro == null) {
                JOptionPane.showMessageDialog(null, "NO SE ENCONTRO EL LIBRO", "ERROR", JOptionPane.ERROR_MESSAGE);
                return label;
            }

            String origen = comboOrigen.getSelectedItem().toString();
            String destino = comboDestino.getSelectedItem().toString();
            boolean porTiempo = checkTiempo.isSelected();
            
            ResultadoDijkstra ruta = porTiempo
                    ? sistema.calcularRutaOptimaPorTiempo(origen, destino)
                    : sistema.calcularRutaOptimaPorCosto(origen, destino);

            if (ruta == null || ruta.getRuta() == null || ruta.getRuta().length == 0) {
                JOptionPane.showMessageDialog(null, "NO SE ENCONTRO UNA RUTA ENTRE " + origen + " Y " + destino,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                return label;
            }

            Transferencia transferencia = new Transferencia(sistema, libro, ruta.getRuta(), porTiempo, panelColas);
            transferencia.start();

            JOptionPane.showMessageDialog(null, "TRANSFERENCIA INICIADA ENTRE " + origen + " Y " + destino,
                    "TRANSFERENCIA", JOptionPane.INFORMATION_MESSAGE);

        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
