package com.diamante.clubconstructor.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.api.sunat.response;
import com.diamante.clubconstructor.dialogos.dialog_single;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.DatosPerson;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.response.MaestrosRequest;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registrarse extends AppCompatActivity implements dialog_single.dialog_single_listener, GoogleApiClient.OnConnectionFailedListener   {

    private GoogleApiClient googleApiClient;
    private static int SIGN_IN_CODE=777;


    private globals global = globals.getInstance();
    private functions function = functions.getInstance();
    private Context context;
    private TextView textTerminos, textPoliticas;
    private EditText edt_nombres, edt_apellidos, edt_email, edt_password, edt_password2;
    private EditText edt_dni, edt_telefono;
    private TextView text_fecha, text_tipo, text_profesion;
    private Button btn_ok, btn_dni;
    private User user;
    private SharedPreferences sharedPreferences;
    private AppCompatCheckBox chkok;
    private DatePickerDialog picker;

    private AlertDialog dialog;
    private DialogFragment dialogSimple;
    private String SELECTION;
    private CircleImageView imgProfile;

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
        setContentView(R.layout.activity_registrarse);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

        inicializa();
    }

    private void inicializa() {
        try{
            Bundle bundle       = this.getIntent().getExtras();
            context             = this;
            if (bundle!=null){
                if (bundle.getString("MODO").equals("GOOGLE")){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();

                    googleApiClient = new GoogleApiClient.Builder(this)
                            .enableAutoManage(this, this)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                            .build();

                    runOnUiThread  (new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(500);
                                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivityForResult(intent, SIGN_IN_CODE);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }));
                }
            }
            textTerminos        = findViewById(R.id.textTerminos);
            textPoliticas       = findViewById(R.id.textPoliticas);

            edt_nombres         = findViewById(R.id.edt_nombres);
            edt_apellidos       = findViewById(R.id.edt_apellidos);
            edt_email           = findViewById(R.id.edt_email);
            edt_password        = findViewById(R.id.edt_contrasena);
            edt_password2       = findViewById(R.id.edt_contrasena2);
            edt_dni             = findViewById(R.id.edt_documento);
            text_tipo           = findViewById(R.id.edt_tipocliente);
            text_profesion      = findViewById(R.id.edt_profesion);
            text_fecha          = findViewById(R.id.edt_fechanacimiento);
            edt_telefono        = findViewById(R.id.edt_telefono);
            btn_ok              = findViewById(R.id.btn_ok);
            btn_dni             = findViewById(R.id.btn_dni);
            chkok               = findViewById(R.id.chkok);
            imgProfile          = findViewById(R.id.imagePerfil);
            user                = new User();
            dialog              = createDialogProgress();

            sharedPreferences   = getApplicationContext().getSharedPreferences(constantes.PREFERENCE_NAME, Context.MODE_PRIVATE);
            textTerminos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, terminos.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            textPoliticas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, politicas.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            btn_dni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edt_dni.getText().toString() == null || edt_dni.getText().toString().isEmpty()){
                        edt_dni.setError("* Campo Obligatorio");
                        edt_dni.requestFocus();
                        return;
                    }
                    if (edt_dni.getText().length()!=8){
                        edt_dni.setError("* DNI inválido");
                        edt_dni.requestFocus();
                        return;
                    }
                    new consultaESSALUD().execute(edt_dni.getText().toString());
                }
            });
            edt_dni.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()==8){
                        new consultaESSALUD().execute(edt_dni.getText().toString());
                    }else{
                        edt_apellidos.setText("");
                        edt_nombres.setText("");
                        text_fecha.setText("");
                        edt_apellidos.setEnabled(true);
                        edt_nombres.setEnabled(true);
                        text_fecha.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            text_fecha.setInputType(InputType.TYPE_NULL);
            text_fecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(registrarse.this,  android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    NumberFormat fecha   = new DecimalFormat("00");
                                    text_fecha.setText(fecha.format(dayOfMonth )+ "/" + fecha.format((month + 1)) + "/" + year);
                                }
                            }, year, month, day);
                    picker.setCancelable(false);
                    picker.show();
                }
            });
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
            user.setProfesion("");
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
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

            if (edt_password.getText().toString() == null || edt_password.getText().toString().isEmpty()){
                edt_password.setError("* Campo Obligatorio");
                edt_password.requestFocus();
                return false;
            }

            if (edt_password2.getText().toString() == null || edt_password2.getText().toString().isEmpty()){
                edt_password2.setError("* Campo Obligatorio");
                edt_password2.requestFocus();
                return false;
            }

            if (!edt_password.getText().toString().equals(edt_password2.getText().toString())){
                createDialogError("Las contraseñas no coinciden, favor de verificar...", "Validación").show();
                edt_password.requestFocus();
                return false;
            }
            if (edt_dni.getText().toString() == null || edt_dni.getText().toString().isEmpty()){
                edt_dni.setError("* Campo Obligatorio");
                edt_dni.requestFocus();
                return false;
            }
            if (edt_dni.getText().toString().length()!=8){
                edt_dni.setError("* El documento no es válido");
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
            if (!chkok.isChecked()){
                createDialogError("Debe aceptar  terminos y condiciones, favor de verificar...", "Validación").show();
                chkok.requestFocus();
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
            user.password       = edt_password.getText().toString();
            user.email          = edt_email.getText().toString();
            user.dni            = edt_dni.getText().toString();
            user.telefono       = edt_telefono.getText().toString();
            user.fechanacimiento= text_fecha.getText().toString();
            user.departamento   = "";
            user.provincia      = "";
            user.distrito       = "";
            user.level          = 1;
            if (user.path==null){
                user.path       = "profile/profile.png";
            }
            if (user.getIdgoogle()==null){
                user.idgoogle   = "";
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: prepare_update").show();
            return false;
        }
        return true;
    }

    private void Update(){
        RequestParameter parameter = new RequestParameter();
        parameter.user= user;
        dialog.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.setuserAdd (parameter);
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
                            open_main();
                        }else if(request.getCodigoError()==2){
                            dialog.dismiss();
                            createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
                        }
                    }else{
                        dialog.dismiss();
                        createDialogError(response.message(), "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        }catch (Exception e){
            dialog.dismiss();
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    private void open_main() {
        try {
            finish();
            Intent i = new Intent(registrarse.this, main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onFailure").show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(registrarse.this, login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            createDialogError(e.getMessage(), "Error: createDialogProgress").show();
        }
        return alertDialog;
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == SIGN_IN_CODE){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);

            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onActivityResult").show();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                Glide.with(context).load(account.getPhotoUrl()).into(imgProfile);
                imgProfile.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (imgProfile.getDrawable() instanceof BitmapDrawable) {
                            //Bitmap bitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();

                            Drawable dr = imgProfile.getDrawable();
                            Bitmap bitmap =  ((BitmapDrawable)dr.getCurrent()).getBitmap();

                            ConvertBitmapToString upload = new ConvertBitmapToString();
                            upload.execute(bitmap);
                        }
                    }
                }, 500);
                edt_email.setText(account.getEmail());
                edt_email.setEnabled(false);
                edt_nombres.setText(account.getDisplayName());
                edt_dni.requestFocus();
                user.setIdgoogle(account.getId());
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){

                        }
                    }
                });
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: handleSignInResult").show();
        }
    }

    private class ConvertBitmapToString extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            // TODO Auto-generated method stub
            String encodedImage = "";

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            params[0].compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encodedImage;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result.length()!=0){
                user.setBase64String(result);
            }
        }
    }

    private class consultaESSALUD extends AsyncTask<String, String,  Boolean> {

        Boolean lbResult=false;
        DatosPerson datosPerson;
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            if (aBoolean){
                edt_apellidos.setText(datosPerson.getApellidoPaterno() + " " + datosPerson.getApellidoMaterno());
                edt_nombres.setText(datosPerson.getNombres());
                text_fecha.setText(datosPerson.getFechaNacimiento());
                edt_password.requestFocus();

                edt_apellidos.setEnabled(false);
                edt_nombres.setEnabled(false);
                text_fecha.setEnabled(false);
            }else{
                createDialogError("Ocurrio un error al conectar con RENIEC, intentelo una vez más.", getString(R.string.app_name)).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {

            SoapObject Request = new SoapObject(constantes.NAMESPACE, constantes.METHOD_NAME);
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(constantes.PARAMETER_NAME);
            propertyInfo.setValue(params[0]);
            propertyInfo.setType(String.class);
            Request.addProperty(propertyInfo);

            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(Request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(constantes.URL);
            try {
                httpTransportSE.call(constantes.SOAP_ACTION, envelope);
                SoapObject obj1 = (SoapObject) envelope.getResponse();


                if (obj1.getProperty(0)!=null){
                    if (obj1.getProperty(0).toString().equals("anyType{}")){
                        return false;
                    }
                }
                datosPerson = new DatosPerson();
                datosPerson.setApellidoMaterno("");
                if (obj1.getProperty(0) != null){
                    datosPerson.setApellidoMaterno(obj1.getProperty(0).toString());
                }
                datosPerson.setApellidoPaterno("");
                if (obj1.getProperty(1) != null){
                    datosPerson.setApellidoPaterno(obj1.getProperty(1).toString());
                }
                datosPerson.setFechaNacimiento("");
                if (obj1.getProperty(2) != null){
                    datosPerson.setFechaNacimiento(obj1.getProperty(2).toString());
                }
                datosPerson.setNombres("");
                if (obj1.getProperty(3) != null){
                    datosPerson.setNombres(obj1.getProperty(3).toString());
                }
                datosPerson.setSexo("");
                if (obj1.getProperty(4) != null){
                    datosPerson.setSexo(obj1.getProperty(4).toString());
                }
                lbResult =true;
            } catch (Exception e){
                dialog.dismiss();
                createDialogError(e.getMessage(), "Error: consultaESSALUD").show();
            }
            return lbResult;
        }
    }

}
