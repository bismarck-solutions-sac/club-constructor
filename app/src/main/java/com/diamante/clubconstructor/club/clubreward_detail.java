package com.diamante.clubconstructor.club;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.club.adapters.RewardListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Promocion;
import com.diamante.clubconstructor.model.Reward;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;
import com.diamante.clubconstructor.util.constantes;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class clubreward_detail extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private Reward reward;
    private AlertDialog dialog;
    private Button btnSave;
    NumberFormat formato    = new DecimalFormat("#,##0.00");

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            Log.v("main", "Inside of onRestoreInstanceState");
            reward = (Reward) savedInstanceState.getSerializable("reward");
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("reward", reward);
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
        setContentView(R.layout.activity_clubreward_detail);
        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("reward") != null))
        {
            reward = (Reward) savedInstanceState.getSerializable("reward");
        }
        inicializa();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
            }
        } catch (Exception e) {
            createDialogError(e.getMessage(), "Error: onOptionsItemSelected").show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicializa() {
        try {
            Bundle bundle       = this.getIntent().getExtras();
            context             = this;
            ImageView home      = findViewById(R.id.home);
            toolbar             = findViewById(R.id.toolbar);
            dialog              = createDialogProgress();
            btnSave             = findViewById(R.id.btnSave);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reward!= null){
                        createDialogQuestion("Se descontará " + formato.format(reward.value) + " puntos de su cuenta.", "¿Está seguro de realizar el canje?").show();
                    }

                }
            });
            if (bundle.getSerializable("Reward")!=null){
                reward                  = (Reward) bundle.getSerializable("Reward");
            }
            if (reward!=null){
                TextView textname       = findViewById(R.id.textname);
                TextView textdesc       = findViewById(R.id.textdescripcion);
                TextView textpoint      = findViewById(R.id.textpoint);
                ImageView imageView     = findViewById(R.id.imgView);
                ImageView imageStock    = findViewById(R.id.imgStock);
                String path             = constantes.URL_BASE_IMAGE + reward.getPath();
                Spanned spanned         = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    spanned = HtmlCompat.fromHtml(String.valueOf(reward.getDescription()), HtmlCompat.FROM_HTML_MODE_COMPACT);
                } else {
                    spanned = Html.fromHtml(String.valueOf(reward.getDescription()));
                }
                textname.setText(reward.getName());
                textdesc.setText(spanned);
                textpoint.setText(formato.format(reward.value) + " puntos");
                Glide.with(context)
                        .load(path)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .error(R.drawable.app_icon_notimage)
                        .into(imageView);
                imageStock.setVisibility(View.GONE);
                if (reward.stock<1){
                    imageStock.setVisibility(View.VISIBLE);
                    btnSave.setEnabled(false);
                    btnSave.setVisibility(View.GONE);
                }
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
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
                    alertDialog.dismiss();
                    UpdateReward();
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
            createDialogError(e.getMessage(), "createDialogQuestion").show();
        }
        return null;
    }

    private void UpdateReward() {
        dialog.show();
        try {
            globals global = globals.getInstance();
            RequestParameter parameter = new RequestParameter();
            parameter.user      = global.getUser();
            parameter.reward    = reward;
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            Call<ResponseData> result = methodWS.rewardsAdd(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            dialog.dismiss();
                            global.setUser(request.user);
                            createDialogInformation(getString(R.string.app_name), "Canje realizado correctamente, se envió un correo con los datos del canje.").show();
                        } else if (request.getCodigoError() == 1) {
                            dialog.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                        } else if (request.getCodigoError() == 2) {
                            dialog.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
                        }
                    } else {
                        dialog.dismiss();
                        createDialogError(response.message(), "onResponse").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                    createDialogError(t.getMessage(), "onFailure").show();
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            createDialogError(e.getMessage(), "onFailure").show();
        }
    }

    public AlertDialog createDialogInformation(String Title, String Message) {

        try {
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_success, null);
            Button btn_ok = v.findViewById(R.id.buttonAction);
            TextView textTitle = v.findViewById(R.id.textTitle);
            TextView textMessage = v.findViewById(R.id.textMessage);

            textTitle.setText(Title);
            textMessage.setText(Message);
            btn_ok.setText("Aceptar");
            builder.setView(v);

            alertDialog = builder.create();

            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });
            return alertDialog;
        }catch (Exception e){
            createDialogError(e.getMessage(), "createDialogInformation").show();
        }
        return null;
    }

}
