package com.diamante.clubconstructor.response;

import com.diamante.clubconstructor.model.GeneralSpinner;

import java.io.Serializable;
import java.util.List;

public class MaestrosRequest implements Serializable {

    private String tabla;
    private String parametro1;
    private String parametro2;
    private String parametro3;
    private String parametro4;
    private String parametro5;

    private List<GeneralSpinner> spinner_tipocliente;
    private List<GeneralSpinner> estructura_tipo;

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getParametro1() {
        return parametro1;
    }

    public void setParametro1(String parametro1) {
        this.parametro1 = parametro1;
    }

    public String getParametro2() {
        return parametro2;
    }

    public void setParametro2(String parametro2) {
        this.parametro2 = parametro2;
    }

    public String getParametro3() {
        return parametro3;
    }

    public void setParametro3(String parametro3) {
        this.parametro3 = parametro3;
    }

    public String getParametro4() {
        return parametro4;
    }

    public void setParametro4(String parametro4) {
        this.parametro4 = parametro4;
    }

    public String getParametro5() {
        return parametro5;
    }

    public void setParametro5(String parametro5) {
        this.parametro5 = parametro5;
    }

    public List<GeneralSpinner> getSpinner_tipocliente() {
        return spinner_tipocliente;
    }

    public void setSpinner_tipocliente(List<GeneralSpinner> spinner_tipocliente) {
        this.spinner_tipocliente = spinner_tipocliente;
    }

    public List<GeneralSpinner> getEstructura_tipo() {
        return estructura_tipo;
    }

    public void setEstructura_tipo(List<GeneralSpinner> estructura_tipo) {
        this.estructura_tipo = estructura_tipo;
    }
}
