package com.diamante.clubconstructor.club;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.club.adapters.CharlasListAdapter;
import com.diamante.clubconstructor.club.adapters.EventosListAdapter;
import com.diamante.clubconstructor.club.adapters.JobsListAdapter;
import com.diamante.clubconstructor.club.adapters.JobsPublicationListAdapter;
import com.diamante.clubconstructor.club.adapters.ManualesListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class clubcapacitacion extends AppCompatActivity {

    private globals global = globals.getInstance();
    private Context context;
    private RecyclerView recyclerView;
    private EventosListAdapter adapter1;
    private CharlasListAdapter adapter2;
    private ManualesListAdapter adapter3;
    private int MODO=0;
    private ProgressBar progressBar;
    private TextView textMessage;

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("global", (Serializable) global);
            state.putSerializable("MODO", MODO);
        }catch (Exception e){
            createDialogError(e.getMessage(), "onSaveInstanceState").show();
        }
        super.onSaveInstanceState(state);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            global  = (globals) savedInstanceState.getSerializable("global");
            MODO    = savedInstanceState.getInt("MODO");
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
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
        setContentView(R.layout.activity_clubcapacitacion);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("global") != null){
                global = (globals) savedInstanceState.getSerializable("global");
                global.setUser(global.user);
            }
            if (savedInstanceState.getInt("MODO") != 0){
                MODO = savedInstanceState.getInt("MODO");
            }

        }
        inicializa();
    }

    private void inicializa() {
        try {
            context             = this;
            ImageView home      = findViewById(R.id.home);
            recyclerView        = findViewById(R.id.recycler);
            TextView evento     = findViewById(R.id.eventos);
            TextView charla     = findViewById(R.id.charlas);
            TextView manual     = findViewById(R.id.manuales);
            progressBar         = findViewById(R.id.progress_bar);
            textMessage         = findViewById(R.id.textMessage);

            evento.setBackground(getDrawable(R.drawable.border_capacitacion_blue));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                evento.setTextColor(getColor(R.color.colorWhite));
            }else {
                evento.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            evento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MODO==0){
                        return;
                    }
                    evento.setBackground(getDrawable(R.drawable.border_capacitacion_blue));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        evento.setTextColor(getColor(R.color.colorWhite));
                    }else {
                        evento.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    }

                    charla.setBackground(getDrawable(R.drawable.border_capacitacion_transparent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        charla.setTextColor(getColor(R.color.colorPrimary));
                    }else {
                        charla.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }

                    manual.setBackground(getDrawable(R.drawable.border_capacitacion_transparent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manual.setTextColor(getColor(R.color.colorPrimary));
                    }else {
                        manual.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }

                    MODO=0;
                    loadData();
                }
            });
            charla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MODO==1){
                        return;
                    }
                    charla.setBackground(getDrawable(R.drawable.border_capacitacion_blue));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        charla.setTextColor(getColor(R.color.colorWhite));
                    }else {
                        charla.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    }

                    evento.setBackground(getDrawable(R.drawable.border_capacitacion_transparent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        evento.setTextColor(getColor(R.color.colorPrimary));
                    }else {
                        evento.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }

                    manual.setBackground(getDrawable(R.drawable.border_capacitacion_transparent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manual.setTextColor(getColor(R.color.colorPrimary));
                    }else {
                        manual.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }
                    MODO=1;
                    loadData();
                }
            });
            manual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MODO==2){
                        return;
                    }
                    manual.setBackground(getDrawable(R.drawable.border_capacitacion_blue));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manual.setTextColor(getColor(R.color.colorWhite));
                    }else {
                        manual.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    }

                    charla.setBackground(getDrawable(R.drawable.border_capacitacion_transparent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        charla.setTextColor(getColor(R.color.colorPrimary));
                    }else {
                        charla.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }

                    evento.setBackground(getDrawable(R.drawable.border_capacitacion_transparent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        evento.setTextColor(getColor(R.color.colorPrimary));
                    }else {
                        evento.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }
                    MODO=2;
                    loadData();
                }
            });
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            loadData();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
    }

    private void loadData() {
        try {
            RequestParameter parameter = new RequestParameter();
            parameter.setUser(global.getUser());
            progressBar.setVisibility(View.VISIBLE);
            textMessage.setVisibility(View.GONE);
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            Call<ResponseData> result=null;
            switch (MODO){
                case 0:
                    result = methodWS.geteventosList(parameter);
                    break;
                case 1:
                    result = methodWS.getcharlasList();
                    break;
                case 2:
                    result = methodWS.getmanualesList();
                    break;
            }

            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            switch (MODO){
                                case 0:
                                    adapter1 = new EventosListAdapter(context, request.eventos_list);
                                    recyclerView.setAdapter(adapter1);
                                    adapter1.notifyDataSetChanged();
                                    textMessage.setText("No hay eventos presenciales");
                                    if (request.eventos_list==null){
                                        textMessage.setVisibility(View.VISIBLE);
                                    }else if (request.eventos_list.size()==0){
                                        textMessage.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 1:
                                    adapter2 = new CharlasListAdapter(context, request.charlas_list);
                                    recyclerView.setAdapter(adapter2);
                                    adapter2.notifyDataSetChanged();
                                    textMessage.setText("No hay charlas virtuales");
                                    if (request.charlas_list==null){
                                        textMessage.setVisibility(View.VISIBLE);
                                    }else if (request.charlas_list.size()==0){
                                        textMessage.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 2:
                                    adapter3 = new ManualesListAdapter(context, request.manual_list);
                                    recyclerView.setAdapter(adapter3);
                                    adapter3.notifyDataSetChanged();
                                    textMessage.setText("No hay manuales");
                                    if (request.manual_list==null){
                                        textMessage.setVisibility(View.VISIBLE);
                                    }else if (request.manual_list.size()==0){
                                        textMessage.setVisibility(View.VISIBLE);
                                    }
                                    break;
                            }
                            progressBar.setVisibility(View.GONE);
                        } else if (request.getCodigoError() == 2) {
                            progressBar.setVisibility(View.GONE);
                            textMessage.setVisibility(View.VISIBLE);
                            textMessage.setText("Error: " + response.body().mensajeSistema);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        textMessage.setVisibility(View.VISIBLE);
                        textMessage.setText("Error: " + response.message());
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
}
