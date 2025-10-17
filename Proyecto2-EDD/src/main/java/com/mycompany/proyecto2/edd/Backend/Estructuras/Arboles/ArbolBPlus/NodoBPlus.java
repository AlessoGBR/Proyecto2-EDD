/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolBPlus;

/**
 *
 * @author alesso
 */
public class NodoBPlus {

    public int numClaves;
    public String[] claves;
    public Object[] valores;
    public NodoBPlus[] hijos;
    public boolean esHoja;
    public NodoBPlus siguiente;

    public NodoBPlus(int orden, boolean esHoja) {
        this.numClaves = 0;
        this.claves = new String[orden - 1];
        this.valores = new Object[orden - 1];
        this.hijos = new NodoBPlus[orden];
        this.esHoja = esHoja;
        this.siguiente = null;
    }

    public int getNumClaves() {
        return numClaves;
    }

    public String[] getClaves() {
        return claves;
    }

    public Object[] getValores() {
        return valores;
    }

    public NodoBPlus[] getHijos() {
        return hijos;
    }

    public boolean isEsHoja() {
        return esHoja;
    }

    public NodoBPlus getSiguiente() {
        return siguiente;
    }
}
