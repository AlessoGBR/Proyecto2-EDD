/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus;

import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

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
    
    public NodoBPlus getRaiz() {
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
        
        String genero = libro.getGenero().toUpperCase();
        
        if (raiz.numClaves == orden - 1) {
            NodoBPlus nuevaRaiz = new NodoBPlus(orden, false);
            nuevaRaiz.hijos[0] = raiz;
            dividirHijo(nuevaRaiz, 0);
            raiz = nuevaRaiz;
        }
        
        insertarNoLleno(raiz, genero, libro);
        tamanio++;
        mensaje = "LIBRO INSERTADO CORRECTAMENTE: " + libro.getTitulo();
    }
    
    private void insertarNoLleno(NodoBPlus nodo, String clave, Libro libro) {
        int i = nodo.numClaves - 1;
        
        if (nodo.esHoja) {
            while (i >= 0 && clave.compareTo(nodo.claves[i]) < 0) {
                nodo.claves[i + 1] = nodo.claves[i];
                nodo.valores[i + 1] = nodo.valores[i];
                i--;
            }
            
            if (i >= 0 && clave.equals(nodo.claves[i])) {
                ListaEnlazada lista = (ListaEnlazada) nodo.valores[i];
                lista.insertarAlFinal(libro);
            } else {
                nodo.claves[i + 1] = clave;
                ListaEnlazada lista = new ListaEnlazada();
                lista.insertarAlFinal(libro);
                nodo.valores[i + 1] = lista;
                nodo.numClaves++;
            }
        } else {
            while (i >= 0 && clave.compareTo(nodo.claves[i]) < 0) {
                i--;
            }
            i++;
            
            if (nodo.hijos[i].numClaves == orden - 1) {
                dividirHijo(nodo, i);
                if (clave.compareTo(nodo.claves[i]) > 0) {
                    i++;
                }
            }
            insertarNoLleno(nodo.hijos[i], clave, libro);
        }
    }
    
    private void dividirHijo(NodoBPlus padre, int indice) {
        NodoBPlus nodoLleno = padre.hijos[indice];
        NodoBPlus nuevoNodo = new NodoBPlus(orden, nodoLleno.esHoja);
        
        int mitad = orden / 2;
        nuevoNodo.numClaves = orden - mitad - 1;
        
        if (nodoLleno.esHoja) {
            for (int j = 0; j < nuevoNodo.numClaves; j++) {
                nuevoNodo.claves[j] = nodoLleno.claves[j + mitad];
                nuevoNodo.valores[j] = nodoLleno.valores[j + mitad];
            }
            
            nuevoNodo.siguiente = nodoLleno.siguiente;
            nodoLleno.siguiente = nuevoNodo;
            nodoLleno.numClaves = mitad;
            
            for (int j = padre.numClaves; j > indice; j--) {
                padre.hijos[j + 1] = padre.hijos[j];
            }
            padre.hijos[indice + 1] = nuevoNodo;
            
            for (int j = padre.numClaves - 1; j >= indice; j--) {
                padre.claves[j + 1] = padre.claves[j];
            }
            padre.claves[indice] = nuevoNodo.claves[0];
            padre.numClaves++;
        } else {
            for (int j = 0; j < nuevoNodo.numClaves; j++) {
                nuevoNodo.claves[j] = nodoLleno.claves[j + mitad + 1];
            }
            
            for (int j = 0; j < orden - mitad; j++) {
                nuevoNodo.hijos[j] = nodoLleno.hijos[j + mitad + 1];
            }
            
            nodoLleno.numClaves = mitad;
            
            for (int j = padre.numClaves; j > indice; j--) {
                padre.hijos[j + 1] = padre.hijos[j];
            }
            padre.hijos[indice + 1] = nuevoNodo;
            
            for (int j = padre.numClaves - 1; j >= indice; j--) {
                padre.claves[j + 1] = padre.claves[j];
            }
            padre.claves[indice] = nodoLleno.claves[mitad];
            padre.numClaves++;
        }
    }
    
    public ListaEnlazada buscarPorGenero(String genero) {
        ListaEnlazada resultados = buscarEnNodo(raiz, genero.toUpperCase());
        
        if (resultados != null && resultados.getTamanio() > 0) {
            mensaje = "SE ENCONTRARON " + resultados.getTamanio() + " LIBROS DEL GENERO " + genero;
        } else {
            mensaje = "NO SE ENCONTRARON LIBROS DEL GENERO " + genero;
            resultados = new ListaEnlazada();
        }
        
        return resultados;
    }
    
    private ListaEnlazada buscarEnNodo(NodoBPlus nodo, String clave) {
        int i = 0;
        while (i < nodo.numClaves && clave.compareTo(nodo.claves[i]) > 0) {
            i++;
        }
        
        if (nodo.esHoja) {
            if (i < nodo.numClaves && clave.equals(nodo.claves[i])) {
                return (ListaEnlazada) nodo.valores[i];
            }
            return null;
        } else {
            if (i < nodo.numClaves && clave.equals(nodo.claves[i])) {
                i++;
            }
            return buscarEnNodo(nodo.hijos[i], clave);
        }
    }
    
    public ListaEnlazada obtenerTodosLosLibros() {
        ListaEnlazada todos = new ListaEnlazada();
        NodoBPlus hoja = obtenerPrimeraHoja(raiz);
        
        while (hoja != null) {
            for (int i = 0; i < hoja.numClaves; i++) {
                ListaEnlazada lista = (ListaEnlazada) hoja.valores[i];
                for (int j = 0; j < lista.getTamanio(); j++) {
                    todos.insertarAlFinal(lista.obtener(j));
                }
            }
            hoja = hoja.siguiente;
        }
        
        mensaje = "SE OBTUVIERON " + todos.getTamanio() + " LIBROS EN TOTAL";
        return todos;
    }
    
    private NodoBPlus obtenerPrimeraHoja(NodoBPlus nodo) {
        if (nodo.esHoja) {
            return nodo;
        }
        return obtenerPrimeraHoja(nodo.hijos[0]);
    }
    
    public String[] obtenerGeneros() {
        ListaEnlazada generosLista = new ListaEnlazada();
        NodoBPlus hoja = obtenerPrimeraHoja(raiz);
        
        while (hoja != null) {
            for (int i = 0; i < hoja.numClaves; i++) {
                Libro libroTemp = new Libro(hoja.claves[i], "", "", 0, hoja.claves[i], null);
                if (generosLista.buscarPorGenero(hoja.claves[i]).getTamanio() == 0) {
                    generosLista.insertarAlFinal(libroTemp);
                }
            }
            hoja = hoja.siguiente;
        }
        
        String[] generos = new String[generosLista.getTamanio()];
        for (int i = 0; i < generosLista.getTamanio(); i++) {
            generos[i] = generosLista.obtener(i).getGenero();
        }
        
        return generos;
    }
    
    public boolean eliminar(String genero, String isbn) {
        genero = genero.toUpperCase();
        ListaEnlazada lista = buscarEnNodo(raiz, genero);
        
        if (lista == null) {
            mensaje = "ERROR: NO SE ENCONTRO EL GENERO " + genero;
            return false;
        }
        
        boolean eliminado = lista.eliminarPorISBN(isbn);
        
        if (eliminado) {
            tamanio--;
            mensaje = "LIBRO ELIMINADO CORRECTAMENTE DEL GENERO " + genero;
            
            if (lista.getTamanio() == 0) {
                eliminarGeneroVacio(raiz, genero);
            }
        } else {
            mensaje = "ERROR: NO SE ENCONTRO EL LIBRO CON ISBN " + isbn + " EN EL GENERO " + genero;
        }
        
        return eliminado;
    }
    
    private boolean eliminarGeneroVacio(NodoBPlus nodo, String clave) {
        int i = 0;
        while (i < nodo.numClaves && clave.compareTo(nodo.claves[i]) > 0) {
            i++;
        }
        
        if (nodo.esHoja) {
            if (i < nodo.numClaves && clave.equals(nodo.claves[i])) {
                for (int j = i; j < nodo.numClaves - 1; j++) {
                    nodo.claves[j] = nodo.claves[j + 1];
                    nodo.valores[j] = nodo.valores[j + 1];
                }
                nodo.numClaves--;
                return true;
            }
            return false;
        } else {
            if (i < nodo.numClaves && clave.equals(nodo.claves[i])) {
                i++;
            }
            return eliminarGeneroVacio(nodo.hijos[i], clave);
        }
    }
    
    public void recorridoPorGeneros(AccionGenero accion) {
        NodoBPlus hoja = obtenerPrimeraHoja(raiz);
        
        while (hoja != null) {
            for (int i = 0; i < hoja.numClaves; i++) {
                String genero = hoja.claves[i];
                ListaEnlazada libros = (ListaEnlazada) hoja.valores[i];
                accion.ejecutar(genero, libros);
            }
            hoja = hoja.siguiente;
        }
    }
    
    public void recorridoTodosLosLibros(AccionLibro accion) {
        NodoBPlus hoja = obtenerPrimeraHoja(raiz);
        
        while (hoja != null) {
            for (int i = 0; i < hoja.numClaves; i++) {
                ListaEnlazada lista = (ListaEnlazada) hoja.valores[i];
                for (int j = 0; j < lista.getTamanio(); j++) {
                    accion.ejecutar(lista.obtener(j));
                }
            }
            hoja = hoja.siguiente;
        }
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
        void ejecutar(String genero, ListaEnlazada libros);
    }
}
