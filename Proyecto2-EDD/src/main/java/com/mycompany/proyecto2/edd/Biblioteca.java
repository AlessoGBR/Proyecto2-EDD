/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Cola.Cola;
import com.mycompany.proyecto2.edd.Backend.Estructuras.ListaEnlazada.ListaEnlazada;

/**
 *
 * @author alesso
 */
public class Biblioteca {

    private String id;
    private String nombre;
    private String ubicacion;
    private int tiempoIngreso;
    private int tiempoTraspaso;
    private int intervaloDespacho;

    private ListaEnlazada catalogoLocal;
    private Cola colaIngreso;
    private Cola colaPreparacion;
    private Cola colaSalida;

    public Biblioteca(String id, String nombre, String ubicacion, int tiempoIngreso,
            int tiempoTraspaso, int intervaloDespacho) {
        this.id = id;
        this.nombre = nombre.toUpperCase();
        this.ubicacion = ubicacion;
        this.tiempoIngreso = tiempoIngreso;
        this.tiempoTraspaso = tiempoTraspaso;
        this.intervaloDespacho = intervaloDespacho;

        this.catalogoLocal = new ListaEnlazada();
        this.colaIngreso = new Cola();
        this.colaPreparacion = new Cola();
        this.colaSalida = new Cola();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getTiempoIngreso() {
        return tiempoIngreso;
    }

    public void setTiempoIngreso(int tiempoIngreso) {
        this.tiempoIngreso = tiempoIngreso;
    }

    public int getTiempoTraspaso() {
        return tiempoTraspaso;
    }

    public void setTiempoTraspaso(int tiempoTraspaso) {
        this.tiempoTraspaso = tiempoTraspaso;
    }

    public int getIntervaloDespacho() {
        return intervaloDespacho;
    }

    public void setIntervaloDespacho(int intervaloDespacho) {
        this.intervaloDespacho = intervaloDespacho;
    }

    public ListaEnlazada getCatalogoLocal() {
        return catalogoLocal;
    }

    public Cola getColaIngreso() {
        return colaIngreso;
    }

    public Cola getColaPreparacion() {
        return colaPreparacion;
    }

    public Cola getColaSalida() {
        return colaSalida;
    }

    public int getTotalLibros() {
        return catalogoLocal.getTamanio();
    }
}
