/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Objetos;

/**
 *
 * @author alesso
 */
public enum EstadoLibro {
    DISPONIBLE("Disponible"),
    PRESTADO("Prestado"),
    EN_TRANSITO("En Tránsito"),
    AGOTADO("Agotado");

    private String descripcion;

    EstadoLibro(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
