package com.diamante.clubconstructor.cotizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.cotizacion.adapter.CotizacionCartAdapter;
import com.diamante.clubconstructor.dialogos.dialog_single;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.login.adaptadores.UserDireccionAdapter;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.Direccion;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.MaestrosRequest;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.diamante.clubconstructor.util.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cotizacion_step4 extends AppCompatActivity implements dialog_single.dialog_single_listener   {

    private functions function = functions.getInstance();
    private globals global = globals.getInstance();
    private Context context;
    private Cotizacion cotizacion;
    private Local local;

    private Toolbar toolbar;
    private StepperIndicator stepperIndicator;
    private ProgressBar progressBar, progressBar2;
    private TextView textMessage, textdireccion;
    private EditText edt_direccion;
    private CotizacionCartAdapter adapter;
    private UserDireccionAdapter direccionAdapter;
    private RecyclerView recyclerView, recycler_direccion;
    private SwitchCompat switchCompat;
    private AppCompatButton btnProcesar;
    private LinearLayout lny_lugar;
    private ImageView btnDireccion;
    private AlertDialog dialog;
    private DialogFragment dialogSimple;
    private Direccion userDireccion ;
    private String SELECTOR;


    private EditText textdato, textreferencia, edt_dpto, edt_prov, edt_dist;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            local       = (Local) savedInstanceState.getSerializable("Local");
            cotizacion  = (Cotizacion) savedInstanceState.getSerializable("cotizacion");
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("local", local);
            state.putSerializable("cotizacion", cotizacion);

        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizacion_step4);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("local") != null){
                local = (Local) savedInstanceState.getSerializable("local");
            }
            if (savedInstanceState.getSerializable("cotizacion") != null){
                cotizacion = (Cotizacion) savedInstanceState.getSerializable("cotizacion");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            context             = this;
            stepperIndicator    = findViewById(R.id.stepper_indicator);
            stepperIndicator.setStepCount(constantes.STEP_COTIZACION_COUNT);
            stepperIndicator.setCurrentStep(4);

            toolbar             = findViewById(R.id.toolbar);
            progressBar         = findViewById(R.id.progress_bar);
            progressBar2        = findViewById(R.id.progress_bar2);
            textMessage         = findViewById(R.id.textMessage);
            recyclerView        = findViewById(R.id.recycler);
            recycler_direccion  = findViewById(R.id.recycler_direccion);
            switchCompat        = findViewById(R.id.switch1);
            btnProcesar         = findViewById(R.id.btnContinue);
            edt_direccion       = findViewById(R.id.edt_direccion);
            lny_lugar           = findViewById(R.id.lny_lugar);
            dialog              = createDialogProgress();
            textdireccion       = findViewById(R.id.textdireccion);
            btnDireccion        = findViewById(R.id.btndireccion);
            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogQuestion("Su cotización aun no ha sido guardada.", "Estas seguro de salir?").show();
                }
            });

            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            Bundle bundle       = this.getIntent().getExtras();
            if (bundle!= null){
                local       = (Local) bundle.getSerializable("Local");
                cotizacion  = (Cotizacion) bundle.getSerializable("cotizacion");
            }
            lny_lugar.setVisibility(View.GONE);
            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        lny_lugar.setVisibility(View.VISIBLE);
                        cotizacion.setRecojoflag("O");
                        edt_direccion.setText(cotizacion.lugarentrega.toUpperCase());
                        user_direcciones();
                    }else {
                        lny_lugar.setVisibility(View.GONE);
                        edt_direccion.setText("S/D");
                        cotizacion.setRecojoflag("F");
                    }
                    Cotizacion info = new Cotizacion();
                    info.setMontototal(0.00);
                    setup_datos(info);
                    calcular_totales();
                }
            });
            btnProcesar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()){
                        Update();
                    }
                }
            });
            btnDireccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogLugar().show();
                }
            });
            edt_direccion.setFocusable(false);
            edt_direccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogLugar().show();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
            linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
            recycler_direccion.setHasFixedSize(true);
            recycler_direccion.setLayoutManager(linearLayoutManager1);

            calcular_totales();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    private boolean validate() {
        try {
            if (switchCompat.isChecked()){
                if (direccionAdapter!=null){
                    if (direccionAdapter.lastCheckedPosition<0){
                        createDialogError("Debe seleccionar una dirección de despacho...", "Validaciones").show();
                        return false;
                    }
                }
            }
            if (cotizacion!=null){
                if (cotizacion.detalle!=null){
                    if (cotizacion.detalle.size()==0){
                        createDialogError("No existen items para la cotización...", "Validaciones").show();
                        return false;
                    }
                }
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
        return true;
    }

    private void calcular_totales() {
        RequestParameter parameter = new RequestParameter();
        parameter.cotizacion    = cotizacion;
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.cotizacion_totales(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            cotizacion = request.cotizacion;
                            setup_datos(cotizacion);
                            if (cotizacion == null ){
                                textMessage.setVisibility(View.VISIBLE);
                            }
                        } else if (request.getCodigoError() == 2) {
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Error: " + request.mensajeSistema);
                        }
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Error: " + response.body().mensajeSistema);
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText("Error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setText("Error: " + e.getMessage());
        }
    }


    private void user_direcciones() {
        RequestParameter parameter = new RequestParameter();
        parameter.user = global.getUser();
        if (cotizacion.userEmpresa!=null){
            parameter.user.codigopersona = cotizacion.userEmpresa.codigopersona;
        }
        progressBar2.setVisibility(View.VISIBLE);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.userDireccion(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        global.getUser().setDireccion_list(request.user.direccion_list);
                        if (request.getCodigoError() == 0) {
                            direccionAdapter = new UserDireccionAdapter(context, global.getUser().getDireccion_list(), cotizacion);
                            recycler_direccion.setAdapter(direccionAdapter);
                            direccionAdapter.notifyDataSetChanged();
                            if (request.user.direccion_list == null ){
                                textdireccion.setVisibility(View.VISIBLE);
                            }else if (request.user.direccion_list.size() == 0){
                                textdireccion.setVisibility(View.VISIBLE);
                            }
                        } else if (request.getCodigoError() == 2) {
                            textdireccion.setVisibility(View.VISIBLE);
                            textdireccion.setText("Error: " + request.mensajeSistema);
                        }
                        progressBar2.setVisibility(View.GONE);
                    } else {
                        progressBar2.setVisibility(View.GONE);
                        textdireccion.setVisibility(View.VISIBLE);
                        textdireccion.setText("Error: " + response.body().mensajeSistema);
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar2.setVisibility(View.GONE);
                    textdireccion.setVisibility(View.VISIBLE);
                    textdireccion.setText("Error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            progressBar2.setVisibility(View.GONE);
            textdireccion.setVisibility(View.VISIBLE);
            textdireccion.setText("Error: " + e.getMessage());
        }
    }

    private void setup_datos(Cotizacion cotizacion) {
        try {
            NumberFormat decimalFormat   = new DecimalFormat("#,##0.00");
            TextView textTotal;
            textTotal   = findViewById(R.id.textTotal);
            adapter = new CotizacionCartAdapter(context, cotizacion);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            textTotal.setText(decimalFormat.format(cotizacion.montototal));
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: setup_datos").show();
        }
    }

    private void Update(){
        try {
            RequestParameter parameter = new RequestParameter();
            if (direccionAdapter != null) {
                cotizacion.userDireccion = direccionAdapter.select();
                cotizacion.direccionList = direccionAdapter.getDireccions();
            }
            parameter.cotizacion = cotizacion;
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            try {
                dialog.show();
                Call<ResponseData> result = methodWS.cotizacion_add(parameter);
                result.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            ResponseData request = response.body();
                            if (request.getCodigoError() == 0) {
                                dialog.dismiss();
                                cotizacion = request.cotizacion;
                                global.setUser(cotizacion.user);
                                Intent i = new Intent(getApplicationContext(), cotizacion_finish.class);
                                i.putExtra("Cotizacion", cotizacion);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }else if (request.getCodigoError()==1){
                                dialog.dismiss();
                                createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();

                            } else if (request.getCodigoError() == 2) {
                                dialog.dismiss();
                                createDialogError(request.getMensajeSistema(), getString(R.string.app_name)).show();
                            }
                        } else {
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
            } catch (Exception e) {
                dialog.dismiss();
                createDialogError(e.getMessage(), "Error: Update").show();
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    createDialogQuestion("Su cotización aun no ha sido guardada.", "Estas seguro de salir?").show();
                    break;
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                createDialogQuestion("Su cotización aun no ha sido guardada.", "Estas seguro de salir?").show();
            }
            return true;
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onKeyDown").show();
        }
        return false;
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
                createDialogError(e.getMessage(), "Error: createDialogProgress").show();
            }
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
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
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    alertDialog.dismiss();
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
            createDialogError(e.getMessage(), "Error: createDialogProgress").show();
        }
        return null;
    }

    public AlertDialog createDialogLugar() {
        try {
            final Button btn_ok, btn_no;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_lugarentrega, null);

            btn_ok          = v.findViewById(R.id.btn_ok);
            btn_no          = v.findViewById(R.id.btn_cancel);
            textdato        = v.findViewById(R.id.edt_direccion);
            textreferencia  = v.findViewById(R.id.edt_referencia);
            edt_dpto        = v.findViewById(R.id.edt_departamento);
            edt_prov        = v.findViewById(R.id.edt_provincia);
            edt_dist        = v.findViewById(R.id.edt_distrito);
            textdato.setText(edt_direccion.getText().toString().toUpperCase());
            textreferencia.setText("");
            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textdato.getText().length()==0){
                        textdato.setText("");
                        return;
                    }
                    if (userDireccion.getDepartamento() == null){
                        edt_dpto.setError("El departamento es requerido");
                        return;
                    }
                    if (userDireccion.getDepartamento().length()==0){
                        edt_dpto.setError("El departamento es requerido");
                        return;
                    }
                    if (userDireccion.getProvincia() == null ){
                        edt_prov.setError("La provincia es requerida");
                        return;
                    }
                    if (userDireccion.getProvincia().length()==0){
                        edt_prov.setError("La provincia es requerida");
                        return;
                    }
                    if (userDireccion.getCodigopostal() == null){
                        edt_dist.setError("El distrito es requerido");
                        return;
                    }
                    if (userDireccion.getCodigopostal().length()==0){
                        edt_dist.setError("El distrito es requerido");
                        return;
                    }
                    userDireccion.setReferencia(textreferencia.getText().toString());
                    userDireccion.setPersona(cotizacion.clientenumero);
                    if (cotizacion.userEmpresa!=null){
                        userDireccion.setPersona(cotizacion.userEmpresa.codigopersona);
                    }
                    if (textdireccion.getVisibility()==View.VISIBLE){
                        textdireccion.setVisibility(View.GONE);
                    }
                    cotizacion.setLugarentrega(textdato.getText().toString());
                    userDireccion.setPersona(cotizacion.user.codigopersona);
                    if (cotizacion.userEmpresa!=null){
                        userDireccion.setPersona(cotizacion.userEmpresa.codigopersona);
                    }
                    userDireccion.setAccion("Add");
                    userDireccion.setSecuencia(0);
                    userDireccion.setDireccion(cotizacion.getLugarentrega());
                    userDireccion.setPais("PER");
                    userDireccion.setRutadespacho("Z1");
                    userDireccion.setTelefono(global.getUser().getTelefono());
                    userDireccion.setFax("");
                    userDireccion.setUbigeo("");
                    userDireccion.setSelected(true);
                    userDireccion.setUltimousuario("SYSTEM");
                    UpdateDireccion(userDireccion, alertDialog);
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            edt_dpto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTOR = "DPTO";
                    getDepartamento();
                }
            });
            edt_prov.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTOR = "PROV";
                    getProvincia();
                }
            });
            edt_dist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SELECTOR = "DIST";
                    getDistrito();
                }
            });
            userDireccion   = new Direccion();
            return alertDialog;
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: createDialogLugar").show();
        }
        return null;
    }



    @Override
    public void onDialogPositiveClick(DialogFragment dialog, GeneralSpinner spinner) {
        try {
            switch (SELECTOR){
                case "DPTO":
                    userDireccion.setDepartamento(spinner.id);
                    userDireccion.setDepartamento_name(spinner.descripcion);
                    userDireccion.setProvincia("");
                    userDireccion.setCodigopostal("");
                    edt_dpto.setError(null);
                    edt_dpto.setText(spinner.descripcion);
                    edt_prov.setText("");
                    edt_prov.setError(null);
                    edt_dist.setText("");
                    edt_dist.setError(null);
                    break;
                case "PROV":
                    userDireccion.setProvincia(spinner.id);
                    userDireccion.setProvincia_name(spinner.descripcion);
                    userDireccion.setCodigopostal("");
                    edt_prov.setText(spinner.descripcion);
                    edt_prov.setError(null);
                    edt_dist.setText("");
                    edt_dist.setError(null);
                    break;
                case "DIST":
                    userDireccion.setCodigopostal(spinner.id);
                    userDireccion.setCodigopostal_name(spinner.descripcion);
                    edt_dist.setText(spinner.descripcion);
                    edt_dist.setError(null);
                    break;
            }
            dialogSimple.dismiss();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onDialogPositiveClick").show();
        }
    }

    private void getDepartamento(){
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        MaestrosRequest dato= new MaestrosRequest();
        dato.setTabla("DEPARTAMENTOS");
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest = dato;
        try {
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int POSITION=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            POSITION = function.read_generalspinner_position(request.spinner_general, userDireccion.getDepartamento());
                            dialogSimple = new dialog_single("Seleccione un Valor", request.spinner_general, POSITION);
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
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    private void getProvincia(){
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        MaestrosRequest dato= new MaestrosRequest();
        dato.setTabla("PROVINCIAS");
        dato.setParametro1(userDireccion.getDepartamento());
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest = dato;
        try {
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int POSITION=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            POSITION = function.read_generalspinner_position(request.spinner_general, userDireccion.getProvincia());
                            dialogSimple = new dialog_single("Seleccione un Valor", request.spinner_general, POSITION);
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
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    private void getDistrito(){
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        MaestrosRequest dato= new MaestrosRequest();
        dato.setTabla("DISTRITOS");
        dato.setParametro1(userDireccion.getDepartamento());
        dato.setParametro2(userDireccion.getProvincia());
        RequestParameter parameter = new RequestParameter();
        parameter.maestrosRequest = dato;
        try {
            Call<ResponseData> result = methodWS.general_spinner (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        int POSITION=-1;
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            POSITION = function.read_generalspinner_position(request.spinner_general, userDireccion.getCodigopostal());
                            dialogSimple = new dialog_single("Seleccione un Valor", request.spinner_general, POSITION);
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
            createDialogError(e.getMessage(), "Error: Update").show();
        }
    }

    private void UpdateDireccion(Direccion direccion, AlertDialog alertDialog){
        try {
            if (direccion.getPersona()==0){
                global.getUser().getDireccion_list().add(userDireccion);
                int pos = global.getUser().getDireccion_list().size() -1;
                direccionAdapter = new UserDireccionAdapter(context, global.getUser().getDireccion_list(), cotizacion, pos);
                recycler_direccion.setAdapter(direccionAdapter);
                direccionAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
                return;
            }
            RequestParameter parameter = new RequestParameter();
            parameter.direccion = direccion;
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            Call<ResponseData> result = methodWS.direccionAdd (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            direccion.setSecuencia(request.direccion.getSecuencia());
                            global.getUser().getDireccion_list().add(userDireccion);
                            int pos = global.getUser().getDireccion_list().size() -1;
                            direccionAdapter = new UserDireccionAdapter(context, global.getUser().getDireccion_list(), cotizacion, pos);
                            recycler_direccion.setAdapter(direccionAdapter);
                            direccionAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();

                        }else if(request.getCodigoError()==2){
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
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
            createDialogError(e.getMessage(), "Error: UpdateDireccion").show();
        }
    }
}
