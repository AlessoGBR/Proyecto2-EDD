/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Frontend.GestionLibros;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import com.mycompany.proyecto2.edd.Backend.Util.SistemaBiblioteca;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alesso
 */
public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private int currentRow;
    private ListarLibros manejo;
    private DefaultTableModel modeloLibros;
    private SistemaBiblioteca sistema;
    private String nombreCliente;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel modeloLibros, SistemaBiblioteca sistema) {
        super(checkBox);
        button = new JButton();
        this.modeloLibros = modeloLibros;
        this.sistema = sistema;
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

        label = "ELIMINAR";
        button.setText(label);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            String isbn = (String) modeloLibros.getValueAt(currentRow, 2);
            System.out.println(isbn);
            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "¿SEGURO DESEAS ELIMINAR EL LIBRO: \n" + isbn + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> {
                    boolean exito = sistema.eliminarLibro(isbn);

                    if (exito) {
                        modeloLibros.removeRow(currentRow);
                        JOptionPane.showMessageDialog(
                                null,
                                this.sistema.getMensaje(),
                                "Eliminación exitosa",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "NO ES PUDO ELIMINAR EL LIBRO"
                                + sistema.getMensaje(),
                                "Error al eliminar",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "OPERACION CANCELADA",
                        "Cancelado",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
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
