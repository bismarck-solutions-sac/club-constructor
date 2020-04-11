package com.diamante.clubconstructor.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.model.About;
import com.diamante.clubconstructor.network.HelperWS;
import com.diamante.clubconstructor.network.MethodWS;
import com.diamante.clubconstructor.request.RequestParameter;
import com.diamante.clubconstructor.response.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class politicas extends AppCompatActivity {

    private TextView textPoliticas;
    private Spanned spanned;
    private Context context;
    private AlertDialog dialogProgress;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politicas);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
        try {
            context         = this;
            textPoliticas   = findViewById(R.id.textPoliticas);
            dialogProgress  = createDialogProgress();
            loadData();
        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: onCreate").show();
        }
    }

    private void loadData() {

        RequestParameter parameter = new RequestParameter();
        parameter.about = new About();
        parameter.about.id=3;
        dialogProgress.show();
        MethodWS methodWS = HelperWS.getConfiguration().create(MethodWS.class);
        try {
            Call<ResponseData> result = methodWS.about(parameter);
            result.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        ResponseData request = response.body();
                        if (request.getCodigoError() == 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                spanned = HtmlCompat.fromHtml(String.valueOf(request.about.description), HtmlCompat.FROM_HTML_MODE_COMPACT);
                            } else {
                                spanned = Html.fromHtml(String.valueOf(request.about.description));
                            }
                            textPoliticas.setText(spanned);
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

    public AlertDialog createDialogProgress() {
        AlertDialog alertDialog =null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        try {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_circularloader, null);
            TextView textMessage = v.findViewById(R.id.textMessage);

            textMessage.setText("...Cargando información...");
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
}
