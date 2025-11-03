/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus;

import com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB.ColeccionLibro;

/**
 *
 * @author alesso
 */
public class NodoBPlus {

    private ColeccionLibro[] claves;
    private NodoBPlus[] hijos;
    private int numClaves;
    private boolean esHoja;
    private NodoBPlus siguiente;

    public NodoBPlus(int orden, boolean esHoja) {
        this.claves = new ColeccionLibro[orden];
        this.hijos = new NodoBPlus[orden - 1];
        this.numClaves = 0;
        this.esHoja = esHoja;
        this.siguiente = null;
    }

    public ColeccionLibro[] getClaves() {
        return claves;
    }

    public NodoBPlus[] getHijos() {
        return hijos;
    }

    public int getNumClaves() {
        return numClaves;
    }

    public void setNumClaves(int numClaves) {
        this.numClaves = numClaves;
    }

    public boolean isEsHoja() {
        return esHoja;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }

    public NodoBPlus getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoBPlus siguiente) {
        this.siguiente = siguiente;
    }

    public ColeccionLibro getClaveMinima() {
        if (numClaves > 0) {
            return claves[0];
        }
        return null;
    }
}
