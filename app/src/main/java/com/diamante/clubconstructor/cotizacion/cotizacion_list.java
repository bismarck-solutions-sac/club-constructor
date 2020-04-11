package com.diamante.clubconstructor.cotizacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.CotListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Cotizacion;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cotizacion_list extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textMessage;
    private globals global = globals.getInstance();
    private CotListAdapter adapter ;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            Log.v("main", "Inside of onRestoreInstanceState");
            global.setUser((User) savedInstanceState.getSerializable("user"));
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("user", global.getUser());
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
        setContentView(R.layout.activity_cotizaciones);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("user") != null))
        {
            global.setUser((User) savedInstanceState.getSerializable("user"));
        }
        inicializa();
    }

    private void inicializa() {
        try {
            toolbar = findViewById(R.id.toolbar);
            context         = this;
            toolbar         = findViewById(R.id.toolbar);
            recyclerView    = findViewById(R.id.recycler);
            progressBar     = findViewById(R.id.progress_bar);
            textMessage     = findViewById(R.id.textMessage);

            setSupportActionBar(toolbar);
            // add back arrow to toolbar
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
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration( new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            loadData();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
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
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        RequestParameter parameter = new RequestParameter();
        parameter.cotizacion = new Cotizacion();
        parameter.cotizacion.setCompaniasocio(constantes.COMPANIASOCIO);
        parameter.cotizacion.setClientenumero(global.getUser().id);
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.cotizacion_list(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            adapter = new CotListAdapter(context, request.cotizacion_list);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (request.cotizacion_list ==null ){
                                textMessage.setVisibility(View.VISIBLE);
                            }else {
                                if (request.cotizacion_list.size()==0){
                                    textMessage.setVisibility(View.VISIBLE);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        } else if(request.getCodigoError()==2){
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
}
