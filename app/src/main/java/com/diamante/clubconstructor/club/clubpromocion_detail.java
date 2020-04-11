package com.diamante.clubconstructor.club;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.main;
import com.diamante.clubconstructor.model.Promocion;
import com.diamante.clubconstructor.util.constantes;

public class clubpromocion_detail extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
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
        setContentView(R.layout.activity_clubpromocion_detail);

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
            if (bundle.getSerializable("Promocion")!=null){
                Promocion promocion     = (Promocion) bundle.getSerializable("Promocion");
                TextView textname       = findViewById(R.id.textDescripcion);
                ImageView imageView     = findViewById(R.id.imgView);
                String path             = constantes.URL_BASE_IMAGE + promocion.getPath();
                Spanned spanned         = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    spanned = HtmlCompat.fromHtml(String.valueOf(promocion.getDescription()), HtmlCompat.FROM_HTML_MODE_COMPACT);
                } else {
                    spanned = Html.fromHtml(String.valueOf(promocion.getDescription()));
                }
                textname.setText(spanned);
                Glide.with(context)
                        .load(path)
                        .error(R.drawable.app_icon_notimage)
                        .into(imageView);
            }

        }catch (Exception e){
            createDialogError(e.getMessage(), "Error: inicializa").show();
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
