/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Util;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import com.mycompany.proyecto2.edd.Biblioteca;
import com.mycompany.proyecto2.edd.Frontend.Transferencias.PanelColas;
import javax.swing.SwingUtilities;

/**
 *
 * @author alesso
 */
public class Transferencia extends Thread {

    private final SistemaBiblioteca sistema;
    private final Libro libro;
    private final String[] ruta;
    private final boolean porTiempo;
    private PanelColas panelColas;

    public Transferencia(SistemaBiblioteca sistema, Libro libro, String[] ruta, boolean porTiempo, PanelColas panelColas) {
        this.sistema = sistema;
        this.libro = libro;
        this.ruta = ruta;
        this.porTiempo = porTiempo;
        this.panelColas = panelColas;
    }

    @Override
    public void run() {
        try {
            System.out.println("INICIANDO TRANSFERENCIA DE " + libro.getTitulo() + " (" + libro.getIsbn() + ")");
            System.out.println("RUTA: " + String.join(" -> ", ruta));

            for (int i = 0; i < ruta.length; i++) {
                String nombre = ruta[i];
                Biblioteca actual = sistema.buscarBiblioteca(nombre);

                if (actual == null) {
                    continue;
                }

                if (i < ruta.length - 1) {
                    String nombreDestino = ruta[i + 1];
                    Biblioteca destino = sistema.buscarBiblioteca(nombreDestino);

                    if (destino == null) {
                        continue;
                    }

                    actual.getColaSalida().encolar(libro);
                    SwingUtilities.invokeLater(() -> panelColas.actualizarBiblioteca(actual.getId()));
                    libro.setEstado(Libro.EstadoLibro.EN_TRANSITO);
                    System.out.println("[" + actual.getNombre() + "] COLOCANDO LIBRO EN COLA DE SALIDA");
                    Thread.sleep(actual.getIntervaloDespacho() * 1000L);

                    actual.getColaSalida().desencolar();
                    destino.getColaPreparacion().encolar(libro);
                    SwingUtilities.invokeLater(() -> panelColas.actualizarBiblioteca(actual.getId()));
                    System.out.println("LIBRO EN PREPARACION EN " + destino.getNombre());
                    Thread.sleep(destino.getTiempoTraspaso() * 1000L);

                    if (i == ruta.length - 2) {
                        destino.getColaPreparacion().desencolar();
                        destino.getColaIngreso().encolar(libro);
                        SwingUtilities.invokeLater(() -> panelColas.actualizarBiblioteca(actual.getId()));
                        System.out.println("LIBRO LLEGANDO A " + destino.getNombre());
                        Thread.sleep(destino.getTiempoIngreso() * 1000L);

                        destino.getColaIngreso().desencolar();
                        destino.getCatalogoLocal().insertarAlFinal(libro);
                        libro.setBibliotecaActual(destino.getNombre());
                        SwingUtilities.invokeLater(() -> panelColas.actualizarBiblioteca(actual.getId()));
                        libro.setEstado(Libro.EstadoLibro.DISPONIBLE);
                        System.out.println("LIBRO ALMACENADO EN " + destino.getNombre());
                    } else {
                        destino.getColaPreparacion().desencolar();
                        System.out.println("LIBRO SIGUE EN RUTA...");
                    }
                }

                System.out.println("--------------------------------------");
            }

            System.out.println("TRANSFERENCIA COMPLETADA PARA LIBRO: " + libro.getTitulo());

        } catch (InterruptedException e) {
            System.err.println("ERROR EN TRANSFERENCIA: " + e.getMessage());
        }
    }
}
