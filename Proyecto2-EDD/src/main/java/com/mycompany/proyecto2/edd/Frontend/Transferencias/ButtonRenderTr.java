/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Frontend.Transferencias;

import com.mycompany.proyecto2.edd.Frontend.Red.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author alesso
 */
public class ButtonRenderTr extends JButton implements TableCellRenderer {

    public ButtonRenderTr() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setText("ENVIAR");
        setBackground(Color.BLUE);
        setForeground(Color.WHITE);

        return this;
    }
}
