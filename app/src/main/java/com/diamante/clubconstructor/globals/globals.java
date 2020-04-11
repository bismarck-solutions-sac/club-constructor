package com.diamante.clubconstructor.globals;

import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Parametros;
import com.diamante.clubconstructor.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class globals implements Serializable {

    private static globals instance;
    public String   Imei;
    public User     user;
    public List<GeneralSpinner> spinner_tipocliente= new ArrayList<>();
    public List<GeneralSpinner> estructura_tipo = new ArrayList<>();
    public List<Parametros> parametros_list = new ArrayList<>();


    public static synchronized globals getInstance(){
        if(instance==null){
            instance=new globals();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public List<Parametros> getParametros_list() {
        return parametros_list;
    }

    public void setParametros_list(List<Parametros> parametros_list) {
        this.parametros_list = parametros_list;
    }
}
