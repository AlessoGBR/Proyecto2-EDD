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
        this(5);
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
        if (raiz == null) {
            raiz = new NodoB(orden, true);
            raiz.getClaves()[0] = new ColeccionLibro(libro);
            raiz.setNumClaves(1);
            return;
        }

        NodoB r = raiz;

        ColeccionLibro existente = buscarColeccion(raiz, libro.getAnioPublicacion(), libro.getIsbn());
        if (existente != null) {
            existente.agregarCopia(libro);
            return;
        }

        if (r.getNumClaves() == orden - 1) {
            NodoB nuevaRaiz = new NodoB(orden, false);
            nuevaRaiz.getHijos()[0] = r;
            dividirHijo(nuevaRaiz, 0, r);
            raiz = nuevaRaiz;
            insertarNoLleno(nuevaRaiz, libro);
        } else {
            insertarNoLleno(r, libro);
        }
    }

    private void insertarNoLleno(NodoB nodo, Libro libro) {
        int i = nodo.getNumClaves() - 1;

        if (nodo.isEsHoja()) {
            while (i >= 0 && libro.getAnioPublicacion() < nodo.getClaves()[i].getAnioPublicacion()) {
                nodo.getClaves()[i + 1] = nodo.getClaves()[i];
                i--;
            }
            nodo.getClaves()[i + 1] = new ColeccionLibro(libro);
            nodo.setNumClaves(nodo.getNumClaves() + 1);
        } else {
            while (i >= 0 && libro.getAnioPublicacion() < nodo.getClaves()[i].getAnioPublicacion()) {
                i--;
            }
            i++;

            if (nodo.getHijos()[i].getNumClaves() == orden - 1) {
                dividirHijo(nodo, i, nodo.getHijos()[i]);

                if (libro.getAnioPublicacion() > nodo.getClaves()[i].getAnioPublicacion()) {
                    i++;
                }
            }
            insertarNoLleno(nodo.getHijos()[i], libro);
        }
    }

    private void dividirHijo(NodoB padre, int indice, NodoB lleno) {
        NodoB nuevo = new NodoB(orden, lleno.isEsHoja());
        int t = (orden - 1) / 2;

        nuevo.setNumClaves(t);

        for (int j = 0; j < t; j++) {
            nuevo.getClaves()[j] = lleno.getClaves()[j + t + 1];
            lleno.getClaves()[j + t + 1] = null;
        }

        if (!lleno.isEsHoja()) {
            for (int j = 0; j <= t; j++) {
                nuevo.getHijos()[j] = lleno.getHijos()[j + t + 1];
                lleno.getHijos()[j + t + 1] = null;
            }
        }

        lleno.setNumClaves(t);

        for (int j = padre.getNumClaves(); j >= indice + 1; j--) {
            padre.getHijos()[j + 1] = padre.getHijos()[j];
        }
        padre.getHijos()[indice + 1] = nuevo;

        for (int j = padre.getNumClaves() - 1; j >= indice; j--) {
            padre.getClaves()[j + 1] = padre.getClaves()[j];
        }
        padre.getClaves()[indice] = lleno.getClaves()[t];
        lleno.getClaves()[t] = null;

        padre.setNumClaves(padre.getNumClaves() + 1);
    }

    private ColeccionLibro buscarColeccion(NodoB nodo, int anio, String isbn) {
        if (nodo == null) {
            return null;
        }

        int i = 0;
        while (i < nodo.getNumClaves() && anio > nodo.getClaves()[i].getAnioPublicacion()) {
            i++;
        }

        if (i < nodo.getNumClaves()
                && anio == nodo.getClaves()[i].getAnioPublicacion()
                && nodo.getClaves()[i].getIsbn().equals(isbn)) {
            return nodo.getClaves()[i];
        }

        if (nodo.isEsHoja()) {
            return null;
        }
        return buscarColeccion(nodo.getHijos()[i], anio, isbn);
    }

    public ColeccionLibro buscarPorAnio(int anio) {
        ColeccionLibro resultado = buscarEnNodo(raiz, anio);

        if (resultado != null) {
            mensaje = "COLECCION ENCONTRADA: " + resultado.getCopias().size()
                    + " LIBROS DEL ANIO " + anio;
        } else {
            mensaje = "NO SE ENCONTRO NINGUNA COLECCION CON EL ANIO " + anio;
        }
        return resultado;
    }

    private ColeccionLibro buscarEnNodo(NodoB nodo, int anio) {
        int i = 0;

        while (i < nodo.numClaves && anio > nodo.claves[i].getAnioPublicacion()) {
            i++;
        }

        if (i < nodo.numClaves && anio == nodo.claves[i].getAnioPublicacion()) {
            return nodo.claves[i];
        }

        if (nodo.esHoja) {
            return null;
        }

        return buscarEnNodo(nodo.hijos[i], anio);
    }

    public ArrayList buscarPorRangoAnios(int anioInicio, int anioFin) {
        ArrayList resultados = new ArrayList();
        buscarRangoEnNodo(raiz, anioInicio, anioFin, resultados);

        if (!resultados.isEmpty()) {
            mensaje = "SE ENCONTRARON " + resultados.size() + " LIBROS EN EL RANGO DE ANIOS";
        } else {
            mensaje = "NO SE ENCONTRARON LIBROS EN EL RANGO DE ANIOS ESPECIFICADO";
        }

        return resultados;
    }

    private void buscarRangoEnNodo(NodoB nodo, int anioInicio, int anioFin, ArrayList<Libro> resultados) {
        if (nodo == null) {
            return;
        }
        int i = 0;

        while (i < nodo.numClaves) {
            int anioClave = nodo.claves[i].getAnioPublicacion();

            if (!nodo.esHoja && anioClave > anioInicio) {
                buscarRangoEnNodo(nodo.hijos[i], anioInicio, anioFin, resultados);
            }

            if (anioClave >= anioInicio && anioClave <= anioFin) {
                ArrayList<Libro> libros = nodo.claves[i].getCopias();
                resultados.addAll(libros);
            }

            i++;
        }

        if (!nodo.esHoja) {
            buscarRangoEnNodo(nodo.hijos[i], anioInicio, anioFin, resultados);
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

        while (i < nodo.numClaves && anio > nodo.claves[i].getAnioPublicacion()) {
            i++;
        }

        if (i < nodo.numClaves && anio == nodo.claves[i].getAnioPublicacion()) {
            if (nodo.esHoja) {
                if (nodo.claves[i].getCopias().size() > 1) {
                    nodo.claves[i].getCopias().removeLast();
                } else {
                    eliminarDeHoja(nodo, i);
                }
            } else {
                eliminarDeNodoInterno(nodo, i);
            }
            return true;
        } else if (!nodo.esHoja) {
            boolean ultimoHijo = (i == nodo.numClaves);

            if (nodo.hijos[i].numClaves < orden / 2) {
                llenar(nodo, i);
            }

            if (ultimoHijo && i > nodo.numClaves) {
                return eliminarDeNodo(nodo.hijos[i - 1], anio);
            } else {
                return eliminarDeNodo(nodo.hijos[i], anio);
            }
        }

        return false;
    }

    private void eliminarDeHoja(NodoB nodo, int indice) {
        for (int j = indice + 1; j < nodo.numClaves; j++) {
            nodo.claves[j - 1] = nodo.claves[j];
        }
        nodo.claves[nodo.numClaves - 1] = null;
        nodo.numClaves--;
    }

    private void eliminarDeNodoInterno(NodoB nodo, int indice) {
        ColeccionLibro coleccion = nodo.claves[indice];

        if (nodo.hijos[indice].numClaves >= orden / 2) {
            ColeccionLibro pred = obtenerPredecesor(nodo, indice);
            nodo.claves[indice] = pred;
            eliminarDeNodo(nodo.hijos[indice], pred.getAnioPublicacion());
        } else if (nodo.hijos[indice + 1].numClaves >= orden / 2) {
            ColeccionLibro succ = obtenerSucesor(nodo, indice);
            nodo.claves[indice] = succ;
            eliminarDeNodo(nodo.hijos[indice + 1], succ.getAnioPublicacion());
        } else {
            fusionar(nodo, indice);
            eliminarDeNodo(nodo.hijos[indice], coleccion.getAnioPublicacion());
        }
    }

    private ColeccionLibro obtenerPredecesor(NodoB nodo, int indice) {
        NodoB actual = nodo.hijos[indice];
        while (!actual.esHoja) {
            actual = actual.hijos[actual.numClaves];
        }
        return actual.claves[actual.numClaves - 1];
    }

    private ColeccionLibro obtenerSucesor(NodoB nodo, int indice) {
        NodoB actual = nodo.hijos[indice + 1];
        while (!actual.esHoja) {
            actual = actual.hijos[0];
        }
        return actual.claves[0];
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
        if (nodo == null) {
            return;
        }

        int i;
        for (i = 0; i < nodo.numClaves; i++) {
            if (!nodo.esHoja) {
                recorridoInOrderNodo(nodo.hijos[i], accion);
            }

            ColeccionLibro coleccion = nodo.claves[i];
            if (coleccion != null && coleccion.getCopias() != null) {
                ArrayList<Libro> listaLibros = coleccion.getCopias();
                for (Libro libro : listaLibros) {
                    accion.ejecutar(libro);
                }
            }
        }

        if (!nodo.esHoja) {
            recorridoInOrderNodo(nodo.hijos[i], accion);
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
