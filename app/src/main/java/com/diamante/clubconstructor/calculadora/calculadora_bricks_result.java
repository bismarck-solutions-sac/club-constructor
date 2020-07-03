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
import com.diamante.clubconstructor.calculadora.adapter.ResultListAdapter;
import com.diamante.clubconstructor.cotizacion.cotizacion_ubicacion;
import com.diamante.clubconstructor.principal;
import com.diamante.clubconstructor.model.Brick;
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

public class calculadora_bricks_result extends AppCompatActivity {

    private functions function = functions.getInstance();
    private Toolbar toolbar;
    private Context context;
    private RecyclerView recyclerView;
    private String ACTION_MODE="Add";

    private Estimated estimated;
    private List<EstimatedDetail> detail = new ArrayList<>();
    private Brick brick;
    private User user;
    private GeneralSpinner brick_type;
    private GeneralSpinner king_wall;
    private ResultListAdapter resultListAdapter;

    private Button btnContinue, btnSave, btnCotizar;
    private ProgressBar progressBar;
    private TextView textMessage;
    private TextView textTitle;
    private double alto;
    private double largo;
    private LinearLayout lnyButtons;
    private RelativeLayout rlyCotizar;
    private AlertDialog dialog;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            brick_type  = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
            king_wall   = (GeneralSpinner) savedInstanceState.getSerializable("king_wall");
            estimated   = (Estimated) savedInstanceState.getSerializable("estimated");
            detail      = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
            largo       = savedInstanceState.getDouble("largo");
            alto        = savedInstanceState.getDouble("alto");

        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("brick_type", brick_type);
            state.putSerializable("king_wall", king_wall);
            state.putSerializable("estimated", estimated);
            state.putSerializable("detail", (Serializable) detail);
            state.putDouble("largo", largo);
            state.putDouble("alto", alto);
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
            function._ga("Calculadora - Resultado", context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_bricks_result);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        try {
            if (savedInstanceState != null)
            {
                if (savedInstanceState.getSerializable("brick_type") != null){
                    brick_type = (GeneralSpinner) savedInstanceState.getSerializable("brick_type");
                }
                if (savedInstanceState.getSerializable("king_wall") != null){
                    king_wall = (GeneralSpinner) savedInstanceState.getSerializable("king_wall");
                }
                if (savedInstanceState.getSerializable("estimated") != null){
                    estimated = (Estimated) savedInstanceState.getSerializable("estimated");
                }
                if (savedInstanceState.getSerializable("detail") != null){
                    detail = (List<EstimatedDetail>) savedInstanceState.getSerializable("detail");
                }
                if (savedInstanceState.getDouble("largo") != 0){
                    largo = savedInstanceState.getDouble("largo", 0);
                }
                if (savedInstanceState.getDouble("alto") != 0){
                    alto = savedInstanceState.getDouble("alto", 0);
                }
            }
            inicializa();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error : onCreate").show();
        }
    }

    private void inicializa() {
        try {
            context     = this;
            Bundle bundle   = this.getIntent().getExtras();
            if (bundle!=null){
                ACTION_MODE     = bundle.getString("ACTION_MODE");
                estimated       = (Estimated) bundle.getSerializable("estimated");
                if (estimated!=null){
                    detail  = estimated.getDetail();
                    if (detail == null){
                        detail = new ArrayList<>();
                    }
                }
            }
            if (ACTION_MODE==null){
                ACTION_MODE= "Add";
            }
            toolbar     = findViewById(R.id.toolbar);
            recyclerView= findViewById(R.id.recycler);
            btnContinue = findViewById(R.id.btnContinue);
            btnSave     = findViewById(R.id.btnSave);
            btnCotizar  = findViewById(R.id.btnCotizar);
            progressBar = findViewById(R.id.progress_bar);
            textMessage = findViewById(R.id.textMessage);
            textTitle   = findViewById(R.id.textTitle);
            lnyButtons  = findViewById(R.id.lnyButtons);
            rlyCotizar  = findViewById(R.id.rly_cotizar);
            ImageView home = findViewById(R.id.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detail.size()!=0 && ACTION_MODE.equals("Add") && user.id>0){
                        createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
                    }else{
                        Intent i = new Intent(getApplicationContext(), principal.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
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
            brick       = (Brick) bundle.get("Brick");
            brick_type  = (GeneralSpinner) bundle.get("Brick_Type");
            king_wall   = (GeneralSpinner) bundle.get("King_wall");
            user        = (User) bundle.get("User");
            alto        = bundle.getInt("alto");
            largo       = bundle.getInt("largo");

            dialog      = createDialogProgress();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, calculadora_brick_type.class);
                    i.putExtra("estimated", estimated );
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogSave("Nombre del cálculo").show();
                }
            });

            btnCotizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, cotizacion_ubicacion.class);
                    i.putExtra("estimated", estimated );
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            if (user!=null){
                if (user.id<0){
                    btnSave.setBackground(getDrawable(R.drawable.button_rectangle_disable));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btnSave.setTextColor(getColor(R.color.black_overlay));
                    }
                    btnSave.setEnabled(false);
                }
            }
            if (ACTION_MODE.equals("Add")){
                calculate_estimated();
            }else {
                if (estimated!=null){
                    textTitle.setText(estimated.getName());
                }
                lnyButtons.setVisibility(View.GONE);
                rlyCotizar.setVisibility(View.VISIBLE);
                resultListAdapter = new ResultListAdapter(context, detail);
                recyclerView.setAdapter(resultListAdapter);
                resultListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error : inicializa").show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (detail.size()!= 0 && ACTION_MODE.equals("Add") && user.id>0){
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
                    if (detail.size()!=0 && ACTION_MODE.equals("Add") && user.id>0){
                        createDialogQuestion("Su cálculo aún no esta guardado", "Seguro de Salir?").show();
                    }else{
                        if (ACTION_MODE.equals("Add")){
                            Intent i = new Intent(getApplicationContext(), calculadora_brick_type.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        finish();
                    }
                    break;
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error : onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculate_estimated(){
        RequestParameter parameter = new RequestParameter();
        parameter.user = new User();
        parameter.user  = user;
        parameter.brick_type = new GeneralSpinner();
        parameter.brick_type    = brick_type;
        parameter.brick = new Brick();
        parameter.brick = brick;
        parameter.estimated = new Estimated();
        parameter.estimated.setDetail(detail);

        parameter.alto = alto;
        parameter.largo = largo;
        parameter.king_wall ="0";
        if (king_wall!=null){
            parameter.king_wall = king_wall.id;
        }
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);

        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.calculation_material(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError()==0){
                            estimated   = request.estimated;
                            detail      = request.estimated.getDetail();
                            if (detail==null){
                                textMessage.setVisibility(View.VISIBLE);
                            }
                            resultListAdapter = new ResultListAdapter(context, detail);
                            recyclerView.setAdapter(resultListAdapter);
                            resultListAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }else if(request.getCodigoError()==2){
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText(request.mensajeSistema);
                        }
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
        parameter.user= user;
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
                            dialog.dismiss();
                            Intent i = new Intent(context, calculadora_brick_type.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
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
        try {
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
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: AlertDialog").show();
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

    public AlertDialog createDialogProgress() {

        try {
            AlertDialog alertDialog =null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_circularloader, null);
            builder.setView(v);
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}