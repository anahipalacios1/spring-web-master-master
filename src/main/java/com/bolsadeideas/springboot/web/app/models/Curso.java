package com.bolsadeideas.springboot.web.app.models;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull
    private int idcurso;
    @NotEmpty
    private String descripcion;

    public Curso() {
    }

    public Curso(int idcurso, String descripcion) {
        this.idcurso = idcurso;
        this.descripcion = descripcion;
    }

    public int getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(int idcurso) {
        this.idcurso = idcurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "idcurso=" + idcurso +
                ", descripcion='" + descripcion + '\'' +
                '}'+"\n";
    }

}
