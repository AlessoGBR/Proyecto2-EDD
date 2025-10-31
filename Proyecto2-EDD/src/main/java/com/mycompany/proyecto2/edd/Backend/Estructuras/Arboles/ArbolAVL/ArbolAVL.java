/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolAVL;

import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import java.util.Comparator;

/**
 *
 * @author alesso
 */
public class ArbolAVL {

    private NodoAVL raiz;
    private Comparator<Libro> comparador;
    private int tamanio;

    public ArbolAVL(Comparator<Libro> comparador) {
        this.raiz = null;
        this.comparador = comparador;
        this.tamanio = 0;
    }

    public ArbolAVL() {
        this((l1, l2) -> l1.getTitulo().compareToIgnoreCase(l2.getTitulo()));
    }

    private int altura(NodoAVL nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    private int obtenerBalance(NodoAVL nodo) {
        return nodo == null ? 0 : altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    private void actualizarAltura(NodoAVL nodo) {
        if (nodo != null) {
            nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        }
    }

    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        NodoAVL T2 = x.derecho;

        // Realizar rotación
        x.derecho = y;
        y.izquierdo = T2;

        // Actualizar alturas
        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        NodoAVL T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    public boolean insertar(Libro libro) {
        int tamanioAnterior = tamanio;
        raiz = insertarRec(raiz, libro);
        return tamanio > tamanioAnterior;
    }

    private NodoAVL insertarRec(NodoAVL nodo, Libro libro) {
        if (nodo == null) {
            tamanio++;
            return new NodoAVL(libro);
        }

        int comparacion = comparador.compare(libro, nodo.libro);

        if (comparacion < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, libro);
        } else if (comparacion > 0) {
            nodo.derecho = insertarRec(nodo.derecho, libro);
        } else {
            return nodo;
        }

        actualizarAltura(nodo);

        int balance = obtenerBalance(nodo);

        // IZQUIERDA - IZQUIERDA
        if (balance > 1 && comparador.compare(libro, nodo.izquierdo.libro) < 0) {
            return rotarDerecha(nodo);
        }

        // DERECHA - DERECHA
        if (balance < -1 && comparador.compare(libro, nodo.derecho.libro) > 0) {
            return rotarIzquierda(nodo);
        }

        // IZQUIERDA - DERECHA
        if (balance > 1 && comparador.compare(libro, nodo.izquierdo.libro) > 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // DERECHA - IZQUIERDA
        if (balance < -1 && comparador.compare(libro, nodo.derecho.libro) < 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public Libro buscar(Libro libroBuscado) {
        return buscarRec(raiz, libroBuscado);
    }

    private Libro buscarRec(NodoAVL nodo, Libro libroBuscado) {
        if (nodo == null) {
            return null;
        }

        int comparacion = comparador.compare(libroBuscado, nodo.libro);

        if (comparacion == 0) {
            return nodo.libro;
        } else if (comparacion < 0) {
            return buscarRec(nodo.izquierdo, libroBuscado);
        } else {
            return buscarRec(nodo.derecho, libroBuscado);
        }
    }

    public Libro buscarPorTitulo(String titulo) {
        return buscarPorTituloRec(raiz, titulo);
    }

    private Libro buscarPorTituloRec(NodoAVL nodo, String titulo) {
        if (nodo == null) {
            return null;
        }

        int comparacion = titulo.compareToIgnoreCase(nodo.libro.getTitulo());

        if (comparacion == 0) {
            return nodo.libro;
        } else if (comparacion < 0) {
            return buscarPorTituloRec(nodo.izquierdo, titulo);
        } else {
            return buscarPorTituloRec(nodo.derecho, titulo);
        }
    }

    public ListaEnlazada buscarTodosPorTitulo(String titulo) {
        ListaEnlazada resultados = new ListaEnlazada();
        buscarTodosPorTituloRec(raiz, titulo, resultados);
        return resultados;
    }

    private void buscarTodosPorTituloRec(NodoAVL nodo, String titulo, ListaEnlazada resultados) {
        if (nodo == null) {
            return;
        }

        if (nodo.libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
            resultados.insertarAlFinal(nodo.libro);
        }

        buscarTodosPorTituloRec(nodo.izquierdo, titulo, resultados);
        buscarTodosPorTituloRec(nodo.derecho, titulo, resultados);
    }

    private NodoAVL nodoMinimo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    public boolean eliminar(Libro libro) {
        int tamanioAnterior = tamanio;
        raiz = eliminarRec(raiz, libro);
        return tamanio < tamanioAnterior;
    }

    private NodoAVL eliminarRec(NodoAVL nodo, Libro libro) {
        if (nodo == null) {
            return null;
        }

        int comparacion = comparador.compare(libro, nodo.libro);

        if (comparacion < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, libro);
        } else if (comparacion > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, libro);
        } else {
            tamanio--;

            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            }

            NodoAVL sucesor = nodoMinimo(nodo.derecho);
            nodo.libro = sucesor.libro;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.libro);
            tamanio++;
        }

        if (nodo == null) {
            return null;
        }

        actualizarAltura(nodo);

        int balance = obtenerBalance(nodo);

        // IZQUIERDA - IZQUIERDA
        if (balance > 1 && obtenerBalance(nodo.izquierdo) >= 0) {
            return rotarDerecha(nodo);
        }

        // IZQUIERDA DERECHA
        if (balance > 1 && obtenerBalance(nodo.izquierdo) < 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // DERECHA - DERECHA
        if (balance < -1 && obtenerBalance(nodo.derecho) <= 0) {
            return rotarIzquierda(nodo);
        }

        // DERECHA - IZQUIERDA
        if (balance < -1 && obtenerBalance(nodo.derecho) > 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public void inOrder(AccionLibro accion) {
        inOrderRec(raiz, accion);
    }

    private void inOrderRec(NodoAVL nodo, AccionLibro accion) {
        if (nodo != null) {
            inOrderRec(nodo.izquierdo, accion);
            accion.ejecutar(nodo.libro);
            inOrderRec(nodo.derecho, accion);
        }
    }

    public void preOrder(AccionLibro accion) {
        preOrderRec(raiz, accion);
    }

    private void preOrderRec(NodoAVL nodo, AccionLibro accion) {
        if (nodo != null) {
            accion.ejecutar(nodo.libro);
            preOrderRec(nodo.izquierdo, accion);
            preOrderRec(nodo.derecho, accion);
        }
    }

    public void postOrder(AccionLibro accion) {
        postOrderRec(raiz, accion);
    }

    private void postOrderRec(NodoAVL nodo, AccionLibro accion) {
        if (nodo != null) {
            postOrderRec(nodo.izquierdo, accion);
            postOrderRec(nodo.derecho, accion);
            accion.ejecutar(nodo.libro);
        }
    }

    public Libro[] toArray() {
        Libro[] arreglo = new Libro[tamanio];
        inOrderToArray(raiz, arreglo, new int[]{0});
        return arreglo;
    }

    private void inOrderToArray(NodoAVL nodo, Libro[] arreglo, int[] indice) {
        if (nodo != null) {
            inOrderToArray(nodo.izquierdo, arreglo, indice);
            arreglo[indice[0]++] = nodo.libro;
            inOrderToArray(nodo.derecho, arreglo, indice);
        }
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public int getAltura() {
        return altura(raiz);
    }

    public void limpiar() {
        raiz = null;
        tamanio = 0;
    }

    public NodoAVL getRaiz() {
        return raiz;
    }

    public boolean estaBalanceado() {
        return estaBalanceadoRec(raiz);
    }

    private boolean estaBalanceadoRec(NodoAVL nodo) {
        if (nodo == null) {
            return true;
        }

        int balance = Math.abs(obtenerBalance(nodo));

        if (balance > 1) {
            return false;
        }

        return estaBalanceadoRec(nodo.izquierdo) && estaBalanceadoRec(nodo.derecho);
    }

    public void imprimirArbol() {
        imprimirArbolRec(raiz, "", true);
    }

    private void imprimirArbolRec(NodoAVL nodo, String prefijo, boolean esUltimo) {
        if (nodo != null) {
            System.out.println(prefijo + (esUltimo ? "└── " : "├── ")
                    + nodo.libro.getTitulo() + " (h=" + nodo.altura + ")");

            String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");

            if (nodo.derecho != null || nodo.izquierdo != null) {
                if (nodo.izquierdo != null) {
                    imprimirArbolRec(nodo.izquierdo, nuevoPrefijo, nodo.derecho == null);
                }
                if (nodo.derecho != null) {
                    imprimirArbolRec(nodo.derecho, nuevoPrefijo, true);
                }
            }
        }
    }

    @FunctionalInterface
    public interface AccionLibro {

        void ejecutar(Libro libro);
    }
}
