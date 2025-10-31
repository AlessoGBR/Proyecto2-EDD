/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Frontend.Busquedas;

import com.mycompany.proyecto2.edd.Backend.Util.ComparadorBusquedas;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author alesso
 */
public class GraficoComparacion extends JPanel {

    private ComparadorBusquedas.ResultadoComparacion[] resultados;
    private String titulo;

    public GraficoComparacion() {
        this.titulo = "COMPARACION DE TIEMPOS DE BUSQUEDA";
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
    }

    public void setDatos(ComparadorBusquedas.ResultadoComparacion[] resultados) {
        this.resultados = resultados;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (resultados == null || resultados.length == 0) {
            g.setColor(Color.BLACK);
            g.drawString("NO HAY DATOS PARA MOSTRAR", getWidth() / 2 - 80, getHeight() / 2);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margenIzquierdo = 100;
        int margenDerecho = 50;
        int margenSuperior = 80;
        int margenInferior = 100;

        int anchoGrafico = getWidth() - margenIzquierdo - margenDerecho;
        int altoGrafico = getHeight() - margenSuperior - margenInferior;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int tituloX = (getWidth() - fm.stringWidth(titulo)) / 2;
        g2d.drawString(titulo, tituloX, 30);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(margenIzquierdo, margenSuperior, margenIzquierdo, margenSuperior + altoGrafico);
        g2d.drawLine(margenIzquierdo, margenSuperior + altoGrafico,
                margenIzquierdo + anchoGrafico, margenSuperior + altoGrafico);

        double tiempoMaximo = 0;
        for (ComparadorBusquedas.ResultadoComparacion resultado : resultados) {
            if (resultado.getTiempoMs() > tiempoMaximo) {
                tiempoMaximo = resultado.getTiempoMs();
            }
        }

        if (tiempoMaximo == 0) {
            tiempoMaximo = 0.001;
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int numEscalas = 5;
        for (int i = 0; i <= numEscalas; i++) {
            double valor = tiempoMaximo * i / numEscalas;
            int y = margenSuperior + altoGrafico - (i * altoGrafico / numEscalas);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(margenIzquierdo, y, margenIzquierdo + anchoGrafico, y);

            g2d.setColor(Color.BLACK);
            String etiqueta = String.format("%.4f", valor);
            g2d.drawString(etiqueta, margenIzquierdo - 50, y + 5);
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.setColor(Color.BLACK);
        g2d.drawString("TIEMPO (MS)", 10, margenSuperior + altoGrafico / 2);

        Color[] colores = {
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(155, 89, 182),
            new Color(241, 196, 15),
            new Color(231, 76, 60)
        };

        int anchoBarra = anchoGrafico / (resultados.length * 2);
        int espacioEntreBarra = anchoBarra / 2;

        for (int i = 0; i < resultados.length; i++) {
            ComparadorBusquedas.ResultadoComparacion resultado = resultados[i];

            int alturaBarra = (int) ((resultado.getTiempoMs() / tiempoMaximo) * altoGrafico);
            int x = margenIzquierdo + (i * (anchoBarra + espacioEntreBarra)) + espacioEntreBarra;
            int y = margenSuperior + altoGrafico - alturaBarra;

            g2d.setColor(colores[i % colores.length]);
            g2d.fillRect(x, y, anchoBarra, alturaBarra);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, anchoBarra, alturaBarra);

            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String valorTexto = String.format("%.4f", resultado.getTiempoMs());
            int anchoTexto = g2d.getFontMetrics().stringWidth(valorTexto);
            g2d.drawString(valorTexto, x + (anchoBarra - anchoTexto) / 2, y - 5);

            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            String metodo = resultado.getMetodo();
            if (metodo.length() > 15) {
                metodo = metodo.substring(0, 12) + "...";
            }

            g2d.translate(x + anchoBarra / 2, margenSuperior + altoGrafico + 10);
            g2d.rotate(-Math.PI / 4);
            g2d.drawString(metodo, 0, 0);
            g2d.rotate(Math.PI / 4);
            g2d.translate(-(x + anchoBarra / 2), -(margenSuperior + altoGrafico + 10));
        }

        int leyendaX = margenIzquierdo;
        int leyendaY = margenSuperior + altoGrafico + 70;
        int tamañoCuadro = 15;
        int espacioLeyenda = 120;

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i < resultados.length; i++) {
            int x = leyendaX + (i * espacioLeyenda);

            g2d.setColor(colores[i % colores.length]);
            g2d.fillRect(x, leyendaY, tamañoCuadro, tamañoCuadro);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, leyendaY, tamañoCuadro, tamañoCuadro);

            String metodo = resultados[i].getMetodo();
            if (metodo.length() > 10) {
                metodo = metodo.substring(0, 8) + ".";
            }
            g2d.drawString(metodo, x + tamañoCuadro + 5, leyendaY + 12);
        }
    }
}
