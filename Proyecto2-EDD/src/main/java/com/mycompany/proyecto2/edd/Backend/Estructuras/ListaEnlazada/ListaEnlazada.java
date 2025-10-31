/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class ListaEnlazada {

    private NodoLista cabeza;
    private NodoLista cola;
    private int tamanio;
    public String mensaje;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void insertarAlInicio(Libro libro) {
        NodoLista nuevoNodo = new NodoLista(libro);

        if (estaVacia()) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            nuevoNodo.siguiente = cabeza;
            cabeza = nuevoNodo;
        }
        tamanio++;
    }

    public void insertarAlFinal(Libro libro) {
        NodoLista nuevoNodo = new NodoLista(libro);

        if (estaVacia()) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            cola = nuevoNodo;
        }
        tamanio++;
    }

    public void insertarEnPosicion(Libro libro, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            this.mensaje = "POSICION INVALIDA " + posicion;
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }

        if (posicion == 0) {
            insertarAlInicio(libro);
            return;
        }

        if (posicion == tamanio) {
            insertarAlFinal(libro);
            return;
        }

        NodoLista nuevoNodo = new NodoLista(libro);
        NodoLista actual = cabeza;

        for (int i = 0; i < posicion - 1; i++) {
            actual = actual.siguiente;
        }

        nuevoNodo.siguiente = actual.siguiente;
        actual.siguiente = nuevoNodo;
        tamanio++;
    }

    public Libro eliminarPrimero() {
        if (estaVacia()) {
            this.mensaje = "NO SE PUEDE ELIMINAR EN LISTA VACIA";
            throw new RuntimeException("No se puede eliminar de una lista vacía");
        }

        Libro libro = cabeza.libro;
        cabeza = cabeza.siguiente;
        tamanio--;

        if (estaVacia()) {
            cola = null;
        }

        return libro;
    }

    public Libro eliminarUltimo() {
        if (estaVacia()) {
            this.mensaje = "NO SE PUEDE ELIMINAR EN LISTA VACIA";
            throw new RuntimeException("No se puede eliminar de una lista vacía");
        }

        if (tamanio == 1) {
            Libro libro = cabeza.libro;
            cabeza = null;
            cola = null;
            tamanio = 0;
            return libro;
        }

        NodoLista actual = cabeza;
        while (actual.siguiente != cola) {
            actual = actual.siguiente;
        }

        Libro libro = cola.libro;
        actual.siguiente = null;
        cola = actual;
        tamanio--;

        return libro;
    }

    public Libro eliminarEnPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            this.mensaje = "POSICION INVALIDA: " + posicion;
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }

        if (posicion == 0) {
            return eliminarPrimero();
        }

        NodoLista actual = cabeza;
        for (int i = 0; i < posicion - 1; i++) {
            actual = actual.siguiente;
        }

        Libro libro = actual.siguiente.libro;
        actual.siguiente = actual.siguiente.siguiente;

        if (posicion == tamanio - 1) {
            cola = actual;
        }

        tamanio--;
        return libro;
    }

    public boolean eliminarPorISBN(String isbn) {
        if (estaVacia()) {
            return false;
        }

        if (cabeza.libro.getIsbn().equals(isbn)) {
            eliminarPrimero();
            return true;
        }

        NodoLista actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.libro.getIsbn().equals(isbn)) {
                Libro libroEliminado = actual.siguiente.libro;
                actual.siguiente = actual.siguiente.siguiente;

                if (actual.siguiente == null) {
                    cola = actual;
                }

                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }

        return false;
    }

    public Libro buscarPorISBN(String isbn) {
        NodoLista actual = cabeza;

        while (actual != null) {
            if (actual.libro.getIsbn().equals(isbn)) {
                return actual.libro;
            }
            actual = actual.siguiente;
        }

        return null;
    }

    public Libro buscarPorTitulo(String titulo) {
        NodoLista actual = cabeza;

        while (actual != null) {
            if (actual.libro.getTitulo().equalsIgnoreCase(titulo)) {
                return actual.libro;
            }
            actual = actual.siguiente;
        }

        return null;
    }

    public ListaEnlazada buscarPorAutor(String autor) {
        ListaEnlazada resultados = new ListaEnlazada();
        NodoLista actual = cabeza;

        while (actual != null) {
            if (actual.libro.getAutor().equalsIgnoreCase(autor)) {
                resultados.insertarAlFinal(actual.libro);
            }
            actual = actual.siguiente;
        }

        return resultados;
    }

    public ListaEnlazada buscarPorGenero(String genero) {
        ListaEnlazada resultados = new ListaEnlazada();
        NodoLista actual = cabeza;

        while (actual != null) {
            if (actual.libro.getGenero().equalsIgnoreCase(genero)) {
                resultados.insertarAlFinal(actual.libro);
            }
            actual = actual.siguiente;
        }

        return resultados;
    }

    public ListaEnlazada buscarPorEstado(Libro.EstadoLibro estado) {
        ListaEnlazada resultados = new ListaEnlazada();
        NodoLista actual = cabeza;

        while (actual != null) {
            if (actual.libro.getEstado() == estado) {
                resultados.insertarAlFinal(actual.libro);
            }
            actual = actual.siguiente;
        }

        return resultados;
    }

    public Libro obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            this.mensaje = "POSICION INVALIDA: " + posicion;
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }

        NodoLista actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        return actual.libro;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void limpiar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    public Libro[] toArray() {
        Libro[] arreglo = new Libro[tamanio];
        NodoLista actual = cabeza;
        int indice = 0;

        while (actual != null) {
            arreglo[indice++] = actual.libro;
            actual = actual.siguiente;
        }

        return arreglo;
    }

    public void recorrer(AccionLibro accion) {
        NodoLista actual = cabeza;
        int indice = 0;

        while (actual != null) {
            accion.ejecutar(actual.libro, indice);
            actual = actual.siguiente;
            indice++;
        }
    }

    public NodoLista getCabeza() {
        return cabeza;
    }

    public void setCabeza(NodoLista cabeza) {
        this.cabeza = cabeza;
    }

    public NodoLista getCola() {
        return cola;
    }

    public void setCola(NodoLista cola) {
        this.cola = cola;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "LISTA VACIA";
        }

        StringBuilder sb = new StringBuilder("LISTA DE LIBROS [" + tamanio + " ELEMENTOS]:\n");
        NodoLista actual = cabeza;
        int indice = 0;

        while (actual != null) {
            sb.append("  ").append(indice).append(". ")
                    .append(actual.libro.getTitulo())
                    .append(" - ").append(actual.libro.getAutor())
                    .append(" (").append(actual.libro.getIsbn()).append(")\n");
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
