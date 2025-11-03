/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.ColeccionLibro;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import java.util.ArrayList;

/**
 *
 * @author alesso
 */
public class ArbolBPlus {

    private String mensaje;
    private int orden;
    private NodoBPlus raiz;
    private int tamanio;

    public ArbolBPlus(int orden) {
        if (orden < 3) {
            this.mensaje = "EL ORDEN DEL ARBOL B+ DEBE SER AL MENOS 3";
            this.orden = 3;
        } else {
            this.orden = orden;
            this.mensaje = "ARBOL B+ CREADO CORRECTAMENTE CON ORDEN " + orden;
        }
        this.raiz = new NodoBPlus(this.orden, true);
        this.tamanio = 0;
    }

    public ArbolBPlus() {
        this(10);
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacio() {
        return tamanio == 0;
    }

    public NodoBPlus getRaiz() {
        return raiz;
    }

    public int getOrden() {
        return orden;
    }

    public void insertar(Libro libro) {
        if (raiz == null) {
            raiz = new NodoBPlus(orden, true);
            raiz.getClaves()[0] = new ColeccionLibro(libro);
            raiz.setNumClaves(1);
            tamanio++;
            return;
        }

        NodoBPlus nodo = raiz;

        if (nodo.getNumClaves() == orden - 1) {
            NodoBPlus nuevaRaiz = new NodoBPlus(orden, false);
            nuevaRaiz.getHijos()[0] = nodo;
            dividirNodo(nuevaRaiz, 0, nodo);
            raiz = nuevaRaiz;
            insertarEnNodo(nuevaRaiz, libro);
        } else {
            insertarEnNodo(nodo, libro);
        }

        tamanio++;
    }

    private void insertarEnNodo(NodoBPlus nodo, Libro libro) {
        if (nodo.isEsHoja()) {
            int i = 0;
            while (i < nodo.getNumClaves() && libro.getAnioPublicacion() > nodo.getClaves()[i].getAnioPublicacion()) {
                i++;
            }

            if (i < nodo.getNumClaves() && nodo.getClaves()[i].mismoGrupo(libro)) {
                nodo.getClaves()[i].agregarCopia(libro);
                return;
            }

            for (int j = nodo.getNumClaves(); j > i; j--) {
                nodo.getClaves()[j] = nodo.getClaves()[j - 1];
            }
            nodo.getClaves()[i] = new ColeccionLibro(libro);
            nodo.setNumClaves(nodo.getNumClaves() + 1);

            if (nodo.getNumClaves() == orden) {
                dividirHoja(nodo);
            }
        } else {
            int i = nodo.getNumClaves() - 1;
            while (i >= 0 && libro.getAnioPublicacion() < nodo.getClaves()[i].getAnioPublicacion()) {
                i--;
            }
            i++;

            if (nodo.getHijos()[i].getNumClaves() == orden - 1) {
                dividirNodo(nodo, i, nodo.getHijos()[i]);
                if (libro.getAnioPublicacion() > nodo.getClaves()[i].getAnioPublicacion()) {
                    i++;
                }
            }
            insertarEnNodo(nodo.getHijos()[i], libro);
        }
    }

    private void dividirNodo(NodoBPlus padre, int indiceHijo, NodoBPlus hijo) {
        int mitad = (orden - 1) / 2;
        NodoBPlus nuevoNodo = new NodoBPlus(orden, hijo.isEsHoja());

        for (int i = mitad + 1, j = 0; i < hijo.getNumClaves(); i++, j++) {
            nuevoNodo.getClaves()[j] = hijo.getClaves()[i];
            hijo.getClaves()[i] = null;
            nuevoNodo.setNumClaves(nuevoNodo.getNumClaves() + 1);
        }

        hijo.setNumClaves(mitad);

        if (!hijo.isEsHoja()) {
            for (int i = mitad + 1, j = 0; i <= orden; i++, j++) {
                nuevoNodo.getHijos()[j] = hijo.getHijos()[i];
                hijo.getHijos()[i] = null;
            }
        } else {
            nuevoNodo.setSiguiente(hijo.getSiguiente());
            hijo.setSiguiente(nuevoNodo);
        }

        for (int i = padre.getNumClaves(); i > indiceHijo; i--) {
            padre.getClaves()[i] = padre.getClaves()[i - 1];
            padre.getHijos()[i + 1] = padre.getHijos()[i];
        }

        padre.getClaves()[indiceHijo] = hijo.getClaves()[mitad];
        padre.getHijos()[indiceHijo + 1] = nuevoNodo;
        padre.setNumClaves(padre.getNumClaves() + 1);
    }

    private void dividirHoja(NodoBPlus hoja) {
        int mitad = (orden + 1) / 2;
        NodoBPlus nuevaHoja = new NodoBPlus(orden, true);

        int j = 0;
        for (int i = mitad; i < hoja.getNumClaves(); i++) {
            nuevaHoja.getClaves()[j++] = hoja.getClaves()[i];
            hoja.getClaves()[i] = null;
        }

        nuevaHoja.setNumClaves(j);
        hoja.setNumClaves(mitad);

        nuevaHoja.setSiguiente(hoja.getSiguiente());
        hoja.setSiguiente(nuevaHoja);

        if (hoja == raiz) {
            NodoBPlus nuevoPadre = new NodoBPlus(orden, false);
            nuevoPadre.getClaves()[0] = nuevaHoja.getClaveMinima();
            nuevoPadre.getHijos()[0] = hoja;
            nuevoPadre.getHijos()[1] = nuevaHoja;
            nuevoPadre.setNumClaves(1);
            raiz = nuevoPadre;
        } else {
            insertarClaveEnPadre(hoja, nuevaHoja.getClaveMinima(), nuevaHoja);
        }
    }

    private void insertarClaveEnPadre(NodoBPlus hijoIzq, ColeccionLibro clave, NodoBPlus hijoDer) {
        if (hijoIzq == raiz) {
            NodoBPlus nuevoPadre = new NodoBPlus(orden, false);
            nuevoPadre.getClaves()[0] = clave;
            nuevoPadre.getHijos()[0] = hijoIzq;
            nuevoPadre.getHijos()[1] = hijoDer;
            nuevoPadre.setNumClaves(1);
            raiz = nuevoPadre;
            return;
        }

        NodoBPlus padre = buscarPadre(raiz, hijoIzq);

        int i = 0;
        while (i < padre.getNumClaves() && padre.getHijos()[i] != hijoIzq) {
            i++;
        }

        for (int j = padre.getNumClaves(); j > i; j--) {
            padre.getClaves()[j] = padre.getClaves()[j - 1];
            padre.getHijos()[j + 1] = padre.getHijos()[j];
        }

        padre.getClaves()[i] = clave;
        padre.getHijos()[i + 1] = hijoDer;
        padre.setNumClaves(padre.getNumClaves() + 1);

        if (padre.getNumClaves() == orden) {
            dividirNodo(buscarPadre(raiz, padre), 0, padre);
        }
    }

    private NodoBPlus buscarPadre(NodoBPlus actual, NodoBPlus hijo) {
        if (actual == null || actual.isEsHoja()) {
            return null;
        }

        for (int i = 0; i <= actual.getNumClaves(); i++) {
            if (actual.getHijos()[i] == hijo) {
                return actual;
            } else {
                NodoBPlus padre = buscarPadre(actual.getHijos()[i], hijo);
                if (padre != null) {
                    return padre;
                }
            }
        }
        return null;
    }

    private ColeccionLibro buscarColeccion(NodoBPlus nodo, int anio, String isbn) {
        if (nodo == null) {
            return null;
        }

        int i = 0;
        while (i < nodo.getNumClaves() && anio > nodo.getClaves()[i].getAnioPublicacion()) {
            i++;
        }

        if (nodo.isEsHoja()) {
            if (i < nodo.getNumClaves()) {
                ColeccionLibro c = nodo.getClaves()[i];
                if (c.getAnioPublicacion() == anio && c.getIsbn().equals(isbn)) {
                    return c;
                }
            }
            return null;
        } else {
            return buscarColeccion(nodo.getHijos()[i], anio, isbn);
        }
    }

    public ArrayList<Libro> buscarPorGenero(String genero) {
        ArrayList<Libro> resultados = new ArrayList<>();
        NodoBPlus hoja = obtenerPrimeraHoja();

        genero = genero.toUpperCase();

        while (hoja != null) {
            for (int i = 0; i < hoja.getNumClaves(); i++) {
                ColeccionLibro coleccion = hoja.getClaves()[i];
                if (coleccion != null) {
                    for (Libro libro : coleccion.getCopias()) {
                        if (libro.getGenero().toUpperCase().equals(genero)) {
                            resultados.add(libro);
                        }
                    }
                }
            }
            hoja = hoja.getSiguiente();
        }

        if (resultados.isEmpty()) {
            mensaje = "NO SE ENCONTRARON LIBROS DEL GENERO " + genero;
        } else {
            mensaje = "SE ENCONTRARON " + resultados.size() + " LIBROS DEL GENERO " + genero;
        }

        return resultados;
    }

    public ArrayList<Libro> obtenerTodosLosLibros() {
        ArrayList<Libro> todos = new ArrayList<>();
        NodoBPlus hoja = obtenerPrimeraHoja();

        while (hoja != null) {
            for (int i = 0; i < hoja.getNumClaves(); i++) {
                ColeccionLibro coleccion = hoja.getClaves()[i];
                if (coleccion != null) {
                    todos.addAll(coleccion.getCopias());
                }
            }
            hoja = hoja.getSiguiente();
        }

        mensaje = "SE OBTUVIERON " + todos.size() + " LIBROS EN TOTAL";
        return todos;
    }

    public String[] obtenerGeneros() {
        ArrayList<String> generos = new ArrayList<>();
        NodoBPlus hoja = obtenerPrimeraHoja();

        while (hoja != null) {
            for (int i = 0; i < hoja.getNumClaves(); i++) {
                ColeccionLibro coleccion = hoja.getClaves()[i];
                if (coleccion != null) {
                    for (Libro libro : coleccion.getCopias()) {
                        String genero = libro.getGenero().toUpperCase();
                        if (!generos.contains(genero)) {
                            generos.add(genero);
                        }
                    }
                }
            }
            hoja = hoja.getSiguiente();
        }

        return generos.toArray(new String[0]);
    }

    public boolean eliminarPorGeneroYIsbn(String genero, String isbn) {
        boolean eliminado = false;
        NodoBPlus hoja = obtenerPrimeraHoja();
        genero = genero.toUpperCase();

        while (hoja != null) {
            for (int i = 0; i < hoja.getNumClaves(); i++) {
                ColeccionLibro coleccion = hoja.getClaves()[i];
                if (coleccion != null) {
                    ArrayList<Libro> libros = coleccion.getCopias();
                    for (int j = 0; j < libros.size(); j++) {
                        Libro libro = libros.get(j);
                        if (libro.getGenero().toUpperCase().equals(genero)
                                && libro.getIsbn().equals(isbn)) {
                            libros.remove(j);
                            eliminado = true;
                            mensaje = "LIBRO ELIMINADO CORRECTAMENTE: " + libro.getTitulo();
                            break;
                        }
                    }
                }
            }
            hoja = hoja.getSiguiente();
        }

        if (!eliminado) {
            mensaje = "ERROR: NO SE ENCONTRO EL LIBRO CON ISBN " + isbn + " EN EL GENERO " + genero;
        }

        return eliminado;
    }

    public void recorridoPorGeneros(AccionGenero accion) {
        NodoBPlus hoja = obtenerPrimeraHoja();

        while (hoja != null) {
            for (int i = 0; i < hoja.getNumClaves(); i++) {
                ColeccionLibro coleccion = hoja.getClaves()[i];
                if (coleccion != null) {
                    for (Libro libro : coleccion.getCopias()) {
                        accion.ejecutar(libro.getGenero(), coleccion.getCopias());
                    }
                }
            }
            hoja = hoja.getSiguiente();
        }
    }

    public void recorridoTodosLosLibros(AccionLibro accion) {
        NodoBPlus hoja = obtenerPrimeraHoja();

        while (hoja != null) {
            for (int i = 0; i < hoja.getNumClaves(); i++) {
                ColeccionLibro coleccion = hoja.getClaves()[i];
                if (coleccion != null) {
                    for (Libro libro : coleccion.getCopias()) {
                        accion.ejecutar(libro);
                    }
                }
            }
            hoja = hoja.getSiguiente();
        }
    }

    private NodoBPlus obtenerPrimeraHoja() {
        NodoBPlus actual = raiz;
        while (actual != null && !actual.isEsHoja()) {
            actual = actual.getHijos()[0];
        }
        return actual;
    }

    public void limpiar() {
        raiz = new NodoBPlus(orden, true);
        tamanio = 0;
        mensaje = "ARBOL B+ LIMPIADO CORRECTAMENTE";
    }

    @FunctionalInterface
    public interface AccionLibro {

        void ejecutar(Libro libro);
    }

    @FunctionalInterface
    public interface AccionGenero {

        void ejecutar(String genero, ArrayList<Libro> libros);
    }
}
