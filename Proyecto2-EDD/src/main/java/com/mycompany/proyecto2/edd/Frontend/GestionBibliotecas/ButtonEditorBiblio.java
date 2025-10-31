/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Frontend.GestionBibliotecas;

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
public class ButtonEditorBiblio extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    private int currentRow;
    private DefaultTableModel modeloBiblio;
    private SistemaBiblioteca sistema;

    public ButtonEditorBiblio(JCheckBox checkBox, DefaultTableModel modeloBiblio, SistemaBiblioteca sistema) {
        super(checkBox);
        this.modeloBiblio = modeloBiblio;
        this.sistema = sistema;

        button = new JButton();
        button.setOpaque(true);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);

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
        isPushed = true;

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            String nombreBiblioteca = (String) modeloBiblio.getValueAt(currentRow, 1);

            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "¿SEGURO DESEAS ELIMINAR LA BIBLIOTECA: \n" + nombreBiblioteca + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> {
                    boolean exito = sistema.eliminarBiblioteca(nombreBiblioteca);

                    if (exito) {
                        modeloBiblio.removeRow(currentRow);
                        JOptionPane.showMessageDialog(
                                null,
                                "BIBLIOTECA \"" + nombreBiblioteca + "\" ELIMINADA CORRECTAMENTE",
                                "Eliminación exitosa",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "NO ES PUDO ELIMINAR LA BIBLIOTECA: \"" + nombreBiblioteca + "\".\n"
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
