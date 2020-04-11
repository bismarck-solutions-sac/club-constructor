package com.diamante.clubconstructor.club;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.adapters.BonusCanjeListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Level;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class clublevel extends AppCompatActivity {

    private Context         context;
    private User            user;
    private Toolbar         toolbar;
    private CircleImageView cimglevel;
    private TextView        textLevel, textpuntos, textpuntosnext, textmessage;
    private AlertDialog     dialogProgress;
    private RecyclerView    recyclerView;
    private BonusCanjeListAdapter adapter;
    private globals global = globals.getInstance();

    NumberFormat decimalFormat   = new DecimalFormat("#,##0.00");

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("global", (Serializable) global);
            state.putSerializable("user", user);
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
            user    = (User) savedInstanceState.getSerializable("user");
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
        setContentView(R.layout.activity_clublevel);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("global") != null){
                global = (globals) savedInstanceState.getSerializable("global");
                global.setUser(global.user);
            }
            if (savedInstanceState.getSerializable("user") != null){
                user = (User) savedInstanceState.getSerializable("user");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            context         = this;
            Bundle bundle   = this.getIntent().getExtras();
            toolbar         = findViewById(R.id.toolbar);
            cimglevel       = findViewById(R.id.cimglevel);
            textLevel       = findViewById(R.id.textLevel);
            textpuntos      = findViewById(R.id.textpuntos);
            textpuntosnext  = findViewById(R.id.textpuntosnext);
            recyclerView    = findViewById(R.id.recycler);
            textmessage     = findViewById(R.id.textMessage);
            dialogProgress  = createDialogProgress();
            if (bundle!= null){
                user       = (User) bundle.getSerializable("user");
                setup_level(user);
            }
            setSupportActionBar(toolbar);
            // add back arrow to toolbar
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setTitle("Mi categoria");
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

    private void setup_level(User user) {
        try {
            List<Level> levels;
            levels = user.getLevel_list();
            if (levels!= null) {
                int rows = levels.size();
                for (int i=0; i<rows; i++){
                    if (levels.get(i).id == user.level){
                        Glide.with(context).load(constantes.URL_BASE_IMAGE + levels.get(i).path).into(cimglevel);
                        textLevel.setText(levels.get(i).name);
                    }if (levels.get(i).id > user.level){
                        textpuntosnext.setText(decimalFormat.format(levels.get(i).value_init));
                        break;
                    }
                }
            }
            textpuntos.setText(decimalFormat.format(user.puntos));
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: setup_level").show();
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
            return super.onOptionsItemSelected(item);
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

    private void loadData() {
        RequestParameter parameter = new RequestParameter();
        parameter.user = user;
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.userBonusSelect(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            adapter = new BonusCanjeListAdapter(context, request.user.bonus_canje);
                            recyclerView.setAdapter(adapter);
                            global.setUser(request.user);
                            user = request.user;
                            setup_level(request.user);
                            dialogProgress.dismiss();
                        } else if (request.getCodigoError() == 2) {
                            dialogProgress.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                        }
                        if (user.bonus_canje == null){
                            textmessage.setVisibility(View.VISIBLE);
                        }else if(user.bonus_canje.size()==0){
                            textmessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        dialogProgress.dismiss();
                        createDialogError(response.message(), "onResponse").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialogProgress.dismiss();
                    createDialogError(t.getMessage(), "onFailure").show();
                }
            });
        } catch (Exception e) {
            dialogProgress.dismiss();
            createDialogError(e.getMessage(), "loadData").show();
        }
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
                e.printStackTrace();
            }
            return alertDialog;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
