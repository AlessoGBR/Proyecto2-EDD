/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos;

/**
 *
 * @author alesso
 */
public class Grafo {

    private String mensaje;
    private int numVertices;
    private int maxVertices;
    private String[] nombresBibliotecas;
    private int[][] matrizAdyacencia;
    private int[][] matrizTiempos;
    private int[][] matrizCostos;

    public Grafo(int maxVertices) {
        if (maxVertices <= 0) {
            this.maxVertices = 10;
            this.mensaje = "ERROR: CAPACIDAD INVALIDA, SE ASIGNO CAPACIDAD DE 10";
        } else {
            this.maxVertices = maxVertices;
            this.mensaje = "GRAFO CREADO CORRECTAMENTE CON CAPACIDAD " + maxVertices;
        }

        this.numVertices = 0;
        this.nombresBibliotecas = new String[this.maxVertices];
        this.matrizAdyacencia = new int[this.maxVertices][this.maxVertices];
        this.matrizTiempos = new int[this.maxVertices][this.maxVertices];
        this.matrizCostos = new int[this.maxVertices][this.maxVertices];

        inicializarMatrices();
    }

    public Grafo() {
        this(20);
    }

    private void inicializarMatrices() {
        for (int i = 0; i < maxVertices; i++) {
            for (int j = 0; j < maxVertices; j++) {
                matrizAdyacencia[i][j] = 0;
                matrizTiempos[i][j] = 0;
                matrizCostos[i][j] = 0;
            }
        }
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getMaxVertices() {
        return maxVertices;
    }

    public String[] getNombresBibliotecas() {
        return nombresBibliotecas;
    }

    public int[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    public int[][] getMatrizTiempos() {
        return matrizTiempos;
    }

    public int[][] getMatrizCostos() {
        return matrizCostos;
    }

    public boolean agregarBiblioteca(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            mensaje = "ERROR: EL NOMBRE DE LA BIBLIOTECA NO PUEDE ESTAR VACIO";
            return false;
        }

        if (numVertices >= maxVertices) {
            mensaje = "ERROR: NO SE PUEDEN AGREGAR MAS BIBLIOTECAS, CAPACIDAD MAXIMA ALCANZADA";
            return false;
        }

        if (buscarIndiceBiblioteca(nombre) != -1) {
            mensaje = "ERROR: YA EXISTE UNA BIBLIOTECA CON EL NOMBRE " + nombre;
            return false;
        }

        nombresBibliotecas[numVertices] = nombre.toUpperCase();
        numVertices++;
        mensaje = "BIBLIOTECA AGREGADA CORRECTAMENTE: " + nombre;
        return true;
    }

    public int buscarIndiceBiblioteca(String nombre) {
        if (nombre == null) {
            return -1;
        }

        for (int i = 0; i < numVertices; i++) {
            if (nombresBibliotecas[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    public boolean agregarConexion(String origen, String destino, int tiempo, int costo) {
        int indiceOrigen = buscarIndiceBiblioteca(origen);
        int indiceDestino = buscarIndiceBiblioteca(destino);

        if (indiceOrigen == -1) {
            mensaje = "ERROR: NO EXISTE LA BIBLIOTECA DE ORIGEN: " + origen;
            return false;
        }

        if (indiceDestino == -1) {
            mensaje = "ERROR: NO EXISTE LA BIBLIOTECA DE DESTINO: " + destino;
            return false;
        }

        if (indiceOrigen == indiceDestino) {
            mensaje = "ERROR: NO SE PUEDE CREAR UNA CONEXION DE UNA BIBLIOTECA CONSIGO MISMA";
            return false;
        }

        if (tiempo < 0) {
            mensaje = "ERROR: EL TIEMPO NO PUEDE SER NEGATIVO";
            return false;
        }

        if (costo < 0) {
            mensaje = "ERROR: EL COSTO NO PUEDE SER NEGATIVO";
            return false;
        }

        matrizAdyacencia[indiceOrigen][indiceDestino] = 1;
        matrizTiempos[indiceOrigen][indiceDestino] = tiempo;
        matrizCostos[indiceOrigen][indiceDestino] = costo;

        mensaje = "CONEXION AGREGADA: " + origen + " -> " + destino
                + " (TIEMPO: " + tiempo + ", COSTO: " + costo + ")";
        return true;
    }

    public boolean agregarConexionBidireccional(String biblioteca1, String biblioteca2,
            int tiempo, int costo) {
        boolean resultado1 = agregarConexion(biblioteca1, biblioteca2, tiempo, costo);
        if (!resultado1) {
            return false;
        }

        boolean resultado2 = agregarConexion(biblioteca2, biblioteca1, tiempo, costo);
        if (!resultado2) {
            eliminarConexion(biblioteca1, biblioteca2);
            return false;
        }

        mensaje = "CONEXION BIDIRECCIONAL AGREGADA: " + biblioteca1 + " <-> " + biblioteca2
                + " (TIEMPO: " + tiempo + ", COSTO: " + costo + ")";
        return true;
    }

    public boolean eliminarConexion(String origen, String destino) {
        int indiceOrigen = buscarIndiceBiblioteca(origen);
        int indiceDestino = buscarIndiceBiblioteca(destino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            mensaje = "ERROR: UNA O AMBAS BIBLIOTECAS NO EXISTEN";
            return false;
        }

        if (matrizAdyacencia[indiceOrigen][indiceDestino] == 0) {
            mensaje = "ERROR: NO EXISTE CONEXION ENTRE " + origen + " Y " + destino;
            return false;
        }

        matrizAdyacencia[indiceOrigen][indiceDestino] = 0;
        matrizTiempos[indiceOrigen][indiceDestino] = 0;
        matrizCostos[indiceOrigen][indiceDestino] = 0;

        mensaje = "CONEXION ELIMINADA: " + origen + " -> " + destino;
        return true;
    }

    public boolean existeConexion(String origen, String destino) {
        int indiceOrigen = buscarIndiceBiblioteca(origen);
        int indiceDestino = buscarIndiceBiblioteca(destino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            return false;
        }

        return matrizAdyacencia[indiceOrigen][indiceDestino] == 1;
    }

    public int obtenerTiempoConexion(String origen, String destino) {
        int indiceOrigen = buscarIndiceBiblioteca(origen);
        int indiceDestino = buscarIndiceBiblioteca(destino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            mensaje = "ERROR: UNA O AMBAS BIBLIOTECAS NO EXISTEN";
            return -1;
        }

        if (matrizAdyacencia[indiceOrigen][indiceDestino] == 0) {
            mensaje = "ERROR: NO EXISTE CONEXION ENTRE " + origen + " Y " + destino;
            return -1;
        }

        return matrizTiempos[indiceOrigen][indiceDestino];
    }

    public int obtenerCostoConexion(String origen, String destino) {
        int indiceOrigen = buscarIndiceBiblioteca(origen);
        int indiceDestino = buscarIndiceBiblioteca(destino);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            mensaje = "ERROR: UNA O AMBAS BIBLIOTECAS NO EXISTEN";
            return -1;
        }

        if (matrizAdyacencia[indiceOrigen][indiceDestino] == 0) {
            mensaje = "ERROR: NO EXISTE CONEXION ENTRE " + origen + " Y " + destino;
            return -1;
        }

        return matrizCostos[indiceOrigen][indiceDestino];
    }

    public String[] obtenerVecinos(String biblioteca) {
        int indice = buscarIndiceBiblioteca(biblioteca);

        if (indice == -1) {
            mensaje = "ERROR: LA BIBLIOTECA NO EXISTE: " + biblioteca;
            return new String[0];
        }

        int contadorVecinos = 0;
        for (int j = 0; j < numVertices; j++) {
            if (matrizAdyacencia[indice][j] == 1) {
                contadorVecinos++;
            }
        }

        String[] vecinos = new String[contadorVecinos];
        int pos = 0;
        for (int j = 0; j < numVertices; j++) {
            if (matrizAdyacencia[indice][j] == 1) {
                vecinos[pos++] = nombresBibliotecas[j];
            }
        }

        mensaje = "SE ENCONTRARON " + contadorVecinos + " VECINOS PARA " + biblioteca;
        return vecinos;
    }

    public int obtenerGradoSalida(String biblioteca) {
        int indice = buscarIndiceBiblioteca(biblioteca);

        if (indice == -1) {
            mensaje = "ERROR: LA BIBLIOTECA NO EXISTE: " + biblioteca;
            return -1;
        }

        int grado = 0;
        for (int j = 0; j < numVertices; j++) {
            if (matrizAdyacencia[indice][j] == 1) {
                grado++;
            }
        }

        return grado;
    }

    public int obtenerGradoEntrada(String biblioteca) {
        int indice = buscarIndiceBiblioteca(biblioteca);

        if (indice == -1) {
            mensaje = "ERROR: LA BIBLIOTECA NO EXISTE: " + biblioteca;
            return -1;
        }

        int grado = 0;
        for (int i = 0; i < numVertices; i++) {
            if (matrizAdyacencia[i][indice] == 1) {
                grado++;
            }
        }

        return grado;
    }

    public void imprimirMatrizAdyacencia() {
        System.out.println("MATRIZ DE ADYACENCIA:");
        System.out.print("     ");
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-5s", nombresBibliotecas[i].substring(0, Math.min(4, nombresBibliotecas[i].length())));
        }
        System.out.println();

        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-5s", nombresBibliotecas[i].substring(0, Math.min(4, nombresBibliotecas[i].length())));
            for (int j = 0; j < numVertices; j++) {
                System.out.printf("%-5d", matrizAdyacencia[i][j]);
            }
            System.out.println();
        }
    }

    public void imprimirMatrizTiempos() {
        System.out.println("MATRIZ DE TIEMPOS:");
        System.out.print("     ");
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-6s", nombresBibliotecas[i].substring(0, Math.min(5, nombresBibliotecas[i].length())));
        }
        System.out.println();

        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-5s", nombresBibliotecas[i].substring(0, Math.min(4, nombresBibliotecas[i].length())));
            for (int j = 0; j < numVertices; j++) {
                if (matrizAdyacencia[i][j] == 1) {
                    System.out.printf("%-6d", matrizTiempos[i][j]);
                } else {
                    System.out.printf("%-6s", "-");
                }
            }
            System.out.println();
        }
    }

    public void imprimirMatrizCostos() {
        System.out.println("MATRIZ DE COSTOS:");
        System.out.print("     ");
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-6s", nombresBibliotecas[i].substring(0, Math.min(5, nombresBibliotecas[i].length())));
        }
        System.out.println();

        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%-5s", nombresBibliotecas[i].substring(0, Math.min(4, nombresBibliotecas[i].length())));
            for (int j = 0; j < numVertices; j++) {
                if (matrizAdyacencia[i][j] == 1) {
                    System.out.printf("%-6d", matrizCostos[i][j]);
                } else {
                    System.out.printf("%-6s", "-");
                }
            }
            System.out.println();
        }
    }

    public void imprimirGrafo() {
        System.out.println("GRAFO DE BIBLIOTECAS:");
        System.out.println("NUMERO DE BIBLIOTECAS: " + numVertices);
        System.out.println("CONEXIONES:");

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (matrizAdyacencia[i][j] == 1) {
                    System.out.println(nombresBibliotecas[i] + " -> " + nombresBibliotecas[j]
                            + " (TIEMPO: " + matrizTiempos[i][j]
                            + ", COSTO: " + matrizCostos[i][j] + ")");
                }
            }
        }
    }

    public void limpiar() {
        numVertices = 0;
        inicializarMatrices();
        for (int i = 0; i < maxVertices; i++) {
            nombresBibliotecas[i] = null;
        }
        mensaje = "GRAFO LIMPIADO CORRECTAMENTE";
    }
}
