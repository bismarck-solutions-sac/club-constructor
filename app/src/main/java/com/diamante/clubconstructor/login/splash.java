package com.diamante.clubconstructor.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.diamante.clubconstructor.BuildConfig;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Parametros;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.MaestrosRequest;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class splash extends AppCompatActivity{

    private LazyLoader lazyLoader  =  null;
    private functions function = functions.getInstance();
    private globals global = globals.getInstance();
    private SharedPreferences sharedPreferences;
    private Context context;
    static final Integer PHONESTATS = 0x1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

        try {
            context = this;
            lazyLoader = findViewById(R.id.fullscreen_loader);
            sharedPreferences = getApplicationContext().getSharedPreferences(constantes.PREFERENCE_NAME, Context.MODE_PRIVATE);

            LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                    ContextCompat.getColor(this, R.color.loader_selected),
                    ContextCompat.getColor(this, R.color.loader_selected));
            loader.setAnimDuration(500);
            loader.setFirstDelayDuration(100);
            loader.setSecondDelayDuration(200);
            lazyLoader.setInterpolator(new LinearInterpolator());

            if (!function.compruebaConexion(context)) {
                Intent intent = new Intent(context, conexion.class);
                startActivity(intent);
                finish();
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        consultarPermiso(android.Manifest.permission.READ_PHONE_STATE, PHONESTATS);
                    }
                }, 500);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LoginUser(final String ls_email, final String ls_password){
        RequestParameter parameter = new RequestParameter();
        User user       = new User();
        user.email      = ls_email;
        user.password   = ls_password;
        parameter.user  = user;
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.getLoginUser (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            global.setUser(request.user);
                            Intent i = new Intent(context, main.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("user", request.user);
                            startActivity(i);
                            finish();
                        }else if(request.getCodigoError()==2){
                            Intent i = new Intent(context, login.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: LoginUser").show();
        }
    }

    private void loadMaestros(){
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest  = new MaestrosRequest();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.getMaestrosList (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            global.setSpinner_tipocliente(request.getSpinner_tipocliente());
                            global.setEstructura_tipo(request.getEstructura_tipo());
                            if (sharedPreferences.getString(constantes.SETTING_USER,"").length()!=0) {
                                LoginUser(sharedPreferences.getString(constantes.SETTING_USER, ""), sharedPreferences.getString(constantes.SETTING_PASS, ""));
                            }else{
                                global.setImei(obtenerIMEI());
                                Intent i = new Intent(context, login.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: loadMaestros").show();
        }
    }

    private void consultarPermiso(String permission, Integer requestCode) {
        try {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
                }
            } else {
                f_validate_version();
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: consultarPermiso").show();
        }
    }

    private String obtenerIMEI() {
        try {
            final TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
                return telephonyManager.getImei();
            } else {
                return telephonyManager.getDeviceId();
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: obtenerIMEI").show();
        }
        return null;
    }

    public AlertDialog createDialogError(String message, String title) {
        final Button btn_ok;
        final TextView textTitle, textMessage;
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_error, null);

        btn_ok      = v.findViewById(R.id.buttonAction);
        textTitle   = v.findViewById(R.id.textTitle);
        textMessage = v.findViewById(R.id.textMessage);

        textTitle.setText(title);
        textMessage.setText(message);
        btn_ok.setText(getResources().getString(R.string.okay));
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            switch (requestCode) {
                case 1 /* El codigo que puse a mi request */: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
                                return;
                            }
                        }
                        f_validate_version();
                    } else {
                        f_validate_version();
                    }
                    return;
                }
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onRequestPermissionsResult").show();
        }
    }

    private void f_validate_version() {
        MethodWS methodWS = null;
        RequestParameter parameter = new RequestParameter();
        MaestrosRequest masters = null;
        try {
            masters = new MaestrosRequest();
            masters.setTabla("PARAMETROS");
            masters.setParametro1("VM");
            parameter.setMaestrosRequest(masters);
            methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            try {
                Call<List<Parametros>> listCall = methodWS.getParametros(parameter);
                listCall.enqueue(new Callback<List<Parametros>>() {
                    @Override
                    public void onResponse(Call<List<Parametros>> call, Response<List<Parametros>> response) {
                        if (response.isSuccessful()) {
                            List<Parametros> request = response.body();
                            global.setParametros_list(request);
                            double version = function.f_read_parametros_number("999999", "VM", "VERSIONAPP");
                            if (BuildConfig.VERSION_CODE >= version) {
                                loadMaestros();
                            } else {
                                lazyLoader.setVisibility(View.GONE);
                                new AlertDialog.Builder(context)
                                        .setIcon(android.R.drawable.ic_menu_info_details)
                                        .setTitle("Información")
                                        .setCancelable(false)
                                        .setMessage("Existe una nueva versión disponible, favor de actualizar...")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String link_update = function.f_read_parametros_explicacion("999999", "VM", "URLLINKAPP");
                                                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(link_update));
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Parametros>> call, Throwable t) {
                        createDialogError(t.getMessage(), "Error: f_validate_version").show();
                    }
                });
            } catch (Exception e) {
                createDialogError(e.getMessage(), "Error: f_validate_version").show();
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: f_validate_version").show();
        }
    }
}
