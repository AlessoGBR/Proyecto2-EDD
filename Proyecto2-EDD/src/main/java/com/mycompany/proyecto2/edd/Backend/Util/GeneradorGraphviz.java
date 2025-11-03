/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Util;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolAVL.ArbolAVL;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolAVL.NodoAVL;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.ArbolB;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.ColeccionLibro;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.NodoB;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus.ArbolBPlus;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus.NodoBPlus;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos.Grafo;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos.ResultadoDijkstra;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.NodoLista;
import com.mycompany.proyecto2.edd.Backend.Estructuras.TablaHash.TablaHashLibros;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author alesso
 */
public class GeneradorGraphviz {

    private String mensaje;
    private String ultimoPathGenerado;

    public GeneradorGraphviz() {
        this.mensaje = "";
        this.ultimoPathGenerado = "";
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getUltimoPathGenerado() {
        return ultimoPathGenerado;
    }

    private String obtenerDirectorioEjecucion() {
        try {
            String path = new File(".").getCanonicalPath();
            return path;
        } catch (IOException e) {
            return System.getProperty("user.dir");
        }
    }

    public String generarImagenArbolAVL(ArbolAVL arbol, String nombreArchivo) {
        if (arbol == null || arbol.estaVacio()) {
            mensaje = "ERROR: EL ARBOL AVL ESTA VACIO";
            return null;
        }

        try {
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + nombreArchivo + ".dot";
            String rutaPng = directorio + File.separator + nombreArchivo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph ArbolAVL {\n");
            dot.append("    node [shape=record, style=filled, fillcolor=lightblue];\n");
            dot.append("    graph [ranksep=0.5, nodesep=0.5];\n");

            if (arbol.getRaiz() != null) {
                generarNodosAVL(arbol.getRaiz(), dot, 0);
            }

            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN: " + e.getMessage();
            return null;
        }
    }

    private int contadorNodos = 0;

    private void generarNodosAVL(NodoAVL nodo, StringBuilder dot, int idPadre) {
        if (nodo == null) {
            return;
        }

        int idActual = contadorNodos++;
        String titulo = nodo.getLibro().getTitulo();
        if (titulo.length() > 15) {
            titulo = titulo.substring(0, 12) + "...";
        }

        dot.append("    node").append(idActual)
                .append(" [label=\"{").append(titulo)
                .append("|H=").append(nodo.getAltura())
                .append("}\"];\n");

        if (idPadre >= 0) {
            dot.append("    node").append(idPadre)
                    .append(" -> node").append(idActual).append(";\n");
        }

        if (nodo.getIzquierdo() != null) {
            generarNodosAVL(nodo.getIzquierdo(), dot, idActual);
        }

        if (nodo.getDerecho() != null) {
            generarNodosAVL(nodo.getDerecho(), dot, idActual);
        }
    }

    public String generarImagenArbolB(ArbolB arbol, String nombreArchivo) {
        if (arbol == null || arbol.estaVacio()) {
            mensaje = "ERROR: EL ARBOL B ESTA VACIO";
            return null;
        }

        try {
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + nombreArchivo + ".dot";
            String rutaPng = directorio + File.separator + nombreArchivo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph ArbolB {\n");
            dot.append("    node [shape=record, style=filled, fillcolor=lightgreen];\n");
            dot.append("    graph [ranksep=0.8];\n");

            contadorNodos = 0;
            if (arbol.getRaiz() != null) {
                generarNodosB(arbol.getRaiz(), dot, -1);
            }

            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN: " + e.getMessage();
            return null;
        }
    }

    private void generarNodosB(NodoB nodo, StringBuilder dot, int idPadre) {
        if (nodo == null) {
            return;
        }

        int idActual = contadorNodos++;

        StringBuilder label = new StringBuilder("{");
        for (int i = 0; i < nodo.numClaves; i++) {
            if (i > 0) {
                label.append("|");
            }

            ColeccionLibro coleccion = nodo.claves[i];
            if (coleccion != null) {
                label.append("AÑO: ").append(coleccion.getAnioPublicacion())
                        .append("\\n(").append(coleccion.getCopias().size()).append(" LIBROS)");
            }
        }
        label.append("}");

        dot.append("    node").append(idActual)
                .append(" [label=\"").append(label.toString())
                .append("\", shape=record, style=filled, fillcolor=lightgoldenrod1];\n");

        if (idPadre >= 0) {
            dot.append("    node").append(idPadre)
                    .append(" -> node").append(idActual).append(";\n");
        }

        if (!nodo.esHoja) {
            for (int i = 0; i <= nodo.numClaves; i++) {
                if (nodo.hijos[i] != null) {
                    generarNodosB(nodo.hijos[i], dot, idActual);
                }
            }
        }
    }

    public String generarImagenArbolBPlus(ArbolBPlus arbol, String nombreArchivo) {
        if (arbol == null || arbol.estaVacio()) {
            mensaje = "ERROR: EL ARBOL B+ ESTA VACIO";
            return null;
        }

        try {
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + nombreArchivo + ".dot";
            String rutaPng = directorio + File.separator + nombreArchivo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph ArbolBPlus {\n");
            dot.append("    node [shape=record, style=filled, fillcolor=lightyellow];\n");
            dot.append("    graph [ranksep=0.8];\n");

            contadorNodos = 0;
            if (arbol.getRaiz() != null) {
                generarNodosBPlus(arbol.getRaiz(), dot, -1);
            }

            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN: " + e.getMessage();
            return null;
        }
    }

    private void generarNodosBPlus(NodoBPlus nodo, StringBuilder dot, int idPadre) {
        if (nodo == null) {
            return;
        }

        int idActual = contadorNodos++;

        StringBuilder label = new StringBuilder("{");
        for (int i = 0; i < nodo.getNumClaves(); i++) {
            if (i > 0) {
                label.append("|");
            }

            ColeccionLibro coleccion = nodo.getClaves()[i];
            if (coleccion != null) {
                label.append(coleccion.getAnioPublicacion());
            } else {
                label.append(" ");
            }
        }
        label.append("}");

        dot.append("    node").append(idActual)
                .append(" [label=\"").append(label.toString())
                .append("\", shape=record, style=filled, fillcolor=")
                .append(nodo.isEsHoja() ? "lightyellow" : "lightblue")
                .append("];\n");

        if (idPadre >= 0) {
            dot.append("    node").append(idPadre)
                    .append(" -> node").append(idActual)
                    .append(" [color=gray];\n");
        }

        if (!nodo.isEsHoja()) {
            for (int i = 0; i <= nodo.getNumClaves(); i++) {
                if (nodo.getHijos()[i] != null) {
                    generarNodosBPlus(nodo.getHijos()[i], dot, idActual);
                }
            }
        }

        if (nodo.isEsHoja() && nodo.getSiguiente() != null) {
            int idSiguiente = contadorNodos;
            if (nodo.getSiguiente().getNumClaves() > 0) {
                generarNodosBPlus(nodo.getSiguiente(), dot, -1);
            }
            dot.append("    node").append(idActual)
                    .append(" -> node").append(idSiguiente)
                    .append(" [style=dashed, color=red, label=\"siguiente\"];\n");
        }
    }

    public String generarImagenTablaHash(TablaHashLibros tablaHash, String nombreArchivo) {
        if (tablaHash == null || tablaHash.getTamanio() == 0) {
            mensaje = "ERROR: LA TABLA HASH ESTA VACIA";
            return null;
        }

        try {
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + nombreArchivo + ".dot";
            String rutaPng = directorio + File.separator + nombreArchivo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph TablaHash {\n");
            dot.append("    rankdir=LR;\n");
            dot.append("    node [shape=record];\n");

            Libro[] libros = tablaHash.toArray();

            int capacidad = tablaHash.getCapacidad();
            int mostrarHasta = Math.min(capacidad, 20);

            for (int i = 0; i < mostrarHasta; i++) {
                dot.append("    indice").append(i)
                        .append(" [label=\"").append(i).append("\", style=filled, fillcolor=lightgray];\n");
            }

            if (capacidad > mostrarHasta) {
                dot.append("    mas [label=\"...\", shape=plaintext];\n");
            }

            contadorNodos = 0;
            for (Libro libro : libros) {
                int hash = Math.abs(libro.getIsbn().hashCode() % capacidad);

                if (hash < mostrarHasta) {
                    int idNodo = contadorNodos++;
                    String titulo = libro.getTitulo();
                    if (titulo.length() > 12) {
                        titulo = titulo.substring(0, 10) + "..";
                    }

                    dot.append("    libro").append(idNodo)
                            .append(" [label=\"").append(titulo)
                            .append("\\n").append(libro.getIsbn())
                            .append("\", style=filled, fillcolor=lightblue];\n");

                    dot.append("    indice").append(hash)
                            .append(" -> libro").append(idNodo).append(";\n");
                }
            }

            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN: " + e.getMessage();
            return null;
        }
    }

    public String generarImagenGrafo(Grafo grafo, String nombreArchivo) {
        if (grafo == null || grafo.getNumVertices() == 0) {
            mensaje = "ERROR: EL GRAFO ESTA VACIO";
            return null;
        }

        try {
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + nombreArchivo + ".dot";
            String rutaPng = directorio + File.separator + nombreArchivo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph RedBibliotecas {\n");
            dot.append("    node [shape=circle, style=filled, fillcolor=lightcoral];\n");
            dot.append("    graph [rankdir=TB];\n");

            String[] nombres = grafo.getNombresBibliotecas();
            int[][] matrizAdy = grafo.getMatrizAdyacencia();
            int[][] matrizTiempos = grafo.getMatrizTiempos();
            int[][] matrizCostos = grafo.getMatrizCostos();

            for (int i = 0; i < grafo.getNumVertices(); i++) {
                String nombre = nombres[i];
                if (nombre.length() > 8) {
                    nombre = nombre.substring(0, 6) + "..";
                }
                dot.append("    \"").append(nombres[i]).append("\"")
                        .append(" [label=\"").append(nombre).append("\"];\n");
            }

            for (int i = 0; i < grafo.getNumVertices(); i++) {
                for (int j = 0; j < grafo.getNumVertices(); j++) {
                    if (matrizAdy[i][j] == 1) {
                        dot.append("    \"").append(nombres[i]).append("\"")
                                .append(" -> \"").append(nombres[j]).append("\"")
                                .append(" [label=\"T:").append(matrizTiempos[i][j])
                                .append(" C:").append(matrizCostos[i][j])
                                .append("\"];\n");
                    }
                }
            }

            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN: " + e.getMessage();
            return null;
        }
    }

    public String generarImagenRutaOptima(Grafo grafo, String origen, String destino, boolean porTiempo) {
        if (grafo == null || grafo.getNumVertices() == 0) {
            mensaje = "ERROR: EL GRAFO ESTA VACIO";
            return null;
        }

        ResultadoDijkstra resultado = porTiempo
                ? grafo.dijkstraPorTiempo(origen, destino)
                : grafo.dijkstraPorCosto(origen, destino);

        if (resultado == null || resultado.getRuta() == null || resultado.getRuta().length == 0) {
            mensaje = "ERROR: NO SE ENCONTRO UNA RUTA ENTRE " + origen + " Y " + destino;
            return null;
        }

        try {
            String tipo = porTiempo ? "TIEMPO" : "COSTO";
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + "RUTA_OPTIMA_" + tipo + ".dot";
            String rutaPng = directorio + File.separator + "RUTA_OPTIMA_" + tipo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph RutaOptima {\n");
            dot.append("    node [shape=circle, style=filled, fillcolor=lightblue];\n");
            dot.append("    graph [rankdir=LR];\n");

            String[] nombres = grafo.getNombresBibliotecas();
            int[][] matrizAdy = grafo.getMatrizAdyacencia();
            int[][] matrizTiempos = grafo.getMatrizTiempos();
            int[][] matrizCostos = grafo.getMatrizCostos();

            for (int i = 0; i < grafo.getNumVertices(); i++) {
                String nombre = nombres[i];
                if (nombre.length() > 8) {
                    nombre = nombre.substring(0, 6) + "..";
                }
                dot.append("    \"").append(nombres[i]).append("\" [label=\"")
                        .append(nombre).append("\"];\n");
            }

            for (int i = 0; i < grafo.getNumVertices(); i++) {
                for (int j = 0; j < grafo.getNumVertices(); j++) {
                    if (matrizAdy[i][j] == 1) {
                        dot.append("    \"").append(nombres[i]).append("\" -> \"").append(nombres[j]).append("\"")
                                .append(" [label=\"T:").append(matrizTiempos[i][j])
                                .append(" C:").append(matrizCostos[i][j])
                                .append("\", color=\"gray\"];\n");
                    }
                }
            }

            String[] ruta = resultado.getRuta();
            for (int i = 0; i < ruta.length - 1; i++) {
                dot.append("    \"").append(ruta[i]).append("\" -> \"").append(ruta[i + 1]).append("\"")
                        .append(" [color=\"red\", penwidth=3.0, label=\"RUTA\"];\n");
            }

            for (String nodo : ruta) {
                dot.append("    \"").append(nodo).append("\" [fillcolor=lightgreen, style=filled];\n");
            }

            dot.append("    labelloc=\"t\";\n");
            dot.append("    label=\"RUTA OPTIMA POR ").append(tipo)
                    .append(" | TOTAL: ").append(resultado.getDistanciaTotal()).append("\";\n");
            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN DE RUTA OPTIMA GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN: " + e.getMessage();
            return null;
        }
    }

    public String generarImagenListaEnlazada(ListaEnlazada lista, String nombreArchivo) {
        if (lista == null || lista.estaVacia()) {
            mensaje = "ERROR: LA LISTA ENLAZADA ESTÁ VACÍA";
            return null;
        }

        try {
            String directorio = obtenerDirectorioEjecucion();
            String rutaDot = directorio + File.separator + nombreArchivo + ".dot";
            String rutaPng = directorio + File.separator + nombreArchivo + ".png";

            StringBuilder dot = new StringBuilder();
            dot.append("digraph ListaEnlazada {\n");
            dot.append("    rankdir=LR;\n");
            dot.append("    node [shape=record, style=filled, fillcolor=lightyellow, color=black, fontname=\"Arial\"];\n");
            dot.append("    edge [color=gray40, penwidth=1.5];\n\n");

            NodoLista actual = lista.getCabeza();
            int index = 0;

            while (actual != null) {
                Libro libro = actual.libro;
                String nodoName = "n" + index;
                dot.append(String.format("    %s [label=\"<f0> | <f1> %s\\n%s\\nISBN:%s | <f2>\"];\n",
                        nodoName,
                        libro.getTitulo().replace("\"", "\\\""),
                        libro.getAutor().replace("\"", "\\\""),
                        libro.getIsbn().replace("\"", "\\\"")
                ));
                actual = actual.siguiente;
                index++;
            }

            for (int i = 0; i < index - 1; i++) {
                dot.append(String.format("    n%d -> n%d;\n", i, i + 1));
            }

            dot.append("}\n");

            Files.write(Paths.get(rutaDot), dot.toString().getBytes());

            if (ejecutarGraphviz(rutaDot, rutaPng)) {
                ultimoPathGenerado = rutaPng;
                mensaje = "IMAGEN DE LISTA ENLAZADA GENERADA CORRECTAMENTE: " + rutaPng;
                return rutaPng;
            } else {
                mensaje = "ERROR: NO SE PUDO EJECUTAR GRAPHVIZ";
                return null;
            }

        } catch (IOException e) {
            mensaje = "ERROR AL GENERAR IMAGEN DE LISTA: " + e.getMessage();
            return null;
        }
    }

    private boolean ejecutarGraphviz(String rutaDot, String rutaPng) {
        try {
            String[] comandos = {
                "dot",
                "-Tpng",
                rutaDot,
                "-o",
                rutaPng
            };

            ProcessBuilder pb = new ProcessBuilder(comandos);
            pb.redirectErrorStream(true);
            Process proceso = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream())
            );

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            int exitCode = proceso.waitFor();

            if (exitCode == 0 && new File(rutaPng).exists()) {
                return true;
            } else {
                mensaje = "ERROR: GRAPHVIZ NO ESTA INSTALADO O NO SE ENCUENTRA EN PATH";
                return false;
            }

        } catch (IOException | InterruptedException e) {
            mensaje = "ERROR AL EJECUTAR GRAPHVIZ: " + e.getMessage();
            return false;
        }
    }

    public boolean graphvizEstaDisponible() {
        try {
            ProcessBuilder pb = new ProcessBuilder("dot", "-V");
            Process proceso = pb.start();
            int exitCode = proceso.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
