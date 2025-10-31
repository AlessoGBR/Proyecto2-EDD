/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Estructuras.Grafos;

/**
 *
 * @author alesso
 */
public class ResultadoDijkstra {

    private String[] ruta;
    private int distanciaTotal;
    private boolean esPorTiempo;

    public ResultadoDijkstra(String[] ruta, int distanciaTotal, boolean esPorTiempo) {
        this.ruta = ruta;
        this.distanciaTotal = distanciaTotal;
        this.esPorTiempo = esPorTiempo;
    }

    public String[] getRuta() {
        return ruta;
    }

    public int getDistanciaTotal() {
        return distanciaTotal;
    }

    public boolean isEsPorTiempo() {
        return esPorTiempo;
    }

    public int getNumeroParadas() {
        return ruta.length - 1;
    }

    public String getRutaComoTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ruta.length; i++) {
            sb.append(ruta[i]);
            if (i < ruta.length - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String criterio = esPorTiempo ? "TIEMPO" : "COSTO";
        return "RUTA OPTIMA POR " + criterio + ":\n"
                + "CAMINO: " + getRutaComoTexto() + "\n"
                + criterio + " TOTAL: " + distanciaTotal + "\n"
                + "NUMERO DE PARADAS: " + getNumeroParadas();
    }
}
