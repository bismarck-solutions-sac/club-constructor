package com.diamante.clubconstructor.calculadora;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.calculadora.adapter.BrickListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.principal;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class calculadora_bricks extends AppCompatActivity {

    private functions function = functions.getInstance();
    private Toolbar toolbar;
    private Context context;
    private TextView textTitle, textArea, textMessage;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button btnContinue;

    private globals global = globals.getInstance();
    private GeneralSpinner brick_type;
    private BrickListAdapter brickListAdapter;
    private Estimated estimated;
    private List<EstimatedDetail> detail = new ArrayList<>();

    private int largo, alto, area_total;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            global      = (globals) savedInstanceState.getSerializable("global");
            brick_type  = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            detail      = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
            largo       = savedInstanceState.getInt("largo");
            alto        = savedInstanceState.getInt("alto");
            area_total  = savedInstanceState.getInt("area_total");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("global", (Serializable) global);
            state.putSerializable("brick_type", brick_type);
            state.putSerializable("estimated", estimated);
            state.putSerializable("detail", (Serializable) detail);
            state.putInt("largo", largo);
            state.putInt("alto", alto);
            state.putInt("area_total", area_total);
        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }

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
    protected void onResume(){
        super.onResume();
        try {
            function._ga("Calculadora - Ladrillos", context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_bricks);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("global") != null){
                global = (globals) savedInstanceState.getSerializable("global");
                global.setUser(global.user);
            }
            if (savedInstanceState.getSerializable("brick_type") != null){
                brick_type = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            }
            if (savedInstanceState.getSerializable("estimated") != null){
                estimated = (Estimated) savedInstanceState.getSerializable("estimated");
            }
            if (savedInstanceState.getSerializable("detail") != null){
                detail = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
            }
            if (savedInstanceState.getInt("largo") != 0){
                largo = savedInstanceState.getInt("largo", 0);
            }
            if (savedInstanceState.getInt("alto") != 0){
                alto = savedInstanceState.getInt("alto", 0);
            }
            if (savedInstanceState.getInt("area_total") != 0){
                area_total = savedInstanceState.getInt("area_total", 0);
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            Bundle bundle   = this.getIntent().getExtras();
            context         = this;
            toolbar         = findViewById(R.id.toolbar);
            textTitle       = findViewById(R.id.textTitle);
            textArea        = findViewById(R.id.textarea);
            recyclerView    = findViewById(R.id.recycler);
            progressBar     = findViewById(R.id.progress_bar);
            textMessage     = findViewById(R.id.textMessage);
            btnContinue     = findViewById(R.id.btnContinue);

            if (bundle!=null){
                brick_type  = (GeneralSpinner) bundle.getSerializable("brick_type");
                largo       = Integer.valueOf(bundle.getString("largo"));
                alto        = Integer.valueOf(bundle.getString("alto"));
                estimated   = (Estimated) bundle.getSerializable("estimated");
                if (estimated!=null){
                    detail      = estimated.getDetail();
                }
                area_total  = largo * alto;
                textArea.setText(Html.fromHtml("Área total " + area_total + " m <sup>" + "2" + "</sup>"));
            }

            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detail.size()!=0 && global.user.id>0){
                        createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
                    }else{
                        Intent i = new Intent(getApplicationContext(), principal.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
            });


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);



            if (brick_type!=null){
                textTitle.setText(brick_type.getDescripcion().toUpperCase());
            }
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (brickListAdapter.lastCheckedPosition<0) {
                        return;
                    }
                    Intent i = new Intent(context, calculadora_bricks_result.class);
                    i.putExtra("Brick", brickListAdapter.spinner_select());
                    i.putExtra("Brick_Type", brick_type);
                    i.putExtra("User", global.user);
                    i.putExtra("alto", alto);
                    i.putExtra("largo", largo);
                    i.putExtra("estimated", estimated);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
            myBrickList();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (detail.size()!=0 && global.user.id>0){
                    createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
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
                    if (detail.size()!=0 && global.user.id>0){
                        createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
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
                            brickListAdapter = new BrickListAdapter(context, request.getBricks());
                            recyclerView.setAdapter(brickListAdapter);
                            if (request.getBricks() ==null){
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
                Intent i = new Intent(getApplicationContext(), principal.class);
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