/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2.edd.Backend.Objetos;

/**
 *
 * @author alesso
 */
public class Libro {

    private String titulo;
    private String autor;
    private String isbn;
    private int anioPublicacion;
    private String genero;
    private EstadoLibro estado;
    private String bibliotecaActual;
    private String bibliotecaDestino;
    private String prioridad;

    public enum EstadoLibro {
        DISPONIBLE("Disponible"),
        PRESTADO("Prestado"),
        EN_TRANSITO("En Tránsito"),
        AGOTADO("Agotado");

        private String descripcion;

        EstadoLibro(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public Libro(String titulo, String autor, String isbn, int anioPublicacion,
            String genero, EstadoLibro estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
        this.estado = estado;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
    }

    public String getBibliotecaActual() {
        return bibliotecaActual;
    }

    public void setBibliotecaActual(String bibliotecaActual) {
        this.bibliotecaActual = bibliotecaActual;
    }

    public String getBibliotecaDestino() {
        return bibliotecaDestino;
    }

    public void setBibliotecaDestino(String bibliotecaDestino) {
        this.bibliotecaDestino = bibliotecaDestino;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return "Libro{"
                + "TITULO='" + titulo + '\''
                + ", AUTOR='" + autor + '\''
                + ", ISBN='" + isbn + '\''
                + ", ANIO=" + anioPublicacion
                + ", GENERO='" + genero + '\''
                + ", ESTADO=" + estado.getDescripcion()
                + '}';
    }

    public int compararPorTitulo(Libro otro) {
        return this.titulo.compareToIgnoreCase(otro.titulo);
    }

    public int compararPorIsbn(Libro otro) {
        return this.isbn.compareTo(otro.isbn);
    }

    public int compararPorAnio(Libro otro) {
        return Integer.compare(this.anioPublicacion, otro.anioPublicacion);
    }
}
