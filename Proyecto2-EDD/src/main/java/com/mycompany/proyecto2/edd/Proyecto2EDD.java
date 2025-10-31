/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyecto2.edd;

import com.mycompany.proyecto2.edd.Backend.Util.SistemaBiblioteca;
import com.mycompany.proyecto2.edd.Frontend.Inicio;


/**
 *
 * @author alesso
 */
public class Proyecto2EDD {

    public static void main(String[] args) {
        SistemaBiblioteca sitema = new SistemaBiblioteca();
        Inicio inicio = new Inicio(sitema);
        inicio.setVisible(true);
        
    }

    
}
