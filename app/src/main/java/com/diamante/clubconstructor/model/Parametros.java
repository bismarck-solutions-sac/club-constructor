package com.diamante.clubconstructor.model;

import java.io.Serializable;

/**
 * Created by ArroyoJ on 03/10/2018.
 */

public class Parametros implements Serializable {
    String AplicacionCodigo;
    String Compania;
    String ParametroClave;
    String Descripcion;
    String Explicacion;
    String TipoDato;
    String Texto;
    double Valor =0;
    public Parametros(){
        super();
    }

    public String getAplicacionCodigo() {
        return AplicacionCodigo;
    }

    public void setAplicacionCodigo(String aplicacionCodigo) {
        AplicacionCodigo = aplicacionCodigo;
    }

    public String getCompania() {
        return Compania;
    }

    public void setCompania(String compania) {
        Compania = compania;
    }

    public String getParametroClave() {
        return ParametroClave;
    }

    public void setParametroClave(String parametroClave) {
        ParametroClave = parametroClave;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getExplicacion() {
        return Explicacion;
    }

    public void setExplicacion(String explicacion) {
        Explicacion = explicacion;
    }

    public String getTipoDato() {
        return TipoDato;
    }

    public void setTipoDato(String tipoDato) {
        TipoDato = tipoDato;
    }

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }
}
