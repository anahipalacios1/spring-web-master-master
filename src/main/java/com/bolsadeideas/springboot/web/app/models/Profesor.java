package com.bolsadeideas.springboot.web.app.models;
import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

public class Profesor implements Serializable{

    private static final long serialVersionUID = 1L;
    @NotNull
    private int idProfesor;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String apellido;

    public Profesor() {
    }

    public Profesor(int idProfesor, String nombre, String apellido) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "idProfesor=" + idProfesor +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                '}'+"\n";
    }
}
