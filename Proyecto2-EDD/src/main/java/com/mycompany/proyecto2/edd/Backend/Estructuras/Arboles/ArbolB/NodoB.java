/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB;

/**
 *
 * @author alesso
 */
public class NodoB {

    public int numClaves;
    public ColeccionLibro[] claves;
    public NodoB[] hijos;
    public boolean esHoja;

    public NodoB(int orden, boolean esHoja) {
        this.numClaves = 0;
        this.claves = new ColeccionLibro[orden];
        this.hijos = new NodoB[orden + 1];
        this.esHoja = esHoja;
    }

    public int getNumClaves() {
        return numClaves;
    }

    public ColeccionLibro[] getClaves() {
        return claves;
    }

    public NodoB[] getHijos() {
        return hijos;
    }

    public boolean isEsHoja() {
        return esHoja;
    }

    public void setNumClaves(int numClaves) {
        this.numClaves = numClaves;
    }

    public void setClaves(ColeccionLibro[] claves) {
        this.claves = claves;
    }

    public void setHijos(NodoB[] hijos) {
        this.hijos = hijos;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }

    public boolean estaLleno(int orden) {
        return numClaves == orden - 1;
    }
}
