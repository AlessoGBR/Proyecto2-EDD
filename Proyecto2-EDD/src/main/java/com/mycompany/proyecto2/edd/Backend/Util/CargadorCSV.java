/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Util;

import com.mycompany.proyecto2.edd.Backend.Objetos.Libro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author alesso
 */
public class CargadorCSV {

    private String mensaje;
    private int lineasExitosas;
    private int lineasErroneas;
    private StringBuilder logErrores;

    public CargadorCSV() {
        this.mensaje = "";
        this.lineasExitosas = 0;
        this.lineasErroneas = 0;
        this.logErrores = new StringBuilder();
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getLineasExitosas() {
        return lineasExitosas;
    }

    public int getLineasErroneas() {
        return lineasErroneas;
    }

    public String getLogErrores() {
        return logErrores.toString();
    }

    public boolean cargarCatalogoLibros(String rutaArchivo, SistemaBiblioteca sistema, String nombreBiblioteca) {
        if (!validarArchivo(rutaArchivo)) {
            return false;
        }

        lineasExitosas = 0;
        lineasErroneas = 0;
        logErrores = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            String linea;
            int numeroLinea = 0;

            linea = br.readLine();
            numeroLinea++;

            if (linea == null || !linea.toLowerCase().contains("isbn")) {
                mensaje = "ERROR: ENCABEZADO INVÁLIDO EN EL CATÁLOGO DE LIBROS.";
                return false;
            }

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                    if (datos.length < 9) {
                        logErrores.append("Línea ").append(numeroLinea).append(": FORMATO INCORRECTO - SE ESPERABAN 9 CAMPOS\n");
                        lineasErroneas++;
                        continue;
                    }

                    String titulo = limpiarCampo(datos[0]);
                    String isbn = limpiarCampo(datos[1]);
                    String genero = limpiarCampo(datos[2]);
                    int anio = Integer.parseInt(limpiarCampo(datos[3]));
                    String autor = limpiarCampo(datos[4]);
                    String estadoStr = limpiarCampo(datos[5]);
                    String idOrigen = limpiarCampo(datos[6]);
                    String idDestino = limpiarCampo(datos[7]);
                    String prioridad = limpiarCampo(datos[8]).toLowerCase();

                    Libro.EstadoLibro estado = switch (estadoStr.toUpperCase()) {
                        case "DISPONIBLE" ->
                            Libro.EstadoLibro.DISPONIBLE;
                        case "PRESTADO" ->
                            Libro.EstadoLibro.PRESTADO;
                        case "EN TRANSITO", "EN_TRANSITO" ->
                            Libro.EstadoLibro.EN_TRANSITO;
                        case "AGOTADO" ->
                            Libro.EstadoLibro.AGOTADO;
                        default ->
                            Libro.EstadoLibro.DISPONIBLE;
                    };

                    Libro libro = new Libro(titulo, autor, isbn, anio, genero, estado);
                    libro.setBibliotecaActual(idOrigen);
                    libro.setBibliotecaDestino(idDestino);
                    libro.setPrioridad(prioridad);

                    if (sistema.agregarLibro(libro, nombreBiblioteca)) {
                        lineasExitosas++;
                    } else {
                        logErrores.append("Línea ").append(numeroLinea)
                                .append(": ").append(sistema.getMensaje()).append("\n");
                        lineasErroneas++;
                    }

                } catch (NumberFormatException e) {
                    logErrores.append("Línea ").append(numeroLinea).append(": ERROR DE FORMATO NUMÉRICO (Año)\n");
                    lineasErroneas++;
                } catch (Exception e) {
                    logErrores.append("Línea ").append(numeroLinea).append(": ERROR GENERAL - ")
                            .append(e.getMessage()).append("\n");
                    lineasErroneas++;
                }
            }

            mensaje = "CARGA DE LIBROS COMPLETADA → " + lineasExitosas + " correctas, " + lineasErroneas + " con errores.";
            return true;

        } catch (IOException e) {
            mensaje = "ERROR AL LEER ARCHIVO DE LIBROS: " + e.getMessage();
            return false;
        }
    }

    public boolean cargarBibliotecas(String rutaArchivo, SistemaBiblioteca sistema) {
        if (!validarArchivo(rutaArchivo)) {
            return false;
        }

        lineasExitosas = 0;
        lineasErroneas = 0;
        logErrores = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;
            linea = br.readLine();
            numeroLinea++;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (datos.length < 6) {
                        logErrores.append("Línea ").append(numeroLinea).append(": FORMATO INCORRECTO (faltan columnas)\n");
                        lineasErroneas++;
                        continue;
                    }

                    String id = limpiarCampo(datos[0]);
                    String nombre = limpiarCampo(datos[1]);
                    String ubicacion = limpiarCampo(datos[2]);
                    int tIngreso = Integer.parseInt(limpiarCampo(datos[3]));
                    int tTraspaso = Integer.parseInt(limpiarCampo(datos[4]));
                    int intervalo = Integer.parseInt(limpiarCampo(datos[5]));

                    if (sistema.agregarBiblioteca(id, nombre, ubicacion, tIngreso, tTraspaso, intervalo)) {
                        lineasExitosas++;
                    } else {
                        logErrores.append("Línea ").append(numeroLinea)
                                .append(": ").append(sistema.getMensaje()).append("\n");
                        lineasErroneas++;
                    }

                } catch (NumberFormatException e) {
                    logErrores.append("Línea ").append(numeroLinea).append(": ERROR NUMÉRICO EN CAMPOS DE TIEMPO\n");
                    lineasErroneas++;
                } catch (Exception e) {
                    logErrores.append("Línea ").append(numeroLinea).append(": ERROR - ").append(e.getMessage()).append("\n");
                    lineasErroneas++;
                }
            }

            mensaje = "CARGA DE BIBLIOTECAS COMPLETADA → " + lineasExitosas + " correctas, " + lineasErroneas + " erróneas.";
            return true;

        } catch (IOException e) {
            mensaje = "ERROR AL LEER ARCHIVO DE BIBLIOTECAS: " + e.getMessage();
            return false;
        }
    }

    public boolean cargarConexiones(String rutaArchivo, SistemaBiblioteca sistema) {
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            mensaje = "ERROR: EL ARCHIVO NO EXISTE";
            return false;
        }

        lineasExitosas = 0;
        lineasErroneas = 0;
        logErrores = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;

            linea = br.readLine();
            numeroLinea++;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] datos = linea.split(",");

                    if (datos.length < 3) {
                        logErrores.append("LINEA ").append(numeroLinea)
                                .append(": FORMATO INCORRECTO - FALTAN CAMPOS\n");
                        lineasErroneas++;
                        continue;
                    }

                    String origen = datos[0].trim().replace("\"", "");
                    String destino = datos[1].trim().replace("\"", "");

                    double tiempoDouble = Double.parseDouble(datos[2].trim());
                    int tiempo = (int) Math.round(tiempoDouble);

                    double costoDouble = datos.length > 3
                            ? Double.parseDouble(datos[3].trim())
                            : tiempoDouble * 5;

                    int costo = (int) Math.round(costoDouble);

                    if (sistema.agregarConexion(origen, destino, tiempo, costo, false)) {
                        lineasExitosas++;
                    } else {
                        logErrores.append("LINEA ").append(numeroLinea)
                                .append(": ").append(sistema.getMensaje()).append("\n");
                        lineasErroneas++;
                    }

                } catch (NumberFormatException e) {
                    logErrores.append("LINEA ").append(numeroLinea)
                            .append(": ERROR EN FORMATO DE NUMERO\n");
                    lineasErroneas++;
                } catch (Exception e) {
                    logErrores.append("LINEA ").append(numeroLinea)
                            .append(": ERROR - ").append(e.getMessage()).append("\n");
                    lineasErroneas++;
                }
            }

            mensaje = "CARGA COMPLETADA: " + lineasExitosas + " EXITOSAS, "
                    + lineasErroneas + " ERRONEAS";
            return true;

        } catch (IOException e) {
            mensaje = "ERROR AL LEER EL ARCHIVO: " + e.getMessage();
            return false;
        }
    }

    private String limpiarCampo(String campo) {
        return campo.replace("\"", "").trim();
    }

    private boolean validarArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || !archivo.isFile()) {
            mensaje = "ERROR: EL ARCHIVO NO EXISTE O NO ES VÁLIDO: " + rutaArchivo;
            return false;
        }
        if (!rutaArchivo.endsWith(".csv")) {
            mensaje = "ERROR: EL ARCHIVO DEBE SER DE FORMATO CSV.";
            return false;
        }
        return true;
    }
}
