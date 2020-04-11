package com.diamante.clubconstructor.cotizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.badoualy.stepperindicator.StepperIndicator;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.cotizacion.adapter.BrickListAdapter;
import com.diamante.clubconstructor.dialogos.dialog_custom;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cotizacion_step2 extends AppCompatActivity {

    private StepperIndicator stepperIndicator;
    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private globals global = globals.getInstance();
    private BrickListAdapter brickListAdapter;
    private ProgressBar progressBar;
    private TextView textMessage;
    private Button btnContinue;

    private GeneralSpinner brick_type;
    private List<Brick> bricks = new ArrayList<>();
    private List<Brick> bricks_add = new ArrayList<>();
    private Local local;
    private Estimated estimated;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            local       = (Local) savedInstanceState.getSerializable("local");
            brick_type  = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            bricks      = (List<Brick>) savedInstanceState.getSerializable("bricks");
            bricks_add  = (List<Brick>) savedInstanceState.getSerializable("bricks_add");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("local", local);
            state.putSerializable("brick_type", brick_type);
            state.putSerializable("estimated", estimated);
            state.putSerializable("bricks", (Serializable) bricks);
            state.putSerializable("bricks_add", (Serializable) bricks_add);

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
        setContentView(R.layout.activity_cotizacion_step2);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("local") != null){
                local = (Local) savedInstanceState.getSerializable("local");
            }
            if (savedInstanceState.getSerializable("brick_type") != null){
                brick_type = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.getSerializable("estimated");
            }
            if (savedInstanceState.getSerializable("bricks") != null){
                bricks = (List<Brick>) savedInstanceState.getSerializable("bricks");
            }
            if (savedInstanceState.getSerializable("bricks_add") != null){
                bricks_add = (List<Brick>) savedInstanceState.getSerializable("bricks_add");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            Bundle bundle       = this.getIntent().getExtras();
            if (bundle!= null){
                brick_type  = (GeneralSpinner) bundle.getSerializable("brick_type");
                bricks_add  = (List<Brick>) this.getIntent().getSerializableExtra("bricks_add");
                local       = (Local) bundle.getSerializable("agencia");
                if (brick_type!=null){
                    TextView textTile = findViewById(R.id.textTitle);
                    textTile.setText(brick_type.descripcion.toUpperCase());
                }
            }
            context             = this;
            stepperIndicator    = findViewById(R.id.stepper_indicator);
            ImageView home      = findViewById(R.id.home);
            toolbar             = findViewById(R.id.toolbar);
            progressBar         = findViewById(R.id.progress_bar);
            textMessage         = findViewById(R.id.textMessage);
            btnContinue         = findViewById(R.id.btnContinue);

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bricks_add!=null) {
                        if (bricks_add.size() != 0) {
                            createDialogQuestion("Su cotización aún no esta guardada", "Seguro de Salir?").show();
                        } else {
                            Intent i = new Intent(getApplicationContext(), main.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }else {
                        finish();
                    }
                }
            });
            stepperIndicator.setStepCount(constantes.STEP_COTIZACION_COUNT);
            stepperIndicator.setCurrentStep(2);
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()){
                        prepare_update();
                    }
                }
            });

            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            recyclerView    = findViewById(R.id.recycler);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            myBrickList();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    private void prepare_update(){
        try {
            Brick item;
            if (bricks_add==null){
                bricks_add = new ArrayList<>();
            }
            for (int i=0; i<bricks.size(); i++){
                if (bricks.get(i).selected){
                    item = bricks.get(i);
                    bricks_add.add(item);
                }
            }
            createDialogConfirm().show();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: prepare_update").show();
        }
    }

    private boolean validate(){
        try {
            int selected=0;
            for (int i=0; i<bricks.size(); i++){
                if (bricks.get(i).selected){
                    selected++;
                    break;
                }
            }
            if (selected==0){
                createDialogError("Debe seleccionar al menos un producto.", getString(R.string.app_name)).show();
                return false;
            }
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: validate").show();
        }
        return true;
    }

    private void myBrickList(){
        RequestParameter parameter = new RequestParameter();
        parameter.user = new User();
        parameter.user  = global.user;
        parameter.brick_type = new GeneralSpinner();
        parameter.brick_type    = brick_type;
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.mybrickList (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            bricks  = request.getBricks();
                            for (int i=0; i<bricks.size(); i++){
                                bricks.get(i).selected=false;
                                bricks.get(i).cantidad=1;
                            }
                            brickListAdapter = new BrickListAdapter(context, bricks);
                            recyclerView.setAdapter(brickListAdapter);
                            brickListAdapter.notifyDataSetChanged();
                            if (bricks.size()==0 || bricks == null){
                                textMessage.setVisibility(View.VISIBLE);
                            }
                        }else if(request.getCodigoError()==2){
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Error: " + request.mensajeSistema);
                        }
                        progressBar.setVisibility(View.GONE);
                    }else {
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
        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setText("Error: " + e.getMessage());
        }
    }

    public AlertDialog createDialogConfirm() {
        try {
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();

            View v = inflater.inflate(R.layout.dialog_warning, null);

            builder.setView(v);

            Button btn_aperturar_no =  v.findViewById(R.id.buttonNo);
            Button btn_aperturar_si =  v.findViewById(R.id.buttonYes);
            final TextView textMessage =  v.findViewById(R.id.textMessage);
            final TextView textTitle = v.findViewById(R.id.textTitle);
            final ImageView image = v.findViewById(R.id.imageIcon);

            image.setImageResource(R.drawable.dialog_icon_warning);
            btn_aperturar_no.setText("Agregar \nProducto");
            btn_aperturar_si.setText("Enviar \nCotización");
            textMessage.setText("Desea agregar otro producto o cerrar y enviar su cotización?");
            textTitle.setText(getString(R.string.app_name));
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            btn_aperturar_si.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setup_cotizacion_temp();
                            Intent i = new Intent(context, cotizacion_step3.class);
                            i.putExtra("estimated", estimated);
                            i.putExtra("agencia", local);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            (context).startActivity(i);
                            finish();
                        }
                    }
            );

            btn_aperturar_no.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, cotizacion_step1.class);
                            i.putExtra("bricks_add", (Serializable) bricks_add);
                            i.putExtra("agencia", local);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                            alertDialog.dismiss();
                        }
                    }
            );
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void setup_cotizacion_temp() {
        try {
            estimated = new Estimated();
            estimated.setStatus("A");
            estimated.setId(global.getUser().getId());
            List<EstimatedDetail> lista = new ArrayList<>();
            for (int i=0; i<bricks_add.size(); i++){
                EstimatedDetail item = new EstimatedDetail();
                item.brick = bricks_add.get(i).brick;
                item.brick_name = bricks_add.get(i).descripcionlocal;
                item.type_brick = brick_type.id;
                item.total_brick = bricks_add.get(i).cantidad;
                item.area = 0;
                item.bag_cement = 0;
                item.bag_require = 0;
                item.mortar = 0;
                item.mortar_require = 0;
                item.rock = 0;
                item.rock_require = 0;
                item.path = bricks_add.get(i).path;
                item.unidadcodigo = bricks_add.get(i).unidadcodigo;
                item.igvexoneradoflag = bricks_add.get(i).igvexoneradoflag;
                item.status = "A";
                lista.add(item);
            }
            estimated.setDetail(lista);
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (bricks_add!=null) {
                    if (bricks_add.size() != 0) {
                        createDialogQuestion("Su cotización aún no esta guardada", "Seguro de Salir?").show();
                    } else {
                        finish();
                    }
                }else{
                    finish();
                }
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
                    if (bricks_add!=null){
                        if (bricks_add.size()!=0){
                            createDialogQuestion("Su cotización aún no esta guardada", "Seguro de Salir?").show();
                        }else{
                            finish();
                        }
                    }else {
                        finish();
                    }
                    break;
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
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
            e.printStackTrace();
        }
        return null;
    }
}
