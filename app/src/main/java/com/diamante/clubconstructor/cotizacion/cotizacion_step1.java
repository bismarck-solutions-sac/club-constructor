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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.cotizacion.adapter.EstructuraListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Brick;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.Local;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cotizacion_step1 extends AppCompatActivity {

    private StepperIndicator stepperIndicator;
    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private globals global = globals.getInstance();

    private List<GeneralSpinner> brick_type;
    private Local local;
    private Estimated estimated;
    private EstructuraListAdapter estructuraListAdapter;
    private List<Brick> bricks_add= new ArrayList<>();
    private ProgressBar progressBar;
    private TextView textMessage;
    private RelativeLayout relativeLayout;
    private Button btnContinue;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            local       = (Local) savedInstanceState.getSerializable("Local");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            bricks_add  = (List<Brick>) savedInstanceState.getSerializable("bricks_add");
            brick_type  = (List<GeneralSpinner>) savedInstanceState.getSerializable("brick_type");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("local", local);
            state.putSerializable("estimated", estimated);
            state.putSerializable("bricks_add", (Serializable) bricks_add);
            state.putSerializable("brick_type", (Serializable) brick_type);

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
        setContentView(R.layout.activity_cotizacion_step1);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("local") != null){
                local = (Local) savedInstanceState.getSerializable("local");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.getSerializable("estimated");
            }
            if (savedInstanceState.getSerializable("bricks_add") != null){
                bricks_add = (List<Brick>) savedInstanceState.getSerializable("bricks_add");
            }
            if (savedInstanceState.getSerializable("brick_type") != null){
                brick_type = (List<GeneralSpinner>) savedInstanceState.getSerializable("brick_type");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            context             = this;
            Bundle bundle = this.getIntent().getExtras();
            if (bundle!= null){
                estimated       = (Estimated) bundle.getSerializable("estimated");
                bricks_add      = (List<Brick>) this.getIntent().getSerializableExtra("bricks_add");
                brick_type      = (List<GeneralSpinner>) this.getIntent().getSerializableExtra("bricks_type");
                local           = (Local) bundle.getSerializable("agencia");
            }
            stepperIndicator    = findViewById(R.id.stepper_indicator);
            progressBar         = findViewById(R.id.progress_bar);
            textMessage         = findViewById(R.id.textMessage);
            toolbar             = findViewById(R.id.toolbar);
            ImageView home      = findViewById(R.id.home);
            relativeLayout      = findViewById(R.id.lnysave);
            btnContinue         = findViewById(R.id.btnSave);

            stepperIndicator.setStepCount(constantes.STEP_COTIZACION_COUNT);
            stepperIndicator.setCurrentStep(1);
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
            btnContinue.setOnClickListener(new View.OnClickListener() {
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
            });

            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            recyclerView    = findViewById(R.id.recycler);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            myBrickTypeList();

            if (bricks_add!=null) {
                if (bricks_add.size() > 0) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
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
                item.type_brick = bricks_add.get(i).brick_type;
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
            e.printStackTrace();
        }
        return null;
    }

    private void myBrickTypeList(){
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.brick_type_list ();
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            brick_type  = request.spinner_general;
                            estructuraListAdapter = new EstructuraListAdapter(context, brick_type, bricks_add, local);
                            recyclerView.setAdapter(estructuraListAdapter);
                            estructuraListAdapter.notifyDataSetChanged();
                            if (brick_type ==null || brick_type.size()== 0){
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
}
