package com.bolsadeideas.springboot.web.app.models;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull
    private int idmateria;
    @NotEmpty
    private String descripcion;

    public Materia() {
    }

    public Materia(@NotNull int idmateria, String descripcion) {
        this.idmateria = idmateria;
        this.descripcion = descripcion;
    }

    public int getIdmateria() {
        return idmateria;
    }

    public void setIdmateria(int idmateria) {
        this.idmateria = idmateria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "idmateria=" + idmateria +
                ", descripcion='" + descripcion + '\'' +
                '}'+"\n";
    }
}
