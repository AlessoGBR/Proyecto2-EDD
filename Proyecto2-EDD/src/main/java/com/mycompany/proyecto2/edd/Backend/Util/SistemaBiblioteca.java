/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Util;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolAVL.ArbolAVL;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.ArbolB;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus.ArbolBPlus;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Cola.Cola;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos.Grafo;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos.ResultadoDijkstra;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;
import com.mycompany.proyecto2.edd.Backend.Estructuras.Pila.Pila;
import com.mycompany.proyecto2.edd.Backend.Estructuras.TablaHash.TablaHashLibros;
import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import com.mycompany.proyecto2.edd.Biblioteca;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alesso
 */
public class SistemaBiblioteca {

    private String mensaje;

    private List<Biblioteca> bibliotecas;
    private Grafo grafoRed;
    private MetodosOrdenamiento ordenardor;
    private ArbolAVL arbolAVL;
    private ArbolB arbolB;
    private ArbolBPlus arbolBPlus;
    private TablaHashLibros tablaHash;
    private ListaEnlazada listaPrincipal;
    private Pila pilaDeshacer;

    public SistemaBiblioteca() {
        this.mensaje = "SISTEMA INICIADO CORRECTAMENTE";

        this.bibliotecas = new ArrayList<>();
        this.grafoRed = new Grafo(50);
        this.ordenardor = new MetodosOrdenamiento();
        this.arbolAVL = new ArbolAVL();
        this.arbolB = new ArbolB(10);
        this.arbolBPlus = new ArbolBPlus(10);
        this.tablaHash = new TablaHashLibros(101);
        this.listaPrincipal = new ListaEnlazada();
        this.pilaDeshacer = new Pila();
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean agregarBiblioteca(String id, String nombre, String ubicacion, int tiempoIngreso,
            int tiempoTraspaso, int intervaloDespacho) {

        if (id == null || id.trim().isEmpty()) {
            mensaje = "ERROR: EL ID DE LA BIBLIOTECA NO PUEDE ESTAR VACIO";
            return false;
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            mensaje = "ERROR: EL NOMBRE DE LA BIBLIOTECA NO PUEDE ESTAR VACIO";
            return false;
        }

        if (tiempoIngreso < 0 || tiempoTraspaso < 0 || intervaloDespacho < 0) {
            mensaje = "ERROR: LOS TIEMPOS NO PUEDEN SER NEGATIVOS";
            return false;
        }

        for (Biblioteca b : bibliotecas) {
            if (b.getId().equalsIgnoreCase(id)) {
                mensaje = "ERROR: YA EXISTE UNA BIBLIOTECA CON EL ID " + id;
                return false;
            }
        }

        if (buscarBiblioteca(nombre) != null) {
            mensaje = "ERROR: YA EXISTE UNA BIBLIOTECA CON EL NOMBRE " + nombre;
            return false;
        }

        Biblioteca nueva = new Biblioteca(id, nombre, ubicacion, tiempoIngreso,
                tiempoTraspaso, intervaloDespacho);

        bibliotecas.add(nueva);
        grafoRed.agregarBiblioteca(id);

        mensaje = "BIBLIOTECA AGREGADA CORRECTAMENTE: " + nombre;
        return true;
    }

    public boolean eliminarBiblioteca(String id) {

        Biblioteca bib = buscarBibliotecaPorId(id);

        if (bib == null) {
            mensaje = "ERROR: NO EXISTE LA BIBLIOTECA CON ID: " + id;
            return false;
        }

        if (bib.getTotalLibros() > 0) {
            mensaje = "ERROR: LA BIBLIOTECA TIENE LIBROS, NO SE PUEDE ELIMINAR";
            return false;
        }

        bibliotecas.remove(bib);
        mensaje = "BIBLIOTECA ELIMINADA CORRECTAMENTE: " + id;
        return true;
    }

    public Biblioteca buscarBibliotecaPorId(String id) {
        for (Biblioteca b : bibliotecas) {
            if (b.getId().equalsIgnoreCase(id)) {
                return b;
            }
        }
        return null;
    }

    public Biblioteca buscarBiblioteca(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (Biblioteca bib : bibliotecas) {
            if (bib.getNombre().equalsIgnoreCase(nombre)) {
                return bib;
            }
        }
        return null;
    }

    public List<Biblioteca> getBibliotecas() {
        return bibliotecas;
    }

    public List<Object[]> obtenerBibliotecasParaTabla() {
        List<Object[]> datos = new ArrayList<>();

        for (Biblioteca bib : bibliotecas) {
            Object[] fila = {
                bib.getId(),
                bib.getNombre(),
                bib.getUbicacion(),
                bib.getTiempoIngreso(),
                bib.getTiempoTraspaso(),
                bib.getIntervaloDespacho(),
                bib.getTotalLibros()
            };
            datos.add(fila);
        }

        return datos;
    }

    public boolean agregarLibro(Libro libro, String nombreBiblioteca) {
        if (libro == null) {
            mensaje = "ERROR: EL LIBRO NO PUEDE SER NULO";
            return false;
        }

        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);
        if (bib == null) {
            mensaje = "ERROR: LA BIBLIOTECA NO EXISTE";
            return false;
        }

        Libro existente = tablaHash.buscar(libro.getIsbn());
        if (existente != null) {
            if (!existente.getBibliotecaActual().equals(nombreBiblioteca)) {
                mensaje = "ERROR: EL ISBN " + libro.getIsbn()
                        + " YA EXISTE EN OTRA BIBLIOTECA (" + existente.getBibliotecaActual() + ")";
                return false;
            } else {
                System.out.println("AVISO: Edición repetida del ISBN " + libro.getIsbn());
            }
        }

        pilaDeshacer.apilar(libro);
        listaPrincipal.insertarAlFinal(libro);
        arbolAVL.insertar(libro);
        arbolB.insertar(libro);
        arbolBPlus.insertar(libro);
        tablaHash.insertar(libro);
        bib.getCatalogoLocal().insertarAlFinal(libro);
        libro.setBibliotecaActual(nombreBiblioteca);

        mensaje = "LIBRO AGREGADO CORRECTAMENTE EN TODAS LAS ESTRUCTURAS";
        return true;
    }

    public boolean eliminarLibro(String isbn) {
        Libro libro = tablaHash.buscar(isbn);

        if (libro == null) {
            mensaje = "ERROR: NO SE ENCONTRO EL LIBRO CON ISBN " + isbn;
            return false;
        }

        StringBuilder log = new StringBuilder("LIBRO ELIMINADO DE:\n");

        if (arbolAVL.eliminar(libro)) {
            log.append("- ARBOL AVL\n");
        }

        if (arbolB.eliminar(libro.getAnioPublicacion())) {
            log.append("- ARBOL B\n");
        }

        if (arbolBPlus.eliminarPorGeneroYIsbn(libro.getGenero(), isbn)) {
            log.append("- ARBOL B+\n");
        }

        if (tablaHash.eliminar(isbn)) {
            log.append("- TABLA HASH\n");
        }

        if (listaPrincipal.eliminarPorISBN(isbn)) {
            log.append("- LISTA PRINCIPAL\n");
        }

        for (Biblioteca bib : bibliotecas) {
            if (bib.getCatalogoLocal().eliminarPorISBN(isbn)) {
                log.append("- BIBLIOTECA " + bib.getNombre() + "\n");
            }
        }

        mensaje = log.toString();
        return true;
    }

    public Libro buscarLibroPorISBN(String isbn) {
        return tablaHash.buscar(isbn);
    }

    public Libro buscarLibroPorTitulo(String titulo) {
        return arbolAVL.buscarPorTitulo(titulo);
    }

    public ListaEnlazada buscarLibrosPorISBN(String isbn) {
        return tablaHash.buscarTodos(isbn);
    }

    public ListaEnlazada buscarLibrosPorTitulo(String titulo) {
        return arbolAVL.buscarTodosPorTitulo(titulo);
    }

    public ListaEnlazada buscarLibrosPorAutor(String autor) {
        return listaPrincipal.buscarPorAutor(autor);
    }

    public ArrayList<Libro> buscarLibrosPorGenero(String genero) {
        return arbolBPlus.buscarPorGenero(genero);
    }

    public ArrayList<Libro> buscarLibrosPorRangoAnios(int anioInicio, int anioFin) {
        return arbolB.buscarPorRangoAnios(anioInicio, anioFin);
    }

    public List<Object[]> ordenarLibrosDeBiblioteca(String nombreBiblioteca, String criterio, String metodo) {
        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);

        if (bib == null) {
            mensaje = "ERROR: BIBLIOTECA NO ENCONTRADA";
            return new ArrayList<>();
        }

        if (bib.getCatalogoLocal().estaVacia()) {
            mensaje = "ERROR: LA BIBLIOTECA NO TIENE LIBROS";
            return new ArrayList<>();
        }

        Libro[] librosOrdenados = ordenardor.ordenar(bib.getCatalogoLocal(), criterio, metodo);

        List<Object[]> datos = new ArrayList<>();
        for (int i = 0; i < librosOrdenados.length; i++) {
            Libro libro = librosOrdenados[i];
            Object[] fila = {
                i + 1,
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getGenero(),
                libro.getEstado().getDescripcion()
            };
            datos.add(fila);
        }

        mensaje = ordenardor.getMensaje();
        return datos;
    }

    public List<Object[]> ordenarLibrosDeBibliotecaRed(String nombreBiblioteca, String criterio, String metodo) {
        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);

        if (bib == null) {
            mensaje = "ERROR: BIBLIOTECA NO ENCONTRADA";
            return new ArrayList<>();
        }

        if (bib.getCatalogoLocal().estaVacia()) {
            mensaje = "ERROR: LA BIBLIOTECA NO TIENE LIBROS";
            return new ArrayList<>();
        }

        Libro[] librosOrdenados = ordenardor.ordenar(bib.getCatalogoLocal(), criterio, metodo);

        List<Object[]> datos = new ArrayList<>();
        for (int i = 0; i < librosOrdenados.length; i++) {
            Libro libro = librosOrdenados[i];
            Object[] fila = {
                i + 1,
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),};
            datos.add(fila);
        }

        mensaje = ordenardor.getMensaje();
        return datos;
    }

    public List<Object[]> ordenarTodosLosLibros(String criterio, String metodo) {
        if (listaPrincipal.estaVacia()) {
            mensaje = "ERROR: NO HAY LIBROS EN EL SISTEMA";
            return new ArrayList<>();
        }

        Libro[] librosOrdenados = ordenardor.ordenar(listaPrincipal, criterio, metodo);

        List<Object[]> datos = new ArrayList<>();
        for (int i = 0; i < librosOrdenados.length; i++) {
            Libro libro = librosOrdenados[i];
            Object[] fila = {
                i + 1,
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getGenero(),
                libro.getEstado().getDescripcion(),
                libro.getBibliotecaActual()
            };
            datos.add(fila);
        }

        mensaje = ordenardor.getMensaje();
        return datos;
    }

    public String obtenerEstadisticasOrdenamiento() {
        return "TIEMPO: " + (ordenardor.getTiempoEjecucion() / 1_000_000.0) + " MS\n"
                + "COMPARACIONES: " + ordenardor.getNumeroComparaciones() + "\n"
                + "INTERCAMBIOS: " + ordenardor.getNumeroIntercambios() + "\n"
                + "COMPLEJIDAD: " + obtenerComplejidad();
    }

    private String obtenerComplejidad() {
        String metodo = mensaje.split(":")[1].split("\\|")[0].trim();

        switch (metodo.toUpperCase()) {
            case "BURBUJA":
            case "SELECCION":
            case "INSERCION":
                return "O(N^2)";
            case "SHELL":
                return "O(N^(3/2))";
            case "QUICKSORT":
                return "O(N LOG N)";
            default:
                return "DESCONOCIDA";
        }
    }

    public List<Object[]> obtenerTodosLosLibrosParaTabla() {
        List<Object[]> datos = new ArrayList<>();

        Libro[] libros = listaPrincipal.toArray();
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getGenero(),
                libro.getEstado().getDescripcion(),
                libro.getBibliotecaActual()
            };
            datos.add(fila);
        }

        return datos;
    }

    public List<Object[]> obtenerLibrosDeArbolAVLParaTabla() {
        List<Object[]> datos = new ArrayList<>();

        arbolAVL.inOrder(libro -> {
            Object[] fila = {
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getGenero(),
                libro.getEstado().getDescripcion()
            };
            datos.add(fila);
        });

        return datos;
    }

    public List<Object[]> obtenerLibrosDeBibliotecaParaTabla(String nombreBiblioteca) {
        List<Object[]> datos = new ArrayList<>();

        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);
        if (bib == null) {
            mensaje = "ERROR: BIBLIOTECA NO ENCONTRADA";
            return datos;
        }

        bib.getCatalogoLocal().recorrer((libro, indice) -> {
            Object[] fila = {
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getAnioPublicacion(),
                libro.getGenero(),
                libro.getEstado().getDescripcion()
            };
            datos.add(fila);
        });

        return datos;
    }

    public boolean agregarConexion(String origen, String destino, int tiempo, int costo, boolean bidireccional) {
        if (bidireccional) {
            return grafoRed.agregarConexionBidireccional(origen, destino, tiempo, costo);
        } else {
            return grafoRed.agregarConexion(origen, destino, tiempo, costo);
        }
    }

    public boolean eliminarConexion(String origen, String destino) {
        return grafoRed.eliminarConexion(origen, destino);
    }

    public List<Object[]> obtenerConexionesParaTabla() {
        List<Object[]> datos = new ArrayList<>();

        String[] nombres = grafoRed.getNombresBibliotecas();
        int[][] matrizAdy = grafoRed.getMatrizAdyacencia();
        int[][] matrizTiempos = grafoRed.getMatrizTiempos();
        int[][] matrizCostos = grafoRed.getMatrizCostos();

        for (int i = 0; i < grafoRed.getNumVertices(); i++) {
            for (int j = 0; j < grafoRed.getNumVertices(); j++) {
                if (matrizAdy[i][j] == 1) {
                    Object[] fila = {
                        nombres[i],
                        nombres[j],
                        matrizTiempos[i][j],
                        matrizCostos[i][j]
                    };
                    datos.add(fila);
                }
            }
        }

        return datos;
    }

    public ResultadoDijkstra calcularRutaOptimaPorTiempo(String origen, String destino) {
        return grafoRed.dijkstraPorTiempo(origen, destino);
    }

    public ResultadoDijkstra calcularRutaOptimaPorCosto(String origen, String destino) {
        return grafoRed.dijkstraPorCosto(origen, destino);
    }

    public List<Object[]> obtenerColaIngresoParaTabla(String nombreBiblioteca) {
        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);
        if (bib == null) {
            return new ArrayList<>();
        }

        List<Object[]> datos = new ArrayList<>();
        bib.getColaIngreso().recorrer((libro, indice) -> {
            Object[] fila = {
                indice + 1,
                libro.getTitulo(),
                libro.getIsbn(),
                libro.getEstado().getDescripcion()
            };
            datos.add(fila);
        });

        return datos;
    }

    public List<Object[]> obtenerColaPreparacionParaTabla(String nombreBiblioteca) {
        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);
        if (bib == null) {
            return new ArrayList<>();
        }

        List<Object[]> datos = new ArrayList<>();
        bib.getColaPreparacion().recorrer((libro, indice) -> {
            Object[] fila = {
                indice + 1,
                libro.getTitulo(),
                libro.getIsbn(),
                libro.getEstado().getDescripcion()
            };
            datos.add(fila);
        });

        return datos;
    }

    public List<Object[]> obtenerColaSalidaParaTabla(String nombreBiblioteca) {
        Biblioteca bib = buscarBiblioteca(nombreBiblioteca);
        if (bib == null) {
            return new ArrayList<>();
        }

        List<Object[]> datos = new ArrayList<>();
        bib.getColaSalida().recorrer((libro, indice) -> {
            Object[] fila = {
                indice + 1,
                libro.getTitulo(),
                libro.getIsbn(),
                libro.getEstado().getDescripcion()
            };
            datos.add(fila);
        });

        return datos;
    }

    public boolean deshacerUltimaOperacion() {
        if (pilaDeshacer.estaVacia()) {
            mensaje = "NO HAY OPERACIONES PARA DESHACER";
            return false;
        }

        Libro libro = pilaDeshacer.desapilar();
        eliminarLibro(libro.getIsbn());
        mensaje = "OPERACION DESHECHA: SE ELIMINO " + libro.getTitulo();
        return true;
    }

    public ArbolAVL getArbolAVL() {
        return arbolAVL;
    }

    public ArbolB getArbolB() {
        return arbolB;
    }

    public ArbolBPlus getArbolBPlus() {
        return arbolBPlus;
    }

    public TablaHashLibros getTablaHash() {
        return tablaHash;
    }

    public ListaEnlazada getListaPrincipal() {
        return listaPrincipal;
    }

    public Grafo getGrafoRed() {
        return grafoRed;
    }

    public int getTotalLibros() {
        return listaPrincipal.getTamanio();
    }

    public int getTotalBibliotecas() {
        return bibliotecas.size();
    }

    public ResultadoDijkstra registrarTransferencia(Libro libro, String origen, String destino, boolean usarTiempo) {
        ResultadoDijkstra resultado = usarTiempo
                ? calcularRutaOptimaPorTiempo(origen, destino)
                : calcularRutaOptimaPorCosto(origen, destino);

        if (resultado == null || resultado.getRuta() == null) {
            mensaje = "NO SE PUDO CALCULAR TRANSFERENCIA";
            return null;
        }

        libro.setBibliotecaActual(origen);
        libro.setBibliotecaDestino(destino);
        libro.setEstado(Libro.EstadoLibro.EN_TRANSITO);

        Biblioteca biblioOrigen = buscarBiblioteca(origen);
        if (biblioOrigen == null) {
            mensaje = "NO EXISTE BIBLIOTECA DE ORIGEN: " + origen;
            return null;
        }

        biblioOrigen.getColaSalida().encolar(libro);
        pilaDeshacer.apilar(libro);
        mensaje = "TRANSFERENCIA REGISTRADA " + libro.getTitulo() + " RUTA OPTIMA " + resultado.getRutaComoTexto();
        return resultado;
    }

    public void procesarTransferencia(Libro libro, ResultadoDijkstra resultado) {
        if (resultado == null || resultado.getRuta() == null) {
            mensaje = "NO HAY RUTA PARA ESTE LIBRO";
            return;
        }

        String[] recorrido = resultado.getRuta();
        for (int i = 0; i < recorrido.length; i++) {
            String actualId = recorrido[i];
            Biblioteca actual = buscarBiblioteca(actualId);

            if (actual == null) {
                continue;
            }

            boolean esUltima = (i == recorrido.length - 1);

            if (esUltima) {
                actual.getColaIngreso().encolar(libro);
                actual.getCatalogoLocal().insertarAlFinal(libro);
                libro.setBibliotecaActual(actual.getNombre());
                libro.setEstado(Libro.EstadoLibro.DISPONIBLE);
                mensaje = "LIBRO " + libro.getTitulo() + " LLEGO A SU DESTINO " + actual.getNombre();
            } else if (i == 0) {
                actual.getColaSalida().encolar(libro);
                libro.setBibliotecaActual(actual.getNombre());
                mensaje = "LIBRO " + libro.getTitulo() + " SALIO DE " + actual.getNombre();
            } else {
                actual.getColaPreparacion().encolar(libro);
                libro.setBibliotecaActual(actual.getNombre());
                mensaje = "LIBRO " + libro.getTitulo() + " EN TRANSITO EN " + actual.getNombre();
            }

            pilaDeshacer.apilar(libro);
        }
    }

    public void confirmarEntrega(Libro libro) {
        Biblioteca destino = buscarBiblioteca(libro.getBibliotecaDestino());
        if (destino == null) {
            mensaje = "NO EXISTE LA BIBLIOTECA DE DESTINO";
            return;
        }

        destino.getColaIngreso().eliminarPorISBN(libro.getIsbn());
        destino.getCatalogoLocal().insertarAlFinal(libro);
        libro.setEstado(Libro.EstadoLibro.DISPONIBLE);

        mensaje = "LIBRO " + libro.getTitulo() + "ENTREGADO E INGRESADO EN " + destino.getNombre();
    }

    public void deshacerUltimaTransferencia() {
        if (pilaDeshacer.estaVacia()) {
            mensaje = "NO HAY TRANSFERENCIAS POR DESACER";
            return;
        }

        Libro libro = pilaDeshacer.desapilar();

        Biblioteca origen = buscarBiblioteca(libro.getBibliotecaActual());
        Biblioteca destino = buscarBiblioteca(libro.getBibliotecaDestino());

        if (origen != null) {
            origen.getColaSalida().eliminarPorISBN(libro.getIsbn());
        }
        if (destino != null) {
            destino.getColaIngreso().eliminarPorISBN(libro.getIsbn());
        }

        if (origen != null) {
            libro.setBibliotecaActual(origen.getNombre());
            libro.setEstado(Libro.EstadoLibro.DISPONIBLE);
            origen.getCatalogoLocal().insertarAlFinal(libro);
        }

        mensaje = "TRANSFERENCIA REVERTIDA: " + libro.getTitulo();
    }

    public void limpiarSistema() {
        bibliotecas.clear();
        grafoRed.limpiar();
        arbolAVL.limpiar();
        arbolB.limpiar();
        arbolBPlus.limpiar();
        tablaHash.limpiar();
        listaPrincipal.limpiar();
        pilaDeshacer.limpiar();
        mensaje = "SISTEMA LIMPIADO CORRECTAMENTE";
    }
}
