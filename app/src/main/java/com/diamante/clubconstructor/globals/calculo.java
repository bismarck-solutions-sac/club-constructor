package com.diamante.clubconstructor.globals;

import com.diamante.clubconstructor.model.GeneralSpinner;

public class calculo {

    private static calculo instance;

    public static synchronized calculo getInstance(){
        if(instance==null){
            instance=new calculo();
        }
        return instance;
    }

    public GeneralSpinner estructura;


    public GeneralSpinner getEstructura() {
        return estructura;
    }

    public void setEstructura(GeneralSpinner estructura) {
        this.estructura = estructura;
    }
}
