/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Frontend.GestionLibros;

import com.mycompany.proyecto2.edd.Biblioteca;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author alesso
 */
public class BibliotecasCreadas extends AbstractTableModel {

    private List<Biblioteca> bibliotecas;
    private final String[] columnNames = {
        "Nombre", "Ubicacion", "Tiempo Procesamiento", "Tiempo traspaso", "Despacho"
    };

    public BibliotecasCreadas(List<Biblioteca> clientes) {
        this.bibliotecas = clientes != null ? clientes : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return bibliotecas.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        /*
        Biblioteca cliente = bibliotecas.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return cliente.getIdCliente();
            case 1:
                return cliente.getNombreCompleto();
            case 2:
                return cliente.getDpi();
            case 3:
                return cliente.getTelefono();
            case 4:
                return cliente.getEmail();
            case 5:
                return cliente.getTipoMembresia();
            case 6:
                return cliente.getFechaInicioMembresia() != null
                        ? dateFormat.format(cliente.getFechaInicioMembresia()) : "";
            case 7:
                return cliente.getFechaVencimientoMembresia() != null
                        ? dateFormat.format(cliente.getFechaVencimientoMembresia()) : "";
            case 8:
                return cliente.isMembresiaActiva() ? "Activa" : "Inactiva";
            default:
                return ""; 
        }*/
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 8:
                return String.class;
            default:
                return String.class;
        }
    }

    public Biblioteca getClienteAt(int rowIndex) {
        return bibliotecas.get(rowIndex);
    }

    public void actualizarDatos(List<Biblioteca> nuevosClientes) {
        this.bibliotecas = nuevosClientes != null ? nuevosClientes : new ArrayList<>();
        fireTableDataChanged();
    }
}
