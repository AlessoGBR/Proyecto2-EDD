/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Util;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolAVL.ArbolAVL;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.ArbolB;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus.ArbolBPlus;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Estructuras.TablaHash.TablaHashLibros;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class ComparadorBusquedas {

    private String mensaje;

    public class ResultadoComparacion {

        private String metodo;
        private String estructura;
        private double tiempoMs;
        private boolean encontrado;
        private int comparaciones;

        public ResultadoComparacion(String metodo, String estructura, double tiempoMs,
                boolean encontrado, int comparaciones) {
            this.metodo = metodo;
            this.estructura = estructura;
            this.tiempoMs = tiempoMs;
            this.encontrado = encontrado;
            this.comparaciones = comparaciones;
        }

        public String getMetodo() {
            return metodo;
        }

        public String getEstructura() {
            return estructura;
        }

        public double getTiempoMs() {
            return tiempoMs;
        }

        public boolean isEncontrado() {
            return encontrado;
        }

        public int getComparaciones() {
            return comparaciones;
        }

        public Object[] toArrayForTable() {
            return new Object[]{
                metodo,
                estructura,
                String.format("%.6f", tiempoMs),
                encontrado ? "SI" : "NO",
                comparaciones
            };
        }
    }

    public ComparadorBusquedas() {
        this.mensaje = "";
    }

    public String getMensaje() {
        return mensaje;
    }

    public ResultadoComparacion[] compararTodasLasBusquedas(SistemaBiblioteca sistema, String tituloBuscado) {
        ResultadoComparacion[] resultados = new ResultadoComparacion[5];

        resultados[0] = busquedaSecuencial(sistema.getListaPrincipal(), tituloBuscado);
        resultados[1] = busquedaBinariaAVL(sistema.getArbolAVL(), tituloBuscado);
        resultados[2] = busquedaHash(sistema.getTablaHash(), tituloBuscado);
        resultados[3] = busquedaArbolB(sistema.getArbolB(), tituloBuscado);
        resultados[4] = busquedaArbolBPlus(sistema.getArbolBPlus(), tituloBuscado);

        mensaje = "COMPARACION COMPLETADA: 5 METODOS EJECUTADOS";
        return resultados;
    }

    private ResultadoComparacion busquedaSecuencial(ListaEnlazada lista, String titulo) {
        long inicio = System.nanoTime();
        int comparaciones = 0;
        Libro encontrado = null;

        for (int i = 0; i < lista.getTamanio(); i++) {
            comparaciones++;
            Libro libro = lista.obtener(i);
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                encontrado = libro;
                break;
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        return new ResultadoComparacion(
                "BUSQUEDA SECUENCIAL",
                "LISTA ENLAZADA",
                tiempoMs,
                encontrado != null,
                comparaciones
        );
    }

    private ResultadoComparacion busquedaBinariaAVL(ArbolAVL arbol, String titulo) {
        long inicio = System.nanoTime();

        Libro encontrado = arbol.buscarPorTitulo(titulo);

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        int comparaciones = encontrado != null
                ? (int) Math.ceil(Math.log(arbol.getTamanio()) / Math.log(2)) : 0;

        return new ResultadoComparacion(
                "BUSQUEDA BINARIA",
                "ARBOL AVL",
                tiempoMs,
                encontrado != null,
                comparaciones
        );
    }

    private ResultadoComparacion busquedaHash(TablaHashLibros tablaHash, String titulo) {
        long inicio = System.nanoTime();

        Libro encontrado = null;
        Libro[] libros = tablaHash.toArray();
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                encontrado = libro;
                break;
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        return new ResultadoComparacion(
                "BUSQUEDA HASH",
                "TABLA HASH",
                tiempoMs,
                encontrado != null,
                1
        );
    }

    private ResultadoComparacion busquedaArbolB(ArbolB arbolB, String titulo) {
        long inicio = System.nanoTime();

        Libro encontrado = null;
        ListaEnlazada todos = new ListaEnlazada();
        arbolB.recorridoInOrder(libro -> todos.insertarAlFinal(libro));

        for (int i = 0; i < todos.getTamanio(); i++) {
            if (todos.obtener(i).getTitulo().equalsIgnoreCase(titulo)) {
                encontrado = todos.obtener(i);
                break;
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        int comparaciones = encontrado != null
                ? (int) Math.ceil(Math.log(arbolB.getTamanio()) / Math.log(arbolB.getOrden())) : 0;

        return new ResultadoComparacion(
                "BUSQUEDA ARBOL B",
                "ARBOL B",
                tiempoMs,
                encontrado != null,
                comparaciones
        );
    }

    private ResultadoComparacion busquedaArbolBPlus(ArbolBPlus arbolBPlus, String titulo) {
        long inicio = System.nanoTime();

        Libro encontrado = null;
        ListaEnlazada todos = arbolBPlus.obtenerTodosLosLibros();

        for (int i = 0; i < todos.getTamanio(); i++) {
            if (todos.obtener(i).getTitulo().equalsIgnoreCase(titulo)) {
                encontrado = todos.obtener(i);
                break;
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        int comparaciones = encontrado != null
                ? (int) Math.ceil(Math.log(arbolBPlus.getTamanio()) / Math.log(arbolBPlus.getOrden())) : 0;

        return new ResultadoComparacion(
                "BUSQUEDA ARBOL B+",
                "ARBOL B+",
                tiempoMs,
                encontrado != null,
                comparaciones
        );
    }
}
