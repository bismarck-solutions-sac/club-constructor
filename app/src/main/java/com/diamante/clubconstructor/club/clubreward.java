package com.diamante.clubconstructor.club;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.club.adapters.NoticiasListAdapter;
import com.diamante.clubconstructor.club.adapters.RewardListAdapter;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.response.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class clubreward extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private AlertDialog dialogProgress;
    private RewardListAdapter adapter;

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
        setContentView(R.layout.activity_clubreward);

        inicializa();
    }

    private void inicializa() {
        try {
            context             = this;
            dialogProgress      = createDialogProgress();
            ImageView home      = findViewById(R.id.home);
            recyclerView        = findViewById(R.id.recycler);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2,RecyclerView.VERTICAL,false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setClipToPadding(true);

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), main.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            loadData();

        }catch (Exception e){
            createDialogError(e.getMessage(), "inicializa").show();
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

    private void loadData() {
        dialogProgress.show();
        try {
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            Call<ResponseData> result = methodWS.getrewardsList();
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            adapter = new RewardListAdapter(context, request.reward_list);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            dialogProgress.dismiss();
                        } else if (request.getCodigoError() == 2) {
                            dialogProgress.dismiss();
                            createDialogError(request.mensajeSistema, getString(R.string.app_name)).show();
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
            createDialogError(e.getMessage(), "onFailure").show();
        }
    }
}
