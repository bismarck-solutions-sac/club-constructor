package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class UserEmpresa implements Serializable {

    public int id;
    public String ruc;
    public String razonsocial;
    public String direccion;
    public int codigopersona;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCodigopersona() {
        return codigopersona;
    }

    public void setCodigopersona(int codigopersona) {
        this.codigopersona = codigopersona;
    }
}
