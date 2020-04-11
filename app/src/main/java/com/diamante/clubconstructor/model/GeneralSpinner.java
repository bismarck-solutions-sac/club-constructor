package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class GeneralSpinner implements Serializable {
    public String id;
    public String descripcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
