package com.diamante.clubconstructor.club;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.diamante.clubconstructor.adapters.MenuListAdapter;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Estimated;
import com.diamante.clubconstructor.model.EstimatedDetail;
import com.diamante.clubconstructor.model.GeneralSpinner;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class clubmenu extends AppCompatActivity {

    private Context context;
    private MenuListAdapter menuListAdapter;
    private User user;

    private RecyclerView recyclerView;
    private AlertDialog dialogProgress;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            user      = (User) savedInstanceState.getSerializable("user");
        }catch (Exception e){
            createDialogError(e.getMessage(), "onRestoreInstanceState").show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        try {
            state.putSerializable("user", user);
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
        setContentView(R.layout.activity_clubmenu);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getSerializable("user") != null){
                user = (User) savedInstanceState.getSerializable("user");
            }
        }
        inicializa();
    }

    private void inicializa() {
        try {
            Bundle bundle       = this.getIntent().getExtras();
            context             = this;
            dialogProgress      = createDialogProgress();
            ImageView home      = findViewById(R.id.home);
            recyclerView        = findViewById(R.id.recycler);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2,RecyclerView.VERTICAL,false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setClipToPadding(true);
            if (bundle!=null){
                user  = (User) bundle.getSerializable("user");
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

            loadData();

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
        }
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
            createDialogError(e.getMessage(), "Error: createDialogProgress").show();
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

    private void loadData() {
        dialogProgress.show();
        try {
            MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
            RequestParameter parameter = new RequestParameter();
            parameter.setType_menu("C");
            Call<ResponseData> result = methodWS.getMenuClubList(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            menuListAdapter = new MenuListAdapter(context, request.menu, user);
                            recyclerView.setAdapter(menuListAdapter);
                            menuListAdapter.notifyDataSetChanged();
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
