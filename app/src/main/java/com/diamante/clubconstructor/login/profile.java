package com.diamante.clubconstructor.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.MenuListAdapter;
import com.diamante.clubconstructor.dialogos.dialog_single;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.MaestrosRequest;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity implements dialog_single.dialog_single_listener{
    private functions function = functions.getInstance();
    private globals global = globals.getInstance();

    private AlertDialog dialogProgress;
    private Context context;
    private EditText edt_nombres, edt_apellidos, edt_email;
    private EditText edt_dni, edt_telefono;
    private TextView text_fecha, text_tipo, text_profesion;
    private Button btn_ok, btn_clave;
    private User user;
    private String SELECTION;
    private DialogFragment dialogSimple;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        inicializa();
    }

    private void inicializa() {
        try {
            Bundle bundle       = this.getIntent().getExtras();
            if (bundle != null) {
                user = (User) bundle.getSerializable("user");
            }
            context             = this;
            dialogProgress      = createDialogProgress();
            edt_nombres         = findViewById(R.id.edt_nombres);
            edt_apellidos       = findViewById(R.id.edt_apellidos);
            edt_email           = findViewById(R.id.edt_email);
            edt_dni             = findViewById(R.id.edt_documento);
            text_tipo           = findViewById(R.id.edt_tipocliente);
            text_profesion      = findViewById(R.id.edt_profesion);
            text_fecha          = findViewById(R.id.edt_fechanacimiento);
            edt_telefono        = findViewById(R.id.edt_telefono);
            btn_ok              = findViewById(R.id.btn_ok);
            btn_clave           = findViewById(R.id.btn_password);

            edt_dni.setEnabled(false);
            text_tipo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTION = "TIPO";
                    int LINEA =-1;
                    if (user.getTipocliente()!=null){
                        LINEA = function.read_tipocliente_position(user.getTipocliente());
                    }
                    dialogSimple = new dialog_single("Seleccione un valor", global.getSpinner_tipocliente(), LINEA);
                    dialogSimple.show(getSupportFragmentManager(), "NoticeDialogFragment");
                }
            });

            text_profesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int POSITION =-1;
                    if (user.getTipocliente()!=null){
                        load_profesion();
                    }
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()){
                        if (prepare_update()){
                            Update();
                        }
                    }
                }
            });

            btn_clave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogPassword().show();
                }
            });

            loadData();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    private void Update(){
        RequestParameter parameter = new RequestParameter();
        parameter.user= user;
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.setuserUpdate (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            dialogProgress.dismiss();
                            global.setUser(request.getUser());
                            Intent i = new Intent(getApplicationContext(), main.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }else if(request.getCodigoError()==2){
                            dialogProgress.dismiss();
                            createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
                        }
                    }else{
                        dialogProgress.dismiss();
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialogProgress.dismiss();
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            dialogProgress.dismiss();
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean validate(){
        try{
            if (edt_nombres.getText().toString() == null || edt_nombres.getText().toString().isEmpty()){
                edt_nombres.setError("* Campo Obligatorio");
                edt_nombres.requestFocus();
                return false;
            }

            if (edt_apellidos.getText().toString() == null || edt_apellidos.getText().toString().isEmpty()){
                edt_apellidos.setError("* Campo Obligatorio");
                edt_apellidos.requestFocus();
                return false;
            }

            if (!validarEmail(edt_email.getText().toString())){
                edt_email.setError("Ingrese un email válido");
                edt_email.requestFocus();
                return false;
            }
            if (edt_dni.getText().toString() == null || edt_dni.getText().toString().isEmpty()){
                edt_dni.setError("* Campo Obligatorio");
                edt_dni.requestFocus();
                return false;
            }
            if (edt_telefono.getText().toString() == null || edt_telefono.getText().toString().isEmpty()){
                edt_telefono.setError("* Campo Obligatorio");
                edt_telefono.requestFocus();
                return false;
            }
            if (text_fecha.getText().toString() == null || text_fecha.getText().toString().isEmpty()){
                text_fecha.setError("* Campo Obligatorio");
                text_fecha.requestFocus();
                return false;
            }
            if (text_tipo.getText().toString() == null || text_tipo.getText().toString().isEmpty()){
                text_tipo.setError("* Campo Obligatorio");
                text_tipo.requestFocus();
                return false;
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: validate").show();
            return false;
        }

        return true;
    }

    private boolean prepare_update(){
        try {
            user.first_name     = edt_nombres.getText().toString();
            user.last_name      = edt_apellidos.getText().toString();
            user.email          = edt_email.getText().toString();
            user.dni            = edt_dni.getText().toString();
            user.telefono       = edt_telefono.getText().toString();
            user.fechanacimiento= text_fecha.getText().toString();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: prepare_update").show();
            return false;
        }
        return true;
    }

    private void loadData() {
        RequestParameter parameter = new RequestParameter();
        parameter.setUser(user);
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.getuserProfile(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            dialogProgress.dismiss();
                            user = request.getUser();
                            edt_nombres.setText(user.getFirst_name());
                            edt_apellidos.setText(user.getLast_name());
                            edt_dni.setText(user.getDni());
                            edt_email.setText(user.getEmail());
                            text_fecha.setText(user.getFechanacimiento().substring(0,10));
                            edt_telefono.setText(user.getTelefono());
                            text_tipo.setText(user.getTipocliente_name());
                            text_profesion.setText(user.getProfesion_name());
                        } else if (request.getCodigoError() == 2) {
                            dialogProgress.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                        }
                    } else {
                        dialogProgress.dismiss();
                        createDialogError(response.message(), getString(R.string.app_name)).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialogProgress.dismiss();
                    createDialogError(t.getMessage(), "onFailure").show();
                }
            });
        } catch (Exception e) {
            dialogProgress.dismiss();
            createDialogError(e.getMessage(), "onFailure").show();
        }
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

    public AlertDialog createDialogProgress() {
        AlertDialog alertDialog =null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        try {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_circularloader, null);
            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }catch (Exception e){
            e.printStackTrace();
        }
        return alertDialog;
    }

    public AlertDialog createDialogInformation(String Title, String Message) {

        try {
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_success, null);
            Button btn_ok = v.findViewById(R.id.buttonAction);
            TextView textTitle = v.findViewById(R.id.textTitle);
            TextView textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(Title);
            textMessage.setText(Message);
            btn_ok.setText("Aceptar");
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, GeneralSpinner spinner) {
        switch (SELECTION){
            case "TIPO":
                if (spinner!=null){
                    user.setTipocliente(spinner.getId());
                    text_tipo.setText(spinner.getDescripcion());
                }
                break;
            case "PROFESION":
                if (spinner!=null){
                    user.setProfesion(spinner.getId());
                    text_profesion.setText(spinner.getDescripcion());
                    edt_dni.requestFocus();
                }
                break;
        }
        dialog.dismiss();
    }

    private void load_profesion() {
        try {
            SELECTION = "PROFESION";
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            MaestrosRequest dato= new MaestrosRequest();
            dato.setTabla("CC_PROFESION");
            RequestParameter parameter = new RequestParameter();
            parameter.maestrosRequest = dato;
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int LINEA=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            if (user.profesion !=null){
                                LINEA = function.read_generalspinner_position(request.spinner_general, user.profesion);
                            }
                            dialogSimple = new dialog_single("Seleccione un valor", request.spinner_general, LINEA);
                            dialogSimple.show(getSupportFragmentManager(), "NoticeDialogFragment");
                        }else if(request.getCodigoError()==2){
                            createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
                        }
                    }else{
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: load_profesion").show();
        }
    }

    public AlertDialog createDialogPassword() {
        try {
            final Button btn_ok;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_password, null);
            EditText edt_pass   = v.findViewById(R.id.edt_contrasena);
            TextView edt_pass2  = v.findViewById(R.id.edt_contrasena2);
            btn_ok              = v.findViewById(R.id.btn_ok);

            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edt_pass.getText().toString() == null || edt_pass.getText().toString().isEmpty()){
                        edt_pass.setError("* Campo Obligatorio");
                        edt_pass.requestFocus();
                        return ;
                    }

                    if (edt_pass2.getText().toString() == null || edt_pass2.getText().toString().isEmpty()){
                        edt_pass2.setError("* Campo Obligatorio");
                        edt_pass2.requestFocus();
                        return;
                    }
                    if (!edt_pass.getText().toString().equals(edt_pass2.getText().toString())){
                        createDialogError("Las contraseñas no coinciden, favor de verificar...", "Validación").show();
                        edt_pass.requestFocus();
                        return;
                    }
                    user.setPassword(edt_pass.getText().toString());

                    updatePassword(alertDialog);
                }
            });
            return alertDialog;
        }catch (Exception e){

        }
        return null;
    }

    private void updatePassword(AlertDialog alertDialog) {
        RequestParameter parameter = new RequestParameter();
        parameter.setUser(user);
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.setuserPassword(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            dialogProgress.dismiss();
                            alertDialog.dismiss();

                            SharedPreferences sharedPreferences;
                            sharedPreferences   = getApplicationContext().getSharedPreferences(constantes.PREFERENCE_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(constantes.SETTING_USER, user.email);
                            editor.putString(constantes.SETTING_PASS, user.password);
                            editor.commit();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    createDialogInformation(getString(R.string.app_name), "Clave actualizada correctamente").show();
                                }
                            }, 1000);
                        } else if (request.getCodigoError() == 2) {
                            dialogProgress.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                        }
                    } else {
                        dialogProgress.dismiss();
                        createDialogError(response.message(), "onResponse").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialogProgress.dismiss();
                    createDialogError(t.getMessage(), "onFailure").show();
                }
            });
        } catch (Exception e) {
            dialogProgress.dismiss();
            createDialogError(e.getMessage(), "onFailure").show();
        }
    }
}
