/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Pila;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class NodoPila {

    public Libro libro;
    public NodoPila siguiente;

    NodoPila(Libro libro) {
        this.libro = libro;
        this.siguiente = null;
    }
}
