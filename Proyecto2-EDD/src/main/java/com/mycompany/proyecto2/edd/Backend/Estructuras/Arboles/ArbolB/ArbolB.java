/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB;

import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class ArbolB {

    private String mensaje;
    private int orden;
    private NodoB raiz;
    private int tamanio;

    public ArbolB(int orden) {
        if (orden < 3) {
            this.mensaje = "EL ORDEN DEL ARBOL B DEBE SER AL MENOS 3";
            this.orden = 3;
        } else {
            this.orden = orden;
            this.mensaje = "ARBOL B CREADO CORRECTAMENTE CON ORDEN " + orden;
        }
        this.raiz = new NodoB(this.orden, true);
        this.tamanio = 0;
    }

    public ArbolB() {
        this(3);
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

    public NodoB getRaiz() {
        return raiz;
    }

    public int getOrden() {
        return orden;
    }

    public void insertar(Libro libro) {
        if (libro == null) {
            mensaje = "ERROR: NO SE PUEDE INSERTAR UN LIBRO NULO";
            return;
        }

        if (buscarPorAnio(libro.getAnioPublicacion()) != null) {
            mensaje = "ERROR: YA EXISTE UN LIBRO CON EL ANIO " + libro.getAnioPublicacion();
            return;
        }

        if (raiz.getNumClaves() == orden - 1) {
            NodoB nuevaRaiz = new NodoB(orden, false);
            nuevaRaiz.getHijos()[0] = raiz;
            dividirHijo(nuevaRaiz, 0);
            raiz = nuevaRaiz;
        }

        insertarNoLleno(raiz, libro);
        tamanio++;
        mensaje = "LIBRO INSERTADO CORRECTAMENTE: " + libro.getTitulo();
    }

    private void insertarNoLleno(NodoB nodo, Libro libro) {
        int i = nodo.getNumClaves() - 1;

        if (nodo.isEsHoja()) {
            while (i >= 0 && libro.getAnioPublicacion() < nodo.getClaves()[i].getAnioPublicacion()) {
                nodo.getClaves()[i + 1] = nodo.getClaves()[i];
                i--;
            }
            nodo.getClaves()[i + 1] = libro;
            nodo.numClaves++;
        } else {
            while (i >= 0 && libro.getAnioPublicacion() < nodo.getClaves()[i].getAnioPublicacion()) {
                i--;
            }
            i++;

            if (nodo.getHijos()[i].numClaves == orden - 1) {
                dividirHijo(nodo, i);
                if (libro.getAnioPublicacion() > nodo.getClaves()[i].getAnioPublicacion()) {
                    i++;
                }
            }
            insertarNoLleno(nodo.getHijos()[i], libro);
        }
    }

    private void dividirHijo(NodoB padre, int indice) {
        NodoB nodoLleno = padre.getHijos()[indice];
        NodoB nuevoNodo = new NodoB(orden, nodoLleno.isEsHoja());

        int mitad = orden / 2;
        nuevoNodo.numClaves = orden - mitad - 1;

        for (int j = 0; j < nuevoNodo.numClaves; j++) {
            nuevoNodo.getClaves()[j] = nodoLleno.getClaves()[j + mitad + 1];
        }

        if (!nodoLleno.isEsHoja()) {
            for (int j = 0; j < orden - mitad; j++) {
                nuevoNodo.getHijos()[j] = nodoLleno.getHijos()[j + mitad + 1];
            }
        }

        nodoLleno.numClaves = mitad;

        for (int j = padre.numClaves; j > indice; j--) {
            padre.getHijos()[j + 1] = padre.getHijos()[j];
        }
        padre.getHijos()[indice + 1] = nuevoNodo;

        for (int j = padre.numClaves - 1; j >= indice; j--) {
            padre.getClaves()[j + 1] = padre.getClaves()[j];
        }
        padre.getClaves()[indice] = nodoLleno.getClaves()[mitad];
        padre.numClaves++;
    }

    public Libro buscarPorAnio(int anio) {
        Libro resultado = buscarEnNodo(raiz, anio);
        if (resultado != null) {
            mensaje = "LIBRO ENCONTRADO: " + resultado.getTitulo();
        } else {
            mensaje = "NO SE ENCONTRO NINGUN LIBRO CON EL ANIO " + anio;
        }
        return resultado;
    }

    private Libro buscarEnNodo(NodoB nodo, int anio) {
        int i = 0;
        while (i < nodo.numClaves && anio > nodo.getClaves()[i].getAnioPublicacion()) {
            i++;
        }

        if (i < nodo.numClaves && anio == nodo.getClaves()[i].getAnioPublicacion()) {
            return nodo.getClaves()[i];
        }

        if (nodo.isEsHoja()) {
            return null;
        }

        return buscarEnNodo(nodo.getHijos()[i], anio);
    }

    public ListaEnlazada buscarPorRangoAnios(int anioInicio, int anioFin) {
        ListaEnlazada resultados = new ListaEnlazada();
        buscarRangoEnNodo(raiz, anioInicio, anioFin, resultados);

        if (resultados.getTamanio() > 0) {
            mensaje = "SE ENCONTRARON " + resultados.getTamanio() + " LIBROS EN EL RANGO DE ANIOS";
        } else {
            mensaje = "NO SE ENCONTRARON LIBROS EN EL RANGO DE ANIOS ESPECIFICADO";
        }

        return resultados;
    }

    private void buscarRangoEnNodo(NodoB nodo, int anioInicio, int anioFin, ListaEnlazada resultados) {
        int i = 0;

        while (i < nodo.numClaves) {
            if (!nodo.isEsHoja() && nodo.getClaves()[i].getAnioPublicacion() > anioInicio) {
                buscarRangoEnNodo(nodo.getHijos()[i], anioInicio, anioFin, resultados);
            }

            if (nodo.getClaves()[i].getAnioPublicacion() >= anioInicio
                    && nodo.getClaves()[i].getAnioPublicacion() <= anioFin) {
                resultados.insertarAlFinal(nodo.getClaves()[i]);
            }

            i++;
        }

        if (!nodo.isEsHoja()) {
            buscarRangoEnNodo(nodo.getHijos()[i], anioInicio, anioFin, resultados);
        }
    }

    public boolean eliminar(int anio) {
        if (raiz == null) {
            mensaje = "ERROR: EL ARBOL ESTA VACIO";
            return false;
        }

        boolean resultado = eliminarDeNodo(raiz, anio);

        if (raiz.numClaves == 0) {
            if (!raiz.isEsHoja()) {
                raiz = raiz.getHijos()[0];
            }
        }

        if (resultado) {
            tamanio--;
            mensaje = "LIBRO ELIMINADO CORRECTAMENTE DEL ANIO " + anio;
        } else {
            mensaje = "NO SE ENCONTRO NINGUN LIBRO CON EL ANIO " + anio;
        }

        return resultado;
    }

    private boolean eliminarDeNodo(NodoB nodo, int anio) {
        int i = 0;
        while (i < nodo.numClaves && anio > nodo.getClaves()[i].getAnioPublicacion()) {
            i++;
        }

        if (i < nodo.numClaves && anio == nodo.getClaves()[i].getAnioPublicacion()) {
            if (nodo.isEsHoja()) {
                eliminarDeHoja(nodo, i);
            } else {
                eliminarDeNodoInterno(nodo, i);
            }
            return true;
        } else if (!nodo.isEsHoja()) {
            boolean estaEnUltimoHijo = (i == nodo.numClaves);

            if (nodo.getHijos()[i].numClaves < orden / 2) {
                llenar(nodo, i);
            }

            if (estaEnUltimoHijo && i > nodo.numClaves) {
                return eliminarDeNodo(nodo.getHijos()[i - 1], anio);
            } else {
                return eliminarDeNodo(nodo.getHijos()[i], anio);
            }
        }

        return false;
    }

    private void eliminarDeHoja(NodoB nodo, int indice) {
        for (int i = indice + 1; i < nodo.numClaves; i++) {
            nodo.getClaves()[i - 1] = nodo.getClaves()[i];
        }
        nodo.numClaves--;
    }

    private void eliminarDeNodoInterno(NodoB nodo, int indice) {
        Libro libro = nodo.getClaves()[indice];

        if (nodo.getHijos()[indice].numClaves >= orden / 2) {
            Libro pred = obtenerPredecesor(nodo, indice);
            nodo.getClaves()[indice] = pred;
            eliminarDeNodo(nodo.getHijos()[indice], pred.getAnioPublicacion());
        } else if (nodo.getHijos()[indice + 1].numClaves >= orden / 2) {
            Libro succ = obtenerSucesor(nodo, indice);
            nodo.getClaves()[indice] = succ;
            eliminarDeNodo(nodo.getHijos()[indice + 1], succ.getAnioPublicacion());
        } else {
            fusionar(nodo, indice);
            eliminarDeNodo(nodo.getHijos()[indice], libro.getAnioPublicacion());
        }
    }

    private Libro obtenerPredecesor(NodoB nodo, int indice) {
        NodoB actual = nodo.getHijos()[indice];
        while (!actual.isEsHoja()) {
            actual = actual.getHijos()[actual.numClaves];
        }
        return actual.getClaves()[actual.numClaves - 1];
    }

    private Libro obtenerSucesor(NodoB nodo, int indice) {
        NodoB actual = nodo.getHijos()[indice + 1];
        while (!actual.isEsHoja()) {
            actual = actual.getHijos()[0];
        }
        return actual.getClaves()[0];
    }

    private void llenar(NodoB nodo, int indice) {
        if (indice != 0 && nodo.getHijos()[indice - 1].numClaves >= orden / 2) {
            tomarDelAnterior(nodo, indice);
        } else if (indice != nodo.numClaves && nodo.getHijos()[indice + 1].numClaves >= orden / 2) {
            tomarDelSiguiente(nodo, indice);
        } else {
            if (indice != nodo.numClaves) {
                fusionar(nodo, indice);
            } else {
                fusionar(nodo, indice - 1);
            }
        }
    }

    private void tomarDelAnterior(NodoB nodo, int indice) {
        NodoB hijo = nodo.getHijos()[indice];
        NodoB hermano = nodo.getHijos()[indice - 1];

        for (int i = hijo.numClaves - 1; i >= 0; i--) {
            hijo.getClaves()[i + 1] = hijo.getClaves()[i];
        }

        if (!hijo.isEsHoja()) {
            for (int i = hijo.numClaves; i >= 0; i--) {
                hijo.getHijos()[i + 1] = hijo.getHijos()[i];
            }
        }

        hijo.getClaves()[0] = nodo.getClaves()[indice - 1];

        if (!hijo.isEsHoja()) {
            hijo.getHijos()[0] = hermano.getHijos()[hermano.numClaves];
        }

        nodo.getClaves()[indice - 1] = hermano.getClaves()[hermano.numClaves - 1];

        hijo.numClaves++;
        hermano.numClaves--;
    }

    private void tomarDelSiguiente(NodoB nodo, int indice) {
        NodoB hijo = nodo.getHijos()[indice];
        NodoB hermano = nodo.getHijos()[indice + 1];

        hijo.getClaves()[hijo.numClaves] = nodo.getClaves()[indice];

        if (!hijo.isEsHoja()) {
            hijo.getHijos()[hijo.numClaves + 1] = hermano.getHijos()[0];
        }

        nodo.getClaves()[indice] = hermano.getClaves()[0];

        for (int i = 1; i < hermano.numClaves; i++) {
            hermano.getClaves()[i - 1] = hermano.getClaves()[i];
        }

        if (!hermano.isEsHoja()) {
            for (int i = 1; i <= hermano.numClaves; i++) {
                hermano.getHijos()[i - 1] = hermano.getHijos()[i];
            }
        }

        hijo.numClaves++;
        hermano.numClaves--;
    }

    private void fusionar(NodoB nodo, int indice) {
        NodoB hijo = nodo.getHijos()[indice];
        NodoB hermano = nodo.getHijos()[indice + 1];

        hijo.getClaves()[hijo.numClaves] = nodo.getClaves()[indice];

        for (int i = 0; i < hermano.numClaves; i++) {
            hijo.getClaves()[i + hijo.numClaves + 1] = hermano.getClaves()[i];
        }

        if (!hijo.isEsHoja()) {
            for (int i = 0; i <= hermano.numClaves; i++) {
                hijo.getHijos()[i + hijo.numClaves + 1] = hermano.getHijos()[i];
            }
        }

        for (int i = indice + 1; i < nodo.numClaves; i++) {
            nodo.getClaves()[i - 1] = nodo.getClaves()[i];
        }

        for (int i = indice + 2; i <= nodo.numClaves; i++) {
            nodo.getHijos()[i - 1] = nodo.getHijos()[i];
        }

        hijo.numClaves += hermano.numClaves + 1;
        nodo.numClaves--;
    }

    public void recorridoInOrder(AccionLibro accion) {
        recorridoInOrderNodo(raiz, accion);
    }

    private void recorridoInOrderNodo(NodoB nodo, AccionLibro accion) {
        int i;
        for (i = 0; i < nodo.numClaves; i++) {
            if (!nodo.isEsHoja()) {
                recorridoInOrderNodo(nodo.getHijos()[i], accion);
            }
            accion.ejecutar(nodo.getClaves()[i]);
        }

        if (!nodo.isEsHoja()) {
            recorridoInOrderNodo(nodo.getHijos()[i], accion);
        }
    }

    public void limpiar() {
        raiz = new NodoB(orden, true);
        tamanio = 0;
        mensaje = "ARBOL B LIMPIADO CORRECTAMENTE";
    }

    @FunctionalInterface
    public interface AccionLibro {

        void ejecutar(Libro libro);
    }
}
