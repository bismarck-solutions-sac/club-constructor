package com.diamante.clubconstructor.cotizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.api.sunat.response;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.login.adaptadores.UserEmpresaAdapter;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.CotizacionDetalle;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.model.UserEmpresa;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cotizacion_step3 extends AppCompatActivity {

    private globals global = globals.getInstance();

    private Context context;
    private Toolbar toolbar;
    private AlertDialog dialogProgress;
    private StepperIndicator stepperIndicator;
    private TextView textNombre, textEmail;
    private ImageView image1;
    private LinearLayout lny_1,lny_2;
    private SwitchCompat switchCompat;
    private Button btnContinue;
    private boolean RETURN_HOME = false;

    private RecyclerView recyclerView;

    private Estimated estimated;
    private Local local;
    private User user;
    private UserEmpresaAdapter userEmpresaAdapter;
    private TextView textMessage;
    private response responseSUNAT;
    private AppCompatButton btn_empresa;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            local       = (Local) savedInstanceState.getSerializable("Local");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            user        = (User) savedInstanceState.getSerializable("user");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("local", local);
            state.putSerializable("estimated", estimated);
            state.putSerializable("user",  user);

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
        setContentView(R.layout.activity_cotizacion_step3);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("local") != null){
                local = (Local) savedInstanceState.getSerializable("local");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.get("estimated");
            }
            if (savedInstanceState.getSerializable("user") != null){
                user = (User) savedInstanceState.getSerializable("user");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            user                = global.getUser();
            context             = this;
            Bundle bundle       = this.getIntent().getExtras();
            dialogProgress      = createDialogProgress();
            if (bundle!= null){
                local           = (Local) bundle.getSerializable("agencia");
                estimated       = (Estimated) bundle.getSerializable("estimated");
            }
            stepperIndicator    = findViewById(R.id.stepper_indicator);
            stepperIndicator.setStepCount(constantes.STEP_COTIZACION_COUNT);
            stepperIndicator.setCurrentStep(3);

            toolbar     = findViewById(R.id.toolbar);
            btnContinue = findViewById(R.id.btnContinue);
            recyclerView= findViewById(R.id.recycler);
            textMessage = findViewById(R.id.textMessage);
            btn_empresa = findViewById(R.id.btn_empresa);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //recyclerView.addItemDecoration( new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()){
                        setup_cotizacion();
                    }
                }
            });
            btn_empresa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogEmpresaAdd().show();
                }
            });
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            setup_data_user();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    private boolean validate() {
        try {
            if (switchCompat.isChecked()){
                if (userEmpresaAdapter.lastCheckedPosition<0){
                    createDialogError("Debe seleccionar una empresa...", "Validaciones").show();
                    return false;
                }
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: validate").show();
        }
        return true;
    }

    private void setup_cotizacion() {
        try {
            Cotizacion cotizacion = new Cotizacion();
            cotizacion.detalle = new ArrayList<>();

            cotizacion.companiasocio = constantes.COMPANIASOCIO;
            cotizacion.numerodocumento = "";
            cotizacion.clientenumero = user.id;
            if (user.codigopersona!=0){
                cotizacion.clientenumero = user.codigopersona;
            }
            cotizacion.clientenombre = user.full_name;
            cotizacion.clienteruc = user.dni;
            cotizacion.monedadocumento = "LO";
            cotizacion.montobruto = 0.00;
            cotizacion.montoafecto = 0.00;
            cotizacion.montonoafecto = 0.00;
            cotizacion.montodescuentos =0.00;
            cotizacion.montoflete = 0.00;
            cotizacion.montoimpuestoventas =0.00;
            cotizacion.montototal =0.00;
            cotizacion.almacencodigo = local.almacencodigo;
            cotizacion.sucursal = local.sucursal;
            cotizacion.recojoflag = "F";
            cotizacion.contacto= user.telefono;
            cotizacion.comentarios = "";
            cotizacion.preparadopor=user.id;
            cotizacion.estado="PR";
            cotizacion.user = user;
            if (local!=null){
                if (local.address_2!=null){
                    cotizacion.clientedireccion = local.address;
                    cotizacion.lugarentrega=local.address_2;
                }else{
                    cotizacion.clientedireccion = "-";
                    cotizacion.lugarentrega="-";
                }
            }else {
                cotizacion.clientedireccion = "-";
                cotizacion.lugarentrega="-";
            }

            if (userEmpresaAdapter!=null){
                cotizacion.userEmpresa = userEmpresaAdapter.select();
            }
            if (estimated!=null){
                List<EstimatedDetail> detail = new ArrayList<>();
                EstimatedDetail item = new EstimatedDetail();
                detail = estimated.getDetail();
                int linea=1;
                for (int i=0; i<detail.size(); i++){
                    item = detail.get(i);
                    CotizacionDetalle info = new CotizacionDetalle();
                    info.companiasocio = cotizacion.companiasocio;
                    info.numerodocumento = cotizacion.numerodocumento;
                    info.tiporegistro   = "P";
                    info.linea = linea;
                    info.tipodetalle = "I";
                    info.itemcodigo = item.brick;
                    info.descripcion = item.brick_name;
                    info.unidadcodigo = item.unidadcodigo;
                    info.preciounitario = 0.00;
                    info.preciounitariooriginal = 0.00;
                    info.cantidadpedida = item.total_brick;
                    info.porcentajedescuento = 0.00;
                    info.monto =0.00;
                    info.montodescuento =0.00;
                    info.montoflete =0.00;
                    info.preciounitarioflete =0.00;
                    info.preciounitarioflete02 =0.00;
                    info.igvexoneradoflag = item.igvexoneradoflag;
                    info.estado ="PR";
                    info.precionumeroregistro =0;
                    info.promocionnumero =0;
                    info.codigoflete =0;
                    info.path = item.path;
                    cotizacion.detalle.add(info);
                    linea ++;
                }
            }
            Intent i = new Intent(context, cotizacion_step4.class);
            i.putExtra("cotizacion", cotizacion);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: setup_cotizacion").show();
        }
    }

    private void setup_data_user() {
        try {
            textNombre      = findViewById(R.id.textNombres);
            textEmail       = findViewById(R.id.textEmail);

            image1          = findViewById(R.id.image1);
            switchCompat    = findViewById(R.id.switch1);
            lny_1           = findViewById(R.id.lny_1);
            lny_2           = findViewById(R.id.lny_2);

            lny_2.setVisibility(View.GONE);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lny_1.getVisibility() == View.VISIBLE){
                        lny_1.setVisibility(View.GONE);
                        image1.setImageDrawable(getApplicationContext().getDrawable(R.drawable.app_icon_arrow_down));
                    }else{
                        lny_1.setVisibility(View.VISIBLE);
                        image1.setImageDrawable(getApplicationContext().getDrawable(R.drawable.app_icon_arrow_up));
                    }
                }
            });

            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RETURN_HOME = true;
                    createDialogQuestion("Su cotización aun no ha sido guardada.", "Estas seguro de salir?").show();
                }
            });

            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        lny_2.setVisibility(View.VISIBLE);
                    }else {
                        lny_2.setVisibility(View.GONE);
                    }
                    if (user.userempresa==null){
                        textMessage.setVisibility(View.VISIBLE);
                    }else if (user.userempresa.size()==0){
                        textMessage.setVisibility(View.VISIBLE);
                    }else{
                        textMessage.setVisibility(View.GONE);
                    }
                    userEmpresaAdapter = new UserEmpresaAdapter(context, user.userempresa);
                    recyclerView.setAdapter(userEmpresaAdapter);
                    userEmpresaAdapter.notifyDataSetChanged();
                }
            });
            textNombre.setText(user.full_name);
            textEmail.setText(user.email);

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: setup_data_user").show();
        }
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
            return false;
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

    private void consultaRUC(String RUC, EditText textRazonSocial, EditText textDireccion) {
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration(constantes.API_SUNAT_BASE).create(MethodWS.class);
        try {
            Call<response> result = methodWS.getAPIRuc(RUC);
            result.enqueue(new Callback<response>() {
                @Override
                public void onResponse(Call<response> call, Response<response> response) {
                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            textRazonSocial.setText(response.body().getRazon_social());
                            textDireccion.setText(response.body().getDomicilio_fiscal());
                        }
                        closeDialogProgress();
                    } else {
                        closeDialogProgress();
                        createDialogError("Error no identificado", "Error: onResponse").show();
                    }
                }
                @Override
                public void onFailure(Call<response> call, Throwable t) {
                    closeDialogProgress();
                    createDialogError(t.getMessage(), "Error: onFailure").show();
                }
            });
        } catch (Exception e) {
            closeDialogProgress();
            createDialogError(e.getMessage(), "Error: consultaRUC").show();
        }
    }

    private void closeDialogProgress(){
        try {
            if (dialogProgress!= null){
                dialogProgress.dismiss();
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: closeDialogProgress").show();
        }
    }

    public AlertDialog createDialogProgress() {
        try {
            AlertDialog alertDialog =null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            try {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_circularloader, null);
                TextView textMessage = v.findViewById(R.id.textMessage);
                textMessage.setText("...Consultando SUNAT...");
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
                    if (RETURN_HOME){
                        Intent i = new Intent(getApplicationContext(), main.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
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
            e.printStackTrace();
        }
        return null;
    }

    public AlertDialog createDialogEmpresaAdd() {
        try {
            final ImageView imgFind;
            final Button btn_ok, btn_no;
            final EditText textRuc, textRazonSocial, textDireccion;
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_empresa_add, null);

            btn_ok          = v.findViewById(R.id.btn_ok);
            btn_no          = v.findViewById(R.id.btn_cancel);
            textRuc         = v.findViewById(R.id.edt_ruc);
            textRazonSocial = v.findViewById(R.id.edt_razonsocial);
            textDireccion   = v.findViewById(R.id.edt_direccion);
            imgFind         = v.findViewById(R.id.imgFind);

            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textRuc.getText().length()==0){
                        textRuc.setError("* Campo obligatorio");
                        textRuc.requestFocus();
                        return;
                    }
                    if (textRazonSocial.getText().length()==0){
                        textRazonSocial.setError("* Campo obligatorio");
                        textRazonSocial.requestFocus();
                        return;
                    }
                    if (textDireccion.getText().length()==0){
                        textDireccion.setText("");
                        textDireccion.requestFocus();
                        return;
                    }
                    UserEmpresa dato = new UserEmpresa();
                    dato.id         = user.id;
                    dato.ruc        = textRuc.getText().toString();
                    dato.razonsocial= textRazonSocial.getText().toString();
                    dato.direccion  = textDireccion.getText().toString();
                    UpdateUserEmpresa(dato, alertDialog );
                }
            });
            textRuc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textRazonSocial.setError(null);
                    textRazonSocial.setText("");
                    textDireccion.setText("-");
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            imgFind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textRuc.getWindowToken(), 0);
                    consultaRUC(textRuc.getText().toString(), textRazonSocial, textDireccion);
                }
            });
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void UpdateUserEmpresa(UserEmpresa dato, AlertDialog dialog){
        RequestParameter parameter = new RequestParameter();
        parameter.userEmpresa= dato;
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.userEmpresaAdd (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            if (textMessage.getVisibility()==View.VISIBLE){
                                textMessage.setVisibility(View.GONE);
                            }
                            user.userempresa.add(dato);
                            global.setUser(user);
                            userEmpresaAdapter = new UserEmpresaAdapter(context, user.userempresa);
                            recyclerView.setAdapter(userEmpresaAdapter);
                            userEmpresaAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }else if(request.getCodigoError()==2){
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
}
