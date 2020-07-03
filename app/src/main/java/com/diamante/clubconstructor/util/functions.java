package com.diamante.clubconstructor.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Parametros;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.util.List;

public class functions {

    private static functions instance;
    private globals global = globals.getInstance();

    public static synchronized functions getInstance(){
        if(instance==null){
            instance=new functions();
        }
        return instance;
    }

    public String f_read_parametros_texto(String compania, String aplicacion, String clave){
        String ls_return="";
        List<Parametros> parametros = global.getParametros_list();
        if (parametros==null){
            return ls_return;
        }
        int row= parametros.size();
        for (int i=0; i< row; i++){
            if (parametros.get(i).getCompania().equals(compania)){
                if (parametros.get(i).getAplicacionCodigo().equals(aplicacion)){
                    if (parametros.get(i).getParametroClave().equals(clave)){
                        ls_return = parametros.get(i).getTexto();
                        break;
                    }
                }
            }
        }
        return ls_return;
    }

    public String f_read_parametros_explicacion(String compania, String aplicacion, String clave){
        String ls_return="";
        List<Parametros> parametros = global.getParametros_list();
        if (parametros==null){
            return ls_return;
        }
        int row= parametros.size();
        for (int i=0; i< row; i++){
            if (parametros.get(i).getCompania().equals(compania)){
                if (parametros.get(i).getAplicacionCodigo().equals(aplicacion)){
                    if (parametros.get(i).getParametroClave().equals(clave)){
                        ls_return = parametros.get(i).getExplicacion();
                        break;
                    }
                }
            }
        }
        return ls_return;
    }

    public double f_read_parametros_number(String compania, String aplicacion, String clave){
        double dbl_return=0;
        List<Parametros> parametros = global.getParametros_list();
        if (parametros==null){
            return dbl_return;
        }
        int row= parametros.size();
        for (int i=0; i< row; i++){
            if (parametros.get(i).getCompania()==null){
                continue;
            }
            if (parametros.get(i).getCompania().equals(compania)){
                if (parametros.get(i).getAplicacionCodigo().equals(aplicacion)){
                    if (parametros.get(i).getParametroClave().equals(clave)){
                        dbl_return = parametros.get(i).getValor();
                        break;
                    }
                }
            }
        }
        return dbl_return;
    }

    public int read_generalspinner_position(List<GeneralSpinner> lista, String id){
        int li_return =-1;
        if (lista==null){
            return li_return;
        }
        int row = lista.size();
        for (int i = 0; i < row; i++) {
            if (lista.get(i).getId().equals(id)) {
                li_return = i;
                break;
            }
        }
        return li_return;
    }

    public int read_tipocliente_position(String tipocliente){
        int li_return =0 ;
        int row = global.getSpinner_tipocliente().size();
        for (int i = 0; i < row; i++) {
            if (global.getSpinner_tipocliente().get(i).getId().equals(tipocliente)) {
                li_return = i;
                break;
            }
        }
        return li_return;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radio de la tierra en  kilómetros
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    public static boolean compruebaConexion(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public static void _ga (String name, Context context){
        try {
            FirebaseAnalytics _mga;
            _mga = FirebaseAnalytics.getInstance(context);
            _mga.setCurrentScreen((Activity) context, name, null /* class override */);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
