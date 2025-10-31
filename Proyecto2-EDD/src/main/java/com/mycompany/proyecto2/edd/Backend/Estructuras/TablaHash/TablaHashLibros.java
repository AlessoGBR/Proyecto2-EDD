/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.TablaHash;

import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.NodoLista;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class TablaHashLibros {

    private String mensaje;
    private int capacidad;
    private int tamanio;
    private ListaEnlazada[] tabla;
    private static final double FACTOR_CARGA_MAXIMO = 0.75;

    public TablaHashLibros(int capacidad) {
        if (capacidad <= 0) {
            this.capacidad = 101;
            this.mensaje = "ERROR: CAPACIDAD INVALIDA, SE ASIGNO CAPACIDAD DE 101";
        } else {
            this.capacidad = obtenerPrimoMasCercano(capacidad);
            this.mensaje = "TABLA HASH CREADA CORRECTAMENTE CON CAPACIDAD " + this.capacidad;
        }

        this.tamanio = 0;
        this.tabla = new ListaEnlazada[this.capacidad];

        for (int i = 0; i < this.capacidad; i++) {
            tabla[i] = new ListaEnlazada();
        }
    }

    public TablaHashLibros() {
        this(101);
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getTamanio() {
        return tamanio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getFactorCarga() {
        return (double) tamanio / capacidad;
    }

    private int obtenerPrimoMasCercano(int n) {
        if (n <= 2) {
            return 2;
        }

        if (n % 2 == 0) {
            n++;
        }

        while (!esPrimo(n)) {
            n += 2;
        }

        return n;
    }

    private boolean esPrimo(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }

    private int funcionHash(String isbn) {
        int hash = 0;

        for (int i = 0; i < isbn.length(); i++) {
            hash = (hash * 31 + isbn.charAt(i)) % capacidad;
        }

        return Math.abs(hash);
    }

    public boolean insertar(Libro libro) {
        if (libro == null) {
            mensaje = "ERROR: NO SE PUEDE INSERTAR UN LIBRO NULO";
            return false;
        }

        if (buscar(libro.getIsbn()) != null) {
            mensaje = "ERROR: YA EXISTE UN LIBRO CON EL ISBN " + libro.getIsbn();
            return false;
        }

        if (getFactorCarga() >= FACTOR_CARGA_MAXIMO) {
            redimensionar();
        }

        int indice = funcionHash(libro.getIsbn());
        tabla[indice].insertarAlFinal(libro);
        tamanio++;
        mensaje = "LIBRO INSERTADO CORRECTAMENTE: " + libro.getTitulo();
        return true;
    }

    public Libro buscar(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            mensaje = "ERROR: EL ISBN NO PUEDE ESTAR VACIO";
            return null;
        }

        int indice = funcionHash(isbn);
        Libro libro = tabla[indice].buscarPorISBN(isbn);

        if (libro != null) {
            mensaje = "LIBRO ENCONTRADO: " + libro.getTitulo();
        } else {
            mensaje = "NO SE ENCONTRO EL LIBRO CON ISBN: " + isbn;
        }

        return libro;
    }

    public ListaEnlazada buscarTodos(String isbn) {
        ListaEnlazada resultados = new ListaEnlazada();

        if (isbn == null || isbn.trim().isEmpty()) {
            mensaje = "ERROR: EL ISBN NO PUEDE ESTAR VACÍO";
            return resultados;
        }

        int indice = funcionHash(isbn);
        ListaEnlazada lista = tabla[indice]; // asumiendo que cada posición es una lista de libros
        NodoLista actual = lista.getCabeza();

        while (actual != null) {
            Libro libro = (Libro) actual.getLibro();
            if (libro.getIsbn().equalsIgnoreCase(isbn)) {
                resultados.insertarAlFinal(libro);
            }
            actual = actual.getSiguiente();
        }

        if (!resultados.estaVacia()) {
            mensaje = "SE ENCONTRARON " + resultados.getTamanio()+ " LIBROS CON ISBN: " + isbn;
        } else {
            mensaje = "NO SE ENCONTRARON LIBROS CON ISBN: " + isbn;
        }

        return resultados;
    }

    public boolean eliminar(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            mensaje = "ERROR: EL ISBN NO PUEDE ESTAR VACIO";
            return false;
        }

        int indice = funcionHash(isbn);
        boolean eliminado = tabla[indice].eliminarPorISBN(isbn);

        if (eliminado) {
            tamanio--;
            mensaje = "LIBRO ELIMINADO CORRECTAMENTE CON ISBN: " + isbn;
        } else {
            mensaje = "NO SE ENCONTRO EL LIBRO CON ISBN: " + isbn;
        }

        return eliminado;
    }

    private void redimensionar() {
        int nuevaCapacidad = obtenerPrimoMasCercano(capacidad * 2);
        ListaEnlazada[] nuevaTabla = new ListaEnlazada[nuevaCapacidad];

        for (int i = 0; i < nuevaCapacidad; i++) {
            nuevaTabla[i] = new ListaEnlazada();
        }

        int capacidadAnterior = capacidad;
        capacidad = nuevaCapacidad;

        for (int i = 0; i < capacidadAnterior; i++) {
            for (int j = 0; j < tabla[i].getTamanio(); j++) {
                Libro libro = tabla[i].obtener(j);
                int nuevoIndice = funcionHash(libro.getIsbn());
                nuevaTabla[nuevoIndice].insertarAlFinal(libro);
            }
        }

        tabla = nuevaTabla;
        mensaje = "TABLA HASH REDIMENSIONADA A CAPACIDAD: " + capacidad;
    }

    public boolean actualizar(Libro libro) {
        if (libro == null) {
            mensaje = "ERROR: NO SE PUEDE ACTUALIZAR UN LIBRO NULO";
            return false;
        }

        int indice = funcionHash(libro.getIsbn());
        Libro libroExistente = tabla[indice].buscarPorISBN(libro.getIsbn());

        if (libroExistente == null) {
            mensaje = "ERROR: NO SE ENCONTRO EL LIBRO CON ISBN: " + libro.getIsbn();
            return false;
        }

        tabla[indice].eliminarPorISBN(libro.getIsbn());
        tabla[indice].insertarAlFinal(libro);
        mensaje = "LIBRO ACTUALIZADO CORRECTAMENTE: " + libro.getTitulo();
        return true;
    }

    public int obtenerNumeroColisiones() {
        int colisiones = 0;

        for (int i = 0; i < capacidad; i++) {
            if (tabla[i].getTamanio() > 1) {
                colisiones += tabla[i].getTamanio() - 1;
            }
        }

        return colisiones;
    }

    public int obtenerCadenaMasLarga() {
        int maxLongitud = 0;

        for (int i = 0; i < capacidad; i++) {
            if (tabla[i].getTamanio() > maxLongitud) {
                maxLongitud = tabla[i].getTamanio();
            }
        }

        return maxLongitud;
    }

    public int obtenerEspaciosVacios() {
        int vacios = 0;

        for (int i = 0; i < capacidad; i++) {
            if (tabla[i].getTamanio() == 0) {
                vacios++;
            }
        }

        return vacios;
    }

    public Libro[] toArray() {
        Libro[] arreglo = new Libro[tamanio];
        int pos = 0;

        for (int i = 0; i < capacidad; i++) {
            for (int j = 0; j < tabla[i].getTamanio(); j++) {
                arreglo[pos++] = tabla[i].obtener(j);
            }
        }

        return arreglo;
    }

    public void recorrer(AccionLibro accion) {
        for (int i = 0; i < capacidad; i++) {
            tabla[i].recorrer((libro, indice) -> {
                accion.ejecutar(libro);
            });
        }
    }

    public void limpiar() {
        for (int i = 0; i < capacidad; i++) {
            tabla[i].limpiar();
        }
        tamanio = 0;
        mensaje = "TABLA HASH LIMPIADA CORRECTAMENTE";
    }

    public void imprimirEstadisticas() {
        System.out.println("\nESTADISTICAS DE LA TABLA HASH:");
        System.out.println("CAPACIDAD: " + capacidad);
        System.out.println("TAMANIO: " + tamanio);
        System.out.println("FACTOR DE CARGA: " + String.format("%.2f", getFactorCarga()));
        System.out.println("NUMERO DE COLISIONES: " + obtenerNumeroColisiones());
        System.out.println("CADENA MAS LARGA: " + obtenerCadenaMasLarga());
        System.out.println("ESPACIOS VACIOS: " + obtenerEspaciosVacios());
    }

    public void imprimirTabla() {
        System.out.println("\nCONTENIDO DE LA TABLA HASH:");

        for (int i = 0; i < capacidad; i++) {
            if (tabla[i].getTamanio() > 0) {
                System.out.print("INDICE " + i + ": ");
                tabla[i].recorrer((libro, indice) -> {
                    System.out.print("[" + libro.getIsbn() + ": " + libro.getTitulo() + "] ");
                });
                System.out.println();
            }
        }
    }

    @FunctionalInterface
    public interface AccionLibro {

        void ejecutar(Libro libro);
    }
}
