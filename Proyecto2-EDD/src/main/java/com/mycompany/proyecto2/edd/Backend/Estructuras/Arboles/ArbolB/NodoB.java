/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Arboles.ArbolB;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;

/**
 *
 * @author alesso
 */
public class NodoB {

    public int numClaves;
    private Libro[] claves;
    private NodoB[] hijos;
    private boolean esHoja;

    public NodoB(int orden, boolean esHoja) {
        this.numClaves = 0;
        this.claves = new Libro[orden - 1];
        this.hijos = new NodoB[orden];
        this.esHoja = esHoja;
    }

    public int getNumClaves() {
        return numClaves;
    }

    public Libro[] getClaves() {
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

    public void setClaves(Libro[] claves) {
        this.claves = claves;
    }

    public void setHijos(NodoB[] hijos) {
        this.hijos = hijos;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }
}
