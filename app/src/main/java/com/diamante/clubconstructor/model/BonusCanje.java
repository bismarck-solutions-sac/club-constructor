package com.diamante.clubconstructor.model;

import java.io.Serializable;

public class BonusCanje implements Serializable {

    public String companiasocio;
    public int canje;
    public int cliente;
    public String fecha;
    public double puntajeacumulado;
    public double puntajeconsumido;
    public String referenciadocumento;
    public String ingresoegresoflag;
    public String referenciatipodocumento;
    public String estado;
    public String unidadreplicacion;
    public int cobranzanumero;
    public String origen;
    public String vendedor;

    public String getCompaniasocio() {
        return companiasocio;
    }

    public void setCompaniasocio(String companiasocio) {
        this.companiasocio = companiasocio;
    }

    public int getCanje() {
        return canje;
    }

    public void setCanje(int canje) {
        this.canje = canje;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPuntajeacumulado() {
        return puntajeacumulado;
    }

    public void setPuntajeacumulado(double puntajeacumulado) {
        this.puntajeacumulado = puntajeacumulado;
    }

    public double getPuntajeconsumido() {
        return puntajeconsumido;
    }

    public void setPuntajeconsumido(double puntajeconsumido) {
        this.puntajeconsumido = puntajeconsumido;
    }

    public String getReferenciadocumento() {
        return referenciadocumento;
    }

    public void setReferenciadocumento(String referenciadocumento) {
        this.referenciadocumento = referenciadocumento;
    }

    public String getIngresoegresoflag() {
        return ingresoegresoflag;
    }

    public void setIngresoegresoflag(String ingresoegresoflag) {
        this.ingresoegresoflag = ingresoegresoflag;
    }

    public String getReferenciatipodocumento() {
        return referenciatipodocumento;
    }

    public void setReferenciatipodocumento(String referenciatipodocumento) {
        this.referenciatipodocumento = referenciatipodocumento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUnidadreplicacion() {
        return unidadreplicacion;
    }

    public void setUnidadreplicacion(String unidadreplicacion) {
        this.unidadreplicacion = unidadreplicacion;
    }

    public int getCobranzanumero() {
        return cobranzanumero;
    }

    public void setCobranzanumero(int cobranzanumero) {
        this.cobranzanumero = cobranzanumero;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
}
