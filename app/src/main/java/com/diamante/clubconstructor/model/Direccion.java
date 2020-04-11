package com.diamante.clubconstructor.model;

import java.io.Serializable;

/**
 * Created by ArroyoJ on 13/07/2017.
 */

public class Direccion implements Serializable {
    int     persona;
    int     secuencia;
    String  direccion;
    String  pais;
    String  departamento;
    String  departamento_name;
    String  provincia;
    String  provincia_name;
    String  codigopostal;
    String  codigopostal_name;
    String  rutadespacho;
    String  telefono;
    String  fax;
    String  ubigeo;
    String  accion;
    String  referencia;
    boolean selected;
    String  ultimousuario;

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public String getRutadespacho() {
        return rutadespacho;
    }

    public void setRutadespacho(String rutadespacho) {
        this.rutadespacho = rutadespacho;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getUltimousuario() {
        return ultimousuario;
    }

    public void setUltimousuario(String ultimousuario) {
        this.ultimousuario = ultimousuario;
    }

    public String getDepartamento_name() {
        return departamento_name;
    }

    public void setDepartamento_name(String departamento_name) {
        this.departamento_name = departamento_name;
    }

    public String getProvincia_name() {
        return provincia_name;
    }

    public void setProvincia_name(String provincia_name) {
        this.provincia_name = provincia_name;
    }

    public String getCodigopostal_name() {
        return codigopostal_name;
    }

    public void setCodigopostal_name(String codigopostal_name) {
        this.codigopostal_name = codigopostal_name;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
