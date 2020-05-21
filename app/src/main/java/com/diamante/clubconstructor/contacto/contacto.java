package com.diamante.clubconstructor.contacto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.principal;
import com.diamante.clubconstructor.model.Message;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.functions;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class contacto extends AppCompatActivity {

    private globals global = globals.getInstance();
    private functions function = functions.getInstance();

    private AlertDialog dialog;
    private Context context;
    private ImageView imageConsulta;
    private LinearLayout lnyPhone, lnyConsulta;
    private TextView textphono, textfacebook, textwhatsapp;
    private EditText edit_consulta;
    private Button btn_ok;
    private User user;

    private Toolbar toolbar;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            Log.v("main", "Inside of onRestoreInstanceState");
            global      = (globals) savedInstanceState.getSerializable("global");
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("global", (Serializable) global);
        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }

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
        setContentView(R.layout.activity_contacto);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        if ((savedInstanceState != null))
        {
            if (savedInstanceState.getSerializable("global") != null){
                global = (globals) savedInstanceState.getSerializable("global");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            context         = this;
            Bundle bundle   = this.getIntent().getExtras();
            if (bundle.getSerializable("user")!=null){
                user        = (User) bundle.getSerializable("user");
            }
            dialog          = createDialogProgress();
            toolbar         = findViewById(R.id.toolbar);
            imageConsulta   = findViewById(R.id.imageConsulta);
            lnyPhone        = findViewById(R.id.lnyphone);
            lnyConsulta     = findViewById(R.id.lnyconsulta);
            btn_ok          = findViewById(R.id.btn_ok);
            edit_consulta   = findViewById(R.id.edit_consulta);

            textphono       = findViewById(R.id.phone);
            textfacebook    = findViewById(R.id.facebook);
            textwhatsapp    = findViewById(R.id.whatsapp);
            lnyConsulta.setVisibility(View.GONE);
            btn_ok.setVisibility(View.GONE);
            setSupportActionBar(toolbar);
            // add back arrow to toolbar
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            textphono.setText(function.f_read_parametros_texto("999999", "VM", "CCPHONECAL"));
            textphono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(contacto.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                            return;
                        }
                    }
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setData(Uri.parse("tel:" + textphono.getText().toString()));
                    startActivity(i);
                }
            });

            textfacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dato = function.f_read_parametros_explicacion("999999", "VM", "CCFACEBOOK");
                    Uri uri     = Uri.parse(dato);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    try{
                        startActivity(intent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            textwhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String _dato        = function.f_read_parametros_texto("999999", "VM", "CCWHATSAPP");
                    Intent _intencion   = new Intent("android.intent.action.MAIN");
                    _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                    _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators(_dato)+"@s.whatsapp.net");
                    try {
                        startActivity(_intencion);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            imageConsulta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lnyConsulta.getVisibility()==View.GONE){
                        btn_ok.setVisibility(View.VISIBLE);
                        lnyConsulta.setVisibility(View.VISIBLE);
                        imageConsulta.setImageDrawable(getApplicationContext().getDrawable(R.drawable.app_icon_arrow_up));
                    }else{
                        btn_ok.setVisibility(View.GONE);
                        lnyConsulta.setVisibility(View.GONE);
                        imageConsulta.setImageDrawable(getApplicationContext().getDrawable(R.drawable.app_icon_arrow_down));
                    }
                }
            });

            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), principal.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()){
                        createDialogQuestion("¿Seguro de enviar la consulta?", getString(R.string.app_name)).show();
                    }
                }
            });

        }catch (Exception e){
            createDialogError(e.getMessage(), "inicializa").show();
        }
    }

    private void prepare_update(){
        try {
            dialog.show();
            Message message = new Message();
            message.id                  = 0;
            message.id_user             = user.id;
            message.id_category_message = 1;
            message.id_status_messages  = 1;
            message.name                = user.full_name;
            message.email               = user.email;
            message.subject             = "Constructor Diamante - Solicitud Contacto";
            message.message             = edit_consulta.getText().toString();
            message.send_email          = "0";

            try {
                RequestParameter parameter = new RequestParameter();
                parameter.message    = message;
                MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
                Call<ResponseData> result = methodWS.messageAdd(parameter);
                result.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            ResponseData request = response.body();
                            if (request.getCodigoError() == 0) {
                                dialog.dismiss();
                                createDialogInformation(getString(R.string.app_name), "Mensaje Enviado correctamente.").show();
                            } else if (request.getCodigoError() == 1) {
                                dialog.dismiss();
                                createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                            } else if (request.getCodigoError() == 2) {
                                dialog.dismiss();
                                createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                            }
                        } else {
                            dialog.dismiss();
                            createDialogError(response.message(), "onResponse").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        dialog.dismiss();
                        createDialogError(t.getMessage(), "onFailure").show();
                    }
                });
            } catch (Exception e) {
                dialog.dismiss();
                createDialogError(e.getMessage(), "onFailure").show();
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "prepare_update").show();
        }
    }

    private boolean validate() {
        try {
            if (edit_consulta.getText().toString() == null || edit_consulta.getText().toString().isEmpty()){
                edit_consulta.setError("* Campo Obligatorio");
                edit_consulta.requestFocus();
                return false;
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "validate").show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
            }
        } catch (Exception e) {
        }
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog createDialogProgress() {
        try {
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
        }catch (Exception e){
            createDialogError(e.getMessage(), "createDialogProgress").show();
        }
        return null;
    }

    public AlertDialog createDialogError(String message, String title) {
        try {
            final Button btn_ok;
            final TextView textTitle, textMessage;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_error, null);

            btn_ok      = v.findViewById(R.id.buttonAction);
            textTitle   = v.findViewById(R.id.textTitle);
            textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(title);
            textMessage.setText(message);
            btn_ok.setText(context.getResources().getString(R.string.okay));
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

    public AlertDialog createDialogQuestion(String message, String title) {
        try {
            final Button btn_ok, btn_no;
            final TextView textTitle, textMessage;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_warning, null);

            btn_ok      = v.findViewById(R.id.buttonYes);
            btn_no      = v.findViewById(R.id.buttonNo);
            textTitle   = v.findViewById(R.id.textTitle);
            textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(title);
            textMessage.setText(message);
            btn_ok.setText(context.getResources().getString(R.string.okay));
            btn_no.setText(context.getResources().getString(R.string.cancelar));
            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    prepare_update();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            return alertDialog;
        }catch (Exception e){
            createDialogError(e.getMessage(), "createDialogQuestion").show();
        }
        return null;
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
                    finish();
                }
            });
            return alertDialog;
        }catch (Exception e){
            createDialogError(e.getMessage(), "createDialogInformation").show();
        }
        return null;
    }
}
