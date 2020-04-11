package com.diamante.clubconstructor.calculadora;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.adapter.BrickListAdapter;
import com.diamante.clubconstructor.calculadora.adapter.EstructuraListAdapter;
import com.diamante.clubconstructor.dialogos.dialog_custom;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class calculadora_brick_type extends AppCompatActivity {

    private Toolbar                 toolbar;
    private Context                 context;
    private RecyclerView            recyclerView;
    private EstructuraListAdapter   estructuraListAdapter;
    private RelativeLayout          relativeLayout;
    private AlertDialog             dialog;
    private Button                  btnSave;
    private ProgressBar             progressBar;
    private TextView                textMessage;

    private globals                 global = globals.getInstance();
    private List<GeneralSpinner>    brick_type = new ArrayList<>();
    private Estimated               estimated;
    private List<EstimatedDetail>   detail = new ArrayList<>();

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            global      = (globals) savedInstanceState.getSerializable("global");
            brick_type  = (List<GeneralSpinner>) savedInstanceState.getSerializable("brick_type");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            detail      = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("global", (Serializable) global);
            state.putSerializable("brick_type", (Serializable) brick_type);
            state.putSerializable("estimated", estimated);
            state.putSerializable("detail", (Serializable) detail);
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
        setContentView(R.layout.activity_calculadora_material);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("global") != null){
                global = (globals) savedInstanceState.getSerializable("global");
                global.setUser(global.user);
            }
            if (savedInstanceState.getSerializable("brick_type") != null){
                brick_type = (List<GeneralSpinner>) savedInstanceState.getSerializable("brick_type");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.getSerializable("estimated");
            }
            if (savedInstanceState.getSerializable("detail") != null){
                detail = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle!=null){
                estimated   = (Estimated) bundle.getSerializable("estimated");
                detail      = estimated.getDetail();
                if (detail==null){
                    detail = new ArrayList<>();
                }
            }
            context     = this;
            toolbar     = findViewById(R.id.toolbar);
            btnSave     = findViewById(R.id.btnSave);
            progressBar = findViewById(R.id.progress_bar);
            textMessage = findViewById(R.id.textMessage);
            dialog      = createDialogProgress();

            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            recyclerView    = findViewById(R.id.recycler);
            relativeLayout  = findViewById(R.id.lnysave);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detail.size()!=0 && global.user.id>0 ){
                        createDialogQuestion("Su cálculo aún no ha sido guardado.", "Seguro de Salir?").show();
                    }else{
                        Intent i = new Intent(getApplicationContext(), main.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
            });
            if (detail.size()>0){
                relativeLayout.setVisibility(View.VISIBLE);
            }else{
                relativeLayout.setVisibility(View.GONE);
            }
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogSave("Nombre del cálculo").show();
                }
            });
            myBrickTypeList();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (detail.size()!=0 && global.user.id>0){
                    createDialogQuestion("Su cálculo aún no ha sido guardado.", "Seguro de Salir?").show();
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
                    if (detail.size()!=0){
                        createDialogQuestion("Su cálculo aún no ha sido guardado.", "Seguro de Salir?").show();
                    }else{
                        finish();
                    }
                    break;
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
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
                            estructuraListAdapter = new EstructuraListAdapter(context, brick_type, estimated);
                            recyclerView.setAdapter(estructuraListAdapter);
                            estructuraListAdapter.notifyDataSetChanged();
                            if (brick_type ==null || brick_type.size()== 0){
                                textMessage.setVisibility(View.VISIBLE);
                            }
                        }else if(request.getCodigoError()==2){
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText(request.mensajeSistema);
                        }
                        progressBar.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText(response.body().mensajeSistema);
                    }
                }
                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText(t.getMessage());
                }
            });
        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setText(e.getMessage());
        }
    }

    private void Update(){
        RequestParameter parameter = new RequestParameter();
        parameter.user= global.getUser();
        estimated.status ="PE";
        parameter.estimated = estimated;
        parameter.estimated.setDetail(detail);
        dialog.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.calculation_add (parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            detail = new ArrayList<>();
                            estimated = new Estimated();
                            relativeLayout.setVisibility(View.GONE);
                            estructuraListAdapter = new EstructuraListAdapter(context, brick_type, estimated);
                            recyclerView.setAdapter(estructuraListAdapter);
                            estructuraListAdapter.notifyDataSetChanged();
                            dialog.dismiss();
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

    public AlertDialog createDialogSave(String title) {
        final Button btn_ok;
        final TextView textTitle;
        final EditText textMessage;
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_calculation_save, null);

        btn_ok      = v.findViewById(R.id.buttonAction);
        textTitle   = v.findViewById(R.id.textTitle);
        textMessage = v.findViewById(R.id.textMessage);
        textMessage.requestFocus();
        textTitle.setText(title);
        btn_ok.setText(context.getResources().getString(R.string.okay));
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogZoom_back;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textMessage.getText().toString().length()!=0){
                    estimated.setName(textMessage.getText().toString());
                    alertDialog.dismiss();
                    Update();
                }else {
                    textMessage.setError("* El campo no puede estar vacío.");
                }
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

    public AlertDialog createDialogError(String message, String title) {
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
    }

    public AlertDialog createDialogQuestion(String message, String title) {
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
    }
}
