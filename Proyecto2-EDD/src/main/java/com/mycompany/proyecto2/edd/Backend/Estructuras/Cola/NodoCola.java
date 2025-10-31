/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Cola;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class NodoCola {

    public Libro libro;
    public NodoCola siguiente;

    public NodoCola(Libro libro) {
        this.libro = libro;
        this.siguiente = null;
    }
}
