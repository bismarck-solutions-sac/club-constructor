package com.diamante.clubconstructor.api.sunat;

import java.io.Serializable;

public class response implements Serializable {

    public String ruc;
    public String razon_social;
    public String ciiu;
    public String fecha_actividad;
    public String contribuyente_condicion;
    public String contribuyente_tipo;
    public String contribuyente_estado;
    public String nombre_comercial;
    public String fecha_inscripcion;
    public String domicilio_fiscal;
    public String sistema_emision;
    public String sistema_contabilidad;
    public String actividad_exterior;
    public String emision_electronica;
    public String fecha_inscripcion_ple;
    public String fecha_baja;

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getCiiu() {
        return ciiu;
    }

    public void setCiiu(String ciiu) {
        this.ciiu = ciiu;
    }

    public String getFecha_actividad() {
        return fecha_actividad;
    }

    public void setFecha_actividad(String fecha_actividad) {
        this.fecha_actividad = fecha_actividad;
    }

    public String getContribuyente_condicion() {
        return contribuyente_condicion;
    }

    public void setContribuyente_condicion(String contribuyente_condicion) {
        this.contribuyente_condicion = contribuyente_condicion;
    }

    public String getContribuyente_tipo() {
        return contribuyente_tipo;
    }

    public void setContribuyente_tipo(String contribuyente_tipo) {
        this.contribuyente_tipo = contribuyente_tipo;
    }

    public String getContribuyente_estado() {
        return contribuyente_estado;
    }

    public void setContribuyente_estado(String contribuyente_estado) {
        this.contribuyente_estado = contribuyente_estado;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(String fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public String getDomicilio_fiscal() {
        return domicilio_fiscal;
    }

    public void setDomicilio_fiscal(String domicilio_fiscal) {
        this.domicilio_fiscal = domicilio_fiscal;
    }

    public String getSistema_emision() {
        return sistema_emision;
    }

    public void setSistema_emision(String sistema_emision) {
        this.sistema_emision = sistema_emision;
    }

    public String getSistema_contabilidad() {
        return sistema_contabilidad;
    }

    public void setSistema_contabilidad(String sistema_contabilidad) {
        this.sistema_contabilidad = sistema_contabilidad;
    }

    public String getActividad_exterior() {
        return actividad_exterior;
    }

    public void setActividad_exterior(String actividad_exterior) {
        this.actividad_exterior = actividad_exterior;
    }

    public String getEmision_electronica() {
        return emision_electronica;
    }

    public void setEmision_electronica(String emision_electronica) {
        this.emision_electronica = emision_electronica;
    }

    public String getFecha_inscripcion_ple() {
        return fecha_inscripcion_ple;
    }

    public void setFecha_inscripcion_ple(String fecha_inscripcion_ple) {
        this.fecha_inscripcion_ple = fecha_inscripcion_ple;
    }

    public String getFecha_baja() {
        return fecha_baja;
    }

    public void setFecha_baja(String fecha_baja) {
        this.fecha_baja = fecha_baja;
    }
}
