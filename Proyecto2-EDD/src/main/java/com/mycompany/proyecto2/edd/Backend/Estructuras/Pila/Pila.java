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
public class Pila {

    private String mensaje;

    private NodoPila tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
        this.mensaje = "PILA CREADA CORRECTAMENTE";
    }

    public String getMensaje() {
        return mensaje;
    }

    public void apilar(Libro libro) {
        if (libro == null) {
            mensaje = "ERROR: NO SE PUEDE APILAR UN LIBRO NULO";
            return;
        }

        NodoPila nuevoNodo = new NodoPila(libro);
        nuevoNodo.siguiente = tope;
        tope = nuevoNodo;
        tamanio++;
        mensaje = "LIBRO APILADO CORRECTAMENTE: " + libro.getTitulo();
    }

    public Libro desapilar() {
        if (estaVacia()) {
            mensaje = "ERROR: LA PILA ESTA VACIA, NO SE PUEDE DESAPILAR";
            return null;
        }

        Libro libro = tope.libro;
        tope = tope.siguiente;
        tamanio--;
        mensaje = "LIBRO DESAPILADO CORRECTAMENTE: " + libro.getTitulo();
        return libro;
    }

    public Libro verTope() {
        if (estaVacia()) {
            mensaje = "ERROR: LA PILA ESTA VACIA";
            return null;
        }

        mensaje = "TOPE DE LA PILA: " + tope.libro.getTitulo();
        return tope.libro;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void limpiar() {
        tope = null;
        tamanio = 0;
        mensaje = "PILA LIMPIADA CORRECTAMENTE";
    }

    public Libro[] toArray() {
        Libro[] arreglo = new Libro[tamanio];
        NodoPila actual = tope;
        int indice = 0;

        while (actual != null) {
            arreglo[indice++] = actual.libro;
            actual = actual.siguiente;
        }

        return arreglo;
    }

    public void recorrer(AccionLibro accion) {
        NodoPila actual = tope;
        int indice = 0;

        while (actual != null) {
            accion.ejecutar(actual.libro, indice);
            actual = actual.siguiente;
            indice++;
        }
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "PILA VACIA []";
        }

        StringBuilder sb = new StringBuilder("PILA [" + tamanio + " ELEMENTOS]:\n");
        NodoPila actual = tope;
        int indice = 0;

        while (actual != null) {
            sb.append("  ").append(indice).append(". ")
                    .append(actual.libro.getTitulo())
                    .append(" - ").append(actual.libro.getAutor())
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
