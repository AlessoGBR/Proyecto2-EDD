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
public class Cola {

    private String mensaje; 
    private NodoCola frente;
    private NodoCola finalCola;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.tamanio = 0;
        this.mensaje = "COLA CREADA CORRECTAMENTE";
    }

    public String getMensaje() {
        return mensaje;
    }

    public void encolar(Libro libro) {
        if (libro == null) {
            mensaje = "ERROR: NO SE PUEDE ENCOLAR UN LIBRO NULO";
            return;
        }

        NodoCola nuevoNodo = new NodoCola(libro);

        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        } else {
            finalCola.siguiente = nuevoNodo;
            finalCola = nuevoNodo;
        }

        tamanio++;
        mensaje = "LIBRO ENCOLADO CORRECTAMENTE: " + libro.getTitulo();
    }

    public Libro desencolar() {
        if (estaVacia()) {
            mensaje = "ERROR: LA COLA ESTA VACIA, NO SE PUEDE DESENCOLAR";
            return null;
        }

        Libro libro = frente.libro;
        frente = frente.siguiente;

        if (frente == null) {
            finalCola = null;
        }

        tamanio--;
        mensaje = "LIBRO DESENCOLADO CORRECTAMENTE: " + libro.getTitulo();
        return libro;
    }

    public Libro verFrente() {
        if (estaVacia()) {
            mensaje = "ERROR: LA COLA ESTA VACIA";
            return null;
        }

        mensaje = "FRENTE DE LA COLA: " + frente.libro.getTitulo();
        return frente.libro;
    }

    public Libro verFinal() {
        if (estaVacia()) {
            mensaje = "ERROR: LA COLA ESTA VACIA";
            return null;
        }

        mensaje = "FINAL DE LA COLA: " + finalCola.libro.getTitulo();
        return finalCola.libro;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void limpiar() {
        frente = null;
        finalCola = null;
        tamanio = 0;
        mensaje = "COLA LIMPIADA CORRECTAMENTE";
    }

    public Libro[] toArray() {
        Libro[] arreglo = new Libro[tamanio];
        NodoCola actual = frente;
        int indice = 0;

        while (actual != null) {
            arreglo[indice++] = actual.libro;
            actual = actual.siguiente;
        }

        return arreglo;
    }

    public void recorrer(AccionLibro accion) {
        NodoCola actual = frente;
        int indice = 0;

        while (actual != null) {
            accion.ejecutar(actual.libro, indice);
            actual = actual.siguiente;
            indice++;
        }
    }

    public boolean contiene(String isbn) {
        NodoCola actual = frente;

        while (actual != null) {
            if (actual.libro.getIsbn().equals(isbn)) {
                return true;
            }
            actual = actual.siguiente;
        }

        return false;
    }

    public Libro buscarPorISBN(String isbn) {
        NodoCola actual = frente;

        while (actual != null) {
            if (actual.libro.getIsbn().equals(isbn)) {
                mensaje = "LIBRO ENCONTRADO EN LA COLA: " + actual.libro.getTitulo();
                return actual.libro;
            }
            actual = actual.siguiente;
        }

        mensaje = "NO SE ENCONTRO EL LIBRO CON ISBN: " + isbn;
        return null;
    }

    public boolean eliminarPorISBN(String isbn) {
        if (estaVacia()) {
            mensaje = "ERROR: LA COLA ESTA VACIA";
            return false;
        }

        if (frente.libro.getIsbn().equals(isbn)) {
            desencolar();
            return true;
        }

        NodoCola actual = frente;
        while (actual.siguiente != null) {
            if (actual.siguiente.libro.getIsbn().equals(isbn)) {
                Libro libro = actual.siguiente.libro;
                actual.siguiente = actual.siguiente.siguiente;

                if (actual.siguiente == null) {
                    finalCola = actual;
                }

                tamanio--;
                mensaje = "LIBRO ELIMINADO DE LA COLA: " + libro.getTitulo();
                return true;
            }
            actual = actual.siguiente;
        }

        mensaje = "NO SE ENCONTRO EL LIBRO CON ISBN: " + isbn;
        return false;
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "COLA VACIA []";
        }

        StringBuilder sb = new StringBuilder("COLA [" + tamanio + " ELEMENTOS]:\n");
        NodoCola actual = frente;
        int indice = 0;

        while (actual != null) {
            sb.append("  ").append(indice).append(". ")
                    .append(actual.libro.getTitulo())
                    .append(" - ").append(actual.libro.getAutor())
                    .append(" (").append(actual.libro.getEstado().getDescripcion()).append(")")
                    .append("\n");
            actual = actual.siguiente;
            indice++;
        }

        return sb.toString();
    }

    @FunctionalInterface
    public interface AccionLibro {

        void ejecutar(Libro libro, int indice);
    }
}
