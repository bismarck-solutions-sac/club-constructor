package com.diamante.clubconstructor.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diamante.clubconstructor.R;
import com.diamante.clubconstructor.globals.globals;
import com.diamante.clubconstructor.model.User;
import com.diamante.clubconstructor.util.constantes;

import java.io.IOException;

public class image extends AppCompatActivity {
    private ImageView imageView;
    private AlertDialog dialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        inicializa();
    }

    private void inicializa() {
        try {
            User user = new User();
            Bundle bundle   = this.getIntent().getExtras();
            if (bundle!=null){
                user = (User) bundle.getSerializable("user");
            }
            imageView       = findViewById(R.id.imgView);
            dialog          = createDialogProgress();
            toolbar         = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            if (user!= null){
                Glide.with(this)
                        .load(constantes.URL_BASE_IMAGE + user.getPath())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .error(R.drawable.app_icon_profile)
                        .into(imageView);
            }
            dialog.show();
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public AlertDialog createDialogProgress() {
        AlertDialog alertDialog =null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
}
