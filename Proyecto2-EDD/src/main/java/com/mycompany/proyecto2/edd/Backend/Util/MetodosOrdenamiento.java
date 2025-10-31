/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Util;

import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class MetodosOrdenamiento {

    private String mensaje;
    private long tiempoEjecucion;
    private int numeroComparaciones;
    private int numeroIntercambios;

    public MetodosOrdenamiento() {
        this.mensaje = "";
        this.tiempoEjecucion = 0;
        this.numeroComparaciones = 0;
        this.numeroIntercambios = 0;
    }

    public String getMensaje() {
        return mensaje;
    }

    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public int getNumeroComparaciones() {
        return numeroComparaciones;
    }

    public int getNumeroIntercambios() {
        return numeroIntercambios;
    }

    public Libro[] ordenar(ListaEnlazada lista, String criterio, String metodo) {
        if (lista == null || lista.estaVacia()) {
            mensaje = "ERROR: LA LISTA ESTA VACIA";
            return new Libro[0];
        }

        Libro[] arreglo = lista.toArray();

        numeroComparaciones = 0;
        numeroIntercambios = 0;
        long inicio = System.nanoTime();

        switch (metodo.toUpperCase()) {
            case "BURBUJA":
            case "INTERCAMBIO":
                ordenamientoBurbuja(arreglo, criterio);
                break;
            case "SELECCION":
            case "SELECCION DIRECTA":
                ordenamientoSeleccion(arreglo, criterio);
                break;
            case "INSERCION":
            case "INSERCION DIRECTA":
                ordenamientoInsercion(arreglo, criterio);
                break;
            case "SHELL":
                ordenamientoShell(arreglo, criterio);
                break;
            case "QUICKSORT":
            case "QUICK":
                ordenamientoQuickSort(arreglo, 0, arreglo.length - 1, criterio);
                break;
            default:
                mensaje = "ERROR: METODO DE ORDENAMIENTO NO RECONOCIDO";
                return arreglo;
        }

        long fin = System.nanoTime();
        tiempoEjecucion = fin - inicio;

        mensaje = "ORDENAMIENTO COMPLETADO: " + metodo.toUpperCase()
                + " | TIEMPO: " + (tiempoEjecucion / 1_000_000.0) + " MS"
                + " | COMPARACIONES: " + numeroComparaciones
                + " | INTERCAMBIOS: " + numeroIntercambios;

        return arreglo;
    }

    private void ordenamientoBurbuja(Libro[] arreglo, String criterio) {
        int n = arreglo.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                numeroComparaciones++;

                if (comparar(arreglo[j], arreglo[j + 1], criterio) > 0) {
                    intercambiar(arreglo, j, j + 1);
                }
            }
        }
    }

    private void ordenamientoSeleccion(Libro[] arreglo, String criterio) {
        int n = arreglo.length;

        for (int i = 0; i < n - 1; i++) {
            int indiceMinimo = i;

            for (int j = i + 1; j < n; j++) {
                numeroComparaciones++;

                if (comparar(arreglo[j], arreglo[indiceMinimo], criterio) < 0) {
                    indiceMinimo = j;
                }
            }

            if (indiceMinimo != i) {
                intercambiar(arreglo, i, indiceMinimo);
            }
        }
    }

    private void ordenamientoInsercion(Libro[] arreglo, String criterio) {
        int n = arreglo.length;

        for (int i = 1; i < n; i++) {
            Libro clave = arreglo[i];
            int j = i - 1;

            while (j >= 0) {
                numeroComparaciones++;

                if (comparar(arreglo[j], clave, criterio) > 0) {
                    arreglo[j + 1] = arreglo[j];
                    numeroIntercambios++;
                    j--;
                } else {
                    break;
                }
            }

            arreglo[j + 1] = clave;
        }
    }

    private void ordenamientoShell(Libro[] arreglo, String criterio) {
        int n = arreglo.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Libro temp = arreglo[i];
                int j = i;

                while (j >= gap) {
                    numeroComparaciones++;

                    if (comparar(arreglo[j - gap], temp, criterio) > 0) {
                        arreglo[j] = arreglo[j - gap];
                        numeroIntercambios++;
                        j -= gap;
                    } else {
                        break;
                    }
                }

                arreglo[j] = temp;
            }
        }
    }

    private void ordenamientoQuickSort(Libro[] arreglo, int inicio, int fin, String criterio) {
        if (inicio < fin) {
            int indicePivote = particion(arreglo, inicio, fin, criterio);
            ordenamientoQuickSort(arreglo, inicio, indicePivote - 1, criterio);
            ordenamientoQuickSort(arreglo, indicePivote + 1, fin, criterio);
        }
    }

    private int particion(Libro[] arreglo, int inicio, int fin, String criterio) {
        Libro pivote = arreglo[fin];
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            numeroComparaciones++;

            if (comparar(arreglo[j], pivote, criterio) <= 0) {
                i++;
                intercambiar(arreglo, i, j);
            }
        }

        intercambiar(arreglo, i + 1, fin);
        return i + 1;
    }

    private void intercambiar(Libro[] arreglo, int i, int j) {
        Libro temp = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temp;
        numeroIntercambios++;
    }

    private int comparar(Libro libro1, Libro libro2, String criterio) {
        switch (criterio.toUpperCase()) {
            case "TITULO":
                return libro1.getTitulo().compareToIgnoreCase(libro2.getTitulo());

            case "AUTOR":
                return libro1.getAutor().compareToIgnoreCase(libro2.getAutor());

            case "ISBN":
                return libro1.getIsbn().compareTo(libro2.getIsbn());

            case "ANIO":
            case "AÑO":
                return Integer.compare(libro1.getAnioPublicacion(), libro2.getAnioPublicacion());

            case "GENERO":
                return libro1.getGenero().compareToIgnoreCase(libro2.getGenero());

            default:
                return 0;
        }
    }
}
