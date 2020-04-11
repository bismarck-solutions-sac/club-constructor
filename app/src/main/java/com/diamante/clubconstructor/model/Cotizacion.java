package com.diamante.clubconstructor.model;

import java.io.Serializable;
import java.util.List;

public class Cotizacion implements Serializable {
    public String companiasocio;
    public String numerodocumento;
    public int clientenumero;
    public String clienteruc;
    public String clientenombre;
    public String clientedireccion;
    public int clientedireccionsecuencia;
    public String fechadocumento;
    public String monedadocumento;
    public double montobruto;
    public double montoafecto;
    public double montonoafecto;
    public double montodescuentos;
    public double montoimpuestoventas;
    public double montototal;
    public double montoflete;
    public String comentarios;
    public String almacencodigo;
    public String sucursal;
    public String sucursal_name;
    public String recojoflag;
    public int preparadopor;
    public String lugarentrega;
    public String contacto;
    public String estado;
    public int items;
    public List<CotizacionDetalle> detalle;
    public User user;
    public UserEmpresa userEmpresa;
    public Direccion userDireccion;
    public List<Direccion> direccionList;

    public String getCompaniasocio() {
        return companiasocio;
    }

    public void setCompaniasocio(String companiasocio) {
        this.companiasocio = companiasocio;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public int getClientenumero() {
        return clientenumero;
    }

    public void setClientenumero(int clientenumero) {
        this.clientenumero = clientenumero;
    }

    public String getClienteruc() {
        return clienteruc;
    }

    public void setClienteruc(String clienteruc) {
        this.clienteruc = clienteruc;
    }

    public String getClientenombre() {
        return clientenombre;
    }

    public void setClientenombre(String clientenombre) {
        this.clientenombre = clientenombre;
    }

    public String getClientedireccion() {
        return clientedireccion;
    }

    public void setClientedireccion(String clientedireccion) {
        this.clientedireccion = clientedireccion;
    }

    public String getMonedadocumento() {
        return monedadocumento;
    }

    public void setMonedadocumento(String monedadocumento) {
        this.monedadocumento = monedadocumento;
    }

    public double getMontobruto() {
        return montobruto;
    }

    public void setMontobruto(double montobruto) {
        this.montobruto = montobruto;
    }

    public double getMontoafecto() {
        return montoafecto;
    }

    public void setMontoafecto(double montoafecto) {
        this.montoafecto = montoafecto;
    }

    public double getMontonoafecto() {
        return montonoafecto;
    }

    public void setMontonoafecto(double montonoafecto) {
        this.montonoafecto = montonoafecto;
    }

    public double getMontodescuentos() {
        return montodescuentos;
    }

    public void setMontodescuentos(double montodescuentos) {
        this.montodescuentos = montodescuentos;
    }

    public double getMontoimpuestoventas() {
        return montoimpuestoventas;
    }

    public void setMontoimpuestoventas(double montoimpuestoventas) {
        this.montoimpuestoventas = montoimpuestoventas;
    }

    public double getMontototal() {
        return montototal;
    }

    public void setMontototal(double montototal) {
        this.montototal = montototal;
    }

    public double getMontoflete() {
        return montoflete;
    }

    public void setMontoflete(double montoflete) {
        this.montoflete = montoflete;
    }

    public String getAlmacencodigo() {
        return almacencodigo;
    }

    public void setAlmacencodigo(String almacencodigo) {
        this.almacencodigo = almacencodigo;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getRecojoflag() {
        return recojoflag;
    }

    public void setRecojoflag(String recojoflag) {
        this.recojoflag = recojoflag;
    }

    public int getPreparadopor() {
        return preparadopor;
    }

    public void setPreparadopor(int preparadopor) {
        this.preparadopor = preparadopor;
    }

    public String getLugarentrega() {
        return lugarentrega;
    }

    public void setLugarentrega(String lugarentrega) {
        this.lugarentrega = lugarentrega;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public List<CotizacionDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<CotizacionDetalle> detalle) {
        this.detalle = detalle;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(String fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public String getSucursal_name() {
        return sucursal_name;
    }

    public void setSucursal_name(String sucursal_name) {
        this.sucursal_name = sucursal_name;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserEmpresa getUserEmpresa() {
        return userEmpresa;
    }

    public void setUserEmpresa(UserEmpresa userEmpresa) {
        this.userEmpresa = userEmpresa;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Direccion getUserDireccion() {
        return userDireccion;
    }

    public void setUserDireccion(Direccion userDireccion) {
        this.userDireccion = userDireccion;
    }

    public List<Direccion> getDireccionList() {
        return direccionList;
    }

    public void setDireccionList(List<Direccion> direccionList) {
        this.direccionList = direccionList;
    }

    public int getClientedireccionsecuencia() {
        return clientedireccionsecuencia;
    }

    public void setClientedireccionsecuencia(int clientedireccionsecuencia) {
        this.clientedireccionsecuencia = clientedireccionsecuencia;
    }
}
