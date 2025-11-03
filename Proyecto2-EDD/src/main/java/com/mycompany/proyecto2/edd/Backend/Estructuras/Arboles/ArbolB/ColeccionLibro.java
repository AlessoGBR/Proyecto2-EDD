/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import java.util.ArrayList;

/**
 *
 * @author alesso
 */
public class ColeccionLibro {

    private int anioPublicacion;
    private String isbn;
    private ArrayList<Libro> copias;

    public ColeccionLibro(Libro libro) {
        this.anioPublicacion = libro.getAnioPublicacion();
        this.isbn = libro.getIsbn();
        this.copias = new ArrayList<>();
        this.copias.add(libro);
    }

    public void agregarCopia(Libro libro) {
        this.copias.add(libro);
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public ArrayList<Libro> getCopias() {
        return copias;
    }

    public Libro getPrimero() {
        return copias.isEmpty() ? null : copias.get(0);
    }

    public int getTotalCopias() {
        return copias.size();
    }

    public boolean mismoGrupo(Libro libro) {
        if (libro == null) {
            return false;
        }
        return this.isbn.equalsIgnoreCase(libro.getIsbn()) && this.anioPublicacion == libro.getAnioPublicacion();
    }

    public Libro getLibroPrincipal() {
        return copias.isEmpty() ? null : copias.get(0);
    }

    public int getCantidadCopias() {
        return copias.size();
    }

    @Override
    public String toString() {
        Libro libroBase = getLibroPrincipal();
        if (libroBase == null) {
            return "COLECCION VACIA";
        }
        return "TITULO: " + libroBase.getTitulo() + " | ISBN: " + isbn + " | ANIO: " + anioPublicacion + " | COPIAS: " + copias.size();
    }
}
