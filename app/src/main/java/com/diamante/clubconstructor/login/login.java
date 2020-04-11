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
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.calculadora_brick_type;
import com.diamante.clubconstructor.dialogos.dialog_custom;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.maps.locales;
import com.diamante.clubconstructor.model.Direccion;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.util.constantes;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity implements dialog_custom.dialog_success_listener {

    private Context context;
    private TextInputLayout til_username;
    private TextInputLayout til_password;
    private TextInputEditText edit_username;
    private TextInputEditText edit_password;
    private Button btn_ok;
    private DialogFragment dialog;
    private TextView textregistrarse, text_forgot;
    private TextView textabout;
    private globals global = globals.getInstance();
    private SharedPreferences sharedPreferences;
    private LinearLayout lny_ubicanos, lny_phone, lny_calc;
    private SignInButton signInButton;

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
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        try {
            inicializa();
        } catch (Exception e) {

        }
    }

    private void inicializa() {
        try {
            context             = this;
            til_username        = findViewById(R.id.til_usename);
            til_password        = findViewById(R.id.til_password);
            edit_username       = findViewById(R.id.edt_username);
            edit_password       = findViewById(R.id.edt_password);
            btn_ok              = findViewById(R.id.btn_ok);
            textregistrarse     = findViewById(R.id.tv_registrate);
            textabout           = findViewById(R.id.tv_conocemas);
            lny_ubicanos        = findViewById(R.id.lny_ubicanos);
            text_forgot         = findViewById(R.id.tv_recuperar);
            lny_phone           = findViewById(R.id.lny_phone);
            lny_calc            = findViewById(R.id.lny_calculadora);
            signInButton        = findViewById(R.id.btn_signin_google);
            signInButton.setSize(SignInButton.SIZE_ICON_ONLY);
            sharedPreferences   = getApplicationContext().getSharedPreferences(constantes.PREFERENCE_NAME, Context.MODE_PRIVATE);
            text_forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, forgot.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()) {
                        LoginUser(edit_username.getText().toString(), edit_password.getText().toString());
                    }
                }
            });
            textregistrarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, registrarse.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, registrarse.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("MODO", "GOOGLE");
                    startActivity(i);
                    finish();
                }
            });
            textabout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, about.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });

            lny_ubicanos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, locales.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });

            lny_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogContactame().show();
                }
            });

            lny_calc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User();
                    user.id =-1;
                    user.first_name = "Invitado";
                    user.full_name = "Invitado";
                    user.tipocliente = "00";
                    user.level = 1;
                    user.fechanacimiento = "04/02/2020";
                    user.email = "invitado@miempresa.com.pe";
                    user.password = "246";
                    user.path = "profile/profile.png";
                    user.token = "";
                    global.setUser(user);
                    Intent i = new Intent(context, calculadora_brick_type.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });

        }catch (Exception e){
            showNoticeDialog(e.getMessage(), "Error: inicializa", constantes.DIALOG_ERROR);
        }
    }

    private boolean validate(){
        try {
            if (!validarEmail(edit_username.getText().toString())){
                showNoticeDialog("Ingrese un correo válido", "Login...", constantes.DIALOG_ERROR);
                edit_username.requestFocus();
                return false;
            }if (edit_password.getText().toString() == null || edit_password.getText().toString().isEmpty()){
                showNoticeDialog("Ingrese su contraseña", "Login...", constantes.DIALOG_ERROR);
                edit_password.requestFocus();
                return false;
            }
        }catch (Exception e){
            showNoticeDialog(e.getMessage(), "Error: validate", constantes.DIALOG_ERROR);
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public void showNoticeDialog(String message, String title, int dialogType) {
        // Create an instance of the dialog fragment and show it
        dialog = new dialog_custom(message, title, dialogType);
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    private void LoginUser(final String ls_email, final String ls_password){
        RequestParameter parameter = new RequestParameter();
        User user = new User();
        user.email = ls_email;
        user.password = ls_password;

        parameter.user= user;
        showNoticeDialog("", "", constantes.DIALOG_LOADER);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.getLoginUser (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            dialog.dismiss();
                            global.setUser(request.user);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(constantes.SETTING_USER, user.email);
                            editor.putString(constantes.SETTING_PASS, user.password);
                            editor.commit();

                            Intent i = new Intent(context, main.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }else if(request.getCodigoError()==2){
                            dialog.dismiss();
                            showNoticeDialog(request.getMensajeSistema(), getString(R.string.app_name), constantes.DIALOG_ERROR);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                    showNoticeDialog(t.getMessage(), "Login...", constantes.DIALOG_ERROR);
                }
            });
        }catch (Exception e){
            dialog.dismiss();
            showNoticeDialog(e.getMessage(), "Login...", constantes.DIALOG_ERROR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1 /* El codigo que puse a mi request */: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse(constantes.CALL_PHONE_NUMBER));
                    startActivity(i);
                    return;
                }
            }
        }
    }

    public AlertDialog createDialogContactame() {
        try {
            final Button btn_ok, btn_no;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_contactame, null);
            TextView textwhatsapp = v.findViewById(R.id.whatsapp);
            TextView textfacebook = v.findViewById(R.id.facebook);
            TextView textphone    = v.findViewById(R.id.phone);

            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            textwhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _intencion = new Intent("android.intent.action.MAIN");
                    _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                    _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators(constantes.WHATSAPP)+"@s.whatsapp.net");
                    startActivity(_intencion);
                    alertDialog.dismiss();
                }
            });

            textfacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(constantes.FACEBOOK);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    try{
                        startActivity(intent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });

            textphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                            return;
                        }
                    }
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setData(Uri.parse(constantes.CALL_PHONE_NUMBER));
                    try {
                        startActivity(i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });
            return alertDialog;
        }catch (Exception e){

        }
        return null;
    }
}
